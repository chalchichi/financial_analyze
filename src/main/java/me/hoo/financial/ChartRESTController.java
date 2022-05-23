package me.hoo.financial;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.io.IOUtils;

import java.awt.*;
import java.io.*;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class ChartRESTController {

    @Autowired
    ChartImageService chartImageService;

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

}
