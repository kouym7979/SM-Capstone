package com.example.sm_capstone.adapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sm_capstone.CalendarActivity;
import com.example.sm_capstone.CalendarPost;
import com.example.sm_capstone.EmployID;
import com.example.sm_capstone.R;
import com.example.sm_capstone.ScheduleModify;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;


public class subCalendarAdapter extends RecyclerView.Adapter<subCalendarAdapter.ItemViewHolder> {
    private List<CalendarPost> datas;
    private Context mcontext;
    private ScheduleModify scheduleModify;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    String writer_name;
    String schedule_id;

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
    public void onBindViewHolder(@NonNull ItemViewHolder holder,final int position) {

        CalendarPost data=datas.get(position);
        holder.writer_name.setText(datas.get(position).getWriter_name());
        holder.start_time.setText(datas.get(position).getStart_time());
        holder.end_time.setText(datas.get(position).getEnd_time());
        holder.reference.setText(datas.get(position).getReference());

        writer_name = datas.get(position).getWriter_name();
        schedule_id = datas.get(position).getSchedule_id();

        Button btn_modify = holder.btn_modify;
        btn_modify.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("aaa", "modify버튼위치"+position);
                Log.d("aaa","선택한스케줄ID"+schedule_id);

//                Intent intent = new Intent(mcontext, ScheduleModify.class);
//                intent.putExtra(schedule_id, schedule_id);

                scheduleModify = new ScheduleModify(mcontext, schedule_id);
                scheduleModify.setCanceledOnTouchOutside(true);
                scheduleModify.setCancelable(true);
                scheduleModify.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                scheduleModify.show();
            }
        });

        Button btn_delete = holder.btn_delete;
        btn_delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("aaa", "delete버튼위치"+position);
                deleteDialog(writer_name);
            }
        });

        Button btn_request = holder.btn_request;
        btn_request.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("aaa", "request버튼위치"+position);
            }
        });


        //아래에 선언한 버튼클릭리스너를 여기에 구현하시면 됩니다!
        //아마 포지션도 고려를 해야할거에요
    }

    @Override
    public int getItemCount() {
        return (null != datas ? datas.size() : 0);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView writer_name;
        private TextView start_time;
        private TextView end_time;
        private TextView reference;
        public Button btn_modify;
        public Button btn_delete;
        public Button btn_request;


        //여기에 calendar_list에 있는 버튼을 선언!!
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            writer_name=itemView.findViewById(R.id.calendar_name);
            start_time=itemView.findViewById(R.id.calendar_starttime);
            end_time=itemView.findViewById(R.id.calendar_endtime);
            reference=itemView.findViewById(R.id.calendar_reference);

            btn_modify = (Button)itemView.findViewById(R.id.btn_modify);
            btn_delete = (Button)itemView.findViewById(R.id.btn_delete);
            btn_request = (Button)itemView.findViewById(R.id.btn_request);


        }
    }

    public void deleteDialog(final String writer){
        final Dialog builder = new Dialog(mcontext);
        builder.setContentView(R.layout.dialog_schedule_delete);
        builder.show();

        final Button delete_yes = (Button)builder.findViewById(R.id.delete_yes);
        final Button delete_no = (Button)builder.findViewById(R.id.delete_no);

        delete_yes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(writer.equals(writer_name)){
                    mStore.collection("CalendarPost").document(schedule_id)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("확인", "삭제되었습니다");
                                }
                            });
                }
                else
                {
                    Toast.makeText(mcontext, "작성자가 아닙니다", Toast.LENGTH_SHORT).show();
                }

                builder.dismiss();
            }
        });
        delete_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
    }
}
