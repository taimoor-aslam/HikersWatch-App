package com.example.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.app.LoaderManager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {
    TextView latitudeView,longitudeView,countryView,
    cityView,addressView;
    Button btn;


    LocationManager locationManager;

    LocationListener locationListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latitudeView=(TextView) findViewById(R.id.latitudeView);
        longitudeView=(TextView) findViewById(R.id.longitudeView);
        cityView=(TextView) findViewById(R.id.cityView);
        countryView=(TextView) findViewById(R.id.countryView);
        addressView=(TextView) findViewById(R.id.addressView);
        btn=(Button) findViewById(R.id.button);


         btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 locationManager=(LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                 locationListener=new LocationListener() {
                     @Override
                     public void onLocationChanged(Location location) {
                         longitudeView.setText(String.valueOf(location.getLongitude()));

                         latitudeView.setText(String .valueOf(location.getLatitude()));

                         Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());

                         try {

                             List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                             String address="";

                             if(addressList!=null&&addressList.size()>0)
                             {

                                 if(addressList.get(0).getSubThoroughfare()!=null)
                                 {
                                     address+=addressList.get(0).getSubThoroughfare();
                                 }
                                 if(addressList.get(0).getThoroughfare()!=null)
                                 {
                                     address+=addressList.get(0).getThoroughfare();
                                 }
                                 if(addressList.get(0).getCountryName()!=null)
                                 {

                                     countryView.setText(addressList.get(0).getCountryName());
                                     address+=addressList.get(0).getCountryName();

                                 }

                                 if(addressList.get(0).getLocality()!=null)
                                 {

                                     cityView.setText(addressList.get(0).getLocality());
                                     address+=addressList.get(0).getLocality();
                                 }
                             }
                             if(!address.equals("")){
                                 addressView.setText(address);
                             }
                         }catch (Exception e)
                         {

                             e.printStackTrace();

                         }

                     }

                     @Override
                     public void onStatusChanged(String provider, int status, Bundle extras) {

                     }

                     @Override
                     public void onProviderEnabled(String provider) {

                     }

                     @Override
                     public void onProviderDisabled(String provider) {

                     }
                 };
                 //check permission
                 if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                         != PackageManager.PERMISSION_GRANTED)
                 {

                     //ask for permission
                     ActivityCompat.requestPermissions(MainActivity.this,
                             new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

                 }
                 else
                 {
                     //if permission granted
                     locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

                 }
             }
         });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==1)
        {

            if(grantResults.length>0 &&grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {

                if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                        ==PackageManager.PERMISSION_GRANTED)
                {

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                }
            }
        }
    }
}