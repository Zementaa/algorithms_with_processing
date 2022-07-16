package de.fernunihagen.mci.group2.coopalgoart.shopf.test.lsystem;

import lombok.Getter;

@Getter
public class Rules {

    private char letter;
    private String string;
    

    public Rules(char letter, String string) {
        this.letter = letter;
        this.string = string;
    }
}
