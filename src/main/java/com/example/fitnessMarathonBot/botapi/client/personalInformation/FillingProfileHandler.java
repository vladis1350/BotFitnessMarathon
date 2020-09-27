package com.example.fitnessMarathonBot.botapi.client.personalInformation;


import com.example.fitnessMarathonBot.bean.Bot;
import com.example.fitnessMarathonBot.bean.UserProfileData;
import com.example.fitnessMarathonBot.botapi.BotState;
import com.example.fitnessMarathonBot.botapi.InputMessageHandler;
import com.example.fitnessMarathonBot.cache.UserDataCache;
import com.example.fitnessMarathonBot.service.ReplyMessagesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class FillingProfileHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private ReplyMessagesService messagesService;
    private Bot myBot;

    public FillingProfileHandler(UserDataCache userDataCache,
                                 ReplyMessagesService messagesService, @Lazy Bot myBot) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
        this.myBot = myBot;
    }

    @Override
    public SendMessage handle(Message message) {
        if (userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.ASK_PERSONAL_INFO)) {
            userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.ASK_NAME);
        }

        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ASK_PERSONAL_INFO;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        String usersAnswer = inputMsg.getText();
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();

//        ReplyKeyboardMarkup replyKeyboardMarkup = getMainMenuKeyboard();

        UserProfileData profileData = userDataCache.getUserProfileData(userId);
        BotState botState = userDataCache.getUsersCurrentBotState(userId);

        SendMessage replyToUser = null;

        if (botState.equals(BotState.ASK_AGE)) {
            System.out.println(usersAnswer);
            profileData.setName(usersAnswer);
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askAge");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_HEIGHT);
        }
        if (botState.equals(BotState.ASK_HEIGHT)) {
            if (userAnswerIsCorrect(usersAnswer)) {
                profileData.setAge(Integer.parseInt(usersAnswer));
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askHeight");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_WEIGHT);
            } else {
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askAge");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_HEIGHT);
            }
        }
        if (botState.equals(BotState.ASK_WEIGHT)) {
            if (userAnswerIsCorrect(usersAnswer)) {
                profileData.setHeight(Integer.parseInt(usersAnswer));
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askWeight");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_PHYSIQUE);
            } else {
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askHeight");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_WEIGHT);
            }
        }
        if (botState.equals(BotState.ASK_PHYSIQUE)) {
            if (userAnswerIsCorrect(usersAnswer)) {
                profileData.setWeight(Integer.parseInt(usersAnswer));
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askPhysique");
                replyToUser.setReplyMarkup(getInlineMessageButtons());
                userDataCache.setUsersCurrentBotState(userId, BotState.PROFILE_FILLED);
            } else {
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askWeight");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_PHYSIQUE);
            }
        }
        if (botState.equals(BotState.PROFILE_FILLED)) {
            profileData.setPhysique(usersAnswer);
            replyToUser = messagesService.getReplyMessage(chatId, "reply.profileFilled");
            userDataCache.setUsersCurrentBotState(userId, BotState.MAIN_MENU);
        }


        userDataCache.saveUserProfileData(userId, profileData);
        return replyToUser;
    }

    private boolean userAnswerIsCorrect(String userAnswer) {
        return userAnswer.matches("[-+]?\\d+");
    }

    private InlineKeyboardMarkup getInlineMessageButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonEctomorph = new InlineKeyboardButton().setText("Эктоморф");
        InlineKeyboardButton buttonMezomorph = new InlineKeyboardButton().setText("Мезофорф");
        InlineKeyboardButton buttonEndomorph = new InlineKeyboardButton().setText("Эндоморф");


        //Every button must have callBackData, or else not work !
        buttonEctomorph.setCallbackData("buttonEctomorph");
        buttonMezomorph.setCallbackData("buttonMezomorph");
        buttonEndomorph.setCallbackData("buttonEndomorph");


        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(buttonEctomorph);
        keyboardButtonsRow1.add(buttonMezomorph);
        keyboardButtonsRow1.add(buttonEndomorph);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

}
