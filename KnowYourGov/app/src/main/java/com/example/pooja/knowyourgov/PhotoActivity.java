package com.example.pooja.knowyourgov;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PhotoActivity extends AppCompatActivity {

    private static final String TAG = "PhotoActivity";
    TextView photoTitle;
    KYGDetails OffDetailList;
    private Intent intent1;
    private String query = "";


    @Override
    protected void onSaveInstanceState(Bundle outState) {


        outState.putString("QUERY", query);

        // Call super last
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // Call super first
        super.onRestoreInstanceState(savedInstanceState);


        query = savedInstanceState.getString("QUERY");

        Log.d(TAG, "onRestoreInstanceState:: "+query);

    }


    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: pressed");
        if(!intent1.getStringExtra("query").equals("")) {
            query = intent1.getStringExtra("query");
        }
        Log.d(TAG, "onBackPressed:: "+query);
        Intent intent = new Intent(PhotoActivity.this,MainActivity.class);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        photoTitle = (TextView) findViewById(R.id.title_photo);
        TextView name_view3 = (TextView) findViewById(R.id.Party_photo);
        TextView office_view3 = (TextView) findViewById(R.id.Name_photo);
         final ImageView imageView3 = (ImageView) findViewById(R.id.photo);
        ScrollView scrollView=(ScrollView)findViewById(R.id.photo_scroll);

         intent1 = getIntent();
        if (intent1.hasExtra("titleForPhoto")) {
            photoTitle.setText(intent1.getStringExtra("titleForPhoto"));
        }        if (intent1.hasExtra("OfficialDetailsForPhotoAct")) {
            OffDetailList = (KYGDetails) intent1.getSerializableExtra("OfficialDetailsForPhotoAct");
            office_view3.setText(OffDetailList.getOfficename());
            name_view3.setText(OffDetailList.getOfficial_name());
            if (OffDetailList.getOffParty().equalsIgnoreCase("Republican")) {
                scrollView.setBackgroundColor(Color.RED);
            }            else if (OffDetailList.getOffParty().equalsIgnoreCase("Democratic") || OffDetailList.getOffParty()
                    .equalsIgnoreCase("Democrat")) {
                scrollView.setBackgroundColor(Color.BLUE);
            }            else {
                scrollView.setBackgroundColor(Color.BLACK);
            }

        }
//getting the image using picasso
        if (OffDetailList != null) {
            Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
// replacing http to https
                    final String httpsURL = OffDetailList.getPhotoUrl().replace("http:", "https:");
                    picasso.load(httpsURL)
                            .error(R.drawable.brokenimage)
                            .placeholder(R.drawable.placeholder)
                            .into(imageView3);
                }
            }).build();
            picasso.load(OffDetailList.getPhotoUrl())
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.placeholder)
                    .into(imageView3);
        } else { Picasso.with(this).load(OffDetailList.getPhotoUrl())
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.missingimage)
                    .into(imageView3);
        }

    }
}
