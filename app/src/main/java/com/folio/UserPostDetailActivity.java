package com.folio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.folio.common.Requests;
import com.folio.project.ProjectPost;
import com.folio.user.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class UserPostDetailActivity extends AppCompatActivity {

    private String postID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_post_detail);
        this.postID = getIntent().getStringExtra("postID");
        createPostDetailUI();
    }

    private void createPostDetailUI(){
        HashMap<String, String> data = new HashMap<>();
        data.put("function", "by_id");
        data.put("id", this.postID);
        Requests.postRequestToken(false, Urls.USER_POST, data,
                new Requests.RequestOperation() {
                    @Override
                    public JSONObject onReply(final String reply) {
                        System.out.println(reply);
                        // Update the endless scroll UI
                        UserPostDetailActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ConstraintLayout layout = findViewById(R.id.user_post_detail_constraint);
                                // Get the text views and update them accordingly
                                TextView user = findViewById(R.id.user_post_detail_user);
                                TextView summary = findViewById(R.id.user_post_detail_summary);
                                try {
                                    JSONObject json = new JSONObject(reply);
                                    String byUser = json.getString("username");
                                    System.out.println(byUser +"lol");
                                    boolean byUs = byUser.equals(User.user.getUsername());
                                    user.setText("by "+(byUs ? "you" : byUser));
                                    summary.setText(json.getString("summary"));
                                }catch(JSONException e){
                                    e.printStackTrace();
                                }
                            }
                        });
                        return null;
                    }

                    @Override
                    public JSONObject onFail(String reply){
                        return null;
                    }
                }
        );
    }
}
