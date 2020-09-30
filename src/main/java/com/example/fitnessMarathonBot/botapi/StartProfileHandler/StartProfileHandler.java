package com.example.fitnessMarathonBot.botapi.StartProfileHandler;

import com.example.fitnessMarathonBot.bean.Bot;
import com.example.fitnessMarathonBot.botapi.BotState;
import com.example.fitnessMarathonBot.botapi.InputMessageHandler;
import com.example.fitnessMarathonBot.cache.UserDataCache;
import com.example.fitnessMarathonBot.service.AdminMainMenuService;
import com.example.fitnessMarathonBot.service.ReplyMessagesService;
import com.example.fitnessMarathonBot.utils.Emojis;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
@Setter
public class StartProfileHandler implements InputMessageHandler {
    private ReplyMessagesService messagesService;
    private UserDataCache userDataCache;
    private AdminMainMenuService adminMainMenuService;
    private Bot myBot;

    private static int mess_count = 0;

    public StartProfileHandler(ReplyMessagesService messagesService, UserDataCache userDataCache,
                               AdminMainMenuService adminMainMenuService, @Lazy Bot myBot) {
        this.messagesService = messagesService;
        this.userDataCache = userDataCache;
        this.adminMainMenuService = adminMainMenuService;
        this.myBot = myBot;
    }

    @SneakyThrows
    @Override
    public SendMessage handle(Message message) {

        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ASK_START;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        mess_count++;
        List<SendMessage> sendMessages = new ArrayList<>();
        String messageStart = "";
        String messageLinkInstagram = "";
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();
        SendMessage replyToUser = null;
        if (userId == 1331718111) {
            replyToUser = adminMainMenuService.getAdminMainMenuMessage(chatId, "Привет! я FitnessMarathonBot - твой помощник в твем не легком деле. Используй главное меню что бы начать работу. Действуй!!! =)");
        } else {
            messageStart = messagesService.getReplyText("reply.askStart");
            messageLinkInstagram = String.format(messagesService.getReplyText("reply.linkInstagram"),
                    Emojis.POINT_DOWN, Emojis.POINT_DOWN, Emojis.POINT_DOWN, Emojis.POINT_DOWN, Emojis.POINT_DOWN, Emojis.HEART);
            sendMessages.add(new SendMessage(chatId, messageStart));
            sendMessages.add(new SendMessage(chatId, messageLinkInstagram));
            myBot.sendListMessages(sendMessages);

            replyToUser = new SendMessage(chatId,
                    String.format(messagesService.getReplyText("reply.requestEnterYourData"), Emojis.MEMO, Emojis.POINT_DOWN));

            replyToUser.setReplyMarkup(getInlineMessageButtons());
        }

        return replyToUser;
    }

    private InlineKeyboardMarkup getInlineMessageButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonInputPersonalInfo = new InlineKeyboardButton().setText("Заполнить личную информацию");

        //Every button must have callBackData, or else not work !
        buttonInputPersonalInfo.setCallbackData("buttonInputPersonalInfo");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(buttonInputPersonalInfo);


        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }
}
