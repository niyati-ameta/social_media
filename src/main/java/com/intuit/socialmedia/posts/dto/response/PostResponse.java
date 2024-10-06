package com.intuit.socialmedia.posts.dto.response;

import com.intuit.socialmedia.posts.entity.Comment;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class PostResponse { //todo equals hashcode on postID
    private String id;
    private String description;
    private List<Comment> comments;
    private OffsetDateTime createdOn;
    private OffsetDateTime updatedOn;
    private String createdByUserId; // userId
    private String createdByUserName; // todo needed or not?
}
