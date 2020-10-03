package com.example.fitnessMarathonBot.fitnessDB.repository;

import com.example.fitnessMarathonBot.fitnessDB.bean.ListGoals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListGoalsRepository extends JpaRepository<ListGoals, Long> {

    ListGoals findListGoalsByTimeStamp(String timeStamp);
}
