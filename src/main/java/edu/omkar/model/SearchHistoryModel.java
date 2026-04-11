package edu.omkar.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchHistoryModel {
    private int histId;
    private int userId;
    private String stateName;
    private String cityName;
    private String locName;
    private int sqFeet;
    private int nbed;
    private int nbath;
    private int age;
    private double predictedPrice;
    private String searchDate;
}
