package me.hoo.financial;

import java.io.*;
import java.util.List;

public class CSVwriter {
    public void writeCSV(List<MAIN_STOCK_20Y_INF> dataList) throws IOException {
        File csv = new File("/Users/ohyunhu//RProject/NasDaq-Analysis/TEMP.csv");
        BufferedWriter bw = null; // 출력 스트림 생성
        try {
            bw = new BufferedWriter(new FileWriter(csv));
            // csv파일의 기존 값에 이어쓰려면 위처럼 true를 지정하고, 기존 값을 덮어쓰려면 true를 삭제한다

            bw.write("TDAY,CLOSE,NAME,OPEN");
            bw.newLine();
            for (int i = 0; i < dataList.size(); i++) {
                MAIN_STOCK_20Y_INF data = dataList.get(i);
                String aData = "";
                String a = data.getTDAY().toString();
                aData = data.getTDAY().toString() + "," + data.getClose() + "," + data.getTICKER().getCOMPANY_NAME() + "," + data.getOpen();
                // 한 줄에 넣을 각 데이터 사이에 ,를 넣는다
                bw.write(aData);
                // 작성한 데이터를 파일에 넣는다
                bw.newLine(); // 개행
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.flush(); // 남아있는 데이터까지 보내 준다
                    bw.close(); // 사용한 BufferedWriter를 닫아 준다
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
