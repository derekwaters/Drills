package com.frisbeeworld.drills.database;

import java.util.ArrayList;

public class Drill {
    private int id;
    private String name;
    private String videoUrl;
    private String imageUrl;
    private ArrayList<String> tags;
    private int minTime;
    private int maxTime;
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public int getMinTime() {
        return minTime;
    }

    public void setMinTime(int minTime) {
        this.minTime = minTime;
    }

    public int getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
    }

    public String getDescription() { return description; }

    public void setDescription(String desc) { this.description = desc; }
    /*

    id
- videoLink
- imageLink
- skills trained
- time required (min - max?)
- space required
- equipment required
- preparation
- target age range
    */

}
