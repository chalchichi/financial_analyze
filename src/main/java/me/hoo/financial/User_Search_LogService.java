package me.hoo.financial;

import me.hoo.financial.oauth.User;
import me.hoo.financial.oauth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class User_Search_LogService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    USER_SEARCH_LOGRepository user_search_logRepository;

    public List<USER_SEARCH_LOG> getusersearchloglist(String email)
    {
        Optional<User> ouser = userRepository.findByEmail(email);
        User user = ouser.get();
        List<USER_SEARCH_LOG> loglist = user_search_logRepository.findUSER_SEARCH_LOGByUserID(user , Sort.by(Sort.Direction.DESC, "SearchTime"));
        return loglist;
    }
}
