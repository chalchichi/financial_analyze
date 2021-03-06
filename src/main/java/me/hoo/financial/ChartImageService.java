package me.hoo.financial;


import lombok.extern.slf4j.Slf4j;
import me.hoo.financial.LogAOP.UserActiveLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class ChartImageService {

    @Autowired
    TICKERS_MASRepository tickers_masRepository;

    @Autowired
    MAIN_STOCK_20Y_INFRepository main_stock_20Y_infRepository;

    @Value("${mysql.user}")
    String mysqluser;

    @Value("${mysql.password}")
    String mysqlpassword;

    @UserActiveLog
    public List<MAIN_STOCK_20Y_INF> chartoneService(Date start, Date end, String ticker, String add_days, String limitcount,String email) throws IOException, InterruptedException, ParseException {
        List<Map<String,Date>> targetlist = new ArrayList<>();
        Optional<TICKERS_MAS> mas = tickers_masRepository.findTICKERS_MASByCName(ticker);
        TICKERS_MAS tickers_mas = mas.get();
        List<MAIN_STOCK_20Y_INF> ALLDATA = main_stock_20Y_infRepository.findMAIN_STOCK_20Y_INFByTICKER(tickers_mas, Sort.by(Sort.Direction.DESC,"TDAY"));
        CSVwriter csvwriter = new CSVwriter(email);
        csvwriter.writeCSV(ALLDATA);
        List<MAIN_STOCK_20Y_INF> betweenticker= main_stock_20Y_infRepository.findMAIN_STOCK_20Y_INFByTDAYBetweenAndTICKEREquals(start ,end ,tickers_mas);
        String cmd = "Rscript /Users/ohyunhu/RProject/NasDaq-Analysis/TimeAnalysis.R";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");   // yyyy-MM-dd HH:mm:ss
        String Target_end_date = formatter.format(betweenticker.get(betweenticker.size()-1).getTDAY());
        String Days = Integer.toString(betweenticker.size());
        cmd +=" "+Target_end_date;
        cmd +=" "+Days;
        cmd +=" "+add_days;
        cmd +=" "+limitcount;
        cmd +=" "+email;
        String s;

        Process p = Runtime.getRuntime().exec(cmd);
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        while ((s = br.readLine()) != null)
        {
            System.out.println(s);
            if(s.contains(","))
            {
                s = s.replace("[1] ","");
                s = s.substring(1,s.length()-1);
                Map<String, Date> fromto= new HashMap<>();
                Date from = formatter.parse(s.split(",")[0]);
                Date to = formatter.parse(s.split(",")[1]);
                fromto.put("from",from);
                fromto.put("to",to);
                targetlist.add(fromto);
            }
        }

        p.waitFor();
        System.out.println("exit: " + p.exitValue());
        if(p.exitValue()==1)
        {
            InputStream standardError = p.getErrorStream(); // ?????????????????? ????????????.
            InputStreamReader ow = new InputStreamReader(standardError); // ?????????????????? ???????????????
            BufferedReader errorReader = new BufferedReader(ow);// ????????? ???????????????.
            StringBuffer stderr = new StringBuffer();
            String lineErr = null;
            while((lineErr = errorReader.readLine()) != null){
                stderr.append(lineErr).append("\n");
            }
            // ?????????????????? ????????? ??????
            System.out.println(stderr.toString());
            throw new InterruptedException();
        }
        p.destroy();
        return betweenticker;

    }

    @UserActiveLog
    public List<MAIN_STOCK_20Y_INF> chartService(Date start, Date end, String ticker, String add_days, String limitcount,String email) throws IOException, InterruptedException, ParseException {

        Optional<TICKERS_MAS> mas = tickers_masRepository.findTICKERS_MASByCName(ticker);
        TICKERS_MAS tickers_mas = mas.get();
        CSVwriter csvwriter = new CSVwriter(email);

        List<MAIN_STOCK_20Y_INF> betweenticker= main_stock_20Y_infRepository.findMAIN_STOCK_20Y_INFByTDAYBetweenAndTICKEREquals(start ,end ,tickers_mas);
        csvwriter.writeCSV(betweenticker);

        String cmd = "Rscript /Users/ohyunhu/RProject/NasDaq-Analysis/AllDataTimeAnalysis.R";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");   // yyyy-MM-dd HH:mm:ss
        String Target_end_date = formatter.format(betweenticker.get(betweenticker.size()-1).getTDAY());
        String Days = Integer.toString(betweenticker.size());
        cmd +=" "+Target_end_date;
        cmd +=" "+Days;
        cmd +=" "+add_days;
        cmd +=" "+limitcount;
        cmd +=" "+email;
        cmd +=" "+mysqluser;
        cmd +=" "+mysqlpassword;
        String s;

        Process p = Runtime.getRuntime().exec(cmd);
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        while ((s = br.readLine()) != null)
        {
            System.out.println(s);
        }

        p.waitFor();
        System.out.println("exit: " + p.exitValue());
        if(p.exitValue()==1)
        {
            InputStream standardError = p.getErrorStream(); // ?????????????????? ????????????.
            InputStreamReader ow = new InputStreamReader(standardError); // ?????????????????? ???????????????
            BufferedReader errorReader = new BufferedReader(ow);// ????????? ???????????????.
            StringBuffer stderr = new StringBuffer();
            String lineErr = null;
            while((lineErr = errorReader.readLine()) != null){
                stderr.append(lineErr).append("\n");
            }
            // ?????????????????? ????????? ??????
            System.out.println(stderr.toString());
            throw new InterruptedException();
        }
        p.destroy();
        return betweenticker;

    }

}
