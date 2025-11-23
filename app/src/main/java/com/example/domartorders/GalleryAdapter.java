package com.example.domartorders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(GalleryItem item);
    }

    private final List<GalleryItem> items;
    private final LayoutInflater inflater;
    private final OnItemClickListener listener;

    public GalleryAdapter(Context context, List<GalleryItem> items, OnItemClickListener listener) {
        this.items = items;
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_gallery, parent, false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {
        GalleryItem item = items.get(position);
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class GalleryViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView titleView;
        private final CardView root;

        GalleryViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.gallery_image);
            titleView = itemView.findViewById(R.id.gallery_title);
            root = (CardView) itemView;
        }

        void bind(GalleryItem item, OnItemClickListener listener) {
            imageView.setImageResource(item.getImageResId());
            imageView.setContentDescription(item.getTitle());
            titleView.setText(item.getTitle());
            root.setOnClickListener(v -> listener.onItemClick(item));
        }
    }
}