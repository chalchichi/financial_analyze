package me.hoo.financial;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.io.IOUtils;

import java.awt.*;
import java.io.*;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ChartRESTController {

    @Autowired
    ChartImageService chartImageService;

    @Autowired
    ChartDataServcie chartDataServcie;

    @Autowired
    TICKERS_MASRepository tickers_masRepository;

    @GetMapping(value = "/Totalchart", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody
    byte[] testsearch(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
                      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date end,
                      @RequestParam String ticker, @RequestParam String add_days, @RequestParam String limitcount) throws IOException, InterruptedException, ParseException {
        chartImageService.chartService(start, end, ticker, add_days, limitcount);

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
        List<Map<String, Date>> targetlist = chartImageService.chartService(start, end, ticker, add_days, limitcount);
        List<MAIN_STOCK_20Y_INF> tabledata = chartDataServcie.gettargetdata(ticker,targetlist);
        return tabledata;
    }
}
