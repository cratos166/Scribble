package com.nbird.scribble.GUESS_DRAWING.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.nbird.scribble.R;

import java.util.ArrayList;

public class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.MyViewHolder> {
    private Context mContext;
    ArrayList<String> wordsListArray;

    public WordsAdapter(Context mContext, ArrayList<String> wordsListArray){
        this.mContext=mContext;
        this.wordsListArray=wordsListArray;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater=LayoutInflater.from(mContext);
        view=mInflater.inflate(R.layout.words_asset,parent,false);

        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.text.setText(wordsListArray.get(position));
    }


    @Override
    public int getItemCount() {
        return wordsListArray.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView text;
        public MyViewHolder(View itemView){
            super(itemView);

            text=(TextView) itemView.findViewById(R.id.text);



        }
    }
}
