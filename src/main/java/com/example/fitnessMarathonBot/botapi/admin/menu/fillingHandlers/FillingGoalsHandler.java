package com.example.fitnessMarathonBot.botapi.admin.menu.fillingHandlers;

import com.example.fitnessMarathonBot.botapi.BotState;
import com.example.fitnessMarathonBot.botapi.InputMessageHandler;
import com.example.fitnessMarathonBot.cache.UserDataCache;
import com.example.fitnessMarathonBot.fitnessDB.bean.ListGoals;
import com.example.fitnessMarathonBot.fitnessDB.repository.ListGoalsRepository;
import com.example.fitnessMarathonBot.service.ReplyMessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class FillingGoalsHandler implements InputMessageHandler {
    private UserDataCache userDataCache;

    @Autowired
    private ListGoalsRepository listGoalsRepository;

    @Autowired
    private ReplyMessagesService messagesService;

    private static String date = "";

    public FillingGoalsHandler(UserDataCache userDataCache, ReplyMessagesService messagesService) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
    }

    @Override
    public SendMessage handle(Message message) {
        if (userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.ASK_ADMIN_GOALS)) {
            userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.ASK_ADMIN_TIME_STAMP);
        }
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ASK_ADMIN_GOALS;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        String usersAnswer = inputMsg.getText();
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();

        Date dateObj = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");
        String currentDate = formatForDateNow.format(dateObj);

        SendMessage replyToUser = null;
        BotState botState = userDataCache.getUsersCurrentBotState(userId);

        if (botState.equals(BotState.ASK_ADMIN_TASK_ONE)) {
            if (checkDate(usersAnswer)) {
                date = usersAnswer;
                if (listGoalsRepository.findListGoalsByTimeStamp(usersAnswer) != null) {
                    return new SendMessage(chatId, "Задания на эту дату уже записаны");
                } else {
                    ListGoals listGoals = ListGoals.builder()
                            .taskOne(usersAnswer)
                            .timeStamp(usersAnswer)
                            .build();
                    listGoalsRepository.save(listGoals);
                    replyToUser = new SendMessage(chatId, messagesService.getReplyText("reply.askAdminTaskOne"));
                    userDataCache.setUsersCurrentBotState(userId, BotState.ASK_ADMIN_TASK_TWO);
                }
            } else {
                replyToUser = new SendMessage(chatId, "Не верный формат даты!");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_ADMIN_TASK_ONE);
            }
        }

        if (botState.equals(BotState.ASK_ADMIN_TASK_TWO)) {
            ListGoals listGoals = listGoalsRepository.findListGoalsByTimeStamp(date);
            listGoals.setTaskOne(usersAnswer);
            listGoalsRepository.save(listGoals);
            replyToUser = new SendMessage(chatId, messagesService.getReplyText("reply.askAdminTaskTwo"));
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_ADMIN_TASK_THREE);
        }
        if (botState.equals(BotState.ASK_ADMIN_TASK_THREE)) {
            ListGoals listGoals = listGoalsRepository.findListGoalsByTimeStamp(date);
            listGoals.setTaskTwo(usersAnswer);
            listGoalsRepository.save(listGoals);
            replyToUser = new SendMessage(chatId, messagesService.getReplyText("reply.askAdminTaskThree"));
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_ADMIN_TASK_FOUR);
        }
        if (botState.equals(BotState.ASK_ADMIN_TASK_FOUR)) {
            ListGoals listGoals = listGoalsRepository.findListGoalsByTimeStamp(date);
            listGoals.setTaskThree(usersAnswer);
            listGoalsRepository.save(listGoals);
            replyToUser = new SendMessage(chatId, messagesService.getReplyText("reply.askAdminTaskFour"));
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_ADMIN_TASK_FIVE);
        }
        if (botState.equals(BotState.ASK_ADMIN_TASK_FIVE)) {
            ListGoals listGoals = listGoalsRepository.findListGoalsByTimeStamp(date);
            listGoals.setTaskFour(usersAnswer);
            listGoalsRepository.save(listGoals);
            replyToUser = new SendMessage(chatId, messagesService.getReplyText("reply.askAdminTaskFive"));
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_ADMIN_TASK_SIX);
        }
        if (botState.equals(BotState.ASK_ADMIN_TASK_SIX)) {
            ListGoals listGoals = listGoalsRepository.findListGoalsByTimeStamp(date);
            listGoals.setTaskFive(usersAnswer);
            listGoalsRepository.save(listGoals);
            replyToUser = new SendMessage(chatId, messagesService.getReplyText("reply.askAdminTaskSix"));
            userDataCache.setUsersCurrentBotState(userId, BotState.GOALS_FILLED);
        }
        if (botState.equals(BotState.GOALS_FILLED)) {
            ListGoals listGoals = listGoalsRepository.findListGoalsByTimeStamp(date);
            listGoals.setTaskSix(usersAnswer);
            listGoalsRepository.save(listGoals);
            replyToUser = new SendMessage(chatId, "Задания на дату: " + date + " успешно записаны!");
            userDataCache.setUsersCurrentBotState(userId, BotState.ADMIN_MAIN_MENU);
        }
        return replyToUser;
    }

    private boolean checkDate(String date) {
        return date.matches("^(?:(?:31(\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$");
    }
}
