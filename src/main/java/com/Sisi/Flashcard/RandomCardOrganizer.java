package com.Sisi.Flashcard;
import java.util.Collections;
import java.util.List;

public class RandomCardOrganizer implements CardOrganizer{
    @Override
    public List<Flashcard> organizeCards(List<Flashcard> cards){
        List<Flashcard> shuffledCards = new java.util.ArrayList<>(cards);
        Collections.shuffle(shuffledCards);
        return shuffledCards;
    }
    
}
