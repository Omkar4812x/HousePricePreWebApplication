package edu.omkar.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CityModel {
    private int id;           // ctid
    private String name;      // cityname
    private int stateId;      // stateid FK
    private String stateName; // from JOIN
}
