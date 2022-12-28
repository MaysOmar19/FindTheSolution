package com.example.findthesolution.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findthesolution.R;
import com.example.findthesolution.modle.GameData;

import java.util.List;

public class StartPlayItemsAdapter extends RecyclerView.Adapter<StartPlayItemsAdapter.StartPlayViewHolder> {

    private List<GameData> arrayList;
    private OnItemClickListenerStartPlay onItemClickListenerStartPlay;
    private Context context;

    private GameData gameData;

    private SharedPreferences sh_totalPoints;

    public StartPlayItemsAdapter(Context context, List<GameData> arrayList, OnItemClickListenerStartPlay onItemClickListenerStartPlay) {
        this.arrayList = arrayList;
        this.onItemClickListenerStartPlay = onItemClickListenerStartPlay;
        this.context = context;
    }

    @NonNull
    @Override
    public StartPlayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_start_play, parent, false);
        StartPlayViewHolder holder = new StartPlayViewHolder(v);
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull StartPlayViewHolder holder, int position) {

        gameData = arrayList.get(holder.getAdapterPosition());

        int level_no = arrayList.get(position).getLevel_no();
        int unlock_points = arrayList.get(position).getUnlock_points();

        sh_totalPoints = context.getSharedPreferences("Points", Context.MODE_PRIVATE);
        int totalPoints = sh_totalPoints.getInt("totalPoints", 0);
        int points_level = sh_totalPoints.getInt("points" + level_no, 0);
        int questionsCount_level = sh_totalPoints.getInt("questionsCount" + level_no, 1);
        float rating =(float) points_level / questionsCount_level * 100;


        holder.tv_level_no.setText("رقم المرحلة : " + level_no);
        holder.tv_request_count.setText("عدد النقاط المطلوبة : " + unlock_points);
        holder.tv_level_rate.setText("تقييم المرحلة : " + rating +" %");

        holder.tv_level_no.setTag(totalPoints);
        holder.img_lock.setTag(gameData);

        if (totalPoints >= unlock_points) {
            holder.img_lock.setImageResource(R.drawable.ic_lock_open);
        } else {
            holder.img_lock.setImageResource(R.drawable.ic_lock);
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class StartPlayViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_level_no, tv_request_count, tv_level_rate;
        private final ImageView img_lock;

        public StartPlayViewHolder(@NonNull View itemView) {
            super(itemView);

            img_lock = itemView.findViewById(R.id.item_start_play_img_lock);
            tv_level_no = itemView.findViewById(R.id.item_start_play_tv_level_no);
            tv_request_count = itemView.findViewById(R.id.item_start_play_tv_request_count);
            tv_level_rate = itemView.findViewById(R.id.item_start_play_tv_level_rate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GameData gameData = (GameData) img_lock.getTag();
                    int totalPoints = (int) tv_level_no.getTag();
                    if (totalPoints >= gameData.getUnlock_points()) {
                        onItemClickListenerStartPlay.onClickStartPlay(v, getAdapterPosition(), gameData);
                    }
                }
            });

        }

    }

    public interface OnItemClickListenerStartPlay {
        void onClickStartPlay(View view, int position, GameData gameData);
    }

}
