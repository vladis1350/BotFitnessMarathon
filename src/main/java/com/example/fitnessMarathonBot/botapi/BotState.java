package com.example.fitnessMarathonBot.botapi;

/**Возможные состояния бота
 */

public enum BotState {
    ASK_START,
    SHOW_MAIN_MENU,
    ASK_PERSONAL_INFO,
    ASK_HEIGHT,
    ASK_WEIGHT,
    ASK_PHYSIQUE,
    ASK_AGE,
    ASK_NAME,

    SHOW_CHAT_WITH_MANAGER,
    SHOW_DAILY_REPORT,
    SHOW_PLAN_FOR_TODAY,
    PLAN_FOR_TODAY,
    DAILY_REPORT,
    MY_INFORMATION,
    WRITE_TO_MANAGER,
    PROFILE_FILLED
}
