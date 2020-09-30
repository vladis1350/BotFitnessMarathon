package com.example.fitnessMarathonBot.botapi.client.personalInformation;


import com.example.fitnessMarathonBot.bean.Bot;
import com.example.fitnessMarathonBot.bean.UserProfileData;
import com.example.fitnessMarathonBot.botapi.BotState;
import com.example.fitnessMarathonBot.botapi.InputMessageHandler;
import com.example.fitnessMarathonBot.cache.UserDataCache;
import com.example.fitnessMarathonBot.service.ReplyMessagesService;
import com.example.fitnessMarathonBot.service.UserMainMenuService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@Getter
@Setter
public class FillingProfileHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private ReplyMessagesService messagesService;
    private Bot myBot;
    private UserMainMenuService userMainMenuService;

    public FillingProfileHandler(UserDataCache userDataCache, UserMainMenuService userMainMenuService,
                                 ReplyMessagesService messagesService, @Lazy Bot myBot) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
        this.myBot = myBot;
        this.userMainMenuService = userMainMenuService;
    }

    @Override
    public SendMessage handle(Message message) {
        if (userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.ASK_PERSONAL_INFO)) {
            userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.ASK_NAME);
        }

        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ASK_PERSONAL_INFO;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        String usersAnswer = inputMsg.getText();
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();

//        ReplyKeyboardMarkup replyKeyboardMarkup = getMainMenuKeyboard();

        UserProfileData profileData = userDataCache.getUserProfileData(userId);
        BotState botState = userDataCache.getUsersCurrentBotState(userId);

        SendMessage replyToUser = null;

        if (botState.equals(BotState.ASK_AGE)) {
            System.out.println(usersAnswer);
            profileData.setName(usersAnswer);
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askAge");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_HEIGHT);
        }
        if (botState.equals(BotState.ASK_HEIGHT)) {
            if (userAnswerIsCorrect(usersAnswer)) {
                profileData.setAge(usersAnswer);
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askHeight");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_WEIGHT);
            } else {
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askAge");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_HEIGHT);
            }
        }
        if (botState.equals(BotState.ASK_WEIGHT)) {
            if (userAnswerIsCorrect(usersAnswer)) {
                profileData.setHeight(Double.parseDouble(usersAnswer));
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askWeight");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_NECK);
            } else {
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askHeight");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_WEIGHT);
            }
        }
        if (botState.equals(BotState.ASK_NECK)) {
            if (userAnswerIsCorrect(usersAnswer)) {
                profileData.setWeight(Double.parseDouble(usersAnswer));
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askNeck");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_ARM);
            } else {
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askWeight");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_NECK);
            }
        }
        if (botState.equals(BotState.ASK_ARM)) {
            if (userAnswerIsCorrect(usersAnswer)) {
                profileData.setNeck(Double.parseDouble(usersAnswer));
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askArm");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_CHEST);
            } else {
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askNeck");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_ARM);
            }
        }
        if (botState.equals(BotState.ASK_CHEST)) {
            if (userAnswerIsCorrect(usersAnswer)) {
                profileData.setArm(Double.parseDouble(usersAnswer));
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askChest");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_WAIST);
            } else {
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askArm");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_CHEST);
            }
        }
        if (botState.equals(BotState.ASK_WAIST)) {
            if (userAnswerIsCorrect(usersAnswer)) {
                profileData.setChest(Double.parseDouble(usersAnswer));
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askWaist");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_BELLY);
            } else {
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askChest");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_WAIST);
            }
        }
        if (botState.equals(BotState.ASK_BELLY)) {
            if (userAnswerIsCorrect(usersAnswer)) {
                profileData.setWaist(Double.parseDouble(usersAnswer));
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askBelly");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_THIGHS);
            } else {
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askWaist");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_BELLY);
            }
        }
        if (botState.equals(BotState.ASK_THIGHS)) {
            if (userAnswerIsCorrect(usersAnswer)) {
                profileData.setBelly(Double.parseDouble(usersAnswer));
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askThighs");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_THIGH);
            } else {
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askBelly");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_THIGHS);
            }
        }
        if (botState.equals(BotState.ASK_THIGH)) {
            if (userAnswerIsCorrect(usersAnswer)) {
                profileData.setThighs(Double.parseDouble(usersAnswer));
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askThigh");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_SHIN);
            } else {
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askThighs");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_THIGH);
            }
        }
        if (botState.equals(BotState.ASK_SHIN)) {
            if (userAnswerIsCorrect(usersAnswer)) {
                profileData.setThigh(Double.parseDouble(usersAnswer));
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askShin");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_DATE);
            } else {
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askThigh");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_SHIN);
            }
        }
        if (botState.equals(BotState.ASK_DATE)) {
            if (userAnswerIsCorrect(usersAnswer)) {
                profileData.setShin(Double.parseDouble(usersAnswer));
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askDate");
                userDataCache.setUsersCurrentBotState(userId, BotState.PROFILE_FILLED);
            } else {
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askShin");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_DATE);
            }
        }
        if (botState.equals(BotState.PROFILE_FILLED)) {
            profileData.setDate(usersAnswer);
//            replyToUser = messagesService.getReplyMessage(chatId, "reply.profileFilled");
            replyToUser = userMainMenuService.getUserMainMenuMessage(chatId, "Профиль успешно заполнен, свои данные вы можете просмотреть в разделе главного меню \"Моя информация\" \nВоспользуйтесь главным меню");
            userDataCache.setUsersCurrentBotState(userId, BotState.MAIN_MENU);
        }


        userDataCache.saveUserProfileData(userId, profileData);
        return replyToUser;
    }

    private boolean userAnswerIsCorrect(String userAnswer) {
        return userAnswer.matches("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?");
    }

    private InlineKeyboardMarkup getInlineMessageButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonEctomorph = new InlineKeyboardButton().setText("Эктоморф");
        InlineKeyboardButton buttonMezomorph = new InlineKeyboardButton().setText("Мезофорф");
        InlineKeyboardButton buttonEndomorph = new InlineKeyboardButton().setText("Эндоморф");

        //Every button must have callBackData, or else not work !
        buttonEctomorph.setCallbackData("buttonEctomorph");
        buttonMezomorph.setCallbackData("buttonMezomorph");
        buttonEndomorph.setCallbackData("buttonEndomorph");


        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(buttonEctomorph);
        keyboardButtonsRow1.add(buttonMezomorph);
        keyboardButtonsRow1.add(buttonEndomorph);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

}
