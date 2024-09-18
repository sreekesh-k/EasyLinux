package com.example.easylinux;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ComandAdapter extends RecyclerView.Adapter<ComandAdapter.CommandViewHolder> {

    private List<Comands> commandList;
    private Context context;

    public ComandAdapter(Context context, List<Comands> commandList) {
        this.context = context;
        this.commandList = commandList;
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

        // Show a popup on click with description and example
        holder.itemView.setOnClickListener(v -> {
            android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(context);
            dialog.setTitle(command.getTitle());
            dialog.setMessage(command.getDescription() + "\n\nExample: " + command.getExample());
            dialog.setPositiveButton("OK", null);
            dialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return commandList.size();
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
