package com.example.fitnessMarathonBot.fitnessDB.service;

import com.example.fitnessMarathonBot.fitnessDB.bean.User;
import com.example.fitnessMarathonBot.fitnessDB.bean.UserPhoto;
import com.example.fitnessMarathonBot.fitnessDB.repository.UserPhotoRepository;
import com.example.fitnessMarathonBot.fitnessDB.repository.UserRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class UserPhotoService {
    @Autowired
    private UserPhotoRepository userPhotoRepository;

    @Autowired
    private UserRepositoryImpl userRepository;

    public void saveUserPhoto(Message message) {
        Date date = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        List<PhotoSize> photos = message.getPhoto();
        String photo_id = Objects.requireNonNull(photos.stream().max(Comparator.comparing(PhotoSize::getFileSize))
                .orElse(null)).getFileId();
        if (userRepository.findUserByChatId(message.getFrom().getId()) != null) {
            User user = userRepository.findUserByChatId(message.getFrom().getId());
            UserPhoto userPhoto = UserPhoto.builder()
                    .user(user)
                    .imageId(photo_id)
                    .imageCategory("Eat")
                    .timeStamp(formatForDateNow.format(date))
                    .build();
            userPhotoRepository.save(userPhoto);
        }
    }

}
