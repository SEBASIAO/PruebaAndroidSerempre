package com.sebasiao.pruebaandroidserempre.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sebasiao.pruebaandroidserempre.MainActivity;
import com.sebasiao.pruebaandroidserempre.MainActivity_ViewBinding;
import com.sebasiao.pruebaandroidserempre.R;
import com.sebasiao.pruebaandroidserempre.models.PostModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.MyViewHolder>{
    private Context context;
    private ArrayList<PostModel> postModelArrayList;
    private boolean isFav;

    public PostsAdapter(Context context, ArrayList<PostModel> postsData,boolean isfav) {
        this.context = context;
        this.postModelArrayList = postsData;
        this.isFav = isfav;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.posts_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (postModelArrayList.size() != 0){
            if (isFav){
                holder.starIv.setVisibility(View.VISIBLE);
                holder.addFavTv.setVisibility(View.GONE);
                holder.idTv.setText(postModelArrayList.get(position).getId()+"");
                holder.titleTvPosts.setText(postModelArrayList.get(position).getTitle());
                holder.bodyTv.setText(postModelArrayList.get(position).getBody());
                if (postModelArrayList.get(position).getId() <= 20){
                    holder.notReadedIv.setVisibility(View.VISIBLE);
                }else{
                    holder.notReadedIv.setVisibility(View.GONE);
                }
                holder.postContainerRl.setOnClickListener(view -> {
                    holder.notReadedIv.setVisibility(View.GONE);
                    Toast.makeText(context,postModelArrayList.get(position).getUserId()+"",Toast.LENGTH_SHORT).show();
                });
            }else{
                holder.starIv.setVisibility(View.GONE);
                holder.addFavTv.setVisibility(View.VISIBLE);
                holder.idTv.setText(postModelArrayList.get(position).getId()+"");
                holder.titleTvPosts.setText(postModelArrayList.get(position).getTitle());
                holder.bodyTv.setText(postModelArrayList.get(position).getBody());
                if (postModelArrayList.get(position).getId() <= 20){
                    holder.notReadedIv.setVisibility(View.VISIBLE);
                }else{
                    holder.notReadedIv.setVisibility(View.GONE);
                }
                holder.postContainerRl.setOnClickListener(view -> {
                    holder.notReadedIv.setVisibility(View.GONE);
                    Toast.makeText(context,postModelArrayList.get(position).getUserId()+"",Toast.LENGTH_SHORT).show();
                });

                holder.addFavTv.setOnClickListener(view -> {
                    if (context instanceof MainActivity){
                        boolean add = ((MainActivity)context).addToFav(position);
                        if (add){
                            holder.addFavTv.setVisibility(View.GONE);
                            holder.starIv.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
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
        @BindView(R.id.notReadedIv)
        ImageView notReadedIv;
        @BindView(R.id.postContainerRl)
        RelativeLayout postContainerRl;
        @BindView(R.id.starIv)
        ImageView starIv;
        @BindView(R.id.addFavTv)
        TextView addFavTv;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }
}
