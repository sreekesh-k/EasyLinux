package com.example.easylinux;

import android.os.Bundle;
import android.widget.TextView;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class menu extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Comands> commandList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadJsonFromAsset();

        ComandAdapter adapter = new ComandAdapter(this, commandList);
        recyclerView.setAdapter(adapter);
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
