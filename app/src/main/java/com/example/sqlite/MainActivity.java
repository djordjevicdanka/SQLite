package com.example.sqlite;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btn_add, btn_viewAll;
    EditText et_name, et_age;
    Switch sw_activeUser;
    ListView lv_userList;

    ArrayAdapter userArrayAdapter;
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_add = (Button) findViewById(R.id.btn_add);
        btn_viewAll = (Button) findViewById(R.id.btn_viewAll);
        et_name = (EditText) findViewById(R.id.name_id);
        et_age = (EditText) findViewById(R.id.age_id);
        sw_activeUser = (Switch) findViewById(R.id.sw_active);
        lv_userList = (ListView) findViewById(R.id.userList_id);

        dataBaseHelper = new DataBaseHelper(MainActivity.this);

        ShowUserOnListView(dataBaseHelper);

        btn_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                UserModel userModel;

                try {
                    userModel = new UserModel(-1, et_name.getText().toString(), Integer.parseInt(et_age.getText().toString()), sw_activeUser.isChecked());
                    Toast.makeText(MainActivity.this, userModel.toString(), Toast.LENGTH_SHORT).show();
                }

                catch (Exception e){
                    Toast.makeText(MainActivity.this, "Error creating user", Toast.LENGTH_SHORT).show();
                    userModel= new UserModel(-1, "error", 0, false);
                }

                DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);

                boolean success = dataBaseHelper.addOne(userModel);

                Toast.makeText(MainActivity.this,"Success=" + success, Toast.LENGTH_SHORT).show();

                ShowUserOnListView(dataBaseHelper);
            }
        });

        btn_viewAll.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);

                ShowUserOnListView(dataBaseHelper);

                //Toast.makeText(MainActivity.this, allUser.toString(), Toast.LENGTH_SHORT).show();

            }
        });

        lv_userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                UserModel clickedUser = (UserModel) parent.getItemAtPosition(position);
               dataBaseHelper.deleteOne(clickedUser);
                ShowUserOnListView(dataBaseHelper);
                Toast.makeText(MainActivity.this, "Deleted" + clickedUser.toString(), Toast.LENGTH_SHORT).show();

            }
        });



    }


    private void ShowUserOnListView(DataBaseHelper dataBaseHelper2) {
        userArrayAdapter = new ArrayAdapter<UserModel>(MainActivity.this, android.R.layout.simple_list_item_1, dataBaseHelper2.getAll());
        lv_userList.setAdapter(userArrayAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main,menu);
        MenuItem menuItem = menu.findItem(R.id.search_icon);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search here!");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                userArrayAdapter.getFilter().filter(s);

                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}