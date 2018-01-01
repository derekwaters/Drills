package com.frisbeeworld.drills.database;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;

public class Drill {
    private String id;
    private String name;
    private String videoUrl;
    private String imageUrl;
    private ArrayList<String> tags;
    private int minTime;
    private int maxTime;
    private String description;
    private int people;

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public int getPeople() { return this.people; }

    public void setPeople(int people) { this.people = people; }

    public boolean matchesTags(ArrayList<String> tags)
    {
        for (String checkTag : tags)
        {
            if (this.getTags().contains(checkTag))
            {
                return true;
            }
        }
        return false;
    }

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
