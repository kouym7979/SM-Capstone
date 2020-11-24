package com.example.sm_capstone;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sm_capstone.Board_Post.Post;

import java.util.ArrayList;
import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>{
    private List<CalendarPost> datas;
    private Context mcontext;

    public ScheduleAdapter(List<CalendarPost> datas, Context mcontext) {
        this.datas = datas;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule, parent, false);
        ScheduleViewHolder holder = new ScheduleViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        CalendarPost data = datas.get(position);
        holder.start_time.setText(datas.get(position).getStart_time());
        holder.end_time.setText(datas.get(position).getEnd_time());
        holder.writer_name.setText(datas.get(position).getWriter_name());
        holder.reference.setText(datas.get(position).getReference());

    }

    @Override
    public int getItemCount() {
        //삼항 연산자
        //arrayList값이 null이 아니면 size반환
        return (datas != null ? datas.size() : 0);
    }

    public interface EventListener<QuerySnapshot>{
        boolean onOptionItemSelected(MenuItem item);
        void onItemClicked(int position);
    }

    class ScheduleViewHolder extends RecyclerView.ViewHolder{

        private TextView writer_name;
        private TextView start_time;
        private TextView end_time;
        private TextView reference;

        public ScheduleViewHolder(@NonNull View itemView){
            super(itemView);
            writer_name = itemView.findViewById(R.id.writer_name);
            start_time = itemView.findViewById(R.id.startTimeLabel);
            end_time = itemView.findViewById(R.id.endTimeLabel);
            reference = itemView.findViewById(R.id.schedule_reference);
        }
    }


}
