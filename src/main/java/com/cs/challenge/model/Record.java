package com.cs.challenge.model;

import lombok.Data;

@Data
public class Record {

    private String id;
    private State state;
    private long timestamp;
    private String type;
    private String host;

}
