package com.Sisi.Flashcard;

import org.apache.commons.cli.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class FlashcardApp {

    private static List<Flashcard> loadCards(String filename) throws IOException {
        List<Flashcard> cards = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    cards.add(new Flashcard(parts[0].trim(), parts[1].trim()));
                } else {
                    System.err.println("Буруу форматтай карт: " + line);
                }
            }
        }
        return cards;
    }

    public static void main(String[] args) {
        Options options = new Options();

        options.addOption(null, "help", false, "Тусламжийн мэдээлэл харуулах");

        Option orderOption = new Option(null, "order", true, "Зохион байгуулалтын төрөл (random, worst-first, recent-mistakes-first)");
        orderOption.setArgName("type");
        options.addOption(orderOption);

        Option repetitionsOption = new Option(null, "repetitions", true, "Нэг картыг хэдэн удаа зөв хариулахыг шаардлага болгох");
        repetitionsOption.setArgName("count");
        options.addOption(repetitionsOption);

        Option invertCardsOption = new Option(null, "invertCards", false, "Картын асуулт, хариултыг сольж харуулах");
        options.addOption(invertCardsOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.err.println("Командын мөрийн аргументийг танихгүй байна: " + e.getMessage());
            formatter.printHelp("flashcard [options] <cards-file>", options);
            System.exit(1);
            return;
        }

        if (cmd.hasOption("help")) {
            formatter.printHelp("flashcard [options] <cards-file>", options);
            return;
        }

        String[] remainingArgs = cmd.getArgs();
        if (remainingArgs.length != 1) {
            System.err.println("Картын файлын нэрийг оруулна уу.");
            formatter.printHelp("flashcard [options] <cards-file>", options);
            System.exit(1);
            return;
        }

        String cardsFile = remainingArgs[0];
        List<Flashcard> cards;
        try {
            cards = loadCards(cardsFile);
            if (cards.isEmpty()) {
                System.out.println("Картны файл хоосон байна.");
                return;
            }
        } catch (IOException e) {
            System.err.println("Картны файлыг уншихад алдаа гарлаа: " + e.getMessage());
            return;
        }

        String orderType = cmd.getOptionValue("order", "random");
        int repetitionsRequired = cmd.hasOption("repetitions") ? Integer.parseInt(cmd.getOptionValue("repetitions")) : 1;
        boolean invertCards = cmd.hasOption("invertCards");

        CardOrganizer organizer;
        switch (orderType) {
            case "random":
                organizer = new RandomCardOrganizer();
                break;
            case "worst-first":
                organizer = new WorstFirstSorter();
                break;
            case "recent-mistakes-first":
                organizer = new RecentMistakesFirstSorter();
                break;
            default:
                System.err.println("Буруу зохион байгуулалтын төрөл: " + orderType + ". 'random', 'worst-first', 'recent-mistakes-first' сонголтууд байна.");
                return;
        }

        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        List<Flashcard> currentRoundCards = new ArrayList<>(cards);
        List<Long> roundTimings = new ArrayList<>();
        List<Achievement> achievements = new ArrayList<>();

        System.out.println("Flashcard системд тавтай морилно уу!");

        while (!currentRoundCards.isEmpty()) {
            currentRoundCards = organizer.organizeCards(currentRoundCards);
            Flashcard currentCard = currentRoundCards.get(0);
            String question = invertCards ? currentCard.getAnswer() : currentCard.getQuestion();
            String correctAnswer = invertCards ? currentCard.getQuestion() : currentCard.getAnswer();

            System.out.println("\nАсуулт: " + question);
            long startTime = System.currentTimeMillis();
            String userAnswer = scanner.nextLine();
            long endTime = System.currentTimeMillis();
            long timeTaken = endTime - startTime;
            roundTimings.add(timeTaken);

            if (userAnswer.trim().equalsIgnoreCase(correctAnswer)) {
                System.out.println("Зөв!");
                currentCard.incrementCorrectCount();
                if (currentCard.getCorrectCount() >= repetitionsRequired) {
                    currentRoundCards.remove(0);
                } else {
                    // Зөв хариулсан ч дахин асуух картуудын жагсаалтад буцааж нэмнэ
                    currentRoundCards.remove(0);
                    currentRoundCards.add(currentCard);
                }
            } else {
                System.out.println("Буруу. Зөв хариулт: " + correctAnswer);
                currentCard.incrementIncorrectCount();
                // Буруу хариулсан картыг дахин асуух картуудын жагсаалтад буцааж нэмнэ
                currentRoundCards.remove(0);
                currentRoundCards.add(currentCard);
            }
            System.out.println("Таны хариулсан карт: " + currentCard);
        }

        System.out.println("\nТойрог дууслаа!");

        // Амжилтуудыг шалгах
        if (!cards.isEmpty() && cards.stream().allMatch(card -> card.getIncorrectCount() == 0)) {
            achievements.add(Achievement.CORRECT);
        }
        for (Flashcard card : cards) {
            if (card.getCorrectCount() >= 3) {
                achievements.add(Achievement.CONFIDENT);
            }
            if (card.getCorrectCount() + card.getIncorrectCount() > 5) {
                achievements.add(Achievement.REPEAT);
            }
        }
        if (!roundTimings.isEmpty()) {
            double averageTime = roundTimings.stream().mapToLong(Long::longValue).average().orElse(0) / 1000.0;
            if (averageTime < 5) {
                achievements.add(Achievement.FAST_LEARNER);
            }
        }

        if (!achievements.isEmpty()) {
            System.out.println("\nТаны амжилтууд:");
            for (Achievement achievement : achievements) {
                System.out.println("- " + achievement.getDescription());
            }
        }

        scanner.close();
    }
}