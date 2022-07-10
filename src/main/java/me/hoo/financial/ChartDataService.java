package me.hoo.financial;

import me.hoo.financial.LogAOP.UserActiveLog;
import me.hoo.financial.VO.datajsonVO;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    ReplyRepository replyRepository;

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

    public List<datajsonVO> getstockinfo(String ticker) throws IOException, InterruptedException {
        String cmd = "python /Users/ohyunhu/RProject/NasDaq-Analysis/getstockinfo.py";
        cmd +=" "+ticker;
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

        List<datajsonVO> keyvalue = new ArrayList<>();
        doc.keySet().forEach(x->
        {
            keyvalue.add(new datajsonVO(x, String.valueOf(doc.get(x))));
        });

        return keyvalue;
    }

    public String registboard(@RequestParam Map<String, String> comment, String writer) throws IOException {
        String title = comment.get("title");
        String content = comment.get("markupStr");
        String plotpath = comment.get("plotpath");
        String Rprojecturl = "/Users/ohyunhu/RProject/NasDaq-Analysis";
        String copypath = Rprojecturl+"/"+title+".html";
        String ticker = comment.get("ticker");
        Optional<TICKERS_MAS> tickers_mas = tickers_masRepository.findTICKERS_MASByCName(ticker);
        File file = new File(Rprojecturl+plotpath);
        File newFile = new File(Rprojecturl+"/"+title+".html");
        Files.copy(file.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Comment newcomment = new Comment();
        newcomment.setTitle(title);
        newcomment.setPlotpath(copypath);
        newcomment.setViews(1);
        newcomment.setContent(content);
        newcomment.setCompany(tickers_mas.get().getCOMPANY_NAME());
        newcomment.setWriter(writer);
        commentRepository.save(newcomment);
        return "OK";
    }

    public List<Comment> getallcomment()
    {
        return commentRepository.findAll();
    }

    @Transactional
    public Comment getcommentboard(String title)
    {
        Optional<Comment> ocomment = commentRepository.findCommentbyTitle(title);
        Comment comment = ocomment.get();
        comment.addview();
        commentRepository.flush();
        return comment;
    }

    @Transactional
    public void saveReply(String username, String rep, String title) {
        Reply reply = new Reply();
        Optional<Comment> comment = commentRepository.findCommentbyTitle(title);
        Comment comment1 = comment.get();
        reply.setWriter(username);
        reply.setCommentreply(rep);
        reply.setComment(comment1);
        replyRepository.save(reply);
    }

    public List<Reply> getReply(Comment comment) {

        List<Reply> replies = replyRepository.findReplyByComment(comment);
        return replies;
    }
}
