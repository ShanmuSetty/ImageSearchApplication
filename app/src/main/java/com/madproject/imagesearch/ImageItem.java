package com.madproject.imagesearch;

public class ImageItem {
    private String imageUrl;
    private String description;

    public ImageItem(String imageUrl, String description) {
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }
}
