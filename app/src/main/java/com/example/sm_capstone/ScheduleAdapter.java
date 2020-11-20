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

    //생성자
    public ScheduleAdapter(Context mcontext, List<CalendarPost> datas) {
        this.datas = datas;
        this.mcontext=mcontext;
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ScheduleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        CalendarPost data = datas.get(position);
        holder.start_time.setText(datas.get(position).getStart_time());
        holder.end_time.setText(datas.get(position).getEnd_time());
        holder.writer_name.setText(datas.get(position).getWriter_name());

        final int pos=holder.getAdapterPosition();
//        holder.itemView.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Context context = v.getContext();
//                if(pos != RecyclerView.NO_POSITION){
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() { return datas.size(); }

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
