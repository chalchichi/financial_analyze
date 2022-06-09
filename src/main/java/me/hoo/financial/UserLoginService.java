package me.hoo.financial;

import me.hoo.financial.oauth.Role;
import me.hoo.financial.oauth.User;
import me.hoo.financial.oauth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Map;

@Service
public class UserLoginService {

    @Autowired
    UserRepository userRepository;
    public void registUser(Map<String, String> userpara) throws SQLIntegrityConstraintViolationException {
        String name =userpara.get("name");
        String email = userpara.get("email");
        String picture = userpara.get("picture");
        String password = userpara.get("password");
        if(userRepository.findByEmail(email).isPresent())
        {
            throw new java.sql.SQLIntegrityConstraintViolationException();
        }
        else
        {
            User user = User.builder()
                    .name(name)
                    .email(email)
                    .picture(picture)
                    .role(Role.GUSET)
                    .build();
            user.setPassword(password);
            userRepository.save(user);
        }
    }
}
