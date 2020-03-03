package com.folio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
        TextView view, previousView=null;
        for(int i = 0; i<19; i++) {
            view = (TextView)inflater.inflate(R.layout.test, null);
            view.setTextColor(getResources().getColor(R.color.textColour));
            Web.RequestTask req = new Web.RequestTask();
            req.setResultText(view);
            req.execute("http://51.75.143.168/testapi.php", "", "name");
            view.setId(View.generateViewId());
            layout.addView(view,i);
            set.clone(layout);
            if(previousView!=null)
                set.connect(view.getId(), ConstraintSet.TOP, previousView.getId(), ConstraintSet.BOTTOM, 60);
            set.applyTo(layout);

            previousView = view;
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
