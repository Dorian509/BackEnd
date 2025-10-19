package com.example.demo;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "user_profile")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Min(20)
    @Max(400)
    private Integer weightKg;

    @Enumerated(EnumType.STRING)
    private ActivityLevel activityLevel = ActivityLevel.MEDIUM;

    @Enumerated(EnumType.STRING)
    private Climate climate = Climate.NORMAL;

    private String timezone = "Europe/Berlin";


    public Long getId() { return id; }
    public Integer getWeightKg() { return weightKg; }
    public void setWeightKg(Integer weightKg) { this.weightKg = weightKg; }
    public ActivityLevel getActivityLevel() { return activityLevel; }
    public void setActivityLevel(ActivityLevel activityLevel) { this.activityLevel = activityLevel; }
    public Climate getClimate() { return climate; }
    public void setClimate(Climate climate) { this.climate = climate; }
    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
}
