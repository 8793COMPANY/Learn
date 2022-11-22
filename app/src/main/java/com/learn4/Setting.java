package com.learn4;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.util.Log;
import android.widget.Toast;


import com.learn4.dto.Chapter;
import com.learn4.room.AppDatabase;
import com.learn4.room.dao.ComponentDao;
import com.learn4.room.dao.ContentsDao;
import com.learn4.room.entity.Component;
import com.learn4.room.entity.Contents;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import jxl.Sheet;
import jxl.Workbook;

public class Setting {
    InputStream is;
    Workbook workbook;
    public HashMap<String,String[]> components = new HashMap<>();
    public List<Contents> contents_list;
    public HashMap<String,List<Chapter>> chapter_list  = new HashMap<>();
    
    AppDatabase db = null;
    Context context;

    public ComponentDao componentDao;
    public ContentsDao contentsDao;

    public Setting(Context context){
        this.context = context;
        Log.e("setting","init!");
        db = AppDatabase.getInstance(this.context);

        componentDao = db.componentDao();
        contentsDao = db.contentsDao();

//        chapter_list = new ArrayList<Chapter>();
    }

    private static Setting settingManager = null;

    //MovieDatabaseManager 싱글톤 패턴으로 구현
    public static Setting getInstance(Context context)
    {
        if(settingManager == null)
        {
            settingManager = new Setting(context);
        }

        return settingManager;
    }


    public boolean dataCheck(){

//        contentsDao.
        boolean check = contentsDao.findAll().size() >0;
        if (check){
            Log.e("already save data in db","!!");
//            return true;
        }else{
            readContents();
            readComponent();
        }

        settingChapterByLevel(1);
        settingChapterByLevel(2);
        settingChapterByLevel(3);

        return check;
    }


    public void insert_contents_data(Contents con){
        contentsDao.insert(con);
    }

    public void insert_component_data(Component component){
        componentDao.insert(component);

    }

    public void printList(){
        List<Contents> contents = contentsDao.findAll();
        for (int i=0; i<contents.size(); i++){
            Log.e("contents",contents.get(i).getName());
            Log.e("contents",contents.get(i).getBasic_problem());
            Log.e("contents",contents.get(i).getBasic_supplies());
            Log.e("----------","------------");
        }

    }

    public void settingChapterByLevel(int level){
        Log.e("in","settingChapterByLevel");
        List<Chapter> chapters = new ArrayList<>();

//        List<Contents> contents = contentsDao.findByLevel(level);
//        Log.e("contentDao",componentDao.findAll().size()+"");
//
//        List<Component> components=componentDao.findAll();
//        for(int i=0; i<components.size(); i++) {
//            Log.e( "onCreate: findAll() : " , components.get(i).getName());
//            Log.e( "onCreate: findAll() : " ,  components.get(i).getNumber());
//            Log.e( "onCreate: findAll() : " ,  components.get(i).getComponent_msg());
//        }

        for (Contents contents : contentsDao.findByLevel(level)){
            Log.e("in","for문");
            Log.e("contents",contents.getName());
            Chapter chapter = new Chapter();
            chapter.id = contents.getId();
            chapter.chapterName =contents.getName();
            chapter.image = contents.getId();
            chapters.add(chapter);
        }

        chapter_list.put(String.valueOf(level),chapters);


    }

    public void printChapter(){
        for (int i=0; i< chapter_list.size(); i++){
            Log.e("checking",chapter_list.get(String.valueOf(i+1)).size()+"");
        }
    }

    public void readComponent(){
        try{
            is = context.getResources().getAssets().open("contents_mode_info.xls");
            workbook = Workbook.getWorkbook(is);
            if (workbook != null){
                Sheet sheet = workbook.getSheet(1);
                if (sheet != null){
                    int colTotal = sheet.getColumns();
                    int rowIndexStart = 1;
                    int rowTotal = sheet.getColumn(colTotal-1).length;

                    String num;
                    Log.e("rowTotal",rowTotal+"");
                    for (int row = rowIndexStart; row<18; row++){
                        num = sheet.getCell(2,row).getContents();

                        String [] components = new String[4];
                        for (int col=1; col<5; col++){
                            components[col - 1] = sheet.getCell(col,row).getContents();
                            if (components[col - 1].equals("")){

                            }else {
                                Log.e("content check ", components[col - 1]);
                            }
                        }
                        Component component = new Component(components[1],components[2],components[3]);
                        insert_component_data(component);
//                        this.components.put(num,components);
                    }

                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void readContents(){
        try{
            is = context.getResources().getAssets().open("contents_mode_info.xls");
            workbook = Workbook.getWorkbook(is);
            if (workbook != null){
                Sheet sheet = workbook.getSheet(0);
                if (sheet != null){
                    int colTotal = sheet.getColumns();
                    int rowIndexStart = 2;
                    int rowTotal = sheet.getColumn(colTotal-1).length;

                    StringBuilder sb;
                    String values = "";
                    int count=1;
                    for (int row = rowIndexStart; row<rowTotal; row++){
                        sb = new StringBuilder();

                        values = "";

                        for (int col=1; col<colTotal; col++){
                            String contents = sheet.getCell(col,row).getContents();
                            if (contents.equals("")){
                                break;
                            }else{
                                Log.e("col "+col+":", contents);


                                if (col == colTotal - 1) {
                                    values += contents;
                                } else {
                                    values += contents + "/";
                                }



                            }

                        }
//                        Log.e("col len",values.length+"");
                        if (!values.equals("")) {
                            String[] value = values.split("/");
                            for (int i = 0; i < value.length; i++) {
                                Log.e("value"+i, value[i]);
                            }


                            Contents contents = new Contents(
                                    Integer.parseInt(value[1]), value[2], value[3], value[4], value[5], value[6], value[7], value[8]);


                            Log.e("contents check",contents.getDeep_problem1());

                            insert_contents_data(contents);
                            count++;

                        }
                    }
                    printList();

                }
            }
        }catch (Exception e){
            Log.e("runtime",e.toString());
        }
    }

    public Chapter settingChapter(int id, String name){
        Chapter chapter = new Chapter();
        chapter.id = id;
        chapter.chapterName = name;
        chapter.image = R.drawable.chapter1;
        return chapter;
    }

    ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(Network network) {
            Toast.makeText(context, "network 연결", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLost(Network network) {
            Toast.makeText(context, "network 해제", Toast.LENGTH_SHORT).show();
        }

    };

    public void registerNetworkCallback() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest wifiNetworkRequest = new NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build();
        cm.registerNetworkCallback(wifiNetworkRequest, networkCallback);
    }

    public void unregisterNetworkCallback() {
        ConnectivityManager cm = (ConnectivityManager)  context.getSystemService(Context.CONNECTIVITY_SERVICE);
        cm.unregisterNetworkCallback(networkCallback);
    }

//    Boolean isWIFIConnected(Context context){
//        Boolean result = false;
//        ConnectivityManager cm = (ConnectivityManager)  context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkCapabilities  capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
//        if (capabilities != null) {
//            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
//                result = true;
//            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
//                result = true;
//            }
//        }
//        return result;
//    }
}
