package com.example.fitnessMarathonBot.botapi.client.personalInformation;

import com.example.fitnessMarathonBot.bean.UserProfileData;
import com.example.fitnessMarathonBot.botapi.BotState;
import com.example.fitnessMarathonBot.botapi.InputMessageHandler;
import com.example.fitnessMarathonBot.cache.UserDataCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * @author has been inspired by Sergei Viacheslaev's work
 */
@Component
public class ShowProfileHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
//    private UserProfileData userProfileData;

    public ShowProfileHandler(UserDataCache userDataCache) {
        this.userDataCache = userDataCache;
    }

    @Override
    public SendMessage handle(Message message) {
        final int userId = message.getFrom().getId();
        final UserProfileData profileData = userDataCache.getUserProfileData(userId);

        userDataCache.setUsersCurrentBotState(userId, BotState.MY_INFORMATION);
        return new SendMessage(message.getChatId(), profileData.toString());
    }

    @Override
    public BotState getHandlerName() {
        return BotState.MY_INFORMATION;
    }
}