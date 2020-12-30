package com.cs.challenge.persistence;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "records")
public class RecordEntity {

    @Id
    private String id;

    private long duration;

    private String type;

    private String host;

    private boolean alert;

}
