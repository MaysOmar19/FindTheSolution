package com.example.findthesolution.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.findthesolution.R;
import com.example.findthesolution.modle.GameData;
import com.example.findthesolution.modle.Questions;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LevelViewPagerAdapter extends PagerAdapter {

    ConstraintLayout layout1 , layout2 , layout3;
    TextView tv_question_no , tv_question_True, tv_question_Choose, tv_question_Fill;
    RadioButton rb_true , rb_false , rb_answer1 , rb_answer2 , rb_answer3 , rb_answer4;
    EditText et_fill;
    private List<GameData> list;
    private Context context;

    private SharedPreferences sh_questionsCount;
    private SharedPreferences.Editor editor;

    int points_level;


    public LevelViewPagerAdapter(Context context, List<GameData> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getItemPosition(@NotNull Object object) {
        return -2;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_view_pager, null);

        sh_questionsCount = context.getSharedPreferences("Points", Context.MODE_PRIVATE);
        editor = sh_questionsCount.edit();

        layout1 = view.findViewById(R.id.item_view_pager_layout1);
        layout2 = view.findViewById(R.id.item_view_pager_layout2);
        layout3 = view.findViewById(R.id.item_view_pager_layout3);
        tv_question_no = view.findViewById(R.id.item_view_pager_tv_question_no);
        tv_question_True = view.findViewById(R.id.item_view_pager_tv_question_True);
        tv_question_Choose = view.findViewById(R.id.item_view_pager_tv_question_Choose);
        tv_question_Fill = view.findViewById(R.id.item_view_pager_tv_question_Fill);
        rb_true = view.findViewById(R.id.item_view_pager_rb_true);
        rb_false = view.findViewById(R.id.item_view_pager_rb_false);
        rb_answer1 = view.findViewById(R.id.item_view_pager_rb_answer1);
        rb_answer2 = view.findViewById(R.id.item_view_pager_rb_answer2);
        rb_answer3 = view.findViewById(R.id.item_view_pager_rb_answer3);
        rb_answer4 = view.findViewById(R.id.item_view_pager_rb_answer4);
        et_fill = view.findViewById(R.id.item_view_pager_et_fill);

        GameData gameData = list.get(position);
        Questions questions = gameData.getQuestions().get(position);
        if (questions.getPattern().getPattern_id() == 1){
            layout1.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.GONE);
            layout3.setVisibility(View.GONE);

            rb_true.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (questions.getTrue_answer().equals("true")){
                        points_level = sh_questionsCount.getInt("points" + gameData.getLevel_no(), 0);
                        points_level = points_level + questions.getPoints();
                        editor.putInt("points" + gameData.getLevel_no() , points_level);
                        editor.apply();

                    }
                }
            });

            rb_false.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (questions.getTrue_answer().equals("false")){
                        points_level = sh_questionsCount.getInt("points" + gameData.getLevel_no(), 0);
                        points_level = points_level + questions.getPoints();
                        editor.putInt("points" + gameData.getLevel_no() , points_level);
                        editor.apply();

                    }
                }
            });
        }else if (questions.getPattern().getPattern_id() == 2){
            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.VISIBLE);
            layout3.setVisibility(View.GONE);


        }else if (questions.getPattern().getPattern_id() == 3){
            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.GONE);
            layout3.setVisibility(View.VISIBLE);


        }

        ((ViewPager)container).addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

}
