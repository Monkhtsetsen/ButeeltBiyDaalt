package com.Sisi.Flashcard;

public class Flashcard {
    private String question;
    private String answer;
    private int correctCount = 0;
    private int incorrectCount = 0;
    private int incorrectCountForRound = 0; // Нэг тойрогт буруу хариулсан тоо
    private long lastIncorrectTime = 0; // Хамгийн сүүлд буруу хариулсан цаг

    public Flashcard(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public int getCorrectCount() {
        return correctCount;
    }

    public int getIncorrectCount() {
        return incorrectCount;
    }

    public int getIncorrectCountForRound() {
        return incorrectCountForRound;
    }

    public void incrementCorrectCount() {
        this.correctCount++;
    }

    public void incrementIncorrectCount() {
        this.incorrectCount++;
        this.lastIncorrectTime = System.currentTimeMillis(); // Буруу хариулахад цагийг шинэчилнэ
    }

    public void resetIncorrectCountForRound() {
        this.incorrectCountForRound = 0;
    }

    public long getLastIncorrectTime() {
        return lastIncorrectTime;
    }

    @Override
    public String toString() {
        return "Asuult: \"" + question + "\", Hariult: \"" + answer + "\" (Zuv: " + correctCount + ", Buruu: " + incorrectCount + ")";
    }
}