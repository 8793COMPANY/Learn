package com.corporation8793.problem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.corporation8793.MySharedPreferences;
import com.corporation8793.R;
import com.corporation8793.custom.AnswerItem;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HorizontalAnswer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HorizontalAnswer extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    AnswerItem supplies1,supplies2;
    TextView name,comment;

    String contents_name;

    public HorizontalAnswer() {
        // Required empty public constructor
    }

    public HorizontalAnswer(String contents_name) {
        this.contents_name = contents_name;
        // Required empty public constructor
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
    public static HorizontalAnswer newInstance(String param1, String param2) {
        HorizontalAnswer fragment = new HorizontalAnswer();
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
        View view = inflater.inflate(R.layout.fragment_horizontal_answer, container, false);



        return view;

    }
}