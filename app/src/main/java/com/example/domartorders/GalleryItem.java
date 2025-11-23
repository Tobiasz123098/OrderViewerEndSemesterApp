package com.example.domartorders;

public class GalleryItem {
    private final int imageResId;
    private final String title;

    public GalleryItem(int imageResId, String title) {
        this.imageResId = imageResId;
        this.title = title;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getTitle() {
        return title;
    }
}