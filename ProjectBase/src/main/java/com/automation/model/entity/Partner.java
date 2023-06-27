package com.automation.model.entity;

import com.automation.utils.DBParams;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = DBParams.TB_PARTNER)
@Data
public class Partner {
    @Id
    @Column(name = DBParams.S_ID)
    private String id;

    @Column(name = DBParams.S_NAME)
    private String name;

    @Column(name = DBParams.S_STATE)
    private String state;

    @Column(name = DBParams.S_TAG)
    private String tag;
}
