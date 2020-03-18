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

public class ProjectPostDetailActivity extends AppCompatActivity {

    private String postID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_post_detail);
        this.postID = getIntent().getStringExtra("postID");
        createPostDetailUI();
    }

    private void createPostDetailUI(){
        HashMap<String, String> data = new HashMap<>();
        data.put("function", "by_id");
        data.put("id", this.postID);
        Requests.postRequestToken(false, Urls.PROJECT_POST, data,
            new Requests.RequestOperation() {
                @Override
                public JSONObject onReply(final String reply) {
                    System.out.println(reply);
                    // Update the endless scroll UI
                    ProjectPostDetailActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ConstraintLayout layout = findViewById(R.id.user_post_detail_constraint);
                            // Get the text views and update them accordingly
                            TextView postTitle = findViewById(R.id.project_post_detail_title);
                            TextView user = findViewById(R.id.project_post_detail_user);
                            TextView description = findViewById(R.id.project_post_detail_description);
                            TextView skills = findViewById(R.id.project_post_detail_skills);
                            try {
                                JSONObject json = new JSONObject(reply);
                                postTitle.setText(json.getString("title"));
                                String byUser = json.getString("user_name");
                                boolean byUs = byUser.equals(User.user.getUsername());
                                user.setText("by "+(byUs ? "you" : byUser));
                                description.setText(json.getString("description"));
                                JSONArray skillsArray = json.getJSONArray("skills");
                                for(int skillIndex = 0; skillIndex<skillsArray.length(); skillIndex++) {
                                    skills.setText(
                                            skillIndex == 0 ? skills.getText() + (String) skillsArray.get(skillIndex) : skills.getText() + ", " + skillsArray.get(skillIndex)
                                    );
                                }
                                // If we posted it, don't put an apply button on
                                if (byUs) {
                                    layout.removeView(layout.findViewById(R.id.project_post_detail_apply));
                                } else {
                                    layout.findViewById(R.id.project_post_detail_apply).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ProjectPost.applyToProject(postID);
                                        }
                                    });
                                }
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
