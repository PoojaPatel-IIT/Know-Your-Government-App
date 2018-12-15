package com.example.pooja.knowyourgov;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by pooja on 19,October,2018
 */
public class KYGAsyncTask extends AsyncTask<String,Void,String>{

private MainActivity ma;
    private ArrayList<KYGDetails> offArrayList;
    private Object[] finaldata;
    String title;
String civicURL = "https://www.googleapis.com/civicinfo/v2/representatives";
String zipFOrURL;
String googleAPIKEY = "AIzaSyAp5MFOoWOeh3U1awj85qfiNLxp9K0uP-o";
    private static final String TAG = "KYGAsyncTask";

    public KYGAsyncTask(MainActivity mainActivity) {

        ma = mainActivity;
    }


    @Override
    protected String doInBackground(String... strings) {

        zipFOrURL = strings[0];
        Uri.Builder buildURL = Uri.parse(civicURL).buildUpon();
        buildURL.appendQueryParameter("key", googleAPIKEY);
        buildURL.appendQueryParameter("address", strings[0]);
        String urltouse = buildURL.build().toString();
        Log.d(TAG, "doInBackground: urlFinal ----- FOR SEARCH "+urltouse);
        StringBuilder stringbuilder = new StringBuilder();
        try {
            URL url = new URL(urltouse);
            Log.d(TAG, "doInBackground: " + urltouse);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStream inputsteam = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputsteam));

