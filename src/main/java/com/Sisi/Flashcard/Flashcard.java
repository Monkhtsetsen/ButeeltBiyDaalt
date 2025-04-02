package com.Sisi.Flashcard;
public class Flashcard {
    private String question;
    private String answer;
    private int correctCount;
    private int incorrectCount;
    public Flashcard(String question, String answer){
        this.question = question;
        this.answer = answer;
        this.correctCount = 0;
        this.incorrectCount = 0;
    }
    public String  getQuestion(){
        return question;
    }
    public String getAnswer(){
        return answer;
    }
    public int getCorrectCount(){
        return correctCount;
    }
    public int getIncorrectCount(){
        return incorrectCount;
    }
    public void incrementCorrectCount(){
        this.correctCount++;
    }
    public void incrementIncorrectCount(){
        this.incorrectCount++;
    }
    @Override
    public String toString(){
        return "Асуулт: " +question+ "Хариулт: " +answer+ "(Зөв: " +correctCount+ ", Буруу: " +incorrectCount+ ")";


    }
}
