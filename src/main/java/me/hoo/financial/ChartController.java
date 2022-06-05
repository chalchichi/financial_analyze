package me.hoo.financial;

import lombok.RequiredArgsConstructor;
import me.hoo.financial.oauth.SessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class ChartController {

    public static List<TICKERS_MAS> tickerlist;

    @Autowired
    ChartImageService chartImageService;

    @Autowired
    TICKERS_MASRepository tickers_masRepository;

    @Autowired
    ChartDataServcie chartDataServcie;

    private final HttpSession httpSession;


    public String mainpage(Model model)
    {
        List<TICKERS_MAS> result = tickers_masRepository.findTICKERS_MASByISMAINSTOCK(true);
        model.addAttribute("TICKERS_MAS",result);
        tickerlist = result;

        int randomfornotcache = (int)(Math.random()*100);
        //no image
        model.addAttribute("TempIMGsrc","http://localhost:8081/files/myplot.png?name="+Integer.toString(randomfornotcache));

        return "main.html";
    }

    @GetMapping("/")
    public String loginpage(Model model)
    {
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if(user != null){
            List<TICKERS_MAS> result = tickers_masRepository.findTICKERS_MASByISMAINSTOCK(true);
            model.addAttribute("TICKERS_MAS",result);
            tickerlist = result;

            int randomfornotcache = (int)(Math.random()*100);

            //for no cache image
            model.addAttribute("TempIMGsrc","http://localhost:8081/files/myplot.png?name="+Integer.toString(randomfornotcache));

            model.addAttribute("UserEmail",user.getEmail());
            model.addAttribute("profileimgsrc",user.getPicture());
            return "main.html";
        }
        return "login.html";
    }



}
