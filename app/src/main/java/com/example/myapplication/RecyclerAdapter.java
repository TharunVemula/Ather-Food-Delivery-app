package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter  extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    ArrayList<Item> arrayList;
    public RecyclerAdapter(ArrayList<Item> arrayList)
    {
        this.arrayList=arrayList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView name,price;
       public MyViewHolder(final View view)
       {
           super(view);
           name=view.findViewById(R.id.itemname);
           price=view.findViewById(R.id.itemprice);
       }
    }

    @NonNull
    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder holder, int position) {
        String name=arrayList.get(position).getName();
        String price=arrayList.get(position).getPrice();
        holder.name.setText(name);
        holder.price.setText(price);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
