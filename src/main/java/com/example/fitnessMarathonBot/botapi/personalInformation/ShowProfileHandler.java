package com.example.fitnessMarathonBot.botapi.personalInformation;

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

    public ShowProfileHandler(UserDataCache userDataCache) {
        this.userDataCache = userDataCache;
    }

    @Override
    public SendMessage handle(Message message) {
        final int userId = message.getFrom().getId();
        final UserProfileData profileData = userDataCache.getUserProfileData(userId);

        userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
        return new SendMessage(message.getChatId(), String.format("%s%nИмя %s%nВозраст %s%nВес %s%nРост" +
                        " %s%nТелосложение %s",
                "Ваши данные", profileData.getName(), profileData.getAge(), profileData.getWeight(),
                profileData.getHeight(), profileData.getPhysique()));
    }

    @Override
    public BotState getHandlerName() {
        return BotState.MY_INFORMATION;
    }
}