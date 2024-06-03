package com.example.ds2024part2;


import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import model.Property;


public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder> {
    private List<Property> propertyList;

    public void setProperties(List<Property> properties) {
        this.propertyList = properties;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_property, parent, false);
        return new PropertyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyViewHolder holder, int position) {
        Property property = propertyList.get(position);
        holder.roomName.setText(property.getRoomName());
        holder.price.setText(String.valueOf(property.getPrice()));
        holder.noOfPersons.setText(String.valueOf(property.getNoOfPersons()));
        holder.area.setText(property.getArea());
        holder.stars.setText(String.valueOf(property.getStars()));
        holder.noOfReviews.setText(String.valueOf(property.getNoOfReviews()));

        // Decode Base64 string and set image
        try {
            Bitmap bitmap = ImageUtils.decodeBase64ToBitmap(property.getRoomImage());
            holder.roomImage.setImageBitmap(bitmap);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return propertyList == null ? 0 : propertyList.size();
    }

    static class PropertyViewHolder extends RecyclerView.ViewHolder {
        TextView roomName, price, noOfPersons, area, stars, noOfReviews;
        ImageView roomImage;

        PropertyViewHolder(View view) {
            super(view);
            roomName = view.findViewById(R.id.roomName);
            price = view.findViewById(R.id.price);
            noOfPersons = view.findViewById(R.id.noOfPersons);
            area = view.findViewById(R.id.area);
            stars = view.findViewById(R.id.stars);
            noOfReviews = view.findViewById(R.id.noOfReviews);
            roomImage = view.findViewById(R.id.roomImage);
        }
    }
}