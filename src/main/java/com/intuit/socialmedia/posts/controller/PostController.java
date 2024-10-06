package com.intuit.socialmedia.posts.controller;

import com.intuit.socialmedia.posts.dto.request.PostCreateRequest;
import com.intuit.socialmedia.posts.dto.request.PostFilterRequest;
import com.intuit.socialmedia.posts.dto.response.PostResponse;
import com.intuit.socialmedia.posts.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("v1/post")
@Tag(name = "PostResponse Controller", description = "CRUD APIs related to PostResponse")
public class PostController {

    @Autowired
    PostService postService;
    @Operation(summary = "Create PostResponse API", description = "Creates a post for authenticated user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Successfully created post",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PostResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PostResponse> createPost(PostCreateRequest request) {
        postService.addUpdatePost(request);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/list")
    public ResponseEntity<List<PostResponse>> list(@RequestBody PostFilterRequest filter) {
        //log.info("PostFilterRequest request : {}, page: {}, size: {}", filter, pageNumber, pageSize);
        return (ResponseEntity<List<PostResponse>>) postService.getPostDetail(filter);
//        Pageable page = PageRequest.of(pageNumber - 1, pageSize, Sort.by(sortBy).descending());
//        return ResponseEntity.ok(postService.list(filter, page));
    }
}
