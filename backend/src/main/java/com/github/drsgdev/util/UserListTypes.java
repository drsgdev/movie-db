package com.github.drsgdev.util;

import lombok.Getter;

public enum UserListTypes {
    FAVORITES("favorites"), LAST_VISITED("last_visited"), RATED("rated"), REVIEWED("reviewed");

    @Getter
    String value;

    UserListTypes(String value) {
        this.value = value;
    }
}
