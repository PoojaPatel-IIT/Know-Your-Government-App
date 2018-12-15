package com.example.pooja.knowyourgov;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import com.squareup.picasso.Picasso;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class OfficialActivity extends AppCompatActivity {

    private static final String TAG = "OfficialActivity";

    TextView title;
    KYGDetails offDetailList;
    ImageView photo;
    private String qrystr;
    private Intent intent;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }




    @Override
    protected void onSaveInstanceState(Bundle outState) {


        outState.putString("QUERY", qrystr);

        // Call super last
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: pressed");

        if(!intent.getStringExtra("query").equals("")) {
            qrystr = intent.getStringExtra("query");
        }
        Log.d(TAG, "onBackPressed:: "+qrystr);
        Intent intent = new Intent();
        intent.putExtra("query", qrystr);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // Call super first
        super.onRestoreInstanceState(savedInstanceState);


        qrystr = savedInstanceState.getString("QUERY");

        Log.d(TAG, "onRestoreInstanceState:: "+qrystr);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);


        ImageView google = (ImageView) findViewById(R.id.google);
        ImageView twitter = (ImageView) findViewById(R.id.twitter);
        ImageView facebook = (ImageView) findViewById(R.id.facebook);
        ImageView youtube = (ImageView) findViewById(R.id.youtube);


        title = (TextView) findViewById(R.id.titleOfficialAct);
        TextView OffOffice = (TextView) findViewById(R.id.OffOffice);
        TextView OffName = (TextView) findViewById(R.id.OffName);
        TextView party = (TextView) findViewById(R.id.OffParty);
        photo = (ImageView) findViewById(R.id.OffPhoto);


        TextView addressFound = (TextView) findViewById(R.id.addressFound);
        TextView phoneFound = (TextView) findViewById(R.id.phoneFound);
        TextView emailadd = (TextView) findViewById(R.id.emailFound);
        TextView websiteFound = (TextView) findViewById(R.id.websiteFound);
        ScrollView OffScroll = (ScrollView) findViewById(R.id.OffScroll);





         intent = getIntent();
        qrystr = intent.getStringExtra("query");

        if (intent.hasExtra("titleForPhoto")) {
            title.setText(intent.getStringExtra("titleForPhoto"));
        }
        if (intent.hasExtra("KYGObj")) {
            offDetailList = (KYGDetails) intent.getSerializableExtra("KYGObj");
            OffOffice.setText(offDetailList.getOfficename());
            OffName.setText(offDetailList.getOfficial_name());
            party.setText("(" + offDetailList.getOffParty() + ")");
            addressFound.setText(offDetailList.getOffAddress());
            phoneFound.setText(offDetailList.getOffContact());
            emailadd.setText(offDetailList.getEmail());
            websiteFound.setText(offDetailList.getOffwebsite());
        }

        if (offDetailList.getOffParty().equalsIgnoreCase("Republican")) {
            Log.d(TAG, "onCreate: I AM HERE" + offDetailList);
            OffScroll.setBackgroundColor(Color.RED);
        }
        else if (offDetailList.getOffParty().equalsIgnoreCase("Democratic")
                || offDetailList.getOffParty().equalsIgnoreCase("Democrat")
                || offDetailList.getOffParty().equalsIgnoreCase("Democratic Party"))
            OffScroll.setBackgroundColor(Color.BLUE);
        else {
            Log.d(TAG, "onCreate: I AM  in else");
            OffScroll.setBackgroundColor(Color.BLACK);
        }

        // setting invisibility for the social medial not available


        if (offDetailList.getTwitter().equals("No Data Provided"))
            twitter.setVisibility(View.INVISIBLE);
        if (offDetailList.getYoutube().equals("No Data Provided"))
            youtube.setVisibility(View.INVISIBLE);


        if (offDetailList.getGoogle().equals("No Data Provided"))
            google.setVisibility(View.INVISIBLE);
        if (offDetailList.getFacebook().equals("No Data Provided"))
            facebook.setVisibility(View.INVISIBLE);

