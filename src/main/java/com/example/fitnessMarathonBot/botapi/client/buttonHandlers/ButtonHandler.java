package com.example.fitnessMarathonBot.botapi.client.buttonHandlers;

import com.example.fitnessMarathonBot.service.ReplyMessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class ButtonHandler {

    @Autowired
    private ReplyMessagesService replyMessagesService;

    public SendMessage getMessageAndGoalsButton(long chatId) {
        String message = replyMessagesService.getReplyText("reply.goalsForToday");
        return new SendMessage(chatId, message).setReplyMarkup(getGoalsButton());
    }

    private InlineKeyboardMarkup getGoalsButton() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonTaskOne = new InlineKeyboardButton().setText("1");
        InlineKeyboardButton buttonTaskTwo = new InlineKeyboardButton().setText("2");
        InlineKeyboardButton buttonTaskThree = new InlineKeyboardButton().setText("3");
        InlineKeyboardButton buttonTaskFour = new InlineKeyboardButton().setText("4");
        InlineKeyboardButton buttonTaskFive = new InlineKeyboardButton().setText("5");
        InlineKeyboardButton buttonTaskSix = new InlineKeyboardButton().setText("6");

        //Every button must have callBackData, or else not work !
        buttonTaskOne.setCallbackData("buttonTaskOne");
        buttonTaskTwo.setCallbackData("buttonTaskTwo");
        buttonTaskThree.setCallbackData("buttonTaskThree");
        buttonTaskFour.setCallbackData("buttonTaskFour");
        buttonTaskFive.setCallbackData("buttonTaskFive");
        buttonTaskSix.setCallbackData("buttonTaskSix");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(buttonTaskOne);
        keyboardButtonsRow1.add(buttonTaskTwo);
        keyboardButtonsRow1.add(buttonTaskThree);
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow2.add(buttonTaskFour);
        keyboardButtonsRow2.add(buttonTaskFive);
        keyboardButtonsRow2.add(buttonTaskSix);


        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }
}
