package me.hoo.financial;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ChartImageService {

    @Autowired
    TICKERS_MASRepository tickers_masRepository;

    @Autowired
    MAIN_STOCK_20Y_INFRepository main_stock_20Y_infRepository;

    public List<MAIN_STOCK_20Y_INF> chartService(Date start, Date end,  String ticker,String add_days,String limitcount) throws IOException, InterruptedException {
        Optional<TICKERS_MAS> mas = tickers_masRepository.findTICKERS_MASByCName(ticker);
        TICKERS_MAS tickers_mas = mas.get();
        List<MAIN_STOCK_20Y_INF> ALLDATA = main_stock_20Y_infRepository.findMAIN_STOCK_20Y_INFByTICKER(tickers_mas, Sort.by(Sort.Direction.DESC,"TDAY"));
        CSVwriter csvwriter = new CSVwriter();
        csvwriter.writeCSV(ALLDATA);
        List<MAIN_STOCK_20Y_INF> betweenticker= main_stock_20Y_infRepository.findMAIN_STOCK_20Y_INFByTDAYBetweenAndTICKEREquals(start ,end ,tickers_mas);
        String cmd = "Rscript /Users/ohyunhu/RProject/NasDaq-Analysis/TimeAnalysis.R";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");   // yyyy-MM-dd HH:mm:ss
        String Target_end_date = formatter.format(end);
        String Days = Integer.toString(betweenticker.size());
        cmd +=" "+Target_end_date;
        cmd +=" "+Days;
        cmd +=" "+add_days;
        cmd +=" "+limitcount;
        String s;

        Process p = Runtime.getRuntime().exec(cmd);
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        while ((s = br.readLine()) != null)
            System.out.println(s);
        p.waitFor();
        System.out.println("exit: " + p.exitValue());
        p.destroy();
        return ALLDATA;

    }

}
