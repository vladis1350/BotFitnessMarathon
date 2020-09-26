package com.example.fitnessMarathonBot.botapi.plan;

import com.example.fitnessMarathonBot.bean.UserProfileData;
import com.example.fitnessMarathonBot.botapi.BotState;
import com.example.fitnessMarathonBot.botapi.InputMessageHandler;
import com.example.fitnessMarathonBot.cache.UserDataCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class DailyReportHandler implements InputMessageHandler {
    private UserDataCache userDataCache;

    public DailyReportHandler(UserDataCache userDataCache) {
        this.userDataCache = userDataCache;
    }

    @Override
    public SendMessage handle(Message message) {
        final int userId = message.getFrom().getId();
        final UserProfileData profileData = userDataCache.getUserProfileData(userId);

        userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_DAILY_REPORT);
        return new SendMessage(message.getChatId(), "У вас пока нет отчетов!");
    }

    @Override
    public BotState getHandlerName() {
        return BotState.DAILY_REPORT;
    }
}
