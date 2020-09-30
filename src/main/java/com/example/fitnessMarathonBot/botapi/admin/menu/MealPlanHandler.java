package com.example.fitnessMarathonBot.botapi.admin.menu;

import com.example.fitnessMarathonBot.bean.UserProfileData;
import com.example.fitnessMarathonBot.botapi.BotState;
import com.example.fitnessMarathonBot.botapi.InputMessageHandler;
import com.example.fitnessMarathonBot.cache.UserDataCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class MealPlanHandler implements InputMessageHandler {
    private UserDataCache userDataCache;

    public MealPlanHandler(UserDataCache userDataCache) {
        this.userDataCache = userDataCache;
    }

    @Override
    public SendMessage handle(Message message) {
        final int userId = message.getFrom().getId();
        final UserProfileData profileData = userDataCache.getUserProfileData(userId);

        userDataCache.setUsersCurrentBotState(userId, BotState.MEAL_PLAN);
        return new SendMessage(message.getChatId(), "Здесь будут планы питания");
    }

    @Override
    public BotState getHandlerName() {
        return BotState.MEAL_PLAN;
    }
}
