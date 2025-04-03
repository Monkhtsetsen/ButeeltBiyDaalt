package com.Sisi.Flashcard;
public enum Achievement {
    FAST_LEARNER("Neg asuultand dundajaar 5 secundees doosh hugatsaand hariulsan."),
    CORRECT("Suuliin toirogt buh card-d zuv hariulsan."),
    REPEAT("Neg card d dor hayj 3 udaa zuv hariulsan."),
    CONFIDENT("");
private final String description;
Achievement(String description){
    this.description = description;
}
public String getDescription(){
    return description;
}
}
