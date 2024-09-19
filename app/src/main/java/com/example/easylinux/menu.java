package com.example.easylinux;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.stream.Collectors;


public class menu extends AppCompatActivity {


    private RecyclerView recyclerView;
    private ComandAdapter adapter;
    private List<Comands> commandList = new ArrayList<>();
    private List<Comands> filteredList = new ArrayList<>();
    private SearchView searchView;
    private Button searchButton;
    private TextView welcome ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);
        searchButton = findViewById(R.id.searchButton);
        welcome = findViewById(R.id.welcomename);
        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(ContextCompat.getColor(this, R.color.white));  // Change text color
        searchEditText.setHintTextColor(ContextCompat.getColor(this, R.color.secondary));  // Change hint color

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        welcome.setText("Welcome Back " + username );
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadJsonFromAsset();

        adapter = new ComandAdapter(this, commandList);
        recyclerView.setAdapter(adapter);

        // Search functionality
        searchButton.setOnClickListener(v -> filterCommands(searchView.getQuery().toString()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterCommands(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Optional: you can also filter as the text changes
                return false;
            }
        });
    }

    public void logout(View v){
        SharedPreferences sharedPreferences = getSharedPreferences("Session", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("username");  // Remove the stored username
        editor.apply();

        // Redirect to MainActivity (login screen)
        Toast.makeText(menu.this,"Logout Success",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(menu.this, MainActivity.class);
        startActivity(intent);
        finish();

    }
    private void filterCommands(String query) {
        if (query == null || query.isEmpty()) {
            adapter.updateList(commandList);
        } else {
            filteredList = commandList.stream()
                    .filter(command -> command.getTitle().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList());
            adapter.updateList(filteredList);
        }
    }


    // Load JSON data from the assets folder
    private void loadJsonFromAsset() {
        try {
            InputStream is = getAssets().open("comands.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String json = new String(buffer, StandardCharsets.UTF_8);
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                Comands command = new Comands(
                        obj.getInt("id"),
                        obj.getString("title"),
                        obj.getString("command"),
                        obj.getString("description"),
                        obj.getString("example")
                );

                commandList.add(command);
            }

        } catch (Exception e) {
            Toast.makeText(this, "Error loading JSON", Toast.LENGTH_SHORT).show();
        }
    }
}
