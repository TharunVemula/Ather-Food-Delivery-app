package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class checkoutPage extends AppCompatActivity {

    ArrayList<Item> arrayList;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_page);
        SharedPreferences prefs = getSharedPreferences("packageName", Context.MODE_PRIVATE);
        arrayList=new ArrayList<>();
        Set<String> fetch = prefs.getStringSet("cart",new LinkedHashSet<>());
        Set<String> price_list = prefs.getStringSet("price",new LinkedHashSet<>());
        System.out.println(fetch);
        System.out.println(price_list);
            for(String str: fetch)
            {
                Item item=new Item();
                item.setName(str);
                arrayList.add(item);
            }
            int i=0;
            for(String str:price_list)
            {
                arrayList.get(i).setPrice(str);
                i++;
            }
        Button place=findViewById(R.id.placeorder);
        place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view,"order placed",Snackbar.LENGTH_LONG).show();
                SharedPreferences.Editor myEdit = prefs.edit();
                myEdit.putStringSet("cart",new HashSet<>());
                myEdit.putStringSet("price",new HashSet<>());
                myEdit.apply();
                new CountDownTimer(2000,2000){

                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        finish();
                    }
                }.start();

            }
        });
        recyclerView=findViewById(R.id.recyclerView);
        RecyclerAdapter recyclerAdapter=new RecyclerAdapter(arrayList);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
    }
}