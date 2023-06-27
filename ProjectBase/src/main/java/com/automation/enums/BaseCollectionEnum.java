package com.automation.enums;

public enum BaseCollectionEnum {
    A("A"),
    B("B"),
    C("C"),
    D("D"),
    E("E"),
    F("F"),
    G("G");

    private final String value;

    BaseCollectionEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
