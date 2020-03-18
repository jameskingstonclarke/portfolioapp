package com.folio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.folio.common.Requests;
import com.folio.user.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class NotificationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        ((TextView)findViewById(R.id.username)).setText(User.user.getUsername());

        createNotificationUI();
    }

    public void createNotificationUI(){
        HashMap<String, String> data = new HashMap<>();
        data.put("function", "all");
        data.put("username", User.user.getUsername());
        Requests.postRequestToken(false, Urls.NOTIFICATION, data, new Requests.RequestOperation() {
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
                final int notificationCount = jsonArray.length();
                // Update the endless scroll UI
                NotificationsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Dynamically invoke stuff here
                        ConstraintSet set = new ConstraintSet();
                        ConstraintLayout layout = findViewById(R.id.notificationScrollConstraint);
                        LayoutInflater inflater = getLayoutInflater();
                        RelativeLayout relLayout, previousLayout=null;

                        for(int i = 0; i<notificationCount; i++) {
                            relLayout = (RelativeLayout) inflater.inflate(R.layout.notification_layout, null);
                            relLayout.setId(View.generateViewId());
                            // Get the text views and update them accordingly
                            final TextView summary = relLayout.findViewById(R.id.notification_summary);
                            TextView time = relLayout.findViewById(R.id.notification_time);
                            Button seen = relLayout.findViewById(R.id.notification_seen);
                            try {
                                final JSONObject json = (JSONObject)jsonArray.get(i);
                                summary.setText(json.getString("message"));
                                time.setText(json.getString("time"));
                                seen.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            markAsSeen(json.getString("id"));
                                        }catch(JSONException e){
                                            e.printStackTrace();
                                        }
                                    }
                                });
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
            public JSONObject onFail(String reply) {
                return null;
            }
        });
    }

    private void markAsSeen(String notification_id){
        HashMap<String, String> data = new HashMap<>();
        data.put("function", "seen");
        data.put("notification_id", notification_id);
        Requests.postRequestToken(false, Urls.NOTIFICATION, data, new Requests.RequestOperation() {
            @Override
            public JSONObject onReply(String reply) {
                System.out.println(reply);
                return null;
            }

            @Override
            public JSONObject onFail(String reply) {
                return null;
            }
        });

        // Reset the screen
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
}
