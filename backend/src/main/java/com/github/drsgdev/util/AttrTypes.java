package com.github.drsgdev.util;

import lombok.Getter;

public enum AttrTypes {
    TEXT("text"), DATE("date");

    @Getter
    String value;

    AttrTypes(String value) {
        this.value = value;
    }
}
