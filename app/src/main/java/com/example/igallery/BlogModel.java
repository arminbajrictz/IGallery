package com.example.igallery;

public class BlogModel {

    private String blogTitle;
    private String blogText;
    private String blogPhotoUrl;
    private long blogTimestamp;

    public BlogModel() {
    }

    public String getBlogTitle() {
        return blogTitle;
    }

    public void setBlogTitle(String blogTitle) {
        this.blogTitle = blogTitle;
    }

    public String getBlogText() {
        return blogText;
    }

    public void setBlogText(String blogText) {
        this.blogText = blogText;
    }

    public String getBlogPhotoUrl() {
        return blogPhotoUrl;
    }

    public void setBlogPhotoUrl(String newsPhotoUrl) {
        this.blogPhotoUrl = newsPhotoUrl;
    }

    public long getBlogTimestamp() {
        return blogTimestamp;
    }

    public void setBlogTimestamp(long blogTimestamp) {
        this.blogTimestamp = blogTimestamp;
    }
}
