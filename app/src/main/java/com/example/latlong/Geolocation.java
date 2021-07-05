package com.example.latlong;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import android.os.Handler;



public class Geolocation {
    public static void getAddress(final String locationAddress,final String locationAddress2, final Context context, final Handler handler){
        Thread thread = new Thread(){
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                double result = 0;
                double lat1 = 0;
                double lat2 = 0;
                double long1 = 0;
                double long2 = 0;
                try {

                    List addressList = geocoder.getFromLocationName(locationAddress,1);
                    List addressList2 = geocoder.getFromLocationName(locationAddress2,1);

                    if(addressList != null && addressList.size() > 0 && addressList2 != null && addressList2.size() > 0 ){
                        Address address = (Address) addressList.get(0);
                        Address address2 = (Address) addressList2.get(0);

                        StringBuilder latitude1 = new StringBuilder();
                        StringBuilder longitude1 = new StringBuilder();
                        StringBuilder latitude2 = new StringBuilder();
                        StringBuilder longitude2 = new StringBuilder();

                        latitude1.append(address.getLatitude()).append("\n");
                        longitude1.append(address.getLongitude()).append("\n");
                        latitude2.append(address2.getLatitude()).append("\n");
                        longitude2.append(address2.getLongitude()).append("\n");

                        lat1 =  Double.parseDouble(latitude1.toString());
                        lat2 =  Double.parseDouble(latitude2.toString());
                        long1 =  Double.parseDouble(longitude1.toString());
                        long2 =  Double.parseDouble(longitude2.toString());

                        result = distance(lat1,lat2,long1,long2);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (result != 0 ){
                        message.what = 1;
                        Bundle bundle = new Bundle();

                        bundle.putDouble("address",result);
                        message.setData(bundle);
                    }
                    message.sendToTarget();


                }
            }
        };
        thread.start();
    }
    public static int distance(double lat1,
                                  double lat2, double lon1,
                                  double lon2)
    {
        int Entfernung = 0;
        try {
            String url = "https://graphhopper.com/api/1/matrix?point=" + lat1 + "," + lon1 + "&point=" + lat2 + "," + lon2 + "&type=json&vehicle=foot&debug=true&out_array=weights&out_array=times&out_array=distances&key=f1b84449-c021-49fa-8546-94f4dad7d02b";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            int responseCode = con.getResponseCode();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            String string = response.toString();
            String[] AddressParts = string.split(",");
            String City = AddressParts[1].substring(0, AddressParts[1].length() -1);
             Entfernung = Integer.parseInt(City);





        }
        catch (Exception e){}
        return Entfernung;
    }
}
