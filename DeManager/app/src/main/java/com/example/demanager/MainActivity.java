package com.example.demanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SqliteDatabase mDatabase;
    private DepartmentAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView departmentView = findViewById(R.id.product_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        departmentView.setLayoutManager(linearLayoutManager);
        departmentView.setHasFixedSize(true);
        mDatabase = new SqliteDatabase(this);
        ArrayList<Departments> allDepartments = mDatabase.listDepartments();
        if(allDepartments.size() <= 0){
            {
                // doc file json
                ArrayList<Departments> getFromJson = getDepartmentList();
                if (getFromJson!=null){
                    allDepartments.addAll(getFromJson);
                }
                // insert vao db
                for (Departments department: allDepartments) {
                    mDatabase.addDepartments(department);
                }
            }
        }

        if(allDepartments.size() > 0){
            departmentView.setVisibility(View.VISIBLE);
            mAdapter = new DepartmentAdapter(this, allDepartments);
            departmentView.setAdapter(mAdapter);
        }else {
            departmentView.setVisibility(View.GONE);
        }
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> addTaskDialog());
    }


    //JSON
    private ArrayList<Departments>  getDepartmentList()
    {
        String myJSONStr = loadJSONFromAssets();
        if (myJSONStr==null) {
            return null;
        }
        try
        {
            ArrayList<Departments> departmentList = new ArrayList<>();
            //Json parsing
            JSONObject rootJsonObject = new JSONObject(myJSONStr);

            JSONArray departmentJsonArray = rootJsonObject.getJSONArray("departments");

            for (int i = 0; i < departmentJsonArray.length(); i++)
            {
                // Create a temp
                Departments aDepartments = new Departments();
                JSONObject jsonObject = departmentJsonArray.getJSONObject(i);

                //get departments details

                aDepartments.setDepartment(jsonObject.getString("departmentname"));
                //add to list
                departmentList.add(aDepartments);

            }
            return  departmentList;
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }




    private void addTaskDialog()
    {
        LayoutInflater inflater = LayoutInflater.from(this);
        View subView = inflater.inflate(R.layout.add_department_layout, null);

        final EditText nameField = subView.findViewById(R.id.enter_departmentname);



        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add new DEPARTMENT");
        builder.setView(subView);
        builder.create();

        builder.setPositiveButton("ADD DEPARTMENT", (dialog, which) -> {
            final String name = nameField.getText().toString();


            if(TextUtils.isEmpty(name)){
                Toast.makeText(MainActivity.this, "データを入れてください。", Toast.LENGTH_LONG).show();
            }
            else{
                Departments newDepartment = new Departments(name);
                mDatabase.addDepartments(newDepartment);

                finish();
                startActivity(getIntent());
            }
        });

        builder.setNegativeButton("CANCEL", (dialog, which) -> Toast.makeText(MainActivity.this, "Task cancelled", Toast.LENGTH_LONG).show());
        builder.show();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if(mDatabase != null){
            mDatabase.close();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) search.getActionView();
        search(searchView);
        return true;
    }

    private void search(SearchView searchView)
    {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (mAdapter!=null)
                    mAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }


    // JSon
    private String loadJSONFromAssets()
    {
        String json = null;
        try
        {
            InputStream inputStream = getAssets().open("Jsonfile.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e)
        {
            e.printStackTrace();

        }
        return json;
    }
}
