package com.example.sm_capstone.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sm_capstone.Board_Post.Home_Post;
import com.example.sm_capstone.R;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    private List<Home_Post> datas;
    private Context mcontext;

    public  HomeAdapter(Context mcontext, List<Home_Post> datas){
        this.datas = datas;
        this.mcontext=mcontext;
    }


    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeAdapter.HomeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_post,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        Home_Post data=datas.get(position);
        holder.board_part.setText(datas.get(position).getBoard_part());
        holder.title.setText(datas.get(position).getTitle());

        final int pos=holder.getAdapterPosition();//몇번째 항목을 클릭했는지

        /*holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Context context=v.getContext();
                if(pos!=RecyclerView.NO_POSITION){

                }
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return 8;
    }

    class HomeViewHolder extends RecyclerView.ViewHolder{
       private TextView title;
       private TextView board_part;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);

            title=itemView.findViewById(R.id.post_contents);
            board_part=itemView.findViewById(R.id.check_post);
        }
    }
}
