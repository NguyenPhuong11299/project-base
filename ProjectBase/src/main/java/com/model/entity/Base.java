package com.model.entity;

import com.util.DBParams;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = DBParams.TB_BASE)
@Data
public class Base {
    @Id
    @Column(name = DBParams.S_ID)
    private String id;

    @Column(name = DBParams.S_NAME)
    private String name;

    @Column(name = DBParams.S_STATE)
    private String state;
}
