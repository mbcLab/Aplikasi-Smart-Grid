package com.example.appsabdimas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class HelperAdapter extends FirebaseRecyclerAdapter<ListData, HelperAdapter.MyViewHolder>{

    public HelperAdapter(@NonNull FirebaseRecyclerOptions<ListData> options) {
        super(options);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_list, parent, false);
        return new MyViewHolder (view);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull ListData model) {
        holder.setData(model);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewArus;
        TextView textViewTegangan;
        TextView textViewDaya;
        TextView textViewSaklar;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewArus = itemView.findViewById(R.id.arus);
            textViewTegangan = itemView.findViewById(R.id.tegangan);
            textViewDaya = itemView.findViewById(R.id.daya);
            textViewSaklar = itemView.findViewById(R.id.saklar);
        }


        public void setData(ListData model) {
            textViewArus.setText(model.getArus());
            textViewTegangan.setText(model.getTegangan());
            textViewDaya.setText(model.getDaya());
            textViewSaklar.setText(model.getSaklar());
        }
    }
}

