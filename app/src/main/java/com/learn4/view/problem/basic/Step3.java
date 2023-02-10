package com.learn4.view.problem.basic;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.learn4.util.Application;
import com.learn4.util.MySharedPreferences;
import com.learn4.R;
import com.learn4.view.custom.dialog.ProgressDialog;
import com.learn4.view.custom.dialog.RetakeDialog;
import com.learn.wp_rest.data.acf.UploadReportJson;
import com.learn.wp_rest.data.wp.media.Media;
import com.learn.wp_rest.data.wp.posts.UploadReport;

import static android.app.Activity.RESULT_OK;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import kotlin.Pair;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Step3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Step3 extends Fragment {
    ProgressDialog customProgressDialog;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    LinearLayout upload_area;
    ImageView upload_img;

    Uri photURI;

    private static final int REQUEST_IMAGE_CODE = 101;
    boolean check = false;
    private RetakeDialog retakeDialog;

    String contents_name, chapter_id;
    String mCurrentPhotoPath = "";
    String [] chapter_id_split;

    boolean black_color_check = false;


    public Step3() {
        // Required empty public constructor
    }

    public Step3(String contents_name,String chapter_id) {
        // Required empty public constructor
        this.contents_name = contents_name;
        this.chapter_id = chapter_id;
        chapter_id_split = chapter_id.split("-");
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Step1.
     */
    // TODO: Rename and change types and number of parameters
    public static Step3 newInstance(String param1, String param2) {
        Step3 fragment = new Step3();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.e("hi","!!");
        Log.e("hi","//");


        View view = inflater.inflate(R.layout.fragment_step3, container, false);
        MySharedPreferences.setInt(getContext(),contents_name,2);
        upload_area = view.findViewById(R.id.upload_area);
        upload_img = view.findViewById(R.id.upload_img);

        customProgressDialog = new ProgressDialog(getContext());
        customProgressDialog.setContentView(R.layout.dialog_progress);

        customProgressDialog.setCancelable(false);
        customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        retakeDialog = new RetakeDialog(getContext(), retake_ok,retake_cancel);
        upload_area.setOnClickListener(v->{
            if (check){
                retakeDialog.show();
                Display display = getActivity().getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);

                Window window = retakeDialog.getWindow();

                int x = (int)(size.x * 0.3f);
                int y = (int)(size.y * 0.35f);

                window.setLayout(x,y);

            }else{
                takePicture();
            }


        });

        Log.e("chapter_id",chapter_id);


        return view;
    }

    public void takePicture(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null){
            File photoFile = null;
            File tempDir = getActivity().getCacheDir();

            String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss").format(new Date());
            String imageFileName = "Capture_"+timeStamp+"_";

            try {
                File tempImage = File.createTempFile(imageFileName,".jpg",tempDir);
                mCurrentPhotoPath = tempImage.getAbsolutePath();
                photoFile = tempImage;
            }catch (IOException e){
                e.printStackTrace();
            }

            if (photoFile != null) {
                Log.e("photo file","not null");
                photURI = FileProvider.getUriForFile(getContext(), getActivity().getPackageName() + ".fileprovider", photoFile);
                Log.e("photUri",photURI+"");
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CODE);
            }
        }
    }




    private String getRealPathFromURI(Uri contentUri) {
        if (contentUri.getPath().startsWith("/storage")) {
            return contentUri.getPath();
        }

        String id = DocumentsContract.getDocumentId(contentUri).split(":")[1];
        String[] columns = { MediaStore.Files.FileColumns.DATA };
        String selection = MediaStore.Files.FileColumns._ID + " = " + id;
        Cursor cursor = getContext().getContentResolver().query(MediaStore.Files.getContentUri("external"), columns, selection, null, null);
        try {
            int columnIndex = cursor.getColumnIndex(columns[0]);
            if (cursor.moveToFirst()) {
                return cursor.getString(columnIndex);
            }
        } finally {
            cursor.close();
        }
        return null;
    }



    private void saveImage(File imageFile, Bitmap bitmap, @NonNull String name) throws IOException {
        OutputStream fos;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            ContentResolver resolver = getActivity().getContentResolver();
//            ContentValues contentValues = new ContentValues();
//            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name + ".jpg");
//            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");
//            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM+"/배울래");
//            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            new Thread(()->{

//                File file = new File("src/test/kotlin/com/learn/wp_rest/wp/media/unsubmission_img.png");
//                Log.e("in","file");
//                Pair<String, Media> response_unsubmission_img = Application.mediaRepository.uploadMedia(file);
//
//                Log.e("response_unsubmission_img",response_unsubmission_img.getFirst());
//                Log.e("response_unsubmission_img",response_unsubmission_img.getSecond().toString());
                //미제출 이미지 = http://baeulrae.kr/wp-content/uploads/2022/09/unsubmission_img.jpg

                Log.e("in","thread");
                try {
                    Pair<String, Media> response_ci = Application.mediaRepository.uploadMedia(imageFile);
//                        File file = new File(getRealPathFromURI(photURI));
//                        Pair<String, Media> response_ci = Application.mediaRepository.uploadMedia(file);

                    Log.e("in","thread2");
                    MySharedPreferences.setString(getContext(),"circuit_img"+chapter_id,response_ci.getSecond().getGuid().getRendered());
                    Log.e("in","thread3");

                    Log.e("response_ci",response_ci.getFirst());
                    Log.e("response_ci",response_ci.getSecond().toString());

                    String block_img ="http://baeulrae.kr/wp-content/uploads/2022/09/unsubmission_img.jpg";
                    if (MySharedPreferences.getString(getContext(),"block_img"+chapter_id) != null)
                        block_img = MySharedPreferences.getString(getContext(),"block_img"+chapter_id);

                    Pair<String, UploadReport> response =Application.postsRepository.createUploadReport(
                            chapter_id+". "+contents_name,
                            response_ci.getSecond().getGuid().getRendered(),
                            block_img
                    );
                    Log.e("response",response.getFirst());
                    Log.e("response",response.getSecond().toString());
                    Pair<String, UploadReportJson> upload_result = Application.acfRepository.updateUploadReportAcf(
                            response.getSecond().getId(),
                            Integer.parseInt(chapter_id_split[0]),
                            Integer.parseInt(chapter_id_split[1]),
                            response_ci.getSecond().getGuid().getRendered(),
                            block_img
                    );
                    Log.e("response",upload_result.getFirst());
                    customProgressDialog.dismiss();
                    if (!check) {

                        if (MySharedPreferences.getInt(getContext(),contents_name+" MAX") < 3) {
                            MySharedPreferences.setInt(getContext(), contents_name+" MAX", 3);
                        }
                        MySharedPreferences.setInt(getContext(), contents_name, 3);

                        check = true;
                    }
                }catch (NullPointerException e){
                    Log.e("error","null error");
                    customProgressDialog.dismiss();
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run()
                        {
                            Toast.makeText(getContext(),"사진을 다시 찍어주세요",Toast.LENGTH_SHORT).show();
                        }
                    }, 0);
                }


            }).start();



