package com.iqbalhood.ricecode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by IQBAL on 5/23/2016.
 */
public class About extends AppCompatActivity {
    Button buttonHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        buttonHome = (Button)findViewById(R.id.buttonHome);

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent k = new Intent(About.this, Dashboard.class);
                k.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(k);




            }
        });


    }
}