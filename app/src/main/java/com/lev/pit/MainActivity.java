package com.lev.pit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private PitView pitView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pitView = (PitView) findViewById(R.id.canvas);

    }

    public void addPoint(View v) {
        pitView.addPoint();
    }

}
