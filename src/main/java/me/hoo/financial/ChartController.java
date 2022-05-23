package me.hoo.financial;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class ChartController {

    @Autowired
    ChartImageService chartImageService;

    @Autowired
    TICKERS_MASRepository tickers_masRepository;

    @Autowired
    ChartDataServcie chartDataServcie;

    @GetMapping("/")
    public String mainpage(Model model)
    {
        List<TICKERS_MAS> result = tickers_masRepository.findTICKERS_MASByISMAINSTOCK(true);
        model.addAttribute("TICKERS_MAS",result);
        int randomfornotcache = (int)(Math.random()*100);

        model.addAttribute("TempIMGsrc","http://localhost:8081/files/myplot.png?name="+Integer.toString(randomfornotcache));

        return "index.html";
    }

    @PostMapping("/chartdata")
    public String searchChart(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
                              @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date end,
                              @RequestParam String ticker, @RequestParam String add_days, @RequestParam String limitcount
                            , Model model) throws IOException, InterruptedException, ParseException {
        List<Map<String, Date>> targetlist = chartImageService.chartService(start, end, ticker, add_days, limitcount);

        int randomfornotcache = (int)(Math.random()*100);
        model.addAttribute("TempIMGsrc","http://localhost:8081/files/myplot.png?name="+Integer.toString(randomfornotcache));

        List<MAIN_STOCK_20Y_INF> tabledata = chartDataServcie.gettargetdata(ticker,targetlist);
        model.addAttribute("targeted",tabledata);
        return "TargetDatatable.html";
    }

}
