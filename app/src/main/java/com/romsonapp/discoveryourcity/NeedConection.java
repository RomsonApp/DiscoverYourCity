package com.romsonapp.discoveryourcity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class NeedConection extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_need_connection);
    }

    public void startApp(View view) {
        Intent intent = new Intent(this, Main.class);
        startActivity(intent);
        this.finish();
    }
}
