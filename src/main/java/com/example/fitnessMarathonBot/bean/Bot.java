package com.example.fitnessMarathonBot.bean;

import com.example.fitnessMarathonBot.botapi.admin.telegramAdminFacade.TelegramAdminFacade;
import com.example.fitnessMarathonBot.botapi.client.teleframUserFacade.TelegramUserFacade;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class Bot extends TelegramWebhookBot {
    private String webHookPath;
    private String botUserName;
    private String botToken;

    private TelegramUserFacade telegramUserFacade;
    private TelegramAdminFacade telegramAdminFacade;

    public Bot(TelegramUserFacade telegramUserFacade, TelegramAdminFacade telegramAdminFacade) {
        this.telegramAdminFacade = telegramAdminFacade;
        this.telegramUserFacade = telegramUserFacade;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        int userId = 0;
        if (update.getCallbackQuery() == null){
            userId = update.getMessage().getFrom().getId();
        } else if (update.getMessage() == null) {
            userId = update.getCallbackQuery().getFrom().getId();
        }

        if (userId == 1331718111) {
            return telegramAdminFacade.handleUpdate(update);
        }
            return telegramUserFacade.handleUpdate(update);
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotPath() {
        return webHookPath;
    }
    public void setWebHookPath(String webHookPath) {
        this.webHookPath = webHookPath;
    }

    public void setBotUserName(String botUserName) {
        this.botUserName = botUserName;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    @SneakyThrows
    public void sendPhoto(long chatId, String imageCaption, String imagePath) {
        File image = ResourceUtils.getFile("classpath:" + imagePath);
        SendPhoto sendPhoto = new SendPhoto().setPhoto(image);
        sendPhoto.setChatId(chatId);
        sendPhoto.setCaption(imageCaption);
        execute(sendPhoto);
    }

    @SneakyThrows
    public void sendDocument(long chatId, String caption, File sendFile) {
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(chatId);
        sendDocument.setCaption(caption);
        sendDocument.setDocument(sendFile);
        execute(sendDocument);
    }

    @SneakyThrows
    public void sendListMessages(List<SendMessage> sendMessageList) {
        for (SendMessage message: sendMessageList) {
            execute(message);
            Thread.sleep(10000);
        }
    }

    @SneakyThrows
    public void sendMessageAllParticipantMarathon(List<SendMessage> sendMessageList) {
        for (SendMessage message: sendMessageList) {
            execute(message);
            Thread.sleep(10000);
        }
    }
}
