package me.hoo.financial;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class ChartController {

    @Autowired
    ChartImageService chartImageService;

    @Autowired
    TICKERS_MASRepository tickers_masRepository;



    @GetMapping("/")
    public String mainpage(Model model)
    {
        List<TICKERS_MAS> result = tickers_masRepository.findTICKERS_MASByISMAINSTOCK(true);
        model.addAttribute("TICKERS_MAS",result);
        return "index.html";
    }
}
