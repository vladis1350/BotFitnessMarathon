package com.example.fitnessMarathonBot.botapi.admin.menu;

import com.example.fitnessMarathonBot.bean.UserProfileData;
import com.example.fitnessMarathonBot.botapi.BotState;
import com.example.fitnessMarathonBot.botapi.InputMessageHandler;
import com.example.fitnessMarathonBot.cache.UserDataCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class ParticipantsAndResultHandler implements InputMessageHandler {
    private UserDataCache userDataCache;

    public ParticipantsAndResultHandler(UserDataCache userDataCache) {
        this.userDataCache = userDataCache;
    }

    @Override
    public SendMessage handle(Message message) {
        final int userId = message.getFrom().getId();
        final UserProfileData profileData = userDataCache.getUserProfileData(userId);

        userDataCache.setUsersCurrentBotState(userId, BotState.VIEW_PARTICIPANTS_ADN_RESULTS);
        return new SendMessage(message.getChatId(), "Активные марафоны отсутствуют, вы можете начать новый марафон нажав кнопку \"Начать новый марафон\"");
    }

    @Override
    public BotState getHandlerName() {
        return BotState.VIEW_PARTICIPANTS_ADN_RESULTS;
    }

}
