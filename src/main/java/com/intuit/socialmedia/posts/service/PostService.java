package com.intuit.socialmedia.posts.service;

import com.intuit.socialmedia.posts.dto.request.PostFilterRequest;
import com.intuit.socialmedia.posts.dto.response.PostResponse;
import com.intuit.socialmedia.posts.entity.Comment;
import com.intuit.socialmedia.posts.entity.Post;
import com.intuit.socialmedia.posts.entity.User;
import com.intuit.socialmedia.posts.repository.PostDao;
import com.intuit.socialmedia.posts.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class PostService {
    @Autowired
    PostDao postDao;

    @Autowired
    UserDao userDao;

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
