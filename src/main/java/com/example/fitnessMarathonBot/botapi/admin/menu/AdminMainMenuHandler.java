package com.example.fitnessMarathonBot.botapi.admin.menu;

import com.example.fitnessMarathonBot.botapi.BotState;
import com.example.fitnessMarathonBot.botapi.InputMessageHandler;
import com.example.fitnessMarathonBot.cache.UserDataCache;
import com.example.fitnessMarathonBot.service.UserMainMenuService;
import com.example.fitnessMarathonBot.service.ReplyMessagesService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class AdminMainMenuHandler implements InputMessageHandler {
    private ReplyMessagesService messagesService;
    private UserMainMenuService userMainMenuService;
    private UserDataCache userDataCache;

    public AdminMainMenuHandler(ReplyMessagesService messagesService, UserMainMenuService userMainMenuService, UserDataCache userDataCache) {
        this.messagesService = messagesService;
        this.userMainMenuService = userMainMenuService;
        this.userDataCache = userDataCache;
    }

    @Override
    public SendMessage handle(Message message) {

        userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.SHOW_ADMIN_MAIN_MENU);
        return userMainMenuService.getUserMainMenuMessage(message.getChatId(), messagesService.getReplyText("reply.ShowAdminMainMenu"));
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ADMIN_MAIN_MENU;
    }


}
