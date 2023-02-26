package com.nbird.scribble.WHITE_BOARD.Adapter;

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

public class ObjectAdapter extends RecyclerView.Adapter<ObjectAdapter.MyViewHolder> {
    private Context mContext;
    private List<ObjectModel> mData;
    TextView wordTextView;
    AlertDialog alertDialog;
    public ObjectAdapter(Context mContext, List<ObjectModel> mData, TextView wordTextView, AlertDialog alertDialog){
        this.mContext=mContext;
        this.mData=mData;
        this.wordTextView=wordTextView;
        this.alertDialog=alertDialog;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater= LayoutInflater.from(mContext);
        view=mInflater.inflate(R.layout.object_layout,parent,false);

        return new MyViewHolder(view);

    }



    @Override
    public void onBindViewHolder( final MyViewHolder holder, final int position) {

        holder.name.setText(mData.get(position).getName());

        holder.cardview_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordTextView.setText(mData.get(position).getName());
                try{
                    alertDialog.dismiss();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });




    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        CardView cardview_id;



        public MyViewHolder(View itemView){
            super(itemView);

            name=(TextView) itemView.findViewById(R.id.name);
            cardview_id=(CardView) itemView.findViewById(R.id.cardview_id);
        }


    }

}
