package com.learn4.util;

import android.content.Context;
import android.util.Log;

import com.learn4.data.dto.Chapter;
import com.learn4.data.dto.LearningObjective;
import com.learn4.data.dto.Project;
import com.learn4.data.dto.Subclass;
import com.learn4.data.room.AppDatabase;
import com.learn4.data.room.AppDatabase2;
import com.learn4.data.room.dao.BlockDictionaryDao;
import com.learn4.data.room.dao.ComponentDao;
import com.learn4.data.room.dao.ContentsDao;
import com.learn4.data.room.dao.ContentsGoalDao;
import com.learn4.data.room.dao.TestEntityDao;
import com.learn4.data.room.entity.BlockDictionary;
import com.learn4.data.room.entity.Component;
import com.learn4.data.room.entity.ContentGoal;
import com.learn4.data.room.entity.Contents;
import com.learn4.data.room.entity.TestEntity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;

public class DataSetting {
    InputStream is;
    Workbook workbook;
    public HashMap<String,String[]> components = new HashMap<>();
    public List<Contents> contents_list;
    public HashMap<String,List<Chapter>> chapter_list  = new HashMap<>();
    
    AppDatabase db = null;
    AppDatabase2 db2 = null;
    Context context;
    public List<Project> project_contents_list = new ArrayList<>();

    public ComponentDao componentDao;
    public ContentsDao contentsDao;
    public BlockDictionaryDao blockDictionaryDao;
    public ContentsGoalDao contentsGoalDao;

    public TestEntityDao testEntityDao;

    public DataSetting(Context context){
        this.context = context;
        Log.e("setting","init!");
        db = AppDatabase.getInstance(this.context);
        db2 = AppDatabase2.getInstance(this.context);

        componentDao = db.componentDao();
        contentsDao = db.contentsDao();
        blockDictionaryDao = db2.blockDictionaryDao();
        contentsGoalDao = db2.contentsGoalDao();

        //testEntityDao = db2.testEntityDao();

//        chapter_list = new ArrayList<Chapter>();
    }

    private static DataSetting settingManager = null;

    //MovieDatabaseManager 싱글톤 패턴으로 구현
    public static DataSetting getInstance(Context context)
    {
        if(settingManager == null)
        {
            settingManager = new DataSetting(context);
        }

        return settingManager;
    }


