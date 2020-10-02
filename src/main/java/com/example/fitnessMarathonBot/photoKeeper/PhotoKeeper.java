package com.example.fitnessMarathonBot.photoKeeper;

import com.example.fitnessMarathonBot.service.ReplyMessagesService;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

@Component
public class PhotoKeeper {
    private LinkedHashMap<String, String> photoInfo = new LinkedHashMap<>();

    private ReplyMessagesService replyMessagesService;

    public PhotoKeeper(ReplyMessagesService replyMessagesService) {
        this.replyMessagesService = replyMessagesService;
    }

    public LinkedHashMap<String, String> getPhotoInfo(){
        return photoInfo;
    }

    public void setPhotoInfo() {

    }

}
