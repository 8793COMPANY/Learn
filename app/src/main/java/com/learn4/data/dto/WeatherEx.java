package com.learn4.data.dto;

public class WeatherEx {
    String date; // 날짜
    String time; // 시간
    String local; // 지역
    String TMP; // 기온
    String PCP; // 강수량
    String SKY; // 하늘상태
    String REH; // 습도
    String PTY; // 강수형태
    String WSD; // 풍속

    public WeatherEx(String date, String time, String local, String TMP, String PCP,
                     String SKY, String REH, String PTY, String WSD) {
        this.date = date;
        this.time = time;
        this.local = local;
        this.TMP = TMP;
        this.PCP = PCP;
        this.SKY = SKY;
        this.REH = REH;
        this.PTY = PTY;
        this.WSD = WSD;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getTMP() {
        return TMP;
    }

    public void setTMP(String TMP) {
        this.TMP = TMP;
    }

    public String getPCP() {
        return PCP;
    }

    public void setPCP(String PCP) {
        this.PCP = PCP;
    }

    public String getSKY() {
        return SKY;
    }

    public void setSKY(String SKY) {
        this.SKY = SKY;
    }

    public String getREH() {
        return REH;
    }

    public void setREH(String REH) {
        this.REH = REH;
    }

    public String getPTY() {
        return PTY;
    }

    public void setPTY(String PTY) {
        this.PTY = PTY;
    }

    public String getWSD() {
        return WSD;
    }

    public void setWSD(String WSD) {
        this.WSD = WSD;
    }
}