// setting everything to first No data provided
        if (!addressFound.getText().toString().equals("No Data Provided")) {
            Linkify.addLinks(addressFound, Linkify.MAP_ADDRESSES);
            Log.d(TAG, "onCreate: Addr to linkify" + addressFound);
        }
        if (!phoneFound.getText().toString().equals("No Data Provided"))
            Linkify.addLinks(phoneFound, Linkify.PHONE_NUMBERS);
        if (!emailadd.getText().toString().equals("No Data Provided"))
            Linkify.addLinks(emailadd, Linkify.EMAIL_ADDRESSES);
        if (!websiteFound.getText().toString().equals("No Data Provided"))
            Linkify.addLinks(websiteFound, Linkify.WEB_URLS);

        if (offDetailList != null) {
            // using picasso for picture url
            if(!offDetailList.getPhotoUrl().equals("")){
                Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        // replace http to https and try
        // Here we try https if the http image attempt failed
                        final String changedUrl = offDetailList.getPhotoUrl().replace("http:", "https:");
                        picasso.load(changedUrl)
                                .error(R.drawable.brokenimage)
                                .placeholder(R.drawable.placeholder)
                                .into(photo);
                    }
                }).build();
                picasso.load(offDetailList.getPhotoUrl()).error(R.drawable.brokenimage).placeholder(R.drawable.placeholder)
                        .into(photo);
            }
            // for the missing image
            else { Picasso.with(this).load(R.drawable.missingimage).error(R.drawable.brokenimage).placeholder(R.drawable.missingimage)
                        .into(photo);

            }


           
        }
    }


    public void picture(View v) {
        if(offDetailList != null) {
            if (!offDetailList.getPhotoUrl().equals("")) {
                Intent it = new Intent(this, PhotoActivity.class);
                // puttig the title and offDetailList and sending to photo activity
                it.putExtra("titleForPhoto", title.getText());
                it.putExtra("OfficialDetailsForPhotoAct", offDetailList);
                it.putExtra("query", qrystr);
                Log.d(TAG, "openPhotoActivity: Sending this to PhotoActivity : title "+title.getText() );
                Log.d(TAG, "openPhotoActivity: photo stuff "+offDetailList);
                startActivity(it);
            }
        }
    }

    public void googlePlusClicked(View v) {
        if (offDetailList == null || offDetailList.getGoogle().equals(""))
            return;

        String name = offDetailList.getGoogle();
        Intent intent1 = null;
        try {
            intent1 = new Intent(Intent.ACTION_VIEW);
            intent1.setClassName("com.google.android.apps.plus",
                    "com.google.android.apps.plus.phone.UrlGatewayActivity");
            intent1.putExtra("customAppUri", name);
            startActivity(intent1);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://plus.google.com/" + name)));
        }
    }

    public void youTubeClicked(View v) {
        if (offDetailList == null || offDetailList.getYoutube().equals(""))
            return;

        String name = offDetailList.getYoutube();
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + name));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/" + name)));
        }
    }

    public void twitterClicked(View v) {
        if (offDetailList == null || offDetailList.getTwitter().equals(""))
            return;

        Intent intent = null;
        String name = offDetailList.getTwitter();
        try {
            // get the Twitter app if possible
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + name));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + name));
        }
        startActivity(intent);
    }

    public void facebookClicked(View v) {
        if (offDetailList == null || offDetailList.getFacebook().equals(""))
            return;

        String FACEBOOK_URL = "https://www.facebook.com/" + offDetailList.getFacebook();
        String urlToUse;
        PackageManager packageManager = getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                //urlToUse = "fb://page/" + channels.get("Facebook");
                urlToUse = "fb://page/" + offDetailList.getFacebook();
            }
        } catch (PackageManager.NameNotFoundException e) {
            urlToUse = FACEBOOK_URL; //normal web url
        }
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        facebookIntent.setData(Uri.parse(urlToUse));
        startActivity(facebookIntent);

    }


}
