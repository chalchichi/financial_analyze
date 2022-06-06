package me.hoo.financial;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hoo.financial.LogAOP.UserActiveLog;
import me.hoo.financial.oauth.SessionUser;
import me.hoo.financial.oauth.User;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.io.IOUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import javax.swing.text.html.Option;
import java.awt.*;
import java.io.*;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@Slf4j
public class ChartRESTController {

    @Autowired
    ChartImageService chartImageService;

    @Autowired
    ChartDataService chartDataServcie;

    @Autowired
    User_Search_LogService user_search_logService;

    @Autowired
    TICKERS_MASRepository tickers_masRepository;

    private final HttpSession httpSession;

    @ExceptionHandler(InterruptedException.class)
    public Object nullex(Exception e) {
        log.error("handleHttpRequestMethodNotSupportedException", e);
        return new ResponseEntity<>("R interrupted", HttpStatus.METHOD_NOT_ALLOWED);

    }


    @GetMapping(value = "/Totalchart", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody
    byte[] testsearch(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
                      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date end,
                      @RequestParam String ticker, @RequestParam String add_days, @RequestParam String limitcount) throws IOException, InterruptedException, ParseException {
        chartImageService.chartService(start, end, ticker, add_days, limitcount,null);

        FileInputStream fis = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        String fileDir = "/Users/ohyunhu/RProject/NasDaq-Analysis/myplot.png";

        try {
            fis = new FileInputStream(fileDir);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int readCount = 0;
        byte[] buffer = new byte[1024];
        byte[] fileArray = null;

        try {
            while ((readCount = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, readCount);
            }
            fileArray = baos.toByteArray();
            fis.close();
            baos.close();
        } catch (IOException e) {
            throw new RuntimeException("File Error");
        }
        return fileArray;
    }

    @PostMapping("/chartdata")
    public List<MAIN_STOCK_20Y_INF> searchChart(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
                              @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date end,
                              @RequestParam String ticker, @RequestParam String add_days, @RequestParam String limitcount
            , Model model) throws IOException, InterruptedException, ParseException {
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        List<Map<String, Date>> targetlist = chartImageService.chartService(start, end, ticker, add_days, limitcount,user.getEmail());
        List<MAIN_STOCK_20Y_INF> tabledata = chartDataServcie.gettargetdata(ticker,targetlist);
        return tabledata;
    }

    @PostMapping("/activelog")
    public List<USER_SEARCH_LOG> getUsersearchlog(@RequestParam String email)
    {
        List<USER_SEARCH_LOG> loglist = user_search_logService.getusersearchloglist(email);
        return loglist;
    }

    
}
