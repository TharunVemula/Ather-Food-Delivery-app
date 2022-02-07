package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {
    ArrayList<Item> arrayList;
    private final RecyclerViewClickListener listener;
    public ListAdapter(ArrayList<Item> arrayList,RecyclerViewClickListener listener)
    {
        this.arrayList=arrayList;
        this.listener=listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView name;
        public MyViewHolder(final View view)
        {
            super(view);
            name=view.findViewById(R.id.itemname);
            //price=view.findViewById(R.id.itemprice);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view,getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public ListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_list,parent,false);
        return new ListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.MyViewHolder holder, int position) {
        String name=arrayList.get(position).getName();
        String price=arrayList.get(position).getPrice();
        holder.name.setText(name);
        //holder.price.setText(price);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public interface RecyclerViewClickListener
    {
        void onClick(View view,int position);
    }

}
