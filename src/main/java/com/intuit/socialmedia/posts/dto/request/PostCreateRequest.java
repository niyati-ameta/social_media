package com.intuit.socialmedia.posts.dto.request;

import com.intuit.socialmedia.posts.constant.MediaType;
import lombok.Data;

import java.util.List;

@Data
public class PostCreateRequest {
    private String id; //todo for update request
    private String description;
    private List<Media> mediaList;

    public record Media(String id, MediaType type, byte[] data) { //todo record vs class
        // todo if media is present, all values are mandatory, validator
    }

}
