package com.example.fitnessMarathonBot.botapi.client.menu;

import com.example.fitnessMarathonBot.bean.UserProfileData;
import com.example.fitnessMarathonBot.botapi.BotState;
import com.example.fitnessMarathonBot.botapi.InputMessageHandler;
import com.example.fitnessMarathonBot.cache.UserDataCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class ClientMealPanHandler implements InputMessageHandler {
    private UserDataCache userDataCache;

    public ClientMealPanHandler(UserDataCache userDataCache) {
        this.userDataCache = userDataCache;
    }

    @Override
    public SendMessage handle(Message message) {
        final int userId = message.getFrom().getId();
        final UserProfileData profileData = userDataCache.getUserProfileData(userId);

        userDataCache.setUsersCurrentBotState(userId, BotState.CLIENT_MEAL_PLAN);
        return new SendMessage(message.getChatId(), "План питания для вас на три дня: \n" +
                "\n" +
                " Кушай кашу на обед укрепляй иммунитет!");
    }

    @Override
    public BotState getHandlerName() {
        return BotState.CLIENT_MEAL_PLAN;
    }
}
