package com.nbird.scribble.GUESS_DRAWING.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.nbird.scribble.GUESS_DRAWING.MODEL.ChatModel;
import com.nbird.scribble.R;
import com.nbird.scribble.WHITE_BOARD.Model.ObjectModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<ChatModel> mData;
    public ChatAdapter(Context mContext, ArrayList<ChatModel> mData){
        this.mContext=mContext;
        this.mData=mData;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater= LayoutInflater.from(mContext);
        view=mInflater.inflate(R.layout.layout_chat,parent,false);

        return new MyViewHolder(view);

    }



    @Override
    public void onBindViewHolder( final MyViewHolder holder, final int position) {

        holder.setIsRecyclable(false);

        if(mData.get(position).getKey()==1){
            holder.text.setTextColor(Color.parseColor("#FF018786"));
        }else if(mData.get(position).getKey()==2){

        }else if(mData.get(position).getKey()==3){
            holder.text.setTextColor(Color.parseColor("#98A8D0"));
        }
        holder.text.setText(mData.get(position).getValue());

    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView text;




        public MyViewHolder(View itemView){
            super(itemView);

            text=(TextView) itemView.findViewById(R.id.text);
        }


    }

}
