package com.example.fitnessMarathonBot.fitnessDB.repository;

import com.example.fitnessMarathonBot.fitnessDB.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepositoryImpl extends JpaRepository<User, Long> {

    User findUserByChatId(long chatId);
}
