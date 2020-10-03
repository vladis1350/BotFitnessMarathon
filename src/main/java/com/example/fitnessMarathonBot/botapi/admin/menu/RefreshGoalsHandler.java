package com.example.fitnessMarathonBot.botapi.admin.menu;

import com.example.fitnessMarathonBot.botapi.BotState;
import com.example.fitnessMarathonBot.botapi.InputMessageHandler;
import com.example.fitnessMarathonBot.botapi.admin.adminButtonHandler.AdminButtonHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class RefreshGoalsHandler implements InputMessageHandler {

    @Autowired
    private AdminButtonHandler adminButtonHandler;

    public RefreshGoalsHandler() {}

    @Override
    public SendMessage handle(Message message) {
        long chatId = message.getChatId();

        return adminButtonHandler.getMessageAndGoalsAdminButtons(chatId);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.REFRESH_GOALS;
    }


}
