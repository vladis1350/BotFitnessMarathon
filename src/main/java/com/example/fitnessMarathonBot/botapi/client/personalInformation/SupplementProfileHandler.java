package com.example.fitnessMarathonBot.botapi.client.personalInformation;

import com.example.fitnessMarathonBot.botapi.BotState;
import com.example.fitnessMarathonBot.botapi.InputMessageHandler;
import com.example.fitnessMarathonBot.cache.UserDataCache;
import com.example.fitnessMarathonBot.fitnessDB.bean.BodyParam;
import com.example.fitnessMarathonBot.fitnessDB.bean.User;
import com.example.fitnessMarathonBot.fitnessDB.repository.BodyParamRepositoryImpl;
import com.example.fitnessMarathonBot.fitnessDB.repository.UserRepositoryImpl;
import com.example.fitnessMarathonBot.service.ReplyMessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class SupplementProfileHandler implements InputMessageHandler {
    private UserDataCache userDataCache;

    @Autowired
    private ReplyMessagesService messagesService;

    @Autowired
    private BodyParamRepositoryImpl bodyParamRepository;

    @Autowired
    private UserRepositoryImpl userRepository;

    public SupplementProfileHandler(UserDataCache userDataCache) {
        this.userDataCache = userDataCache;
    }

    @Override
    public SendMessage handle(Message message) {
        if (userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.ASK_SUPPLEMENT_PERSONAL_INFO)) {
            userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.ASK_NECK);
        }
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ASK_SUPPLEMENT_PERSONAL_INFO;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        String usersAnswer = inputMsg.getText();
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();
        SendMessage replyToUser = null;
        BotState botState = userDataCache.getUsersCurrentBotState(userId);

        if (botState.equals(BotState.ASK_ARM)) {
            System.out.println("+++++++");
            User user = userRepository.findUserByChatId(inputMsg.getChatId());
            if (bodyParamRepository.findBodyParamByUser(user) != null) {
                BodyParam bodyParam = bodyParamRepository.findBodyParamByUser(user);
                bodyParam.setNeck(usersAnswer);
                bodyParamRepository.save(bodyParam);
            }
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askArm");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_CHEST);
        }
        if (botState.equals(BotState.ASK_CHEST)) {
            User user = userRepository.findUserByChatId(inputMsg.getChatId());
            if (bodyParamRepository.findBodyParamByUser(user) != null) {
                BodyParam bodyParam = bodyParamRepository.findBodyParamByUser(user);
                bodyParam.setArm(usersAnswer);
                bodyParamRepository.save(bodyParam);
            }
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askChest");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_WAIST);
        }
        if (botState.equals(BotState.ASK_WAIST)) {
            User user = userRepository.findUserByChatId(inputMsg.getChatId());
            if (bodyParamRepository.findBodyParamByUser(user) != null) {
                BodyParam bodyParam = bodyParamRepository.findBodyParamByUser(user);
                bodyParam.setChest(usersAnswer);
                bodyParamRepository.save(bodyParam);
            }
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askWaist");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_BELLY);

        }
        if (botState.equals(BotState.ASK_BELLY)) {
            User user = userRepository.findUserByChatId(inputMsg.getChatId());
            if (bodyParamRepository.findBodyParamByUser(user) != null) {
                BodyParam bodyParam = bodyParamRepository.findBodyParamByUser(user);
                bodyParam.setWaist(usersAnswer);
                bodyParamRepository.save(bodyParam);
            }
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askBelly");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_HIPS);

        }
        if (botState.equals(BotState.ASK_HIPS)) {
            User user = userRepository.findUserByChatId(inputMsg.getChatId());
            if (bodyParamRepository.findBodyParamByUser(user) != null) {
                BodyParam bodyParam = bodyParamRepository.findBodyParamByUser(user);
                bodyParam.setStomach(usersAnswer);
                bodyParamRepository.save(bodyParam);
            }
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askThighs");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_HIP);
        }
        if (botState.equals(BotState.ASK_HIP)) {
            User user = userRepository.findUserByChatId(inputMsg.getChatId());
            if (bodyParamRepository.findBodyParamByUser(user) != null) {
                BodyParam bodyParam = bodyParamRepository.findBodyParamByUser(user);
                bodyParam.setHips(usersAnswer);
                bodyParamRepository.save(bodyParam);
            }
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askThigh");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_SHIN);
        }
        if (botState.equals(BotState.ASK_SHIN)) {
            User user = userRepository.findUserByChatId(inputMsg.getChatId());
            if (bodyParamRepository.findBodyParamByUser(user) != null) {
                BodyParam bodyParam = bodyParamRepository.findBodyParamByUser(user);
                bodyParam.setHip(usersAnswer);
                bodyParamRepository.save(bodyParam);
            }
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askShin");
            userDataCache.setUsersCurrentBotState(userId, BotState.PERSONAL_INFO_FILLED);
        }
        if (botState.equals(BotState.PERSONAL_INFO_FILLED)) {
            User user = userRepository.findUserByChatId(inputMsg.getChatId());
            Date date = new Date();
            SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");
            if (bodyParamRepository.findBodyParamByUser(user) != null) {
                BodyParam bodyParam = bodyParamRepository.findBodyParamByUser(user);
                bodyParam.setShin(usersAnswer);
                bodyParam.setDate(formatForDateNow.format(date));
                bodyParamRepository.save(bodyParam);
            }
            replyToUser = messagesService.getReplyMessage(chatId, "reply.personalInfoFilled");
            userDataCache.setUsersCurrentBotState(userId, BotState.MAIN_MENU);
        }

        return replyToUser;
    }

}
