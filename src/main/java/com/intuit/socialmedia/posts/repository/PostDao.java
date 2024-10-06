package com.intuit.socialmedia.posts.repository;

import com.intuit.socialmedia.posts.constant.Status;
import com.intuit.socialmedia.posts.dto.request.PostFilterRequest;
import com.intuit.socialmedia.posts.dto.response.PostResponse;
import com.intuit.socialmedia.posts.entity.Post;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public interface PostDao extends JpaRepository<Post, String>, JpaSpecificationExecutor<Post> {
    @Query("SELECT new com.example.PostResponse(p.id, p.description, p.createdOn, p.updatedOn, u.email, u.name) " +
            "FROM Post p JOIN User u ON p.createdBy = u.id " +
            "WHERE p.status = :status")
    Slice<PostResponse> findPostsWithUserDetails(@Param("status") String status, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.updatedOn < :createdOn ORDER BY p.updatedOn DESC")
    List<Post> findOlderPosts(OffsetDateTime createdOn, Pageable pageable);

    default Specification<Post> customSpecification(PostFilterRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("status"), Status.ACTIVE.getStatus())); //todo test for value convert in db

            if (StringUtils.isNotBlank(request.getUserEmail())) {
                //todo get id from user email
                predicates.add(criteriaBuilder.equal(root.get("createdBy"), request.getUserEmail()));
            }

            if (request.getFromDate() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.<OffsetDateTime>get("updatedOn").as(java.sql.Date.class), request.getFromDate()));
            }
            if (request.getToDate() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.<OffsetDateTime>get("updatedOn").as(java.sql.Date.class), request.getToDate()));
            }

            query.orderBy(criteriaBuilder.desc(root.get("updatedOn"))); //todo check why warning
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
