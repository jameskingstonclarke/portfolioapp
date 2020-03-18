package com.folio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.folio.common.UI;
import com.folio.common.Requests;
import com.folio.project.ProjectPost;
import com.folio.user.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

public class MainFeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed);

        ((TextView)findViewById(R.id.username)).setText(User.user.getUsername());

        createProjectPostUI();
    }

    public void createProjectPostUI(){
        final Context context = this;
        HashMap<String, String> data = new HashMap<>();
        data.put("function", "all");
        Requests.postRequestToken(false, Urls.PROJECT_POST, data,
                new Requests.RequestOperation() {
                    @Override
                    public JSONObject onReply(String reply) {
                        System.out.println(reply);
                        JSONArray tmpArray = null;
                        try{
                            tmpArray = new JSONArray(reply);
                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                        final JSONArray jsonArray = tmpArray;
                        // Attempt to retrieve the post count
                        final int postCount = jsonArray.length();
                        // Update the endless scroll UI
                        MainFeedActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Dynamically invoke stuff here
                                ConstraintSet set = new ConstraintSet();
                                ConstraintLayout layout = findViewById(R.id.mainProjectPostScrollConstraint);
                                LayoutInflater inflater = getLayoutInflater();
                                RelativeLayout relLayout, previousLayout=null;

                                for(int i = 0; i<postCount; i++) {
                                    relLayout = (RelativeLayout) inflater.inflate(R.layout.project_post_layout, null);
                                    relLayout.setId(View.generateViewId());
                                    // Get the text views and update them accordingly
                                    TextView postTitle = relLayout.findViewById(R.id.project_post_title);
                                    TextView description = relLayout.findViewById(R.id.project_post_description);
                                    TextView user = relLayout.findViewById(R.id.project_post_user);
                                    TextView time = relLayout.findViewById(R.id.project_post_time);
                                    TextView skills = relLayout.findViewById(R.id.project_post_skills);
                                    try {
                                        JSONObject json = (JSONObject)jsonArray.get(i);
                                        final String postID = json.getString("id");
                                        relLayout.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                toProjectPostDetail(postID);
                                            }
                                        });
                                        postTitle.setText(json.getString("title"));
                                        description.setText(json.getString("description"));
                                        String byUser = json.getString("user_name");
                                        boolean byUs = byUser.equals(User.user.getUsername());
                                        user.setText("by "+(byUs ? "you" : byUser));
                                        time.setText(json.getString("time"));
                                        JSONArray skillsArray = json.getJSONArray("skills");
                                        for(int skillIndex = 0; skillIndex<skillsArray.length(); skillIndex++) {
                                            skills.setText(
                                                    skillIndex == 0 ? skills.getText() + (String) skillsArray.get(skillIndex) : skills.getText() + ", " + skillsArray.get(skillIndex)
                                            );
                                        }
                                        final String applied = json.getString("applied");
                                        // If we posted it, don't put an apply button on
                                        if(byUs) {
                                            relLayout.removeView(relLayout.findViewById(R.id.project_post_apply));
                                        }else{
                                            final Button button = relLayout.findViewById(R.id.project_post_apply);
                                            button.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    final String msg;
                                                    if(applied.equals("1")){
                                                        msg = "already applied!";
                                                    }else{
                                                        ProjectPost.applyToProject(postID);
                                                        msg = "applied to project!";
                                                    }
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            UI.popup(context, button, msg);
                                                        }
                                                    });
                                                }
                                            });
                                        }

                                    }catch(JSONException e){
                                        e.printStackTrace();
                                    }
                                    layout.addView(relLayout, i);
                                    set.clone(layout);
                                    if (previousLayout != null)
                                        set.connect(relLayout.getId(), ConstraintSet.TOP, previousLayout.getId(), ConstraintSet.BOTTOM, 60);
                                    set.applyTo(layout);
                                    previousLayout = relLayout;
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

    private void toProjectPostDetail(String postID){
        Intent intent = new Intent(this, ProjectPostDetailActivity.class);
        intent.putExtra("postID", postID);
        startActivity(intent);
    }

    public void to_post(View view){
        Intent intent = new Intent(this, PostActivity.class);
        startActivity(intent);
    }

    public void to_discover(View view){
        Intent intent = new Intent(this, DiscoveryFeedActivity.class);
        startActivity(intent);
    }

    public void to_notifications(View view){
        Intent intent = new Intent(this, NotificationsActivity.class);
        startActivity(intent);
    }
}
