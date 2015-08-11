package com.example.amanahluwalia.sunshine.app;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amanahluwalia.sunshine.app.data.WeatherContract;


public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DETAIL_LOADER = 0;

    View rootView;

    private static final String[] FORECAST_COLUMNS = {
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_PRESSURE,
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.WeatherEntry.COLUMN_DEGREES,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID
    };

    private static final int COL_WEATHER_ID = 0;
    private static final int COL_WEATHER_DATE = 1;
    private static final int COL_WEATHER_DESC = 2;
    private static final int COL_WEATHER_MAX_TEMP = 3;
    private static final int COL_WEATHER_MIN_TEMP = 4;
    private static final int COL_WEATHER_HUMIDITY = 5;
    private static final int COL_WEATHER_PRESSURE = 6;
    private static final int COL_WEATHER_WIND_SPEED = 7;
    private static final int COL_WEATHER_DEGREES = 8;
    private static final int COL_WEATHER_CONDITION_ID = 9;

    public TextView dateWeekView;
    public TextView dateView;
    public TextView highView;
    public TextView lowView;
    public ImageView iconView;
    public TextView forecastView;
    public TextView humidityView;
    public TextView pressureView;
    public TextView windView;

    private ShareActionProvider mShareActionProvider;

    private static final String FORECAST_SHARE_HASHTAG = "#SunshineApp";
    private static String mForecastStr;
    private static final String LOG_TAG = DetailFragment.class.getSimpleName();

    static final String DETAIL_URI = "URI";
    private Uri mUri;


    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mForecastStr + FORECAST_SHARE_HASHTAG);
        return shareIntent;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate menu resource file.
        inflater.inflate(R.menu.detailfragment, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.action_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        if(mShareActionProvider != null){
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }

        // Return true to display menu
        //return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if(arguments != null) {
            mUri = arguments.getParcelable(DetailFragment.DETAIL_URI);
        }

        rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        dateWeekView = (TextView) rootView.findViewById(R.id.dateWeek_textview);
        dateView = (TextView) rootView.findViewById(R.id.date_textview);
        highView = (TextView) rootView.findViewById(R.id.high_textview);
        lowView = (TextView) rootView.findViewById(R.id.low_textview);
        iconView = (ImageView) rootView.findViewById(R.id.icon_view);
        forecastView = (TextView) rootView.findViewById(R.id.forecast_textview);
        humidityView = (TextView) rootView.findViewById(R.id.humidity_textview);
        pressureView = (TextView) rootView.findViewById(R.id.pressure_textview);
        windView = (TextView) rootView.findViewById(R.id.wind_textview);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(null != mUri) {
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    FORECAST_COLUMNS,
                    null,
                    null,
                    null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(!data.moveToFirst())
            return;

       /* String dateString = Utility.formatDate(data.getLong(COL_WEATHER_DATE));
        String weatherDescription = data.getString(COL_WEATHER_DESC);
        Boolean isMetric = Utility.isMetric(getActivity());
        String high = Utility.formatTemperature(getActivity(),COL_WEATHER_MAX_TEMP, isMetric);
        String low = Utility.formatTemperature(getActivity(), COL_WEATHER_MIN_TEMP, isMetric);

        mForecastStr = String.format("%s - %s - %s/%s", dateString, weatherDescription, high, low);

        TextView deatailTextView = (TextView)getView().findViewById(R.id.detail_text);
        deatailTextView.setText(mForecastStr); */

        int weatherId = data.getInt(COL_WEATHER_CONDITION_ID);

        boolean isMetric = Utility.isMetric(getActivity());

        dateWeekView.setText(Utility.getDayName(getActivity(), data.getLong(COL_WEATHER_DATE)));
        dateView.setText(Utility.getFormattedMonthDay(getActivity(), data.getLong(COL_WEATHER_DATE)));
        highView.setText(Utility.formatTemperature(getActivity(), data.getDouble(COL_WEATHER_MAX_TEMP), isMetric));
        lowView.setText(Utility.formatTemperature(getActivity(), data.getDouble(COL_WEATHER_MIN_TEMP), isMetric));
        iconView.setImageResource(Utility.getArtResourceForWeatherCondition(weatherId));
        forecastView.setText(data.getString(COL_WEATHER_DESC));
        humidityView.setText("Humidity: " + data.getString(COL_WEATHER_HUMIDITY));
        pressureView.setText("Pressure: " + data.getString(COL_WEATHER_PRESSURE));
        windView.setText(Utility.getFormattedWind(getActivity(), COL_WEATHER_WIND_SPEED, COL_WEATHER_DEGREES));

        // If onCreateOptionsMenu has already happened, we need to update the share intent now.
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }

    }

    void onLocationChanged( String newLocation ) {
        // replace the uri, since the location has changed
        Uri uri = mUri;
        if (null != uri) {
            long date = WeatherContract.WeatherEntry.getDateFromUri(uri);
            Uri updatedUri = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(newLocation, date);
            mUri = updatedUri;
            getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


}
