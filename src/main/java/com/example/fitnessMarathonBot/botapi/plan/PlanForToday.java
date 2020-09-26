package com.example.fitnessMarathonBot.botapi.plan;

import com.example.fitnessMarathonBot.bean.UserProfileData;
import com.example.fitnessMarathonBot.botapi.BotState;
import com.example.fitnessMarathonBot.botapi.InputMessageHandler;
import com.example.fitnessMarathonBot.cache.UserDataCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class PlanForToday implements InputMessageHandler {
    private UserDataCache userDataCache;

    public PlanForToday(UserDataCache userDataCache) {
        this.userDataCache = userDataCache;
    }

    @Override
    public SendMessage handle(Message message) {
        final int userId = message.getFrom().getId();
        final UserProfileData profileData = userDataCache.getUserProfileData(userId);

        userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_PLAN_FOR_TODAY);
        return new SendMessage(message.getChatId(), "План еще не составлен вашим тренером. Пожалуйста, ожидайте увежомления!");
    }

    @Override
    public BotState getHandlerName() {
        return BotState.PLAN_FOR_TODAY;
    }
}
