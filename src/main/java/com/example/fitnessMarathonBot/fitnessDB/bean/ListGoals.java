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
@Table(name = "list_goals")
public class ListGoals implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_goal_list")
    private long id;

    @Column(name = "time_stamp")
    private String timeStamp;

    @Column(name = "task_one")
    private String taskOne;

    @Column(name = "task_two")
    private String taskTwo;

    @Column(name = "task_three")
    private String taskThree;

    @Column(name = "task_four")
    private String taskFour;

    @Column(name = "task_five")
    private String taskFive;

    @Column(name = "task_six")
    private String taskSix;
}
