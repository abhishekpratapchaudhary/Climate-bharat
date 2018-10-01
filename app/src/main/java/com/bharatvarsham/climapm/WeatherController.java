package com.bharatvarsham.climapm;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class WeatherController extends AppCompatActivity {

    // Constants:
    final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather";

    final String APP_ID = "e72ca729af228beabd5d20e3b7749713";

    final long MIN_TIME = 5000;
    // Distance between location updates (1000m or 1km)
    final float MIN_DISTANCE = 1000;

    final String LOGCAT_TAG="Aryan";





    TextView mCityLabel;
    ImageView mWeatherImage;
    TextView mTemperatureLabel;
    LocationManager locationManager;
    LocationListener locationListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_controller_layout);


        mCityLabel = (TextView) findViewById(R.id.locationTV);
        mWeatherImage = (ImageView) findViewById(R.id.weatherSymbolIV);
        mTemperatureLabel = (TextView) findViewById(R.id.tempTV);
        ImageButton changeCityButton = (ImageButton)findViewById(R.id.changeCityButton);


        changeCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(WeatherController.this,ChangeCityController.class);

                startActivity(intent);
            }
        });

    }


    // TODO: Add onResume() here:

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent=getIntent();
        String city=intent.getStringExtra("City");
        getRequestForCurrentLocation();

        if(city!=null)
        {
          getWeatherForNewCity(city);
        }
        else
        {
            getRequestForCurrentLocation();
        }


    }


    private void getWeatherForNewCity(String city)
    {

        RequestParams params=new RequestParams();

        params.put("q",city);
        params.put("appid",APP_ID);
        letsDoSomeNetworking(params);

    }


    private void getRequestForCurrentLocation() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                Log.i("ResultLocation:", "LocationChanged");
                String longitude=String.valueOf(location.getLongitude());
                String latitude=String.valueOf( location.getLatitude());

                RequestParams params=new RequestParams();

                params.put("lat", latitude);
                params.put("lon", longitude);
                params.put("appid", APP_ID);
                letsDoSomeNetworking(params);

            }


            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.i("LocationChange", "method called()");
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1 );
            return;
        }
        else
            {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, locationListener);
                /*Location location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                String latitude=String.valueOf(location.getLatitude());
                String longitude=String.valueOf(location.getLongitude());

                RequestParams params=new RequestParams();
                params.put("lat",latitude);
                params.put("long",longitude);
                params.put("appid",APP_ID);
                letsDoSomeNetworking(params);*/
            }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if(requestCode==1 && grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
    {
        getRequestForCurrentLocation();


    }

    }






private void letsDoSomeNetworking(RequestParams param)
{
    AsyncHttpClient client = new AsyncHttpClient();

    //client.get(WEATHER_URL,param,new JsonHttpResponseHandler(){
client.get(WEATHER_URL, param, new JsonHttpResponseHandler(){

        @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

            WeatherDataModel weatherDataModel=WeatherDataModel.fromJson(response);

            updateUI(weatherDataModel);

            Log.i("Bharat Mata", "Success! JSON: " + response.toString());
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


            Log.i("Failed Request",throwable.getMessage());
        }


    });

}



    private void updateUI(WeatherDataModel weatherDataModel)
    {
     mCityLabel.setText(weatherDataModel.getmCity());
     mTemperatureLabel.setText(weatherDataModel.getmTemperature());

     int resourceid= getResources().getIdentifier(weatherDataModel.getmIconName(),
             "drawable",getPackageName());

     mWeatherImage.setImageResource(resourceid);


    }

    @Override
    protected void onPause() {
        super.onPause();
        if(locationManager!=null)

        {
          locationManager.removeUpdates(locationListener);
        }
    }





}
