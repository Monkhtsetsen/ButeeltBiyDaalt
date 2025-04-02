package com.Sisi.Flashcard;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class WorstFirstSorter implements CardOrganizer {
    @Override
    public List<Flashcard> organizeCards(List<Flashcard> cards) {
        return cards.stream()
                .sorted(Comparator.comparingInt(Flashcard::getIncorrectCount).reversed())
                .collect(Collectors.toList());
    }
}