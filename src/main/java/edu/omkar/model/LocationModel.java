package edu.omkar.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationModel {
    private int id;           // locid
    private String name;      // locname
    private int cityId;       // ctid FK
    private String cityName;  // from JOIN
    private String stateName; // from JOIN
}
