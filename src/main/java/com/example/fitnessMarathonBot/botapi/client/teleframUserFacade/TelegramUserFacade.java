package com.example.fitnessMarathonBot.botapi.client.teleframUserFacade;

import com.example.fitnessMarathonBot.bean.Bot;
import com.example.fitnessMarathonBot.bean.UserProfileData;
import com.example.fitnessMarathonBot.botapi.BotState;
import com.example.fitnessMarathonBot.botapi.BotStateContext;
import com.example.fitnessMarathonBot.cache.UserDataCache;
import com.example.fitnessMarathonBot.service.LocaleMessageService;
import com.example.fitnessMarathonBot.service.UserMainMenuService;
import com.example.fitnessMarathonBot.service.ReplyMessagesService;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * @author get inspired by Sergei Viacheslaev's video
 */
@Component
@Slf4j
@Getter
@Setter
public class TelegramUserFacade {
    private BotStateContext botStateContext;
    private UserDataCache userDataCache;
    private UserMainMenuService userMainMenuService;
    private Bot myBot;
    private ReplyMessagesService messagesService;


    public TelegramUserFacade(BotStateContext botStateContext, UserDataCache userDataCache, UserMainMenuService userMainMenuService,
                              @Lazy Bot myBot, ReplyMessagesService messagesService) {
        this.botStateContext = botStateContext;
        this.userDataCache = userDataCache;
        this.userMainMenuService = userMainMenuService;
        this.myBot = myBot;
        this.messagesService = messagesService;
    }

    public BotApiMethod<?> handleUpdate(Update update) {
        SendMessage replyMessage = null;

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            log.info("New callbackQuery from User: {}, userId: {}, with data: {}", update.getCallbackQuery().getFrom().getUserName(),
                    callbackQuery.getFrom().getId(), update.getCallbackQuery().getData());
            return processCallbackQuery(callbackQuery);
        }


        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            log.info("New message from User:{}, userId: {}, chatId: {},  with text: {}",
                    message.getFrom().getUserName(), message.getFrom().getId(), message.getChatId(), message.getText());
            replyMessage = handleInputMessage(message);
        }

        return replyMessage;
    }

    private SendMessage handleInputMessage(Message message) {
        String inputMsg = message.getText();
        int userId = message.getFrom().getId();
        long chatId = message.getChatId();
        BotState botState;
        SendMessage replyMessage;

        switch (inputMsg) {
            case "/start":
                botState = BotState.ASK_START;
                break;
            case "Ввод антропометрических данных":
                botState = BotState.ASK_PERSONAL_INFO;
                break;
            case "Задание":
                botState = BotState.TASK;
                break;
            case "Отчёт":
                botState = BotState.REPORT_OF_THE_DAY;
                break;
            case "Моя информация":
                botState = BotState.MY_INFORMATION;
                break;
            case "Ссылка на чат":
                botState = BotState.LINK_TO_CHAT;
                break;
            default:
                botState = userDataCache.getUsersCurrentBotState(userId);
                break;
        }

        userDataCache.setUsersCurrentBotState(userId, botState);

        replyMessage = botStateContext.processInputMessage(botState, message);
        return replyMessage;
    }


    private BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery) {
        final long chatId = buttonQuery.getMessage().getChatId();
        final int userId = buttonQuery.getFrom().getId();
        LocaleMessageService localeMessageService;
        BotApiMethod<?> callBackAnswer = userMainMenuService.getUserMainMenuMessage(chatId);

        if (buttonQuery.getData().equals("buttonInputPersonalInfo")) {
            callBackAnswer = new SendMessage(chatId, messagesService.getReplyText("reply.askName"));
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_AGE);
        } else if (buttonQuery.getData().equals("buttonPersonalInfo")) {
            callBackAnswer = new SendMessage(chatId, messagesService.getReplyText("reply.askNeck"));
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_ARM);
        }

        return callBackAnswer;


    }


    private AnswerCallbackQuery sendAnswerCallbackQuery(String text, boolean alert, CallbackQuery callbackquery) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackquery.getId());
        answerCallbackQuery.setShowAlert(alert);
        answerCallbackQuery.setText(text);
        return answerCallbackQuery;
    }

    @SneakyThrows
    public File getUsersProfile(int userId) {
        UserProfileData userProfileData = userDataCache.getUserProfileData(userId);
        File profileFile = ResourceUtils.getFile("classpath:static/docs/Your_order.TXT");

        try (FileWriter fw = new FileWriter(profileFile.getAbsoluteFile());
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(userProfileData.toString());
        }


        return profileFile;

    }

}