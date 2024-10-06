package com.intuit.socialmedia.posts.constant;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
public enum Status {
    ACTIVE((short) 1), DELETED((short) 0);
    private final short status;

    Status(short status) {
        this.status = status;
    }
}
