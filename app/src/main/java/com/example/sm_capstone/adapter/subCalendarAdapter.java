package com.example.sm_capstone.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sm_capstone.CalendarPost;
import com.example.sm_capstone.R;

import java.util.List;

public class subCalendarAdapter extends RecyclerView.Adapter<subCalendarAdapter.ItemViewHolder> {
    private List<CalendarPost> datas;
    private Context mcontext;

    public  subCalendarAdapter(Context mcontext, List<CalendarPost> datas) {
        this.mcontext=mcontext;
        this.datas=datas;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new subCalendarAdapter.ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        CalendarPost data=datas.get(position);
        holder.name.setText(datas.get(position).getWriter_name());
        holder.date.setText(datas.get(position).getDate());
        //아래에 선언한 버튼클릭리스너를 여기에 구현하시면 됩니다!
        //아마 포지션도 고려를 해야할거에요
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView date;
        //여기에 calendar_list에 있는 버튼을 선언!!
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.calendar_name);
            date=itemView.findViewById(R.id.calendar_date);
        }
    }
}
