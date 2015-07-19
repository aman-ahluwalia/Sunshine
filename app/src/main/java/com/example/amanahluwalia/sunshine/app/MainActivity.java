package com.example.amanahluwalia.sunshine.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private final String FORECASTFRAGMENT_TAG = "FFTAG";

    private String mLocation;


    @Override
    protected void onStart() {
        Log.v(LOG_TAG, "inside onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.v(LOG_TAG, "inside onResume");
        super.onResume();
        String newLoc = Utility.getPreferredLocation(this);
        if(newLoc != null && !newLoc.equals(mLocation)) {
            ForecastFragment ff = (ForecastFragment)getSupportFragmentManager().findFragmentByTag(FORECASTFRAGMENT_TAG);
            if(null != ff) {
                ff.onLocationChanged();
            }
            mLocation = newLoc;
        }
    }

    @Override
    protected void onPause() {
        Log.v(LOG_TAG, "inside onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.v(LOG_TAG, "inside onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.v(LOG_TAG, "inside onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, " inside onCreate");
        setContentView(R.layout.activity_main);
        mLocation = Utility.getPreferredLocation(this);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment(), FORECASTFRAGMENT_TAG)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void showMap(String location) {
        //Uri loc = Uri.parse(location);

        // Using the URI scheme for showing a location found on a map.  This super-handy
        // intent can is detailed in the "Common Intents" page of Android's developer site:
        // http://developer.android.com/guide/components/intents-common.html#Maps
        Uri geoLocation = Uri.parse("geo:0,0?").buildUpon()
                            .appendQueryParameter("q", location)
                            .build();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d(LOG_TAG,"Couldn't call " + location + ", no apps installed!");
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Ha   ndle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.show_map) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String location = prefs.getString(getString(R.string.pref_location_key),
                    getString(R.string.pref_location_default));
            showMap(location);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */

}