//            Log.e("imageUri",imageUri.toString());
//            fos = resolver.openOutputStream(Objects.requireNonNull(imageUri));
        } else {
            File imagesDir = Environment.getExternalStoragePublicDirectory("/learn");
            if(!imagesDir.mkdirs()){
                Log.e("FILE", "Directory not created");
            }else{
                Log.e("hi","file created");
            }
            File image = new File(imagesDir.toString(), name + ".jpg");
            fos = new FileOutputStream(imageFile);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            Objects.requireNonNull(fos).close();
        }

    }

    public String getPathFromUri(Uri uri){
        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null );
        cursor.moveToNext();
        @SuppressLint("Range") String path = cursor.getString( cursor.getColumnIndex( "_data" ) );
        cursor.close();
        return path;
    }

    private Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//Compression quality, here 100 means no compression, the storage of compressed data to baos
        int options = 90;
        Log.e("check byte",baos.toByteArray().length+"");
        while (baos.toByteArray().length / 1024 > 400) {  //Loop if compressed picture is greater than 400kb, than to compression
            baos.reset();//Reset baos is empty baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//The compression options%, storing the compressed data to the baos
            options -= 10;//Every time reduced by 10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//The storage of compressed data in the baos to ByteArrayInputStream
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//The ByteArrayInputStream data generation
        return bitmap;
    }

//    public static boolean reduceImage(String path, long maxSize) {
//        File img = new File(path);
//        boolean result = false;
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        Bitmap bitmap = null;
//        options.inSampleSize=1;
//        while (img.length()>maxSize) {
//            options.inSampleSize = options.inSampleSize+1;
//            bitmap = BitmapFactory.decodeFile(path, options);
//            img.delete();
//            try
//            {
//                FileOutputStream fos = new FileOutputStream(path);
//                img.compress(path.toLowerCase().endsWith("png")?
//                        Bitmap.CompressFormat.PNG:
//                        Bitmap.CompressFormat.JPEG, 100, fos);
//                fos.close();
//                result = true;
//            }catch (Exception errVar) {
//                errVar.printStackTrace();
//            }
//        };
//        return result;
//    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CODE && resultCode == RESULT_OK){
            OutputStream fos;
            Log.e("hi onactivityresult","!!");

//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            upload_img.setImageBitmap(imageBitmap);
            File file = new File(mCurrentPhotoPath);
            Bitmap imageBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
            imageBitmap = compressImage(imageBitmap);


//            upload_img.setImageURI(photURI);
            upload_img.setImageBitmap(imageBitmap);
            Log.e("hi file end","!!");
            try {
                Log.e("hi dialog start","!!");
                customProgressDialog.show();
                Log.e("hi dialog show","!!");
//                Palette.from(imageBitmap).generate(palette -> {
//                    if(palette==null)
//                        return;
//
//
//                    Palette.Swatch vibrantSwatch = palette.getDominantSwatch();
//                    if(vibrantSwatch!=null)
//                    {
//                        Log.e("vibrantSwatch","not null");
//                        int color = vibrantSwatch.getRgb();//swatch에서 대표색 추출
//                        Log.e("color",color+"");
//                        if (color != -15724528)
//                            black_color_check = true;
//                        upload_area.setBackgroundColor(color);
//                    }else{
//                        Log.e("vibrantSwatch","null");
//                    }
//
//                });

//                if (!black_color_check) {
                saveImage(file, imageBitmap, "check");

//                }else{
//                    customProgressDialog.dismiss();
//                    Toast.makeText(getContext(),"사진을 다시 찍어주세요",Toast.LENGTH_SHORT).show();
//                }
                Log.e("hi dialog end","!!");

            } catch (Exception e) {
                Log.e("e",e.toString());
                customProgressDialog.dismiss();
            }


        }
    }

    private View.OnClickListener retake_ok = new View.OnClickListener() {
        public void onClick(View v) {
//            Toast.makeText(getApplicationContext(), "확인버튼",Toast.LENGTH_SHORT).show();
            takePicture();
            retakeDialog.dismiss();
        }
    };

    private View.OnClickListener retake_cancel = new View.OnClickListener() {
        public void onClick(View v) {
//            Toast.makeText(getApplicationContext(), "확인버튼",Toast.LENGTH_SHORT).show();
            retakeDialog.dismiss();
        }
    };
}