package edu.omkar.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModelParam {
    private int id;
    private double intercept;
    private double slopeSqFeet;
    private double slopeNbed;
    private double slopeNbath;
    private double slopeAge;
    private double rSquared;
    private double mse;
    private String trainedAt;
}
