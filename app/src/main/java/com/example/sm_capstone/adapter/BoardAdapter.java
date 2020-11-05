package com.example.sm_capstone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sm_capstone.Board_Post.Post;
import com.example.sm_capstone.R;

import org.w3c.dom.Text;

import java.util.List;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.BoardViewHolder> {

    private List<Post> datas;
    private Context mcontext;
    public BoardAdapter(Context mcontext, List<Post> datas) {
        this.datas = datas;
        this.mcontext=mcontext;
    }

    @NonNull
    @Override
    public BoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BoardViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BoardViewHolder holder, int position) {
        Post data=datas.get(position);
        holder.title.setText(datas.get(position).getTitle());
        holder.contents.setText(datas.get(position).getContents());
        holder.writer.setText(datas.get(position).getContents());




    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public interface  EventListener<QuerySnapshot>{
        boolean onOptionItemSelected(MenuItem item);

        void onItemClicked(int position);
    }

    class BoardViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private TextView contents;
        private TextView writer;
        public BoardViewHolder(@NonNull View itemView){
            super(itemView);

            title=itemView.findViewById(R.id.post_title);
            contents=itemView.findViewById(R.id.post_contents);
            writer=itemView.findViewById(R.id.post_writer);
        }
    }
}
