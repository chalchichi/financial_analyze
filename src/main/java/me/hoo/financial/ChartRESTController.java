package me.hoo.financial;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hoo.financial.Authentication.SessionUser;
import me.hoo.financial.Authentication.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.ParseException;
import java.util.Date;
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

    @Autowired
    UserLoginService userLoginService;



    private final HttpSession httpSession;

    @ExceptionHandler(InterruptedException.class)
    public Object nullex(Exception e) {
        log.error("handleHttpRequestMethodNotSupportedException", e);
        return new ResponseEntity<>("R interrupted", HttpStatus.METHOD_NOT_ALLOWED);

    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<String> duplicatedid(Exception e) {
        log.error("SQLIntegrityConstraintViolationException", e);
        return ResponseEntity.badRequest().body("email duplicated");

    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> loginfail(Exception e) {
        log.error("SQLIntegrityConstraintViolationException", e);
        return ResponseEntity.badRequest().body("login fail");
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
                              @RequestParam String ticker, @RequestParam String add_days, @RequestParam String limitcount, @RequestParam String chk_info
            , Model model) throws IOException, InterruptedException, ParseException {
        List<MAIN_STOCK_20Y_INF> targetlist;
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if(chk_info.equals("All"))
        {
            targetlist = chartImageService.chartService(start, end, ticker, add_days, limitcount,user.getEmail());
        }
        else
        {
            targetlist = chartImageService.chartoneService(start, end, ticker, add_days, limitcount,user.getEmail());
        }
        //List<MAIN_STOCK_20Y_INF> tabledata = chartDataServcie.gettargetdata(ticker,targetlist);
        return targetlist;
    }

    @PostMapping("/activelog")
    public List<USER_SEARCH_LOG> getUsersearchlog(@RequestParam String email)
    {
        List<USER_SEARCH_LOG> loglist = user_search_logService.getusersearchloglist(email);
        return loglist;
    }

    @PostMapping("/account")
    public String registAccount(@RequestParam Map<String, String> userpara) throws SQLIntegrityConstraintViolationException {
        userLoginService.registUser(userpara);
        return "OK";
    }

    @PostMapping("/comment")
    public String registComment(@RequestParam Map<String, String> comment) throws IOException {
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        String writer = user.getName();
        return chartDataServcie.registboard(comment,writer);
    }

    @PostMapping("/commentdata")
    public List<Comment> getComment() throws IOException {
        List<Comment> comments = chartDataServcie.getallcomment();
        return comments;
    }
}
