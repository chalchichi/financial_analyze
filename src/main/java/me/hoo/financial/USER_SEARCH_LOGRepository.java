package me.hoo.financial;

import me.hoo.financial.Authentication.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface USER_SEARCH_LOGRepository extends JpaRepository<USER_SEARCH_LOG,Integer> {
    @Query("select u from USER_SEARCH_LOG u where u.UserID = :user")
    List<USER_SEARCH_LOG> findUSER_SEARCH_LOGByUserID(User user, Sort sort);
}
