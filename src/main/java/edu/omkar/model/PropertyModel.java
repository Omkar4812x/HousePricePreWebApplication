package edu.omkar.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyModel {
    private int id;              // pid
    private String name;         // pname
    private String address;      // paddress
    private int age;             // age
    private int sqFeet;          // asqfeet
    private int nbath;           // nbath
    private int nbed;            // nbed
    private double actualPrice;  // actualprice
    private int locId;           // locid FK
    private String locName;      // from JOIN
    private int cityId;          // from JOIN
    private String cityName;     // from JOIN
    private int stateId;         // from JOIN
    private String stateName;    // from JOIN
}
