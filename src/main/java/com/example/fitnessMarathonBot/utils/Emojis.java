package com.example.fitnessMarathonBot.utils;

import com.vdurmont.emoji.EmojiParser;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum Emojis {
    ARROWDOWN(EmojiParser.parseToUnicode(":arrow_down:"));

    private String emojiName;
    @Override
    public String toString() {
        return emojiName;
    }
}
