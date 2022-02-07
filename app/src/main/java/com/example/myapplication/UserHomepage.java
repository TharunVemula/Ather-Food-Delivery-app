package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class UserHomepage extends AppCompatActivity {
    ArrayList<Item> arrayList;
    RecyclerView recyclerView;
    ListAdapter.RecyclerViewClickListener listener;
    String id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_homepage);
        arrayList=new ArrayList<>();
        try {
            setValue();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        recyclerView=findViewById(R.id.res);
        listener=new ListAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent=new Intent(UserHomepage.this,ItemOrder.class);
                intent.putExtra("id",arrayList.get(position).getPrice());
                startActivity(intent);
            }
        };
        ListAdapter recyclerAdapter=new ListAdapter(arrayList,listener);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
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
        JSONArray jsonArray=(JSONArray) jsonObject.getJSONArray("restaurants");
        for(int i=0;i<jsonArray.length();i++)
        {
            JSONObject jsonObject1=jsonArray.getJSONObject(i);
            Item item=new Item(jsonObject1.get("name").toString(),jsonObject1.get("id").toString());
            arrayList.add(item);
            System.out.println(arrayList.get(i).getName()+arrayList.get(i).getPrice());
        }
    }
}