package com.corporation8793.fragment;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.corporation8793.MySharedPreferences;
import com.corporation8793.R;
import com.corporation8793.dialog.RetakeDialog;

import static android.app.Activity.RESULT_OK;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Step3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Step3 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    LinearLayout upload_area;
    ImageView upload_img;

    private static final int REQUEST_IMAGE_CODE = 101;
    boolean check = false;
    private RetakeDialog retakeDialog;

    String contents_name;

    public Step3() {
        // Required empty public constructor
    }

    public Step3(String contents_name) {
        // Required empty public constructor
        this.contents_name = contents_name;
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
        View view = inflater.inflate(R.layout.fragment_step3, container, false);
        MySharedPreferences.setInt(getContext(),contents_name,2);
        upload_area = view.findViewById(R.id.upload_area);
        upload_img = view.findViewById(R.id.upload_img);

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
        return view;
    }

    public void takePicture(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null){
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CODE);
        }
    }

    private void saveImage(Bitmap bitmap, @NonNull String name) throws IOException {
        OutputStream fos;
        File directory = Environment.getExternalStoragePublicDirectory("/learn");
        if(!directory.mkdirs()){
            Log.e("FILE", "Directory not created");
        }else{
            Log.e("hi","file created");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = getActivity().getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name + ".jpg");
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM+"/배울래");
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            Log.e("imageUri",imageUri.toString());
            Log.e("check 위치", Environment.getExternalStorageDirectory().toString());
            fos = resolver.openOutputStream(Objects.requireNonNull(imageUri));
        } else {
            File imagesDir = Environment.getExternalStoragePublicDirectory("/learn");
            if(!imagesDir.mkdirs()){
                Log.e("FILE", "Directory not created");
            }else{
                Log.e("hi","file created");
            }
            File image = new File(imagesDir.toString(), name + ".jpg");
            fos = new FileOutputStream(image);
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        Objects.requireNonNull(fos).close();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CODE && resultCode == RESULT_OK){
            OutputStream fos;

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            upload_img.setImageBitmap(imageBitmap);

            try {
                saveImage(imageBitmap,"check");
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (!check) {

                if (MySharedPreferences.getInt(getContext(),contents_name+" MAX") < 3) {
                    MySharedPreferences.setInt(getContext(), contents_name+" MAX", 3);
                }
                    MySharedPreferences.setInt(getContext(), contents_name, 3);

                check = true;
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