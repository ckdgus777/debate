package com.example.debate.domain.type;

import lombok.Getter;

public enum SearchType {
    TITLE("제목"),
    CONTENT("본문"),
    USERNAME("이름");

    @Getter
    private final String description;

    SearchType(String description) {
        this.description = description;
    }
}
