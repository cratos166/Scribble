package com.nbird.scribble.GUESS_DRAWING.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.nbird.scribble.R;
import com.nbird.scribble.WHITE_BOARD.Model.ObjectModel;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    private Context mContext;
    private List<String> mData;
    public ChatAdapter(Context mContext, List<String> mData){
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

        holder.text.setText(mData.get(position));

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
