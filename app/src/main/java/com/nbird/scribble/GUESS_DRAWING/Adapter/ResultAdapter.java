package com.nbird.scribble.GUESS_DRAWING.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.nbird.scribble.GUESS_DRAWING.MODEL.ChatModel;
import com.nbird.scribble.GUESS_DRAWING.MODEL.ResultModel;
import com.nbird.scribble.R;

import java.util.ArrayList;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<ResultModel> mData;
    AlertDialog alertDialog;
    public ResultAdapter(Context mContext, ArrayList<ResultModel> mData, AlertDialog alertDialog){
        this.mContext=mContext;
        this.mData=mData;
        this.alertDialog=alertDialog;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater= LayoutInflater.from(mContext);
        view=mInflater.inflate(R.layout.result_item,parent,false);

        return new MyViewHolder(view);

    }



    @Override
    public void onBindViewHolder( final MyViewHolder holder, final int position) {


        holder.myName.setText(mData.get(position).getName());
        holder.mark1.setText(mData.get(position).getPoint2());
        holder.mark2.setText(mData.get(position).getPoint3());
        holder.mark3.setText(mData.get(position).getPoint4());
        holder.player2.setText(mData.get(position).getPlayer2Str());
        holder.player3.setText(mData.get(position).getPlayer3Str());
        holder.player4.setText(mData.get(position).getPlayer4Str());
        holder.totalScore.setText("Total Score : "+mData.get(position).getPoints());


        if(position==0){
            holder.rank.setText(String.valueOf(position+1)+"st");
        }else if(position==0){
            holder.rank.setText(String.valueOf(position+1)+"nd");
        }else if(position==0){
            holder.rank.setText(String.valueOf(position+1)+"rd");
        }else{
            holder.rank.setText(String.valueOf(position+1)+"th");
        }


        Glide.with(mContext).load(mData.get(position).getImageURL()).apply(RequestOptions
                        .bitmapTransform(new RoundedCorners(18)))
                .into(holder.myPic);

    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView myName,mark1,mark2,mark3,player2,player3,player4,totalScore,rank;
        ImageView myPic;





        public MyViewHolder(View itemView){
            super(itemView);

            myName=(TextView) itemView.findViewById(R.id.myName);
            mark1=(TextView) itemView.findViewById(R.id.mark1);
            mark2=(TextView) itemView.findViewById(R.id.mark2);
            mark3=(TextView) itemView.findViewById(R.id.mark3);
            player2=(TextView) itemView.findViewById(R.id.player2);
            player3=(TextView) itemView.findViewById(R.id.player3);
            player4=(TextView) itemView.findViewById(R.id.player4);
            totalScore=(TextView) itemView.findViewById(R.id.totalScore);
            myPic=(ImageView) itemView.findViewById(R.id.myPic);
            rank=(TextView) itemView.findViewById(R.id.rank);

        }


    }

}
