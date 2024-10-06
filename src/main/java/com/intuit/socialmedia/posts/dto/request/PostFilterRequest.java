package com.intuit.socialmedia.posts.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class PostFilterRequest {
    private int epoch; //data older this this epoch
    private int pageSize;
    private Date toDate; // todo by default on updated
    private String userID; // todo mail or id?
}
