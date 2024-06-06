package com.example.ds2024part2;

import android.content.Context;
import android.content.Intent;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;
import model.Property;

public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder> {

    private List<Property> properties;
    private Context context;

    public PropertyAdapter(Context context, List<Property> properties) {
        this.context = context;
        this.properties = properties;
    }

    @NonNull
    @Override
    public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_property, parent, false);
        return new PropertyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyViewHolder holder, int position) {
        Property property = properties.get(position);
        holder.roomName.setText(property.getRoomName());
        holder.price.setText(String.valueOf(property.getPrice()));
        holder.noOfPersons.setText(String.valueOf(property.getNoOfPersons()));
        holder.area.setText(property.getArea());
        holder.stars.setText(String.valueOf(property.getStars()));
        holder.noOfReviews.setText(String.valueOf(property.getNoOfReviews()));

        // Decode Base64 image string to bytes
        byte[] imageBytes = Base64.decode(property.getRoomImage(), Base64.DEFAULT);

        // Use Glide to load the decoded image bytes into ImageView
        Glide.with(context)
                .load(imageBytes)
                .into(holder.roomImage);

        holder.bookNowButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, BookingActivity.class);
            intent.putExtra("PROPERTY_NAME", property.getRoomName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return properties != null ? properties.size() : 0;
    }

    public static class PropertyViewHolder extends RecyclerView.ViewHolder {
        TextView roomName, price, noOfPersons, area, stars, noOfReviews;
        ImageView roomImage;
        Button bookNowButton;

        public PropertyViewHolder(@NonNull View itemView) {
            super(itemView);
            roomName = itemView.findViewById(R.id.roomName);
            price = itemView.findViewById(R.id.price);
            noOfPersons = itemView.findViewById(R.id.noOfPersons);
            area = itemView.findViewById(R.id.area);
            stars = itemView.findViewById(R.id.stars);
            noOfReviews = itemView.findViewById(R.id.noOfReviews);
            roomImage = itemView.findViewById(R.id.roomImage);
            bookNowButton = itemView.findViewById(R.id.bookNowButton);
        }
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
        notifyDataSetChanged();
    }
}