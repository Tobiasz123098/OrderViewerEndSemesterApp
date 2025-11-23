package com.example.domartorders;

import android.os.Bundle;
import android.util.TypedValue;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        Toolbar toolbar = findViewById(R.id.gallery_toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.gallery_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        List<GalleryItem> items = Arrays.asList(
                new GalleryItem(R.drawable.logo, getString(R.string.gallery_item_logo)),
                new GalleryItem(R.drawable.person, getString(R.string.gallery_item_clients)),
                new GalleryItem(R.drawable.unfold, getString(R.string.gallery_item_offers)),
                new GalleryItem(R.drawable.ic_menu_camera, getString(R.string.gallery_item_deliveries)),
                new GalleryItem(R.drawable.ic_menu_gallery, getString(R.string.gallery_item_transport)),
                new GalleryItem(R.drawable.ic_menu_slideshow, getString(R.string.gallery_item_presentations)),
                new GalleryItem(R.drawable.blue_outline, getString(R.string.gallery_item_statuses)),
                new GalleryItem(R.drawable.rounded_orders, getString(R.string.gallery_item_orders))
        );

        GalleryAdapter adapter = new GalleryAdapter(this, items, this::showImageDetails);
        recyclerView.setAdapter(adapter);
    }

    private void showImageDetails(GalleryItem item) {
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(item.getImageResId());
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        int paddingInPx = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                16,
                getResources().getDisplayMetrics());
        imageView.setPadding(paddingInPx, paddingInPx, paddingInPx, paddingInPx);

        new AlertDialog.Builder(this)
                .setTitle(item.getTitle())
                .setView(imageView)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
}