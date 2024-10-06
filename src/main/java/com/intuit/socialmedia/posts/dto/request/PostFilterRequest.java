package com.intuit.socialmedia.posts.dto.request;

import lombok.Data;

import java.util.Date;

@Data
public class PostFilterRequest {
    private Date fromDate;
    private Date toDate; // todo by default on updated
    private String userEmail; // todo mail or id?
}
