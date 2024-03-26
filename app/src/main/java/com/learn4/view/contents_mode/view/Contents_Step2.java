package com.learn4.view.contents_mode.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.learn4.R;
import com.learn4.util.DisplaySize;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Contents_Step2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Contents_Step2 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView title, item_difficulty, learning_objective, contents_condition;

    public Contents_Step2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Contents_Step2.
     */
    // TODO: Rename and change types and number of parameters
    public static Contents_Step2 newInstance(String param1, String param2) {
        Contents_Step2 fragment = new Contents_Step2();
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
        View view = inflater.inflate(R.layout.fragment_contents__step2, container, false);

        title = view.findViewById(R.id.title);
        item_difficulty = view.findViewById(R.id.item_difficulty);
        learning_objective = view.findViewById(R.id.learning_objective);
        contents_condition = view.findViewById(R.id.contents_condition);

        title.setTextSize(DisplaySize.font_size_y_38);
        item_difficulty.setTextSize(DisplaySize.font_size_y_31);
        learning_objective.setTextSize(DisplaySize.font_size_y_32);
        contents_condition.setTextSize(DisplaySize.font_size_y_32);

        return view;
    }
}