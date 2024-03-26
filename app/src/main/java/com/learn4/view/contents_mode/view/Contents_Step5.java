package com.learn4.view.contents_mode.view;

import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.learn4.R;
import com.learn4.util.DisplaySize;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Contents_Step5#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Contents_Step5 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView question_text;
    ImageView block_image;

    public Contents_Step5() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Contents_Step5.
     */
    // TODO: Rename and change types and number of parameters
    public static Contents_Step5 newInstance(String param1, String param2) {
        Contents_Step5 fragment = new Contents_Step5();
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
        View view = inflater.inflate(R.layout.fragment_contents__step5, container, false);

        question_text = view.findViewById(R.id.question_text);
        block_image = view.findViewById(R.id.block_image);

        question_text.setTextSize(DisplaySize.font_size_y_28);

        return view;
    }
}