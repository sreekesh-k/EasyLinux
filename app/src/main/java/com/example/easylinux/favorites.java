package com.example.easylinux;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
public class favorites extends Fragment {

    private RecyclerView recyclerView;
    private ComandAdapter adapter;
    private List<Comands> commandList = new ArrayList<>();
    private TextView welcomeText;
    private Button logoutButton, refreshButton;
    private FavHelper favHelper;  // For database interaction
    private String username;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment (fragment_favorites.xml)
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        recyclerView = view.findViewById(R.id.recyclerView2);
        welcomeText = view.findViewById(R.id.welcomename);
        logoutButton = view.findViewById(R.id.button);
        refreshButton = view.findViewById(R.id.button1);
        favHelper = new FavHelper(getActivity());  // Initialize database helper

        // Get the username from SharedPreferences and display it
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Session", getActivity().MODE_PRIVATE);
        username = sharedPreferences.getString("username", "User");
        String text = "Hi " + username + "! These are your favorite Commands";
        text = text.toUpperCase();
        welcomeText.setText(text);
        loadJsonFromAsset();
        // Set up the RecyclerView with a LinearLayoutManager and Adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ComandAdapter(getActivity(), commandList);
        recyclerView.setAdapter(adapter);

        // Fetch and display favorite commands for the current user
        displayFavoriteCommands(username);

        // Set up the logout functionality
        logoutButton.setOnClickListener(v -> logout());
        refreshButton.setOnClickListener(v -> refresh());
        return view;
    }

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

    public void refresh(){
        displayFavoriteCommands(username);
    }
    // Method to fetch favorite commands for the logged-in user
    private void displayFavoriteCommands(String username) {
        // Get favorite command IDs for the user from the database
        List<Integer> favoriteCommandIds = favHelper.getFavoriteCommandIds(username);

        // If the user has favorite commands, fetch and display them
        if (favoriteCommandIds != null && !favoriteCommandIds.isEmpty()) {
            List<Comands> favoriteCommands = new ArrayList<>();

            // Iterate through the original command list and add favorites to the list
            for (Comands command : commandList) {
                if (favoriteCommandIds.contains(command.getId())) {
                    favoriteCommands.add(command);
                }
            }

            // Update the RecyclerView with the filtered favorite commands list
            adapter.updateList(favoriteCommands);
        } else {
            // Show a message if there are no favorite commands
            Toast.makeText(getActivity(), "No favorite commands found!", Toast.LENGTH_SHORT).show();
        }
    }

    // Handle the logout functionality
    private void logout() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Session", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("username");  // Remove the stored username
        editor.apply();

        // Redirect to the MainActivity (login screen)
        Toast.makeText(getActivity(), "Logout Success", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();  // Close the current activity
    }
}
