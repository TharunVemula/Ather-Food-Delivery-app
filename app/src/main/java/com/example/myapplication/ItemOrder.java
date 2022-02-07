package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class ItemOrder extends AppCompatActivity {
    ArrayList<Item> arrayList;
    RecyclerView recyclerView;
    Intent intent;
    String id="";
    ListAdapter.RecyclerViewClickListener listener;
    FloatingActionButton cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_order);
        intent=getIntent();
        arrayList=new ArrayList<>();
        cart=findViewById(R.id.cart);
        id=intent.getStringExtra("id");
        try {
            setValue();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        recyclerView=findViewById(R.id.order);
        listener=new ListAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                //Toast.makeText(ItemOrder.this, arrayList.get(position).getPrice(), Toast.LENGTH_SHORT).show();
            }
        };
        OrderRecycler recyclerAdapter=new OrderRecycler(arrayList,listener);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
        //SharedPreferences prefs = getSharedPreferences("packageName", Context.MODE_PRIVATE);
       // Set<String> fetch = prefs.getStringSet("cart",new LinkedHashSet<>());
       // Set<String> price_list = prefs.getStringSet("price",new LinkedHashSet<>());
       // System.out.println(fetch);
       // System.out.println(price_list);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ItemOrder.this,checkoutPage.class);
                startActivity(intent);
            }
        });
    }

    private void setValue() throws IOException, JSONException {
        File file = new File(getApplicationContext().getFilesDir(),"register.json");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line = bufferedReader.readLine();
        while (line != null){
            stringBuilder.append(line).append("\n");
            line = bufferedReader.readLine();
        }
        bufferedReader.close();
        String responce = stringBuilder.toString();
        System.out.println(responce);
        JSONObject jsonObject  = new JSONObject(responce);
        JSONArray jsonArray=(JSONArray) jsonObject.getJSONArray("items");
        for(int i=0;i<jsonArray.length();i++)
        {
            JSONObject obj=jsonArray.getJSONObject(i);
            if (obj.has(id)) {
                JSONArray array=obj.getJSONArray(id);
                System.out.println(array.toString());
                for(int j=0;j<array.length();j++)
                {
                    Item item=new Item(array.getJSONObject(j).get("name").toString(),array.getJSONObject(j).get("price").toString());
                    System.out.println(array.getJSONObject(j).get("name").toString());
                    arrayList.add(item);
                }
            }
        }
    }
}