package com.example.sm_capstone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>{

    private List<CalendarPost> datas;
    private Context context;

    public ScheduleAdapter(Context context, List<CalendarPost> datas){
        this.datas = datas;
        this.context = context;
    }

    public ScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ScheduleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        CalendarPost data = datas.get(position);
       //////////////////////
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public interface EventListener{}

    class ScheduleViewHolder extends RecyclerView.ViewHolder{
        private TextView time;
        private TextView writer;
        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);

            time=itemView.findViewById(R.id.schedule_time);
            writer=itemView.findViewById(R.id.schedule_writer);
        }
    }
}
