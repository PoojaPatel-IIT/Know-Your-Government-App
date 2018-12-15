package com.example.pooja.knowyourgov;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.xml.sax.Locator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener, View.OnClickListener {
    private static final String TAG = "MainActivity";


    //  declaration
    private TextView loc;
    private String conLink;
    private RecyclerView rview;
    KYGAsyncTask asyncTask;
    private KYGLocator loc_ref;
    private int MY_PERM_REQ_CODE  = 5;
    public String addtoshow="";
    private String qrystr = "";
    private ArrayList<KYGDetails> detList = new ArrayList<>();
    private ArrayList<KYGDetails> newArray= new ArrayList<>();
    private KYGDetailsAdapter adap;

    @Override
    protected void onSaveInstanceState(Bundle outState) {


        outState.putString("QUERY", qrystr);

        // Call super last
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "onActivityResult: entered here");
                String query = data.getStringExtra("query");
                Log.d(TAG, "onActivityResult::" + query);
                if (doNetworkkCheck()) {
                    new KYGAsyncTask(MainActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, query);
                } else {

                }
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // Call super first
        super.onRestoreInstanceState(savedInstanceState);


        qrystr = savedInstanceState.getString("QUERY");
        if(!qrystr.equals("")) {
            new KYGAsyncTask(MainActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, qrystr);
        }
        Log.d(TAG, "onRestoreInstanceState:: "+qrystr);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loc = findViewById(R.id.loc);
        rview = (RecyclerView) findViewById(R.id.recycler);
        adap = new KYGDetailsAdapter(this, detList);
        rview.setAdapter(adap);
        rview.setLayoutManager(new LinearLayoutManager(this));
        Intent pact = getIntent();
        if(pact != null)
        {
            if(pact.hasExtra("query") )
            {
                if(!pact.getStringExtra("query").equals(""))
                {
                    qrystr  =  pact.getStringExtra("query");
                    Log.d(TAG, "onCreate: "+qrystr);
                    new KYGAsyncTask(MainActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, qrystr);
                }
            }
        }
        if(qrystr.equals("")) {
            Log.d(TAG, "onCreate: "+qrystr);
            doNetCheck_locationper();
        }
    }

    public void settingOffList(Object[] finaldata) {
        if (finaldata == null) {
            Log.d(TAG, "settingOffList:finaldata "+finaldata);
            loc.setText("No Data Provided");
            detList.clear();
        }
        else{
            newArray.clear();
            detList.clear();
            detList.addAll((ArrayList<KYGDetails>) finaldata[1]);
            newArray.addAll((ArrayList) finaldata[1]);
            //Set the location in title
            loc.setText(finaldata[0].toString());
        }
        adap.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.kygpage, menu);
        return true;
    }
    private void doNetCheck_locationper() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            // Toast.makeText(this, "Cannot access ConnectivityManager", Toast.LENGTH_SHORT).show();
            return;
        }

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
        loc_ref = new KYGLocator(this);
        } else {

            //Alert for network Connectivity
            Log.d(TAG, "onCreate: In network");
            loc.setText("No Data For Location");
            android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(MainActivity.this);
            builder1.setMessage("Data cannot be accessed/loaded without an Internet Connection");
            builder1.setTitle("No Network Connection");
            android.app.AlertDialog dialog = builder1.create();
            dialog.show();

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:

                Intent intent = new Intent(this, AboutActivity.class);
                intent.putExtra("qrystr", qrystr);
                startActivity(intent);
                break;
            case R.id.magnifying:
                Log.d(TAG, "onOptionsItemSelected: Search");
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setGravity(Gravity.CENTER_HORIZONTAL);
                input.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
                builder.setView(input);
                //urlinput = input.getText().toString();
                Log.d(TAG, "onOptionsItemSelected: "+ conLink);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        conLink = input.getText().toString();
                        qrystr = conLink;
                        executeKYGAsyncTask(conLink);
                        // asyncTask = new AsnycTask(MainActivity.this);
                        // asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, urlinput.toString());
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                //builder.setMessage("");
                //Html.fromHtml("Hello "+"<b>"+"World"+"</b>", Html.FROM_HTML_MODE_LEGACY)
               // builder.setMessage("Enter a City or State or Zip Code:");
                builder.setMessage(Html.fromHtml("Enter a City or State or Zip Code:" + "<b>",Html.FROM_HTML_MODE_LEGACY ));
                AlertDialog dialog = builder.create();
                dialog.show();
                break;


        }





        return super.onOptionsItemSelected(item);



    }

    public void executeKYGAsyncTask(String zipcode)
    {
        addtoshow=zipcode;
        KYGAsyncTask asnycTask=new KYGAsyncTask(this);
        asnycTask.execute(zipcode);
    }



    @Override
    public boolean onLongClick(View view) {
        onClick(view);
        return false;
    }

    public RecyclerView getRecyclerView() {
        return rview;
    }


    @Override
    public void onClick(View view) {
        Intent intent= new Intent(this,OfficialActivity.class);
        String heading=loc.getText().toString();
        intent.putExtra("titleForPhoto",heading);
        intent.putExtra("query", qrystr);
        final int position=getRecyclerView().getChildLayoutPosition(view);
        intent.putExtra("KYGObj",newArray.get(position));
        startActivity(intent);
    }
    //Fetches the location using GeoCoder
    public void doLocationWork(double latitude, double longitude) {

        Log.d(TAG, "doLocationWork: Lat: " + latitude + ", Lon: " + longitude);

        List<Address> addresses = null;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            Log.d(TAG, "doLocationWork: Getting address now");
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            StringBuilder sb = new StringBuilder();
            String zipcode=addresses.get(0).getPostalCode();
            Log.d(TAG, "doLocationWork: ZIP " + zipcode);
            addtoshow=addresses.get(0).getLocality()+" ,"+ addresses.get(0).getLocality() + " " + addresses.get(0).getPostalCode();
            asyncTask=new KYGAsyncTask(this);
            asyncTask.execute(zipcode);
            Log.d(TAG, "doLocationWork: textset");
            // return sb.toString();

        } catch (IOException e) {
            Log.d(TAG, "doLocationWork: " + e.getMessage());
            Toast.makeText(this, "Address cannot be acquired from provided latitude/longitude", Toast.LENGTH_SHORT).show();
        }
        // Toast.makeText(this, "GeoCoder service is slow - please wait", Toast.LENGTH_SHORT).show();
    }
    // Toast.makeText(this, "GeoCoder service timed out - please try again", Toast.LENGTH_LONG).show();


    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERM_REQ_CODE) {

            if (grantResults.length == 0) {
                Log.d(TAG, "onRequestPermissionsResult: Empty Grant Results");
                return;
            }

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //User granted permissions!
                Log.d("TAG", "Fine location permission granted");
                loc_ref.setUpLocationManager();
                loc_ref.determineLocation();

            } else {
                //User denied Location permissions. Here you could warn the user that without
                //Location permissions the app is not usable.
                Toast.makeText(this, "Have a great day!", Toast.LENGTH_SHORT).show();
                finish();
            }

            for(int i=0 ; i < permissions.length;i++)
            {
                if(permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        loc_ref.setUpLocationManager();
                        loc_ref.determineLocation();
                        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

                    }
                    else {
                        Toast.makeText(this, "Address cannot be acquired from provided latitude/longitude", Toast.LENGTH_SHORT).show();
                    }
                }
            }


        }
    }

    @Override
    protected void onResume() {

        if (doNetworkkCheck())
        {
            if(loc_ref==null)
            {
                loc_ref=new KYGLocator(this);
            }
        }
        super.onResume();
    }


    private boolean doNetworkkCheck() {
        Boolean isConnect=false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = connectivityManager.getActiveNetworkInfo();
        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            isConnect = true;
        } else {
            isConnect = false;
        }
        return isConnect;
    }
}
