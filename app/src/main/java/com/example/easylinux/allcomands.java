package com.example.easylinux;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class allcomands extends Fragment {

    private RecyclerView recyclerView;
    private ComandAdapter adapter;
    private List<Comands> commandList = new ArrayList<>();
    private List<Comands> filteredList = new ArrayList<>();
    private SearchView searchView;
    private Button searchButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment_allcomands.xml layout
        View view = inflater.inflate(R.layout.fragment_allcomands, container, false);

        recyclerView = view.findViewById(R.id.recyclerView1);
        searchView = view.findViewById(R.id.searchView);
        searchButton = view.findViewById(R.id.searchButton);

        // Set up the RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        loadJsonFromAsset();

        // Initialize adapter and set it to the RecyclerView
        adapter = new ComandAdapter(getActivity(), commandList);
        recyclerView.setAdapter(adapter);

        // Set up search functionality
        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        searchEditText.setHintTextColor(ContextCompat.getColor(getActivity(), R.color.secondary));

        // Handle search button click
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

        return view;
    }

    // Load JSON data from assets folder
    private void loadJsonFromAsset() {
        try {
            InputStream is = getActivity().getAssets().open("comands.json");
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
            Toast.makeText(getActivity(), "Error loading JSON", Toast.LENGTH_SHORT).show();
        }
    }

    // Filter commands based on query
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
}
