package com.iqbalhood.ricecode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Dita Ananda Yulaz on 5/17/2016.
 */
public class Result  extends AppCompatActivity {

    Button OK;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        OK = (Button)findViewById(R.id.btnOK);

        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(Result.this, Dashboard.class);
                k.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(k);
            }
        });

    }
}

