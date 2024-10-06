package com.intuit.socialmedia.posts.service;

import com.intuit.socialmedia.posts.constant.Status;
import com.intuit.socialmedia.posts.dto.request.PostCreateRequest;
import com.intuit.socialmedia.posts.dto.request.PostFilterRequest;
import com.intuit.socialmedia.posts.dto.response.PostResponse;
import com.intuit.socialmedia.posts.entity.Comment;
import com.intuit.socialmedia.posts.entity.Post;
import com.intuit.socialmedia.posts.entity.User;
import com.intuit.socialmedia.posts.repository.PostDao;
import com.intuit.socialmedia.posts.repository.UserDao;
import io.swagger.v3.oas.models.links.Link;
import lombok.Builder;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Builder
@Service
public class PostService {
    @Autowired
    PostDao postDao;

    @Autowired
    RedisService redisService;

    public Object addUpdatePost(PostCreateRequest request){
        //Prepare media list. Upload media to S3
        List<String> mediaList = new LinkedList<>();

        //Prepare post details for DB update
        Post.PostBuilder builder = Post.builder().description(request.getDescription())
                .mediaPaths(mediaList).createdBy(request.getCreatedByUserID())
                .status(Status.ACTIVE);
        if(!request.getId().isEmpty())
            builder.id(request.getId());
        Post post = postDao.save(builder.build());

        //Prepare for redis update
        redisService.addToListAndTrim("recent_post", post.getId());
        redisService.setValue(post.getId(), post);
        return "Success";
    }

    public List<PostResponse> getPostDetail(PostFilterRequest request){

        //fetch from redis
        List<String> recentPosts = redisService.getList(request.getUserID()).stream().map((postID) -> (String)postID).toList();
        if(recentPosts.isEmpty()){
            recentPosts = redisService.getList("recent_post").stream().map((postID) -> (String)postID).toList();
            redisService.addList(request.getUserID(), Arrays.asList(recentPosts.toArray()));
        }

        // Create Instant from epoch time
        Instant instant = Instant.ofEpochSecond(request.getEpoch());
        OffsetDateTime offsetDateTime = instant.atOffset(ZoneOffset.UTC);

        List<Post> postsList = redisService.multiGet(recentPosts).stream()
                .map(post -> (Post) post)  // Cast each Object to Post
                .filter(post -> post.getCreatedOn().isBefore(offsetDateTime) || post.getCreatedOn().isEqual(offsetDateTime))  // Use isBefore() and isEqual()
                .toList();

        if(postsList.size() >= request.getPageSize())
            return postsList.subList(0, request.getPageSize()).stream().map((post) ->
                    PostResponse.builder().description(post.getDescription())
                            .createdByUserId(post.getCreatedBy()).build()).toList();


        List<Post> oldPosts = postDao.findOlderPosts(offsetDateTime, Pageable.ofSize(request.getPageSize()));
        redisService.addList(request.getUserID(), Arrays.asList(oldPosts.toArray()));

        return oldPosts.stream().filter(post -> post.getCreatedOn().isBefore(offsetDateTime) || post.getCreatedOn().isEqual(offsetDateTime))
                .map((post) -> PostResponse.builder().description(post.getDescription())
                        .createdByUserId(post.getCreatedBy()).build()).toList();
    }

    public Slice<PostResponse> list(PostFilterRequest filterRequest, Pageable pageable) {
        Slice<Post> posts = postDao.findAll(postDao.customSpecification(filterRequest), pageable);
        return posts.map(this::toPostResponse); // todo add logs, metrics
    }

    private PostResponse toPostResponse(Post post) {
        if (post == null) {
            return null; // todo Handle null post case if necessary
        }

        PostResponse postResponse = new PostResponse();

        postResponse.setId(post.getId());
        postResponse.setDescription(post.getDescription());
        postResponse.setComments(getCommentsByPostId(post.getId())); // Assuming you have a way to retrieve comments from Post
        postResponse.setCreatedOn(post.getCreatedOn());
        postResponse.setUpdatedOn(post.getUpdatedOn());
        postResponse.setCreatedByUserId(post.getCreatedBy());

        // todo  have a method to get the user email from the userId
        postResponse.setCreatedByUserName(getUserNameById(post.getCreatedBy()));

        return postResponse;
    }

    private List<Comment> getCommentsByPostId(String id) {
        //todo complete the method
        return Collections.emptyList();
    }

    // Example method to retrieve user email (you will need to implement this)
    private String getUserNameById(String userId) { //todo complete this method
        // Fetch the user's email from the database or any relevant source using the userId
        // This is a placeholder; replace with your actual logic
        return "name"; // Replace with actual retrieval logic
    }

    private List<User> getUsersByIds(List<String> userIds) { //todo use this method
        return userDao.findByIdIn(userIds);
    }

}
