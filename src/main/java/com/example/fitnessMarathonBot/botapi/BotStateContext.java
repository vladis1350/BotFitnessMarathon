package com.example.fitnessMarathonBot.botapi;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Defines message handlers for each state.
 */
@Component
public class BotStateContext {
    private Map<BotState, InputMessageHandler> messageHandlers = new HashMap<>();

    public BotStateContext(List<InputMessageHandler> messageHandlers) {
        messageHandlers.forEach(handler -> this.messageHandlers.put(handler.getHandlerName(), handler));
    }

    public SendMessage processInputMessage(BotState currentState, Message message) {
        InputMessageHandler currentMessageHandler = findMessageHandler(currentState);
        return currentMessageHandler.handle(message);
    }

    private InputMessageHandler findMessageHandler(BotState currentState) {
        if (isFillingProfileState(currentState)) {
            return messageHandlers.get(BotState.ASK_PERSONAL_INFO);
        } else if (isSupplementProfileState(currentState)) {
            return messageHandlers.get(BotState.ASK_SUPPLEMENT_PERSONAL_INFO);
        }

        return messageHandlers.get(currentState);
    }

    private boolean isFillingProfileState(BotState currentState) {
        switch (currentState) {
            case ASK_NAME:
            case ASK_HEIGHT:
            case ASK_WEIGHT:
            case ASK_AGE:
            case ASK_PERSONAL_INFO:
            case PROFILE_FILLED:
                return true;
            default:
                return false;
        }
    }

    private boolean isSupplementProfileState(BotState currentState) {
        switch (currentState) {
            case ASK_BELLY:
            case ASK_CHEST:
            case ASK_HIPS:
            case ASK_HIP:
            case ASK_ARM:
            case ASK_NECK:
            case ASK_SHIN:
            case ASK_DATE:
            case ASK_WAIST:
            case PERSONAL_INFO_FILLED:
            case ASK_SUPPLEMENT_PERSONAL_INFO:
                return true;
            default:
                return false;
        }
    }


}