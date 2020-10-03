package com.example.fitnessMarathonBot.botapi.client.menu.reportHandler;

import com.example.fitnessMarathonBot.botapi.BotState;
import com.example.fitnessMarathonBot.botapi.InputMessageHandler;
import com.example.fitnessMarathonBot.cache.UserDataCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class DailyReportHandler  implements InputMessageHandler {
    private UserDataCache userDataCache;

    public DailyReportHandler(UserDataCache userDataCache) {
        this.userDataCache = userDataCache;
    }

    @Override
    public SendMessage handle(Message message) {

        userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.REPORT_OF_THE_DAY);
        return new SendMessage(message.getChatId(), "Выберите действие").setReplyMarkup(getInlineMessageButtonsReport());
    }

    @Override
    public BotState getHandlerName() {
        return BotState.REPORT_OF_THE_DAY;
    }

    private InlineKeyboardMarkup getInlineMessageButtonsReport() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonReportPhoto = new InlineKeyboardButton().setText("Отправить фото");
        InlineKeyboardButton buttonReportGoals = new InlineKeyboardButton().setText("Отметить цели");

        //Every button must have callBackData, or else not work !
        buttonReportPhoto.setCallbackData("buttonReportPhoto");
        buttonReportGoals.setCallbackData("buttonReportGoals");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(buttonReportPhoto);
        keyboardButtonsRow1.add(buttonReportGoals);


        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }
}
