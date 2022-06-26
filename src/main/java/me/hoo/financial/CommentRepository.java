package me.hoo.financial;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
    @Query("select u from Comment u where u.Title = :title")
    Optional<Comment> findCommentbyTitle(String title);
}
