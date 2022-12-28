package com.example.findthesolution.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.findthesolution.adapter.LevelViewPagerAdapter;
import com.example.findthesolution.databinding.FragmentLevelBinding;
import com.example.findthesolution.modle.GameData;
import com.example.findthesolution.modle.Pattern;
import com.example.findthesolution.modle.Questions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LevelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LevelFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String LEVEL_NO = "level_no";

    // TODO: Rename and change types of parameters
    private int level_no;
    private int mystery_count = 0;

    FragmentLevelBinding binding;
    ViewPager viewPager;
    TextView tv_level_no , tv_point_count , tv_mystery_count;
    Button btn_skip;

    LevelViewPagerAdapter adapter;
    private List<GameData> levelList;
    private List<Questions> questionList;

    private SharedPreferences sh_questionsCount;
    private SharedPreferences.Editor editor;

    public LevelFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static LevelFragment newInstance(int level_no) {
        LevelFragment fragment = new LevelFragment();
        Bundle args = new Bundle();
        args.putInt(LEVEL_NO, level_no);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            level_no = getArguments().getInt(LEVEL_NO);
        }

        levelList = new ArrayList<>();
        questionList = new ArrayList<>();
    //json//
        try {
            JSONArray jsonArray = new JSONArray(loadJSONFromAsset());

            for (int x = 0; x < jsonArray.length(); x++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(x);
                int level_no = (int) jsonObject.get("level_no");
                int unlock_points = (int) jsonObject.get("unlock_points");

                if (level_no == this.level_no) {
                    JSONArray jsonArrayContentQuestions = jsonObject.getJSONArray("questions");
                    mystery_count = jsonArrayContentQuestions.length();

                    for (int z = 0; z < jsonArrayContentQuestions.length(); z++) {
                        JSONObject jsonObjectContentQuestions = (JSONObject) jsonArrayContentQuestions.get(z);
                        int id = (int) jsonObjectContentQuestions.get("id");
                        String title = (String) jsonObjectContentQuestions.get("title");
                        String answer_1 = (String) jsonObjectContentQuestions.get("answer_1");
                        String answer_2 = (String) jsonObjectContentQuestions.get("answer_2");
                        String answer_3 = (String) jsonObjectContentQuestions.get("answer_3");
                        String answer_4 = (String) jsonObjectContentQuestions.get("answer_4");
                        String true_answer = (String) jsonObjectContentQuestions.get("true_answer");
                        int points = (int) jsonObjectContentQuestions.get("points");
                        int duration = (int) jsonObjectContentQuestions.get("duration");
                        JSONObject patternObject = (JSONObject) jsonObjectContentQuestions.getJSONObject("pattern");
                        int pattern_id = (int) patternObject.get("pattern_id");
                        String pattern_name = (String) patternObject.get("pattern_name");
                        String hint = (String) jsonObjectContentQuestions.get("hint");


                        Pattern pattern = new Pattern(pattern_id, pattern_name);
                        Questions questions = new Questions(id, title, answer_1, answer_2, answer_3, answer_4, true_answer, points
                                , duration, pattern, hint);
                        questionList.add(questions);
                    }
                    levelList.add(new GameData(level_no, unlock_points, questionList));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLevelBinding.inflate(inflater, container, false);
        viewPager = binding.fgLevelViewPager;
        tv_level_no = binding.fgLevelTvLevelNo;
        tv_point_count = binding.fgLevelTvPointCount;
        tv_mystery_count = binding.fgLevelTvMysteryCount;
        btn_skip = binding.fgLevelBtnSkip;


        sh_questionsCount = getActivity().getSharedPreferences("Points", Context.MODE_PRIVATE);
        editor = sh_questionsCount.edit();

        int points_level = sh_questionsCount.getInt("points" + level_no, 0);

        tv_level_no.setText("رقم المرحلة : "+level_no);
        tv_point_count.setText("عدد النقاط للمرحلة : "+points_level);
        tv_mystery_count.setText("عدد الالغاز : "+mystery_count);
        adapter = new LevelViewPagerAdapter(getContext(), levelList);

        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
            }
        });
        return binding.getRoot();
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("puzzleGameData.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}