package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class RestaurantHome extends AppCompatActivity {
    ArrayList<Item> arrayList;
    RecyclerView recyclerView;
    Dialog dialog;
    Intent intent;
    String id;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_home);
        intent=getIntent();
        id=intent.getStringExtra("id");
        swipeRefreshLayout=findViewById(R.id.refreshLayout);
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
        arrayList=new ArrayList<>();
        try {
            setValue();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    arrayList.clear();
                    setValue();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                RecyclerAdapter recyclerAdapter=new RecyclerAdapter(arrayList);
                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(recyclerAdapter);
                recyclerView.setAdapter(recyclerAdapter);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        {

        }
        recyclerView=findViewById(R.id.recview);
        RecyclerAdapter recyclerAdapter=new RecyclerAdapter(arrayList);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
        FloatingActionButton add=findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogue();
                dialog.show();
            }
        });
    }

    private void openDialogue() {

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_item);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //dialog.setCancelable(false);
        Button btn=dialog.findViewById(R.id.add1);
        EditText name=dialog.findViewById(R.id.name1);
        EditText price=dialog.findViewById(R.id.price1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String n=name.getText().toString();
                String p=price.getText().toString();
                try {
                    if (n.isEmpty() || p.isEmpty())
                    {
                        Snackbar.make(view,"Enter Details properly",Snackbar.LENGTH_LONG).show();
                    }
                    else
                        addFoodItem(n,p);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(getApplicationContext(),n+p,Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
        //dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        //ExampleDialogue exampleDialogue=new ExampleDialogue();
        //exampleDialogue.show(getSupportFragmentManager(),"example dialogue");
    }

    private void addFoodItem(String n, String p) throws IOException, JSONException {
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
        JSONObject jsonObject  = new JSONObject(responce);
        JSONArray object=jsonObject.getJSONArray("items");
        int s=0;
        for(int i=0;i<object.length();i++)
        {
            JSONObject obj=object.getJSONObject(i);
            System.out.println(obj.toString());
            if (obj.has(id)) {
                System.out.println("fhgfygchg");
                JSONArray array=obj.getJSONArray(id);
                JSONObject t=new JSONObject();
                t.put("name",n);
                t.put("price",p);
                array.put(t);
                s=i;
                break;
            }
        }
        JSONObject t1=object.getJSONObject(s);
        object.remove(s);
        object.put(t1);
        jsonObject.remove("items");
        jsonObject.put("items",object);
        File file1 = new File(getApplicationContext().getFilesDir(),"register.json");
        FileWriter fileWriter = new FileWriter(file1);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(jsonObject.toString());
        bufferedWriter.close();
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
            //System.out.println(obj.toString());
            if (obj.has(id)) {
                JSONArray array=obj.getJSONArray(id);
                System.out.println(array.toString());
                for(int j=0;j<array.length();j++)
                {
                    Item item=new Item(array.getJSONObject(j).get("name").toString(),array.getJSONObject(j).get("price").toString());
                    System.out.println(array.getJSONObject(j).get("name").toString());
                    arrayList.add(item);
                }
                break;
            }
        }
    }
}