package com.example.pooja.knowyourgov;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    private static final String TAG = "AboutActivity";
    private String query = "";
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        intent = getIntent();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.about,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        String str = intent.getStringExtra("qrystr");
        Log.d(TAG, "onSaveInstanceState:: "+str);
        outState.putString("QUERY", str);

        // Call super last
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: pressed");
        if(!intent.getStringExtra("qrystr").equals("")) {
            query = intent.getStringExtra("qrystr");
        }
        Log.d(TAG, "onBackPressed:: "+query);
        Intent intent = new Intent(AboutActivity.this,MainActivity.class);
        intent.putExtra("query", query);
        startActivityForResult(intent,1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // Call super first
        super.onRestoreInstanceState(savedInstanceState);


        query = savedInstanceState.getString("QUERY");
        Log.d(TAG, "onRestoreInstanceState:: "+query);
    }

}
