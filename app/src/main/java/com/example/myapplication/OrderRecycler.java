package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class OrderRecycler extends RecyclerView.Adapter<OrderRecycler.MyViewHolder> {
    ArrayList<Item> arrayList;
    private final ListAdapter.RecyclerViewClickListener listener;
    public OrderRecycler(ArrayList<Item> arrayList, ListAdapter.RecyclerViewClickListener listener)
    {
        this.arrayList=arrayList;
        this.listener=listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView name,price;
        Button btn;
        public MyViewHolder(final View view)
        {
            super(view);
            name=view.findViewById(R.id.itemname);
            price=view.findViewById(R.id.itemprice);
            btn=view.findViewById(R.id.add3);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view,getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public OrderRecycler.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.order_items,parent,false);
        return new OrderRecycler.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderRecycler.MyViewHolder holder, int position) {
        String name=arrayList.get(position).getName();
        String price=arrayList.get(position).getPrice();
        holder.name.setText(name);
        holder.price.setText(price);
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MutatingSharedPrefs")
            @Override
            public void onClick(View view) {
                //Toast.makeText(view.getContext(), "Item added", Toast.LENGTH_SHORT).show();
                Snackbar.make(view,"Item Added to your cart",Snackbar.LENGTH_LONG).show();
                SharedPreferences prefs = view.getContext().getSharedPreferences("packageName", Context.MODE_PRIVATE);
                SharedPreferences.Editor myEdit = prefs.edit();
                Set<String> fetch = prefs.getStringSet("cart",new LinkedHashSet<>());
                Set<String> price_list = prefs.getStringSet("price",new LinkedHashSet<>());
                fetch.add(name);
                price_list.add(price);
                myEdit.putStringSet("cart",fetch);
                myEdit.putStringSet("price",price_list);
                myEdit.apply();
                holder.btn.setEnabled(false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}