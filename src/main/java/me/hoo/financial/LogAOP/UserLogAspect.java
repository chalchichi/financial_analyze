package me.hoo.financial.LogAOP;

import me.hoo.financial.TICKERS_MAS;
import me.hoo.financial.TICKERS_MASRepository;
import me.hoo.financial.USER_SEARCH_LOG;
import me.hoo.financial.USER_SEARCH_LOGRepository;
import me.hoo.financial.oauth.User;
import me.hoo.financial.oauth.UserRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Aspect
@Component
public class UserLogAspect {

    @Autowired
    TICKERS_MASRepository tickers_masRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    USER_SEARCH_LOGRepository user_search_logRepository;

    //파라미터와 리턴값 출력
    @Around("@annotation(UserActiveLog)")
    public Object logperf2(ProceedingJoinPoint pjp) throws Throwable {
        //파라미터와 리턴값을 object로 받아서 캐스팅후 사용할 수 있다.
        Object res = pjp.proceed();
        Object[] paras = pjp.getArgs();
        for(int i=0; i< paras.length; i++)
        {
            System.out.println("Parameter"+i+" :" + paras[i]);
        }

        USER_SEARCH_LOG user_search_log = new USER_SEARCH_LOG();

        user_search_log.setStartDate((Date) paras[0]);
        user_search_log.setEndDate((Date) paras[1]);

        Optional<TICKERS_MAS> ticker = tickers_masRepository.findTICKERS_MASByCName(String.valueOf(paras[2]));
        if(!ticker.isPresent()) return null;

        user_search_log.setTICKER(ticker.get());
        user_search_log.setAdd_days(Integer.parseInt(paras[3].toString()));
        user_search_log.setLimitcount(Integer.parseInt(paras[4].toString()));

        Optional<User> user = userRepository.findByEmail((String) paras[5]);
        if(!user.isPresent()) return null;
        user_search_log.setUserID(user.get());

        user_search_logRepository.save(user_search_log);
        return res;
    }
}
