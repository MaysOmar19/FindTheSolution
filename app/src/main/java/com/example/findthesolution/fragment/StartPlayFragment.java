package com.example.findthesolution.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.findthesolution.R;
import com.example.findthesolution.databinding.FragmentStartPlayBinding;
import com.example.findthesolution.modle.GameData;
import com.example.findthesolution.adapter.StartPlayItemsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StartPlayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StartPlayFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static final String LEVEL_FRAGMENT = "levelFragment";

    FragmentStartPlayBinding binding;
    RecyclerView rv_plays;
    TextView tv_total_point;
    private StartPlayItemsAdapter adapter;
    private List<GameData> levelList;
    private SharedPreferences sh_questionsCount;
    private SharedPreferences.Editor editor;
    private int questionsCount = 0;

    public StartPlayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StartPlayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StartPlayFragment newInstance(String param1, String param2) {
        StartPlayFragment fragment = new StartPlayFragment();
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

    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStartPlayBinding.inflate(inflater, container, false);
        rv_plays = binding.startPlayRvPlays;
        tv_total_point = binding.startPlayTvTotalPoint;

        rv_plays.setHasFixedSize(true);
        rv_plays.setLayoutManager(new LinearLayoutManager(getContext()));

        levelList = new ArrayList<>();

        sh_questionsCount = getActivity().getSharedPreferences("Points", Context.MODE_PRIVATE);
        editor = sh_questionsCount.edit();
        int totalPoints = sh_questionsCount.getInt("totalPoints", 0);
        tv_total_point.setText("إجمالي النقاط : "+totalPoints);

        try {
            JSONArray jsonArray = new JSONArray(loadJSONFromAsset());

            for (int x = 0; x < jsonArray.length(); x++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(x);
                int level_no = (int) jsonObject.get("level_no");
                int unlock_points = (int) jsonObject.get("unlock_points");

                JSONArray jsonArrayContentQuestions = jsonObject.getJSONArray("questions");
                for (int z = 0; z < jsonArrayContentQuestions.length(); z++) {
                    JSONObject jsonObjectContentQuestions = (JSONObject) jsonArrayContentQuestions.get(z);

                    int points = (int) jsonObjectContentQuestions.get("points");
                    questionsCount = questionsCount + points;
                }
                editor.putInt("questionsCount" + level_no, questionsCount);
                editor.apply();
                questionsCount = 0;
                levelList.add(new GameData(level_no, unlock_points));
            }

            adapter = new StartPlayItemsAdapter(getContext(), levelList, new StartPlayItemsAdapter.OnItemClickListenerStartPlay() {
                @Override
                public void onClickStartPlay(View view, int position, GameData gameData) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_content_main,LevelFragment.newInstance(gameData.getLevel_no()),LEVEL_FRAGMENT).addToBackStack(null).commit();

                }
            });
            rv_plays.setAdapter(adapter);
            adapter.notifyDataSetChanged();


        } catch (JSONException e) {
            e.printStackTrace();
        }

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