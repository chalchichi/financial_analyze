package me.hoo.financial;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Sort;
import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply,Integer> {
    List<Reply> findReplyByComment(Comment comment);
}
