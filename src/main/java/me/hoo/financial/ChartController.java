package me.hoo.financial;

import lombok.RequiredArgsConstructor;
import me.hoo.financial.oauth.SessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    ChartDataService chartDataService;

    private final HttpSession httpSession;

    @Value("resource.server.url")
    String resourceURL;

    public String mainpage(Model model)
    {
        List<TICKERS_MAS> result = tickers_masRepository.findTICKERS_MASByISMAINSTOCK(true);
        model.addAttribute("TICKERS_MAS",result);
        tickerlist = result;

        int randomfornotcache = (int)(Math.random()*100);
        //no image
        model.addAttribute("TempIMGsrc","http://ohora.iptime.org:8081/files/myplot.png?name="+Integer.toString(randomfornotcache));

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
            model.addAttribute("TempIMGsrc","http://ohora.iptime.org:8081/files/myplot.png?name="+Integer.toString(randomfornotcache));

            model.addAttribute("UserEmail",user.getEmail());
            model.addAttribute("profileimgsrc",user.getPicture());
            return "main.html";
        }
        return "login.html";
    }

    @GetMapping("/main.html")
    public String handleStep2Get() {
        return "redirect:/";
    }

    @GetMapping("/news")
    public String getnews(@ModelAttribute("start") @DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
                          @ModelAttribute("end")  @DateTimeFormat(pattern = "yyyy-MM-dd") Date end,
                          Model model) throws IOException, InterruptedException {


        Calendar cal = Calendar.getInstance();
        cal.setTime(start);
        List<String> newslist = new ArrayList<String>();
        while(true)
        {
            if(cal.getTime().equals(end))
            {
                newslist.add(chartDataService.makenewshtmltext(cal.getTime()));
                break;
            }
            newslist.add(chartDataService.makenewshtmltext(cal.getTime()));
            cal.add(Calendar.DATE, 1);
        }
        model.addAttribute("newslist",newslist);
        return "news.html";
    }
}
