package com.example.fitnessMarathonBot.fitnessDB.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "list_user_goals")
public class ListUserGoals implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_goals_id")
    private long id;

    @Column(name = "time_stamp")
    private String timeStamp;

    @Column(name = "task_one")
    private boolean taskOne;

    @Column(name = "task_two")
    private boolean taskTwo;

    @Column(name = "task_three")
    private boolean taskThree;

    @Column(name = "task_four")
    private boolean taskFour;

    @Column(name = "task_five")
    private boolean taskFive;

    @Column(name = "task_six")
    private boolean taskSix;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
