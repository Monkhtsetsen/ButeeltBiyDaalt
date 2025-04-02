package com.Sisi.Flashcard;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

import com.Sisi.Flashcard.Flashcard.CardOrganizer;
public class RecentMistakesFirstSorter implements CardOrganizer {
    @Override
    public List<Flashcard> organizeCards(List<Flashcard> cards){
        List<Flashcard> incorrectCards = cards.stream().filter(card -> card.getIncorrectCount()>0).collect(Collectors.toList());
        List<Flashcard> correctCards = cards.stream().filter(card -> card.getIncorrectCount() == 0).collect(Collectors.toList());
        List<Flashcard> sortedCards = new java.util.ArrayList<>(incorrectCards);
        sortedCards.addAll(correctCards);
        return sortedCards;
    }
    
}
