package com.example.fitnessMarathonBot.utils;

import com.vdurmont.emoji.EmojiParser;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum Emojis {
    ARROWDOWN(EmojiParser.parseToUnicode(":arrow_down:")),
    POINT_RIGHT(EmojiParser.parseToUnicode(":point_right:")),
    POINT_DOWN(EmojiParser.parseToUnicode(":point_down:")),
    MEMO(EmojiParser.parseToUnicode(":memo:")),
    HEART(EmojiParser.parseToUnicode(":heart:")),
    TADA(EmojiParser.parseToUnicode(":tada:"));

    private String emojiName;
    @Override
    public String toString() {
        return emojiName;
    }
}