    public boolean dataCheck(){
//        contentsDao.
        boolean check = contentsDao.findAll().size() >0;
        Log.e("check",contentsDao.findAll().size()+"");
        if (check){
            Log.e("already save data in db","!!");
            readGoal();
            //readGoal2();
//            return true;
        }else{
            Log.e("no data","!!");
            readContents();
            readComponent();
            readBlock();

            readGoal();

        }

        readLearningObjective();
        readProjectContents();

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

    public void insert_blockDictionary_data(BlockDictionary blockDictionary) {
        blockDictionaryDao.insert(blockDictionary);
    }

    public void insert_contentsGoal_data(ContentGoal contentGoal) {
        contentsGoalDao.insert(contentGoal);
    }

    public void insert_testEntity_data(TestEntity testEntity) {
        testEntityDao.insert(testEntity);
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


    public void readLearningObjective() {
        Log.e("learning in","check");
        try {
            is = context.getResources().getAssets().open("test2.xls");
            workbook = Workbook.getWorkbook(is);

            if (workbook != null) {
                Log.e("learning in","sheet");

                Sheet sheet = workbook.getSheet(6);

                if (sheet != null) {
                    int colTotal = sheet.getColumns();

                    Log.e("learning check","colTotal : " + colTotal+"");
                    int rowTotal = sheet.getRows();
                    Log.e("learning check","rowTotal : " + rowTotal+"");



                    int rowIndexStart = 2;
                    boolean plus;

                    for (int row = rowIndexStart; row < rowTotal; row+=3) {

                        plus = true;

                        // 블록 이름 not null 확인
                        String plusCheck = sheet.getCell(1, row).getContents();


                            // 블록 순번 제외
                        ArrayList<Subclass> subclass = new ArrayList<>();

                            String id = sheet.getCell(1, row).getContents();
                            String title = sheet.getCell(2, row).getContents();



                            // 소분류 데이터 가져오기  ex: LED 깜박이기의 1,2,3번 콘텐츠 가져오기
                            for (int subrow = row; subrow <= row+2; subrow++) {
                                Log.e("check learning",sheet.getCell(3,subrow).getContents());
                                Log.e("check learning",sheet.getCell(4,subrow).getContents());
                                Log.e("check learning","-------------");
                                subclass.add(  new Subclass(sheet.getCell(3,subrow).getContents(),
                                        sheet.getCell(4,subrow).getContents(),
                                        sheet.getCell(5,subrow).getContents(),
                                        sheet.getCell(6,subrow).getContents()));
                            }

                            Application.learningObjectives.add(new LearningObjective(id,title,subclass));


                    }
                }
            }
        } catch (Exception e) {
            Log.e("test","error");

            e.printStackTrace();
        }
    }

    public void readGoal2() {
        try {
            is = context.getResources().getAssets().open("testtest.xls");
            workbook = Workbook.getWorkbook(is);

            if (workbook != null) {
                Sheet sheet = workbook.getSheet(6);

                if (sheet != null) {
                    int colTotal = sheet.getColumns();
                    Log.e("check","colTotal : " + colTotal+"");
                    int rowTotal = sheet.getRows();
                    Log.e("check","rowTotal : " + rowTotal+"");

                    int rowIndexStart = 2;
                    boolean plus;

                    for (int row = rowIndexStart; row < rowTotal; row++) {
                        plus = true;

                        // 블록 이름 not null 확인
                        String plusCheck = sheet.getCell(1, row).getContents();

                        if (plusCheck.equals("")) {
                            plus = false;
                        }

                        if (plus) {
                            String[] blocks = new String[4];
                            String totalCategory = "";

                            for (int subrow = row; subrow <= row+2; subrow++) {
                                String changeText = "";

                                for (int col = 3; col <= 6; col++) {
                                    if (col == 5 || col == 6) {
                                        changeText = sheet.getCell(col, subrow).getContents();
                                        changeText = changeText.replaceAll(System.getProperty("line.separator"), " ");

                                        blocks[col - 3] = changeText;
                                    } else {
                                        blocks[col - 3] = sheet.getCell(col, subrow).getContents();
                                    }
                                }

                                int plusNum = Integer.parseInt(blocks[0]) + 1;
                                totalCategory = plusCheck + "-" + Integer.toString(plusNum);

                                // 콘텐츠 목표 및 조건 저장
                                TestEntity testEntity = new TestEntity(plusCheck, blocks[1], totalCategory, blocks[2], blocks[3]);
                                insert_testEntity_data(testEntity);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readGoal() {
        try {
            is = context.getResources().getAssets().open("testtest.xls");
            workbook = Workbook.getWorkbook(is);

            if (workbook != null) {
                Sheet sheet = workbook.getSheet(6);

                if (sheet != null) {
                    int colTotal = sheet.getColumns();
                    Log.e("check","colTotal : " + colTotal+"");
                    int rowTotal = sheet.getRows();
                    Log.e("check","rowTotal : " + rowTotal+"");

                    int rowIndexStart = 2;
                    boolean plus;

                    for (int row = rowIndexStart; row < rowTotal; row++) {
                        plus = true;

                        // 블록 이름 not null 확인
                        String plusCheck = sheet.getCell(1, row).getContents();

                        if (plusCheck.equals("")) {
                            plus = false;
                        }

                        if (plus) {
                            String[] blocks = new String[4];
                            String totalCategory = "";

                            for (int subrow = row; subrow <= row+2; subrow++) {
                                String changeText = "";

                                for (int col = 3; col <= 6; col++) {
                                    if (col == 5 || col == 6) {
                                        changeText = sheet.getCell(col, subrow).getContents();
                                        changeText = changeText.replaceAll(System.getProperty("line.separator"), " ");

                                        blocks[col - 3] = changeText;
                                    } else {
                                        blocks[col - 3] = sheet.getCell(col, subrow).getContents();
                                    }
                                }

                                int plusNum = Integer.parseInt(blocks[0]) + 1;
                                totalCategory = plusCheck + "-" + Integer.toString(plusNum);

                                // 콘텐츠 목표 및 조건 저장
                                ContentGoal contentGoal = new ContentGoal(plusCheck, blocks[1], totalCategory, blocks[2], blocks[3]);
                                insert_contentsGoal_data(contentGoal);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void readBlock() {
        try {
            is = context.getResources().getAssets().open("test2.xls");
            workbook = Workbook.getWorkbook(is);

            if (workbook != null) {
                Sheet sheet = workbook.getSheet(5);

                if (sheet != null) {
                    int colTotal = sheet.getColumns();
                    Log.e("check","colTotal : " + colTotal+"");
                    int rowTotal = sheet.getRows();
                    Log.e("check","rowTotal : " + rowTotal+"");

                    int rowIndexStart = 2;
                    boolean plus;

                    for (int row = rowIndexStart; row < rowTotal; row++) {
                        plus = true;

                        // 블록 이름 not null 확인
                        String plusCheck = sheet.getCell(2, row).getContents();

                        if (plusCheck.equals("")) {
                            plus = false;
                        }

                        if (plus) {
                            // 블록 순번 제외
                            String[] blocks = new String[5];

                            for (int col = 2; col <= 6; col++) {
                                blocks[col - 2] = sheet.getCell(col, row).getContents();

                                Log.e("test", blocks[col - 2]);
                            }

                            // 블록 설명 저장
                            BlockDictionary blockDictionary = new BlockDictionary(blocks[0], blocks[1], blocks[2], blocks[3], blocks[4]);
                            insert_blockDictionary_data(blockDictionary);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e("test","error");
            e.printStackTrace();
        }
    }


    public void readProjectContents() {
        try {
            is = context.getResources().getAssets().open("test2.xls");
            workbook = Workbook.getWorkbook(is);

            if (workbook != null) {
                Sheet sheet = workbook.getSheet(8);

                if (sheet != null) {
                    int colTotal = sheet.getColumns();
                    Log.e("check","colTotal : " + colTotal+"");
                    int rowTotal = sheet.getRows();
                    Log.e("check","rowTotal : " + rowTotal+"");

                    int rowIndexStart = 2;
                    int id_num = 2;
                    int before = 0;
                    String project_name ="";
                    String contents_list = "";
                    boolean check = true;

                    for (int row = rowIndexStart; row < rowTotal; row++) {

                        // 콘텐츠 순번 더 이상 없을 때 나감
                        if (sheet.getCell(3, row).getContents().equals("")){
                            if (before !=0){
                                Log.e("check project in no more contents",contents_list);
                                project_contents_list.add(new Project(id_num, project_name,contents_list));
                                before = 0;
                            }
                            break;
                        }


                        Log.e("check project",row+"");
                        //콘텐츠 순번 다시 1부터 시작할 때 체크
                        if (before >Integer.parseInt(sheet.getCell(3, row).getContents())) {
                            Log.e("check project in ",contents_list);
                            Project project = new Project(id_num, project_name,contents_list);
                            check = true;
                        }

                        before = Integer.parseInt(sheet.getCell(3, row).getContents());

                        //프로젝트명, 순번 저장
                        if (check){
                            project_name = sheet.getCell(3, row).getContents();
                            id_num = Integer.parseInt(sheet.getCell(3, row).getContents());
                            check = false;
                        }


                        contents_list += sheet.getCell(4, row).getContents() + ",";


                    }
                }
            }
        } catch (Exception e) {
            Log.e("test","error");
            e.printStackTrace();
        }
        Log.e("project_list size",project_contents_list.size()+"");
    }

    public void readComponent(){
        try{
            is = context.getResources().getAssets().open("test.xls");
            workbook = Workbook.getWorkbook(is);
            if (workbook != null) {
                Sheet sheet = workbook.getSheet(1);

                if (sheet != null) {
                    int colTotal = sheet.getColumns();
                    Log.e("check","colTotal : " + colTotal+"");
                    int rowTotal = sheet.getRows();
                    Log.e("check","rowTotal : " + rowTotal+"");

                    int rowIndexStart = 1;
                    boolean plus;

                    for (int row = rowIndexStart; row < rowTotal; row++) {
                        plus = true;

                        // 부품 번호 not null 확인
                        String plusCheck = sheet.getCell(2, row).getContents();
                        if (plusCheck.equals("")) {
                            plus = false;
                        }

                        if (plus) {
                            // 부품 사진 제외
                            String[] components = new String[4];
                            for (int col = 1; col < 5; col++) {
                                if (col == 2) {
                                    // -가 있는 경우 _로 바꾸기
                                    if (sheet.getCell(col, row).getContents().contains("-")) {
                                        String cString = sheet.getCell(col, row).getContents().replace("-", "_");
                                        components[col - 1] = cString;
                                    } else {
                                        components[col - 1] = sheet.getCell(col, row).getContents();
                                    }
                                } else if (col == 4) {
                                    String cString = sheet.getCell(col, row).getContents().replaceAll(System.getProperty("line.separator"), " ");
                                    cString = cString.replaceAll("  ", " ");
                                    components[col - 1] = cString;
                                } else {
                                    components[col - 1] = sheet.getCell(col, row).getContents();
                                }

                                Log.e("test", components[col - 1]);
                            }

                            // 부품 번호, 부품 이름, 부품 설명 저장
                            Component component = new Component(components[1],components[2],components[3]);
                            insert_component_data(component);
                        }
                    }
                }
            }
            /*is = context.getResources().getAssets().open("contents_mode_info.xls");
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
                        // number, name, msg
                        Component component = new Component(components[1],components[2],components[3]);
                        insert_component_data(component);
//                        this.components.put(num,components);
                    }
                }
            }*/
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void readContents(){
        try{
            is = context.getResources().getAssets().open("test.xls");
            workbook = Workbook.getWorkbook(is);
            if (workbook != null){
                Sheet sheet = workbook.getSheet(0);

                if (sheet != null){
                    int colTotal = sheet.getColumns();
                    Log.e("check","colTotal : " + colTotal+"");
                    int rowTotal = sheet.getRows();
                    Log.e("check","rowTotal : " + rowTotal+"");

                    int rowIndexStart = 2;
                    String values;
                    boolean plus;

                    for (int row = rowIndexStart; row < rowTotal; row++) {
                        values = "";
                        plus = false;

                        String plusCheck = sheet.getCell(2, row).getContents();
                        if (plusCheck.equals("LMS/콘텐츠")) {
                            plus = true;
                        }

                        if (plus) {
                            for (int col = 1; col < colTotal; col++) {
                                String contents = sheet.getCell(col, row).getContents();

                                // 여기에 추가하기 부품번호 - >> _ 로
                                // 9, 10, 11
                                if (col == colTotal - 1) {
                                    // col == 11인 경우
                                    if (contents.equals("")) {
                                        values += "none";
                                    } else {
                                        if (contents.contains("-")) {
                                            String cContents = contents.replace("-", "_");
                                            values += cContents;
                                        } else {
                                            values += contents;
                                        }
                                    }
                                } else {
                                    if (contents.equals("")) {
                                        values += "none" + "~";
                                    } else {
                                        if (col == 9 || col == 10) {
                                            if (contents.contains("-")) {
                                                String cContents = contents.replace("-", "_");
                                                values += cContents + "~";
                                            } else {
                                                values += contents + "~";
                                            }
                                        } else {
                                            values += contents + "~";
                                        }
                                    }
                                }
                            }

                            if (!values.equals("")) {
                                String[] value = values.split("~");

                                for (int i = 0; i < value.length; i++) {
                                    Log.e("value" + i, "value" + i + " : " + value[i]);
                                }

                                Contents contents = new Contents(
                                        Integer.parseInt(value[2]), value[3], value[4], value[5], value[6], value[8], value[9], value[10]);

                                Log.e("contents check",contents.getDeep_problem1());

                                insert_contents_data(contents);
                            }
                        }
                    }
                    printList();
                }
            }

        /*try{
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
            }*/
        }catch (Exception e){
            Log.e("runtime",e.toString());
        }
    }
}