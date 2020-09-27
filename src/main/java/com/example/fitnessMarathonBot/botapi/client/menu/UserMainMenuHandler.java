package com.example.fitnessMarathonBot.botapi.client.menu;

import com.example.fitnessMarathonBot.botapi.BotState;
import com.example.fitnessMarathonBot.botapi.InputMessageHandler;
import com.example.fitnessMarathonBot.cache.UserDataCache;
import com.example.fitnessMarathonBot.service.UserMainMenuService;
import com.example.fitnessMarathonBot.service.ReplyMessagesService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class UserMainMenuHandler implements InputMessageHandler {
    private ReplyMessagesService messagesService;
    private UserMainMenuService userMainMenuService;
    private UserDataCache userDataCache;

    public UserMainMenuHandler(ReplyMessagesService messagesService, UserMainMenuService userMainMenuService, UserDataCache userDataCache) {
        this.messagesService = messagesService;
        this.userMainMenuService = userMainMenuService;
        this.userDataCache = userDataCache;
    }

    @Override
    public SendMessage handle(Message message) {

        userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.SHOW_MAIN_MENU);
        return userMainMenuService.getUserMainMenuMessage(message.getChatId(), messagesService.getReplyText("reply.ShowUserMainMenu"));
    }

    @Override
    public BotState getHandlerName() {
        return BotState.MAIN_MENU;
    }


}