            String line;
            while ((line = reader.readLine()) != null) {
                stringbuilder.append(line).append("\n");
            }

        }
        catch(FileNotFoundException e)
        {
            
            e.printStackTrace();
            Log.d(TAG, "doInBackground: Exception for file");
            return "";
        }
        catch(Exception e) {

            e.printStackTrace();
            return null;
        }
        return stringbuilder.toString();



    }





    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, "onPostExecute: ");
        super.onPostExecute(s);
        if(fetchKYGData(s)) {
            finaldata = new Object[2];
            finaldata[0] = title;
            finaldata[1] = offArrayList;
            ma.settingOffList(finaldata);
        }
        else
            ma.settingOffList(null);
        Log.d(TAG, "onPostExecute: " + s);

    }

    private String puttitle(String city, String state, String zip) {
        String cit = city;
        if(!cit.equals("") && !state.equals("")) {
            cit += ", " + state + " " + zip;
        }
        else {
            cit += state + " " + zip;
        }
        return cit;
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    private boolean fetchKYGData(String s) {
        if (s == null) {
            Toast.makeText(ma, "Civil Info Service is Unavailable", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(s.equals(""))
        {
            Toast.makeText(ma, "No Data is Available", Toast.LENGTH_SHORT).show();
            return false;
        }
        try {

            JSONObject jsonObject = new JSONObject(s);

            JSONObject normalizedInput = (JSONObject) jsonObject.getJSONObject("normalizedInput");
            String city = normalizedInput.getString("city");
            String state = normalizedInput.getString("state");
            String zip = normalizedInput.getString("zip");
            //
             title=puttitle(city, state, zip);

            //Json array offices and get its attribute name and officialindicesarray
            JSONArray officearray = (JSONArray) jsonObject.getJSONArray("offices");
            JSONArray OfficialsArray = jsonObject.getJSONArray("officials");
            offArrayList= new ArrayList<>();
            if (officearray.length() == 0) {
                //no data available for the entered locationspot
                Toast.makeText(ma, "No Data is Available for the Specified Location", Toast.LENGTH_LONG).show();
            }
            for (int k = 0; k < officearray.length(); k++) {
                JSONObject jsonObject1 = (JSONObject) officearray.get(k);
                String office_name = jsonObject1.getString("name");
                JSONArray officilasarray = (JSONArray) jsonObject1.getJSONArray("officialIndices");
                if (officilasarray.length() == 0) {
                    Toast.makeText(ma, "No data found for " + city + " officials", Toast.LENGTH_SHORT).show();
                }
                for (int l = 0; l < officilasarray.length(); l++) {
                    int index = officilasarray.getInt(l);
                    //JSONArray officials_array=(JSONArray)jsonObject.getJSONArray("officials");
                    JSONObject offObj = (JSONObject) OfficialsArray.get(index);
                    String person_name = offObj.getString("name");
                    // setting party as unknown so that if they are not in party then to keep it as Unknown
                    String unknown = "Unknown";
                    String party = unknown;
                    if (offObj.has("party"))
                        party = offObj.getString("party");



                    String jointaddress = "";
                    if (offObj.has("address"))
                    {
                        JSONArray address_array = (JSONArray) offObj.getJSONArray("address");
                        for (int m = 0; m < address_array.length(); m++) {
                            JSONObject jsonForAddress = (JSONObject) address_array.get(m);
                            if (jsonForAddress.has("line1"))
                                jointaddress = jointaddress + jsonForAddress.get("line1") + ",";
                            if (jsonForAddress.has("line2"))
                                jointaddress = jointaddress + jsonForAddress.get("line2") + ",";
                            if (jsonForAddress.has("line3"))
                                jointaddress = jointaddress + jsonForAddress.get("line3") + ",";
                            if (jsonForAddress.has("city"))
                                jointaddress = jointaddress + jsonForAddress.get("city") + ",";
                            if (jsonForAddress.has("state"))
                                jointaddress = jointaddress + jsonForAddress.get("state") + " ";
                            if (jsonForAddress.has("zip"))
                                jointaddress = jointaddress + jsonForAddress.get("zip") + "";
                            break;
                        }

                    }

                    // setting the phone , website and email to NO Data Provided in beginning
                    String phone = "No Data Provided";
                    String website = "No Data Provided";
                    String urlemail = "No Data Provided";
                    String photoLink = "";

// getting phone of the offical
                    if (offObj.has("phones")) {
                        JSONArray offPhone = (JSONArray) offObj.getJSONArray("phones");
                        Log.d(TAG, "fetchKYGData: "+offPhone);
                        phone = offPhone.getString(0);
                    }
// getting url/website of the offical
                    if (offObj.has("urls")) {
                        JSONArray web = offObj.getJSONArray("urls");
                        Log.d(TAG, "fetchKYGData: "+web);
                        website = web.getString(0);
                    }

// getting email of the offical
                    if (offObj.has("emails")) {
                        JSONArray emailOff = offObj.getJSONArray("emails");
                        Log.d(TAG, "fetchKYGData: "+emailOff);
                        urlemail = emailOff.getString(0);
                    }

// getting photourl of the offical
                    if (offObj.has("photoUrl")) {
                        photoLink = offObj.getString("photoUrl");
                    }

                    String goog = "No Data Provided";
                    String fb = "No Data Provided";
                    String twit = "No Data Provided";
                    String youtube = "No Data Provided";
// getting channels of the offical
                    if(offObj.has("channels"))
                    {
                        JSONArray channel_json = offObj.getJSONArray("channels");
                        for (int o = 0; o < channel_json.length(); o++) {
                            JSONObject channel_object = (JSONObject) channel_json.get(o);
                            // googleplus
                            if (channel_object.has("type") && (channel_object.getString("type").equals("GooglePlus")))
                                goog = channel_object.getString("id");
                            // facebook
                            else if (channel_object.has("type") && (channel_object.getString("type").equals("Facebook")))
                                fb = channel_object.getString("id");
                            // twitter
                            else if (channel_object.has("type") && (channel_object.getString("type").equals("Twitter")))
                                twit = channel_object.getString("id");
                            // youtube
                            else if (channel_object.has("type") && (channel_object.getString("type").equals("YouTube")))
                                youtube = channel_object.getString("id");
                        }
                    }


                    // now getting all the details of the offcials and storing in a list by using setters in KYGDetails class
                    KYGDetails offObj1= new KYGDetails();
                    offObj1.setOffParty(party);
                    offObj1.setOffAddress(jointaddress);
                    offObj1.setOffContact(phone);
                    offObj1.setOffwebsite(website);
                    offObj1.setOffCity(city);
                    offObj1.setOffState(state);
                    offObj1.setOffZip(zip);
                    offObj1.setOffname(person_name);
                    offObj1.setOfficename(office_name);
                    offObj1.setEmail(urlemail);
                    offObj1.setPhotoUrl(photoLink);
                    offObj1.setGoogle(goog);
                    offObj1.setFacebook(fb);
                    offObj1.setTwitter(twit);
                    offObj1.setYoutube(youtube);

                   // add all the above in the following list
                    offArrayList.add(offObj1);


                    Log.d(TAG, "fetchKYGData: OffObj "+offObj1);
                    Log.d(TAG, "fetchKYGData: "+offArrayList);
                }
            }
        }
        catch (Exception e) {

            e.printStackTrace();
            return false;

        }
        return true;
    }



}


