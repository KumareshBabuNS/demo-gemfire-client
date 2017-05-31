package com.example.demogemfireclient;

import com.gemstone.gemfire.pdx.PdxReader;
import com.gemstone.gemfire.pdx.PdxSerializable;
import com.gemstone.gemfire.pdx.PdxWriter;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;

/**
 * Created by derrickwong on 29/5/2017.
 */
@Data @NoArgsConstructor
public class ClientHealthInfo
        implements PdxSerializable {

    public static final String ACCOUNT_ID = "accountId";
    public static final String STEPS = "steps";
    public static final String CALORIES_BURNT = "caloriesBurnt";
    public static final String WEIGHT = "weight";
    public static final String ENERGY_BURNT = "energyBurnt";
    public static final String ENERGY_BURNT_GOAL = "energyBurntGoal";
    public static final String EXERCISE_TIME = "exerciseTime";
    public static final String EXERCISE_TIME_GOAL = "exerciseTimeGoal";
    public static final String STAND_HOURS = "standHours";
    public static final String STAND_HOURS_GOAL = "standHoursGoal";

    @Override
    public void toData(PdxWriter pdxWriter) {
        pdxWriter.writeString(ACCOUNT_ID, accountId)
                .markIdentityField(ACCOUNT_ID);
        pdxWriter.writeLong(STEPS, steps);
        pdxWriter.writeLong(CALORIES_BURNT, caloriesBurnt);
        pdxWriter.writeInt(WEIGHT, weight);
        pdxWriter.writeLong(ENERGY_BURNT, energyBurnt);
        pdxWriter.writeLong(ENERGY_BURNT_GOAL, energyBurntGoal);
        pdxWriter.writeLong(EXERCISE_TIME, exerciseTime);
        pdxWriter.writeLong(EXERCISE_TIME_GOAL, exerciseTimeGoal);
        pdxWriter.writeLong(STAND_HOURS, standHours);
        pdxWriter.writeLong(STAND_HOURS_GOAL, standHoursGoal);
    }

    @Override
    public void fromData(PdxReader pdxReader) {
        accountId=pdxReader.readString(ACCOUNT_ID);
        steps=pdxReader.readLong(STEPS);
        caloriesBurnt=pdxReader.readLong(CALORIES_BURNT);
        weight=pdxReader.readInt(WEIGHT);
        energyBurnt=pdxReader.readLong(ENERGY_BURNT);
        energyBurntGoal=pdxReader.readLong(ENERGY_BURNT_GOAL);
        exerciseTime=pdxReader.readLong(EXERCISE_TIME);
        exerciseTimeGoal=pdxReader.readLong(EXERCISE_TIME_GOAL);
        standHours=pdxReader.readLong(STAND_HOURS);
        standHoursGoal=pdxReader.readLong(STAND_HOURS_GOAL);
    }

    @Id
    private String accountId;
    private Long steps;
    private Long caloriesBurnt;
    private Integer weight;
    private Long energyBurnt;
    private Long energyBurntGoal;
    private Long exerciseTime;
    private Long exerciseTimeGoal;
    private Long standHours;
    private Long standHoursGoal;

    @PersistenceConstructor
    public ClientHealthInfo(
            String accountId, Long steps, Long caloriesBurnt, Integer weight, Long energyBurnt, Long energyBurntGoal, Long exerciseTime, Long exerciseTimeGoal, Long standHours, Long standHoursGoal) {
        this.accountId = accountId;
        this.steps = steps;
        this.caloriesBurnt = caloriesBurnt;
        this.weight = weight;
        this.energyBurnt = energyBurnt;
        this.energyBurntGoal = energyBurntGoal;
        this.exerciseTime = exerciseTime;
        this.exerciseTimeGoal = exerciseTimeGoal;
        this.standHours = standHours;
        this.standHoursGoal = standHoursGoal;
    }
}
