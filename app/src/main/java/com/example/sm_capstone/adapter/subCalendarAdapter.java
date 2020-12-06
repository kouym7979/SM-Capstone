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
import com.example.sm_capstone.DynamicBoard;
import com.example.sm_capstone.EmployID;
import com.example.sm_capstone.PostWrite;
import com.example.sm_capstone.R;
import com.example.sm_capstone.ScheduleAdd;
import com.example.sm_capstone.ScheduleModify;
import com.example.sm_capstone.ScheduleRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.sm_capstone.EmployID.board_part;
import static com.example.sm_capstone.EmployID.end_time;
import static com.example.sm_capstone.EmployID.request_reference;
import static com.example.sm_capstone.EmployID.start_time;


public class subCalendarAdapter extends RecyclerView.Adapter<subCalendarAdapter.ItemViewHolder> {
    private List<CalendarPost> datas;
    private Context mcontext;
    private ScheduleModify scheduleModify;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    String writer_name;
    String schedule_id;
    String start_time;
    String end_time;
    String date;
    String request;
    String request_reference;
    String user_name;

    public  subCalendarAdapter(Context mcontext, List<CalendarPost> datas) {
        this.mcontext=mcontext;
        this.datas=datas;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mAuth.getCurrentUser()!=null){
            mStore.collection("user").document(mAuth.getCurrentUser().getUid())
               .get()
               .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                       if(task.getResult()!=null){
                           user_name = (String)task.getResult().getData().get(EmployID.name);
                           Log.d("확인","현재 사용자 이름입니다"+user_name);
                       }
                   }
               });
        }

        return new subCalendarAdapter.ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder,int position) {

        CalendarPost data=datas.get(position);
        holder.writer_name.setText(datas.get(position).getWriter_name());
        holder.start_time.setText(datas.get(position).getStart_time());
        holder.end_time.setText(datas.get(position).getEnd_time());
        holder.reference.setText(datas.get(position).getReference());

        final int pos = holder.getAdapterPosition();

//        writer_name = datas.get(pos).getWriter_name();
//        start_time = datas.get(pos).getStart_time();
//        end_time = datas.get(pos).getEnd_time();
//        request_reference = datas.get(pos).getRequest_reference();


        Log.d("onBindViewHolder테스트", "writer_name : "+datas.get(pos).getWriter_name());
        Log.d("onBindViewHolder테스트","schedule_id : "+schedule_id);
        Log.d("onBindViewHolder테스트", "start_time : "+datas.get(pos).getStart_time());
        Log.d("onBindViewHolder테스트", "end_time : "+end_time);
        Log.d("onBindViewHolder테스트", "request : "+request);
        Log.d("onBindViewHolder테스트", "pos.request : "+datas.get(pos).getRequest());
        Log.d("onBindViewHolder테스트", "request_reference : "+datas.get(pos).getRequest_reference());



        ////////////////////수정버튼//////////////////////
        Button btn_modify = holder.btn_modify;
        btn_modify.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                Log.d("subCalendarAdapter", "modify버튼위치"+position);
                Log.d("subCalendarAdapter","선택한스케줄ID"+datas.get(pos).getSchedule_id());
                Log.d("subCalendarAdapter", "start_time변수값:"+datas.get(pos).getStart_time());
                Log.d("subCalendarAdapter", "end_time변수값:"+datas.get(pos).getEnd_time());

//                Intent intent = new Intent(mcontext, ScheduleModify.class);
//                intent.putExtra(schedule_id, schedule_id);

                if(datas.get(pos).getWriter_name().equals(user_name)){
                    scheduleModify = new ScheduleModify(mcontext, datas.get(pos).getSchedule_id());
                    scheduleModify.setCanceledOnTouchOutside(true);
                    scheduleModify.setCancelable(true);
                    scheduleModify.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                    scheduleModify.show();
                }
                else
                {
                    Toast.makeText(mcontext, "작성자가 아닙니다", Toast.LENGTH_SHORT).show();
                }

            }
        });


        ///////////////////삭제버튼/////////////////////////////
        Button btn_delete = holder.btn_delete;
        btn_delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                Log.d("aaa", "delete버튼위치"+position);
                deleteDialog(datas.get(pos).getWriter_name(), datas.get(pos).getSchedule_id());
            }
        });

        ///////////////////////요청버튼////////////////////////////////////////
        Button btn_request = holder.btn_request;
        Log.d("확인", "request값:" + request);
        if(datas.get(pos).getRequest().equals("0")) {
            Log.d("subCalendarAdapter", "request=0일때" + datas.get(pos).getRequest());
            btn_request.setText("request");
        }
        if(datas.get(pos).getRequest().equals("1")){
            Log.d("subCalendarAdapter", "request=1일때" + datas.get(pos).getRequest());
            btn_request.setText("accept");
        }

        btn_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("subCalendarAdapter", "request버튼위치" + position);
                Log.d("subCalendarAdapter", "넘겨지는 schedule_id는" + datas.get(pos).getSchedule_id());
                if(datas.get(pos).getRequest().equals("0")) {
                    requestDialog(datas.get(pos).getWriter_name(), datas.get(pos).getSchedule_id());
                }
                if(datas.get(pos).getRequest().equals("1")){
                    acceptDialog(datas.get(pos).getWriter_name(), datas.get(pos).getSchedule_id(), datas.get(pos).getRequest(), datas.get(pos).getRequest_reference());
                }
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

    public void deleteDialog(final String writer,final String schedule_id){
        final Dialog builder = new Dialog(mcontext);
        builder.setContentView(R.layout.dialog_schedule_delete);
        builder.show();

        final Button delete_yes = (Button)builder.findViewById(R.id.delete_yes);
        final Button delete_no = (Button)builder.findViewById(R.id.delete_no);

        delete_yes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(writer.equals(user_name)){
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


    public void requestDialog(final String writer,final String schedule_id){
        final Dialog builder = new Dialog(mcontext);
        builder.setContentView(R.layout.dialog_schedule_request);
        builder.show();

        final Button request_yes = (Button)builder.findViewById(R.id.request_yes);
        final Button request_no = (Button)builder.findViewById(R.id.request_no);

        request_yes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(writer.equals(user_name)){
                    ////request화면으로 연결
                    Intent intent = new Intent(mcontext, ScheduleRequest.class);
                    intent.putExtra("schedule_id", schedule_id);
                    intent.putExtra("start_time", start_time);
                    intent.putExtra("end_time", end_time);
                    Log.d("확인","requestDialog넘겨지는 schedule_id는"+schedule_id);
                    Log.d("확인","requestDialog넘겨지는 start_time는"+start_time);
                    mcontext.startActivity(intent);
                }
                else
                {
                    Toast.makeText(mcontext, "작성자가 아닙니다", Toast.LENGTH_SHORT).show();
                }
                builder.dismiss();
            }
        });
        request_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
    }

    public void acceptDialog(final String writer, final String schedule_id, final String request, final String request_reference){
        final Dialog builder = new Dialog(mcontext);
        builder.setContentView(R.layout.dialog_schedule_accept);
        builder.show();

        TextView mreference = (TextView)builder.findViewById(R.id.request_reference);
        mreference.setText(request_reference);

        final Button accept_yes = (Button)builder.findViewById(R.id.accept_yes);
        final Button accept_no = (Button)builder.findViewById(R.id.accept_no);

        accept_yes.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(mAuth.getCurrentUser()!=null){
                    Log.d("subCalendarAdapter","요청수락할 schedule_id:"+schedule_id);

                    Map<String, Object> data = new HashMap<>();
                    data.put(EmployID.documentId, mAuth.getCurrentUser().getUid());
//                    data.put(EmployID.writer_id, mAuth.getCurrentUser().getUid());
                    data.put(EmployID.writer_name, user_name);
                    data.put(EmployID.request, "0");
                    mStore.collection("CalendarPost").document(schedule_id).update(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(mcontext.getApplicationContext(), "Accept complete", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                builder.dismiss();
            }
        });

        accept_no.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
    }

}
