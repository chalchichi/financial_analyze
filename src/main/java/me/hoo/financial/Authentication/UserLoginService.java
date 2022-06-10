package me.hoo.financial.Authentication;

import lombok.AllArgsConstructor;
import me.hoo.financial.Authentication.Role;
import me.hoo.financial.Authentication.User;
import me.hoo.financial.Authentication.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Map;

@Service
@AllArgsConstructor
public class UserLoginService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

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
            user.setPassword(bCryptPasswordEncoder.encode(password));
            userRepository.save(user);
        }
    }

    public User loadUser(String Email, String Password, HttpSession httpSession) throws UsernameNotFoundException {
        User user = userRepository.findByEmailAndAndPassword(Email,Password)
                .orElseThrow(()->new UsernameNotFoundException("User not exist"));
        httpSession.setAttribute("user", new SessionUser(user));
        return user;
    }

}
