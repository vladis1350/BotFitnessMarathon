package com.example.fitnessMarathonBot.fitnessDB.repository;

import com.example.fitnessMarathonBot.fitnessDB.bean.ListUserGoals;
import com.example.fitnessMarathonBot.fitnessDB.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListUserGoalsRepository extends JpaRepository<ListUserGoals, Long> {
    ListUserGoals findListUserGoalsByUserAndTimeStamp(User user, String timeStamp);
}
