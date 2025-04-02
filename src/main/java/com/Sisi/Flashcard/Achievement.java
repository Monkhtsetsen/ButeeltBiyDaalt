package com.Sisi.Flashcard;
public enum Achievement {
    FAST_LEARNER("Нэг тойрогт дундажаар 5 секундээс доош хугацаанд хариулсан."),
    CORRECT("Сүүлийн тойрогт бүх карт зөв хариулсан."),
    REPEAT("Нэг картад дор хаяж 3 удаа зөв хариулсан."),
    CONFIDENT("");
private final String description;
Achievement(String description){
    this.description = description;
}
public String getDescription(){
    return description;
}
}
