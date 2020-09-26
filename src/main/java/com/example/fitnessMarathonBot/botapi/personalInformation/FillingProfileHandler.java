package com.example.fitnessMarathonBot.botapi.personalInformation;


import com.example.fitnessMarathonBot.bean.Bot;
import com.example.fitnessMarathonBot.bean.UserProfileData;
import com.example.fitnessMarathonBot.botapi.BotState;
import com.example.fitnessMarathonBot.botapi.InputMessageHandler;
import com.example.fitnessMarathonBot.cache.UserDataCache;
import com.example.fitnessMarathonBot.service.MainMenuService;
import com.example.fitnessMarathonBot.service.ReplyMessagesService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

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

        ReplyKeyboardMarkup replyKeyboardMarkup = getMainMenuKeyboard();

        UserProfileData profileData = userDataCache.getUserProfileData(userId);
        BotState botState = userDataCache.getUsersCurrentBotState(userId);

        SendMessage replyToUser = null;

        if (botState.equals(BotState.ASK_NAME)) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askName");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_AGE);
        }
        if(botState.equals(BotState.ASK_AGE)) {
            profileData.setName(usersAnswer);
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askAge");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_HEIGHT);
        }
        if(botState.equals(BotState.ASK_HEIGHT)) {
            profileData.setAge(usersAnswer);
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askHeight");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_WEIGHT);
        }
        if(botState.equals(BotState.ASK_WEIGHT)) {
            profileData.setHeight(usersAnswer);
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askWeight");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_PHYSIQUE);
        }
        if(botState.equals(BotState.ASK_PHYSIQUE)) {
            profileData.setWeight(usersAnswer);
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askPhysique");
            userDataCache.setUsersCurrentBotState(userId, BotState.PROFILE_FILLED);
        }
        if(botState.equals(BotState.PROFILE_FILLED)) {
            profileData.setPhysique(usersAnswer);
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_NAME);
            replyToUser = messagesService.getReplyMessage(chatId, "reply.profileFilled");

            replyToUser.setReplyMarkup(replyKeyboardMarkup);
        }

        userDataCache.saveUserProfileData(userId, profileData);
        return replyToUser;
    }

    private ReplyKeyboardMarkup getMainMenuKeyboard() {

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        KeyboardRow row4 = new KeyboardRow();
        row1.add(new KeyboardButton("План на сегодня"));
        row2.add(new KeyboardButton("Ужедневный отчёт"));
        row3.add(new KeyboardButton("Моя информация"));
        row4.add(new KeyboardButton("Написать Ксюше"));
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboard.add(row4);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

}
