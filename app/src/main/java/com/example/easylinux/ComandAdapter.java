package com.example.easylinux;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
public class ComandAdapter extends RecyclerView.Adapter<ComandAdapter.CommandViewHolder> {

    private List<Comands> commandList;
    private Context context;
    private FavHelper favHelper;

    public ComandAdapter(Context context, List<Comands> commandList) {
        this.context = context;
        this.commandList = commandList;
        this.favHelper = new FavHelper(context); // Initialize database helper
    }

    @NonNull
    @Override
    public CommandViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comands, parent, false);
        return new CommandViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommandViewHolder holder, int position) {
        Comands command = commandList.get(position);
        holder.titleTextView.setText(command.getTitle());
        holder.commandTextView.setText(command.getCommand());

        holder.itemView.setOnClickListener(v -> {
            // Inflate the custom dialog layout
            LayoutInflater inflater = LayoutInflater.from(context);
            View dialogView = inflater.inflate(R.layout.dialog, null);

            // Initialize views from the custom layout
            TextView titleTextView = dialogView.findViewById(R.id.dialog_title);
            TextView messageTextView = dialogView.findViewById(R.id.dialog_message);
            Button addToFavoritesButton = dialogView.findViewById(R.id.add_to_favorites_button);
            Button removeFromFavoritesButton = dialogView.findViewById(R.id.remove_from_favorites_button);
            Button okButton = dialogView.findViewById(R.id.ok_button);

            // Set dialog content
            titleTextView.setText(command.getTitle());
            messageTextView.setText(command.getDescription() + "\n\nExample: " + command.getExample());


            // Check if the command is already a favorite
            SharedPreferences sharedPreferences = context.getSharedPreferences("Session", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("username", "User");
            boolean isFavorite = favHelper.isFavorite(username,command.getId());

            if (isFavorite) {
                addToFavoritesButton.setVisibility(View.GONE);
                removeFromFavoritesButton.setVisibility(View.VISIBLE);
            } else {
                addToFavoritesButton.setVisibility(View.VISIBLE);
                removeFromFavoritesButton.setVisibility(View.GONE);
            }

            // Create and show the dialog
            android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(context, R.style.CustomAlertDialogTheme);
            dialogBuilder.setView(dialogView);

            android.app.AlertDialog dialog = dialogBuilder.create();

            // Handle "Add to Favorites" button click
            addToFavoritesButton.setOnClickListener(v1 -> {
                // Get the username from SharedPreferences

                // Add the command to favorites in the database
                favHelper.addFavorite(username, command.getId());
                Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            });

            // Handle "Remove from Favorites" button click
            removeFromFavoritesButton.setOnClickListener(v1 -> {
                // Get the username from SharedPreferences

                // Remove the command from favorites in the database
                favHelper.removeFavorite(username, command.getId());
                Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            });

            // Handle "OK" button click
            okButton.setOnClickListener(v1 -> dialog.dismiss());

            dialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return commandList.size();
    }

    public void updateList(List<Comands> newList) {
        commandList = newList;
        notifyDataSetChanged();
    }

    public static class CommandViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, commandTextView;

        public CommandViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            commandTextView = itemView.findViewById(R.id.commandTextView);
        }
    }
}
