package com.intuit.socialmedia.posts.service;

import com.intuit.socialmedia.posts.dto.request.RegisterUserRequest;
import com.intuit.socialmedia.posts.entity.User;
import com.intuit.socialmedia.posts.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserService {
    @Autowired
    RedisService redisService;

    @Autowired
    UserDao userDao;

    public Object updateRedisPostForUser(RegisterUserRequest userRequest){
        //create user in DB
        //Use BCrypt of password saving
        User user = User.builder().email(userRequest.getEmail())
                        .name(userRequest.getName()).password(userRequest.getPassword())
                        .profilePic(userRequest.getProfilePic()).build();
        userDao.save(user);

        //cache user userID and Name mapping in redis
        redisService.setValue(user.getId(), user.getName());
        return "Success";
    }
}
