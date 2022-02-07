package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    String value="";
    String id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Spinner spinner = (Spinner) findViewById(R.id.choose1);
        Button submit=(Button) findViewById(R.id.submit1);
        TextInputEditText et=(TextInputEditText)findViewById(R.id.username1);
        TextInputEditText et2=(TextInputEditText)findViewById(R.id.password1);
        List<String> categories = new ArrayList<String>();
        categories.add("Customer");
        categories.add("Restaurent");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                String password= Objects.requireNonNull(et2.getText()).toString();
                if (value.equals("Restaurent")) {
                    try {
                        if(checkUser(name, password))
                        {
                            Intent intent = new Intent(LoginActivity.this, RestaurantHome.class);
                            intent.putExtra("id",id);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, "Enter Correct details", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if (value.equals("Customer"))
                {
                    try {
                        if(checkCustomer(name, password))
                        {
                            Intent intent = new Intent(LoginActivity.this, UserHomepage.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, "Enter Correct details", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private Boolean checkCustomer(String name, String password) throws IOException, JSONException {
        File file = new File(getApplicationContext().getFilesDir(),"users.json");
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
        JSONArray jsonArray=(JSONArray) jsonObject.getJSONArray("user");
        for(int i=0;i<jsonArray.length();i++)
        {
            JSONObject obj=jsonArray.getJSONObject(i);
            if(name.equals(obj.get("username")) && password.equals(obj.get("password")))
            {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    private Boolean checkUser(String name, String password) throws IOException, JSONException {
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
            JSONObject obj=jsonArray.getJSONObject(i);
            if(name.equals(obj.get("username")) && password.equals(obj.get("password")))
            {
                id=obj.get("id").toString();
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
}