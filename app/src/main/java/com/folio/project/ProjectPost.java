package com.folio.project;

import com.folio.Urls;
import com.folio.common.Requests;
import com.folio.user.User;

import org.json.JSONObject;

import java.util.HashMap;

public class ProjectPost {
    private String title, description;

    public ProjectPost(String title, String description){
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public static void applyToProject(String postID){
        HashMap<String, String> data = new HashMap<>();
        data.put("function", "apply");
        data.put("project_post_id", postID);
        data.put("username", User.user.getUsername());
        data.put("message", "tmp message");
        Requests.postRequestToken(false, Urls.PROJECT, data, new Requests.RequestOperation() {
            @Override
            public JSONObject onReply(String reply) {
                return null;
            }

            @Override
            public JSONObject onFail(String reply) {
                return null;
            }
        });
    }
}
