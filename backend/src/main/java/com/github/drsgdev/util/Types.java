package com.github.drsgdev.util;

import lombok.Getter;

public enum Types {
    MOVIE("movie"), SHOW("tv"), PERSON("person"), USER("user"), CAST("cast"), CREW("crew"), RATING(
            "rating"), REVIEW("review"), REFRESH_TOKEN("refresh_token");

    @Getter
    String value;

    Types(String value) {
        this.value = value;
    }
}
