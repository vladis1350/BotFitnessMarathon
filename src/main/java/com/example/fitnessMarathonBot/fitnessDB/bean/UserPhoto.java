package com.example.fitnessMarathonBot.fitnessDB.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "user_photo")
public class UserPhoto implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_photo_id")
    private long id;

    @Column(name = "time_stamp")
    private String timeStamp;

    @Column(name = "image_id")
    private String imageId;

    @Column(name = "image_category")
    private String imageCategory;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
