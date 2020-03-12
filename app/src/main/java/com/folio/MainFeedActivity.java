package com.folio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainFeedActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed);

        OkHttpClient client = new OkHttpClient();
        String postGetURL = Urls.PROJECT_POST;

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("function", "all")
                .build();

        Request request = new Request.Builder()
                .url(postGetURL)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String myResponse = response.body().string();
                    System.out.println(myResponse);
                    final JSONArray jsonArray;
                    JSONArray tempArray = null;
                    // Attempt to retrieve the post count
                    final int postCount;
                    int tempCount = 0;
                    try {
                        tempArray = new JSONArray(myResponse);
                        tempCount = tempArray.length();
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    postCount = tempCount;
                    jsonArray = tempArray;
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
                                    postTitle.setText(json.getString("title"));
                                    description.setText(json.getString("description"));
                                    user.setText("by "+json.getString("user_name"));
                                    time.setText(json.getString("time"));
                                    skills.setText("skills: ");
                                    JSONArray skillsArray = json.getJSONArray("skills");
                                    for(int skillIndex = 0; skillIndex<skillsArray.length(); skillIndex++) {
                                        skills.setText(
                                                skillIndex == 0 ? skills.getText() + (String) skillsArray.get(skillIndex) : skills.getText() + ", " + skillsArray.get(skillIndex)
                                        );
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
                }
            }
        });
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
