package com.learn4;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherData {

    private String nx = "55";	//위도
    private String ny = "127";	//경도
    private String baseDate = "20231114";	//조회하고싶은 날짜
    private String baseTime = "0800";	//조회하고싶은 시간
    private String type = "json";	//조회하고 싶은 type(json, xml 중 고름)


    String weather, tmperature, data_type ="기온";

    String TMP, REH, PTY, PCP, WSD, SKY;


    public WeatherData(){
//        long now = System.currentTimeMillis();
//        Date date = new Date(now);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//        String getTime = sdf.format(date);
//        baseDate = getTime;
//        Log.e("baseDate",baseDate);
    }

    public void lookUpWeather(String date, String time, String local, String data) throws IOException, JSONException {

        Log.e("hi","lookupweather: "+ date+","+ time+","+ local+","+ data);
//		참고문서에 있는 url주소
        String apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";
//         홈페이지에서 받은 키
        String serviceKey = "asO9jixtVSQd1RMbVCUr%2F1yFhPuiL5H9VXW1qGHbnb8TXnIWvVQ4MP0qS0pi4gf2EplaNECQC6ucPukAlFhnyA%3D%3D";

        setLocal(local);
        baseDate = date.replace("-","");
        baseTime = timeChange(time);
        setType(data);

        Log.e("date check",date.replace("-",""));
        StringBuilder urlBuilder = new StringBuilder(apiUrl);
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "="+serviceKey);
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("14", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); //경도
        urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); //위도
        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8")); /* 조회하고싶은 날짜*/
        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8")); /* 조회하고싶은 시간 AM 02시부터 3시간 단위 */
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode(type, "UTF-8"));	/* 타입 */

        /*
         * GET방식으로 전송해서 파라미터 받아오기
         */
        URL url = new URL(urlBuilder.toString());

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        Log.e("Response code: " , conn.getResponseCode()+"");
        
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        
        rd.close();
        conn.disconnect();
        String result= sb.toString();

        Log.e("result",result);

	//=======이 밑에 부터는 json에서 데이터 파싱해 오는 부분이다=====//
    
        // response 키를 가지고 데이터를 파싱
        JSONObject jsonObj_1 = new JSONObject(result);
        String response = jsonObj_1.getString("response");

        // response 로 부터 body 찾기
        JSONObject jsonObj_2 = new JSONObject(response);

        String header = jsonObj_2.getString("header");
        JSONObject jsonObj_header = new JSONObject(header);
        String resultMsg = jsonObj_header.getString("resultMsg");

        if (resultMsg.equals("NO_DATA")){
            tmperature = "NO_DATA";
            return;
        }


        String body = jsonObj_2.getString("body");

        // body 로 부터 items 찾기
        JSONObject jsonObj_3 = new JSONObject(body);
        String items = jsonObj_3.getString("items");
        Log.e("ITEMS",items);

        // items로 부터 itemlist 를 받기 
        JSONObject jsonObj_4 = new JSONObject(items);
        JSONArray jsonArray = jsonObj_4.getJSONArray("item");



        for(int i=0;i<jsonArray.length();i++){
            jsonObj_4 = jsonArray.getJSONObject(i);
            String fcstValue = jsonObj_4.getString("fcstValue");
            String category = jsonObj_4.getString("category");
            Log.e("category",category);
            if(category.equals(data_type)) {
                if(data_type.equals("PCP")){
                    if (fcstValue.equals("강수없음")){
                        fcstValue = "0";
                    }

                }
                    tmperature = fcstValue;

            }
            setData(category, fcstValue);

        }

    }

    public String getData(String type){
        switch (type.trim()){
            case "기온":
                return TMP;

            case "강수량":
                return PCP;

            case "하늘상태":
                return SKY;
            case "습도":
                return REH;

            case "강수형태":
                return PTY;

            case "풍속":
                return WSD;

        }
        return "데이터 없음";
    }

    public void setData(String type, String value){
        switch (type){
            case "PCP":
                PCP = value;
                break;
            case "PTY":
                PTY = value;
                break;
            case "REH":
                REH = value;
                break;
            case "SKY":
                SKY = value;
                break;
            case "WSD":
                WSD = value;
                break;
            case "TMP":
                TMP = value;
                break;
            default:
                break;
        }

    }

    public String timeChange(String time)
    {
        // 현재 시간에 따라 데이터 시간 설정(3시간 마다 업데이트) //
        time = time.replace("시","00");
        switch(time.trim()) {

            case "0200":
            case "0300":
            case "0400":
                time = "0200";
                Log.e("time","0200");
                break;
            case "0500":
            case "0600":
            case "0700":
                time = "0500";
                Log.e("time","0500");
                break;
            case "0800":
            case "0900":
            case "1000":
                time = "0800";
                Log.e("time","0800");
                break;
            case "1100":
            case "1200":
            case "1300":
                time = "1100";
                Log.e("time","1100");
                break;
            case "1400":
            case "1500":
            case "1600":
                time = "1400";
                Log.e("time","1400");
                break;
            case "1700":
            case "1800":
            case "1900":
                time = "1700";
                Log.e("time","1700");
                break;
            case "2000":
            case "2100":
            case "2200":
                time = "2000";
                Log.e("time","2000");
                break;
            case "2300":
            case "0000":
            case "0100":
                time = "2300";
                Log.e("time","2300");
                break;
        }
        return time;
    }

    public String getTmperature(){
        return tmperature;
    }

    public void setLocal(String local){
        Log.e("local",local);
        switch (local.trim()){
            case "서울특별시":
                nx ="37";
                ny = "127";
                break;

            case "인천광역시":
                nx ="37";
                ny = "126";
                break;

            case "대전광역시":
                nx ="36";
                ny = "127";
                break;

            case "대구광역시":
                nx ="35";
                ny = "128";
                break;

            case "울산광역시":
                nx ="35";
                ny = "129";
                break;

            case "광주광역시":
                nx ="35";
                ny = "126";
                break;

            case "부산광역시":
                nx ="35";
                ny = "129";
                break;
        }
        Log.e("위도 경도",nx+","+ny);
    }


    public void setType(String type){
        Log.e("type",type.trim());
        switch (type.trim()){
            case "기온":
                data_type = "TMP";
                break;

            case "강수량":
                data_type = "PCP";
                break;

            case "하늘상태":
                data_type = "SKY";
                break;

            case "습도":
                data_type = "REH";
                break;

            case "강수형태":
                data_type = "PTY";
                break;

            case "풍속":
                data_type = "WSD";
                break;


        }
        Log.e("data_type",data_type);
    }

    public String SkyType(String data){
        Log.e("data",data);
        if (data.trim().equals("1")){
            return "맑음";
        }else if (data.trim().equals("3")){
            return "구름많음";
        } else if (data.trim().equals("4")){
            return "흐림";
        }
       return "보통";
    }

    public String RainType(String rain){
     switch (rain.trim()) {
         case "0":
             return "없음";
         case "1":
             return "비";
         case "2":
             return "비/눈";
         case "3":
             return "눈";
         case "4":
             return "소나기";
     }

        return "없음";
    }

    public void setTime(String time){
        baseTime = time;
    }

}