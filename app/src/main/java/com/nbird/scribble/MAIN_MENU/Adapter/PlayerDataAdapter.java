package com.nbird.scribble.MAIN_MENU.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.nbird.scribble.MAIN_MENU.Model.PlayerDetails;
import com.nbird.scribble.R;

import java.util.List;

public class PlayerDataAdapter extends RecyclerView.Adapter<PlayerDataAdapter.MyViewHolder> {
    private Context mContext;
    private List<PlayerDetails> mData;

    public PlayerDataAdapter(Context mContext, List<PlayerDetails> mData){
        this.mContext=mContext;
        this.mData=mData;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater= LayoutInflater.from(mContext);
        view=mInflater.inflate(R.layout.detail_layout,parent,false);

        return new MyViewHolder(view);

    }



    @Override
    public void onBindViewHolder( final MyViewHolder holder, final int position) {

        holder.setIsRecyclable(false);

        holder.name.setText(mData.get(position).getMyName());

        Glide.with(mContext).load(mData.get(position).getMyImage()).apply(RequestOptions
                        .bitmapTransform(new RoundedCorners(18)))
                .into(holder.propic);



    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        ImageView propic;



        public MyViewHolder(View itemView){
            super(itemView);

            name=(TextView) itemView.findViewById(R.id.name);
            propic=(ImageView) itemView.findViewById(R.id.propic);
        }


    }

}
