package com.sebasiao.pruebaandroidserempre.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sebasiao.pruebaandroidserempre.R;
import com.sebasiao.pruebaandroidserempre.models.PostModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.MyViewHolder>{
    private Context context;
    private ArrayList<PostModel> postModelArrayList;

    public PostsAdapter(Context context, ArrayList<PostModel> postsData) {
        this.context = context;
        this.postModelArrayList = postsData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.posts_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (postModelArrayList.size() != 0){
            holder.idTv.setText(postModelArrayList.get(position).getId()+"");
            holder.titleTvPosts.setText(postModelArrayList.get(position).getTitle());
            holder.bodyTv.setText(postModelArrayList.get(position).getBody());
        }
    }

    @Override
    public int getItemCount() {
        return postModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.idTv)
        TextView idTv;
        @BindView(R.id.titleTvPosts)
        TextView titleTvPosts;
        @BindView(R.id.bodyTv)
        TextView bodyTv;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }
}
