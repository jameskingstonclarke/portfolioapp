package com.folio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.folio.utils.Web;

public class MainFeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed);

        ConstraintSet set = new ConstraintSet();


        ConstraintLayout layout = findViewById(R.id.endlessScrollLayout);
        LayoutInflater inflater = getLayoutInflater();
        RelativeLayout relLayout, previousLayout=null;
        for(int i = 0; i<25; i++) {
            relLayout = (RelativeLayout) inflater.inflate(R.layout.post_layout, null);
            relLayout.setId(View.generateViewId());

            TextView postTitle = relLayout.findViewById(R.id.post_title);
            TextView description = relLayout.findViewById(R.id.post_description);

            Web.RequestTask req = new Web.RequestTask();
            req.setResultText(postTitle);
            req.execute("http://51.75.143.168/appapi/testapi.php", "", "title");
            req = new Web.RequestTask();
            req.setResultText(description);
            req.execute("http://51.75.143.168/appapi/testapi.php", "", "description");

            layout.addView(relLayout, i);
            set.clone(layout);
            if (previousLayout != null)
                set.connect(relLayout.getId(), ConstraintSet.TOP, previousLayout.getId(), ConstraintSet.BOTTOM, 60);
            set.applyTo(layout);

            previousLayout = relLayout;
        }
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
