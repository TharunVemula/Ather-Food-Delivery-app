package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

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
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class RegisterActivity extends AppCompatActivity {
    String value="";
    String id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Spinner spinner = (Spinner) findViewById(R.id.choose);
        Button submit=(Button) findViewById(R.id.submit);
        TextInputEditText et=(TextInputEditText)findViewById(R.id.username);
        TextInputEditText et1=(TextInputEditText)findViewById(R.id.username1);
        TextInputEditText et2=(TextInputEditText)findViewById(R.id.password);
        TextInputEditText et3=(TextInputEditText)findViewById(R.id.confirm);
        List<String> categories = new ArrayList<String>();
        categories.add("Customer");
        categories.add("Restaurent");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                value=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name= Objects.requireNonNull(et.getText()).toString();
                String username= Objects.requireNonNull(et1.getText()).toString();
                String password= Objects.requireNonNull(et2.getText()).toString();
                String confirm= Objects.requireNonNull(et3.getText()).toString();
                //Toast.makeText(RegisterActivity.this,name+password+confirm, Toast.LENGTH_SHORT).show();
                try {
                    if (password.equals(confirm))
                    {
                        if (value.equals("Restaurent")) {
                            writeRestaurentJson(name, password,username);
                            Intent intent = new Intent(RegisterActivity.this, RestaurantHome.class);
                            Toast.makeText(RegisterActivity.this, id, Toast.LENGTH_SHORT).show();
                            intent.putExtra("id",id);
                            startActivity(intent);
                            finish();
                        }
                        else if (value.equals("Customer"))
                        {
                            writeCustomerJson(name,password,username);
                            Intent intent = new Intent(RegisterActivity.this, UserHomepage.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                    else
                        Toast.makeText(RegisterActivity.this, "Enter correct password", Toast.LENGTH_LONG).show();
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void writeCustomerJson(String name, String password,String username) throws JSONException, IOException {
        JSONArray jsonArray=readCustomerJson();
        JSONObject jsonObject = new JSONObject();
        JSONObject obj = new JSONObject();
        obj.put("name",name);
        obj.put("username",username);
        obj.put("password",password);
        jsonArray.put(obj);
        jsonObject.put("user", jsonArray);
        String userString = jsonObject.toString();
        System.out.println(userString);

        File file = new File(getApplicationContext().getFilesDir(),"users.json");
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(userString);
        bufferedWriter.close();
    }

    private JSONArray readCustomerJson() throws IOException, JSONException {
        File file = new File(getApplicationContext().getFilesDir(),"users.json");
        if (!file.exists())
        {
            System.out.println("hello!file not there maaa");
            if (file.createNewFile()){
                System.out.println("File is created!");
                JSONArray jsonArray=new JSONArray();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("user", jsonArray);
                File file2 = new File(getApplicationContext().getFilesDir(),"users.json");
                FileWriter fileWriter = new FileWriter(file2);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(jsonObject.toString());
                bufferedWriter.close();
            }else{
                System.out.println("File already exists.");
            }
        }
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
        JSONArray jsonArray=(JSONArray) jsonObject.getJSONArray("user");
        return jsonArray;
    }

    private void writeRestaurentJson(String name, String password,String username) throws JSONException, IOException {
        JSONObject whole=readRestaurentJson();
        Random r = new Random(System.currentTimeMillis());
        id=String.valueOf(r.nextInt(100000));
        JSONObject obj = new JSONObject();
        obj.put("name",name);
        obj.put("username",username);
        obj.put("password",password);
        obj.put("id",id);
        JSONArray jsonArray=whole.getJSONArray("restaurants");
        jsonArray.put(obj);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("restaurants", jsonArray);
        JSONArray object=whole.getJSONArray("items");
        JSONObject obj1=new JSONObject();
        obj1.put(id,new JSONArray());
        object.put(obj1);
        jsonObject.put("items",object);
        String userString = jsonObject.toString();
        File file = new File(getApplicationContext().getFilesDir(),"register.json");
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(userString);
        bufferedWriter.close();
    }

    private JSONObject readRestaurentJson() throws IOException, JSONException {
        File file = new File(getApplicationContext().getFilesDir(),"register.json");
        if (!file.exists())
        {
            System.out.println("hello! nor file maaa");
            if (file.createNewFile()){
                System.out.println("File is created!");
                JSONArray jsonArray=new JSONArray();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("restaurants", jsonArray);
                jsonObject.put("items", new JSONArray());
                File file2 = new File(getApplicationContext().getFilesDir(),"register.json");
                FileWriter fileWriter = new FileWriter(file2);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(jsonObject.toString());
                bufferedWriter.close();
            }else{
                System.out.println("File already exists.");
            }
        }
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
        return jsonObject;
    }
}