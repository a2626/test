package com.company.insta.instagram.models;

/**
 */

public class Like {

    int story_id;
    String story_image,story_username, user_profile, action;


    public Like(String story_image, String story_username, String user_profile, String action) {
      //  this.story_id = story_id;
        this.story_image = story_image;
        this.story_username = story_username;
        this.user_profile = user_profile;
        this.action = action;
    }


   /* public int getStory_id() {
        return story_id;
    }

    public void setStory_id(int story_id) {
        this.story_id = story_id;
    }*/

    public String getStory_image() {
        return story_image;
    }

    public void setStory_image(String story_image) {
        this.story_image = story_image;
    }

    public String getStory_username() {
        return story_username;
    }

    public void setStory_username(String story_username) {
        this.story_username = story_username;
    }

    public String getuser_profile() {
        return user_profile;
    }

    public void setuser_profile(String user_profile) {
        this.user_profile = user_profile;
    }

    public String getaction() {
        return action;
    }

    public void setaction(String action) {
        this.action = action;
    }
}
