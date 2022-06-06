package me.hoo.financial;

import me.hoo.financial.LogAOP.UserActiveLog;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class ChartDataService {
    @Autowired
    MAIN_STOCK_20Y_INFRepository main_stock_20Y_infRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    TICKERS_MASRepository tickers_masRepository;
    public List<MAIN_STOCK_20Y_INF> gettargetdata(String ticker, List<Map<String, Date>> targetdatelist)
    {
        TICKERS_MAS tickers_mas = tickers_masRepository.findTICKERS_MASByCName(ticker).get();

        List<MAIN_STOCK_20Y_INF> totaltargetlist = new ArrayList<>();
        for(Map<String,Date> m : targetdatelist)
        {
            List<MAIN_STOCK_20Y_INF> tempdate =
                    main_stock_20Y_infRepository.findMAIN_STOCK_20Y_INFByTDAYBetweenAndTICKEREquals(m.get("from"),m.get("to"),tickers_mas);
            tempdate.forEach(x->totaltargetlist.add(x));
        }
        
        return totaltargetlist;
    }

    public Map<String,String> getnews(Date date) throws IOException, InterruptedException {
        Criteria criteria = new Criteria("date");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");   // yyyy-MM-dd HH:mm:ss
        String tday = formatter.format(date);
        criteria.is(tday);
        Query query = new Query(criteria);
        HashMap<String,String> news = mongoTemplate.findOne(query, HashMap.class, "newscollections");
        if(news!=null)
        {
            return news;
        }
        else
        {
            crawlingnews(date);
            return getnews(date);
        }
    }

    public void crawlingnews(Date date) throws IOException, InterruptedException {
        String cmd = "python /Users/ohyunhu/RProject/NasDaq-Analysis/crawing.py";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");   // yyyy-MM-dd HH:mm:ss
        String d = formatter.format(date);
        cmd +=" "+d;
        String s;
        String res = null;
        Process p = Runtime.getRuntime().exec(cmd);
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        while ((s = br.readLine()) != null)
        {
            System.out.println(s);
            res = s;
        }
        p.waitFor();
        System.out.println("exit: " + p.exitValue());
        if(p.exitValue()==1)
        {
            InputStream standardError = p.getErrorStream(); // 에러스트림을 가져온다.
            InputStreamReader ow = new InputStreamReader(standardError); // 에러스트림을 읽어들인다
            BufferedReader errorReader = new BufferedReader(ow);// 버퍼로 읽어들인다.
            StringBuffer stderr = new StringBuffer();
            String lineErr = null;
            while((lineErr = errorReader.readLine()) != null){
                stderr.append(lineErr).append("\n");
            }
            // 에러데이타를 콘솔에 출력
            System.out.println(stderr.toString());
            throw new InterruptedException();
        }
        p.destroy();
        Document doc = Document.parse(res);
        mongoTemplate.insert(res,"newscollections");
        return;
    }
    public String makenewshtmltext(Date date) throws IOException, InterruptedException {
        Map<String,String> news = getnews(date);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");   // yyyy-MM-dd HH:mm:ss
        String tday = formatter.format(date);
        String newshtml = "<details><summary>"+tday+"</summary><div class='current-events-content description'>";
        for(String key : news.keySet())
        {
            if(key.equals("date")) continue;
            if(key.equals("_id")) continue;
            newshtml+="<div class='current-events-content-heading' role='heading'>"+key+"</div>";
            newshtml+=news.get(key);
        }
        newshtml+="</div></details>";
        return newshtml;
    }
}
