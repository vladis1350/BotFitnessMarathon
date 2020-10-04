package com.example.fitnessMarathonBot.botapi.client.personalInformation;

import com.example.fitnessMarathonBot.bean.Bot;
import com.example.fitnessMarathonBot.botapi.BotState;
import com.example.fitnessMarathonBot.botapi.InputMessageHandler;
import com.example.fitnessMarathonBot.cache.UserDataCache;
import com.example.fitnessMarathonBot.fitnessDB.bean.BodyParam;
import com.example.fitnessMarathonBot.fitnessDB.bean.User;
import com.example.fitnessMarathonBot.fitnessDB.bean.UserProfile;
import com.example.fitnessMarathonBot.fitnessDB.repository.BodyParamRepositoryImpl;
import com.example.fitnessMarathonBot.fitnessDB.repository.UserProfileImpl;
import com.example.fitnessMarathonBot.fitnessDB.repository.UserRepositoryImpl;
import com.example.fitnessMarathonBot.service.ReplyMessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author has been inspired by Sergei Viacheslaev's work
 */
@Component
public class ShowProfileHandler implements InputMessageHandler {

    @Autowired
    private ReplyMessagesService messagesService;

    @Autowired
    private UserProfileImpl userProfileRepo;

    @Autowired
    private UserRepositoryImpl userRepository;

    public ShowProfileHandler() {

    }

    @Override
    public SendMessage handle(Message message) {
        SendMessage sendMessage = null;
        long chatId = message.getChatId();
        User user = userRepository.findUserByChatId(chatId);
        if (userProfileRepo.findUserProfileByPkUser(user) == null) {
            sendMessage = new SendMessage(chatId, "Данные отсутствуют");
        } else {
            UserProfile userProfile = userProfileRepo.findUserProfileByPkUser(user);
            String profileInfo = messagesService.getReplyText("reply.profileInfo");
            profileInfo = String.format(profileInfo, userProfile.getFullName(), userProfile.getUserAge(),
                    userProfile.getPk().getBodyParam().getHeight(), userProfile.getPk().getBodyParam().getWeight(),
                    userProfile.getPk().getBodyParam().getArm(), userProfile.getPk().getBodyParam().getStomach(),
                    userProfile.getPk().getBodyParam().getNeck(), userProfile.getPk().getBodyParam().getHips(),
                    userProfile.getPk().getBodyParam().getHip(), userProfile.getPk().getBodyParam().getChest(),
                    userProfile.getPk().getBodyParam().getWaist(), userProfile.getPk().getBodyParam().getShin(),
                    userProfile.getPk().getBodyParam().getDate()).replaceAll("null", "0");
            sendMessage = new SendMessage(chatId, profileInfo).setReplyMarkup(getInlineMessageButtons());
        }
        return sendMessage;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.MY_INFORMATION;
    }

    private InlineKeyboardMarkup getInlineMessageButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonPersonalInfo = new InlineKeyboardButton().setText("Заполнить остальные данные");

        //Every button must have callBackData, or else not work !
        buttonPersonalInfo.setCallbackData("buttonPersonalInfo");


        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(buttonPersonalInfo);


        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }
}