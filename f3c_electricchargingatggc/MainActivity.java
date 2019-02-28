package com.example.gp189.f3c_electricchargingatggc;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.TestLooperManager;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import static android.content.ContentValues.TAG;
import static com.example.gp189.f3c_electricchargingatggc.MainActivity.F_3_C;
import static com.example.gp189.f3c_electricchargingatggc.MainActivity.adapter;
import static com.example.gp189.f3c_electricchargingatggc.MainActivity.myLocations;
import static com.example.gp189.f3c_electricchargingatggc.MainActivity.titles;


public  class MainActivity extends AppCompatActivity {
    public static final String F_3_C = "F3C";
    FloatingActionButton getResults;
    String[] timeStamps;
    public static String[] availableChargers;
    public static String [] myLocations;
    public static String [] titles;
    public static String [] descriptions;
    private ListView myResults;
    public static CustomListAdapter adapter;
    TextToSpeech mySpeech;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.About){
            Toast.makeText(this, "Created by Jozeif Walker in ITEC 4550 on March 13,2018 ", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getResults = findViewById(R.id.getResultsBtn);
        timeStamps = new ArrayList<String>().toArray(new String[4]);
        availableChargers = new ArrayList<String>().toArray(new String[4]);
        myLocations = new ArrayList<String>().toArray(new String[4]);
        titles = new ArrayList<String>().toArray(new String[4]);
        descriptions = new ArrayList<String>().toArray(new String[4]);
        descriptions[0] = "in the Student housing parking lot.";
        descriptions[1] = "When you enter the main deck parking garage, drive up the center lane to the second floor and follow it right. It is on the right hand side near the end of the aisle.";
        descriptions[2] = "In front of the faculty offices.  The building is located across from the day care.";
        descriptions[3] = "In Building B lot against the tree line in the back of the lot.";


        adapter = new CustomListAdapter(this, timeStamps, availableChargers, myLocations, titles);
        myResults = findViewById(R.id.list);
        myResults.setAdapter(adapter);

        mySpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                mySpeech.setLanguage(Locale.US);
                mySpeech.setSpeechRate(-10235);

            }
        });
        myResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                for (int k = 0; k < availableChargers.length; k++) {
                    String toSpeak = ("There are " + availableChargers[i] + " chargers." + "These are located  " + descriptions[i]);
                    Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
                    mySpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);


                }

            }
        });


        getResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new RestOperation().execute();
                new RestOperation1().execute();
                new RestOperation2().execute();
                new RestOperation2.RestOperation3().execute();
                new RestOperation2.RestOperation3().execute();




            }


        });


    }


    private class RestOperation extends AsyncTask<String[],Void,ArrayList<String>> {


        @Override
        protected ArrayList<String> doInBackground(String[]...strings) {
            String[] chargerSites;
            HttpURLConnection connect1 = null;
            HttpURLConnection connect2 = null;
            HttpURLConnection connect3 = null;
            HttpURLConnection connect4 = null;
            BufferedReader bufferedReader;
            String s = "";
            StringBuilder jsonString;
            StringBuilder jsonString2;
            StringBuilder jsonString3;
            StringBuilder jsonString4;


            Scanner scanner = null;
            Scanner scanner2 = null;
            Scanner scanner3 = null;
            Scanner scanner4 = null;

            URL urlOne = null;
            URL urlTwo = null;
            URL urlThree = null;
            URL urlFour = null;
            chargerSites = new String[4];
            chargerSites[0] = "https://mc.chargepoint.com/map-prod/get?{\"station_list\":{\"ne_lat\":33.9820,\"ne_lon\":-84.0031,\"sw_lat\":33.9811,\"sw_lon\":-84.0048}}";
            chargerSites[1] = "https://mc.chargepoint.com/map-prod/get?{\"station_list\":{\"ne_lat\":33.9805,\"ne_lon\":-84.0063,\"sw_lat\":33.9795,\"sw_lon\":-84.0080}}";
            chargerSites[2] = "https://mc.chargepoint.com/map-prod/get?{\"station_list\":{\"ne_lat\":33.9819,\"ne_lon\":-83.9991,\"sw_lat\":33.9809,\"sw_lon\":-84.0008}}";
            chargerSites[3] = "https://mc.chargepoint.com/map-prod/get?{\"station_list\":{\"ne_lat\":33.9779,\"ne_lon\":-84.0018,\"sw_lat\":33.9770,\"sw_lon\":-84.0035}}";
            ArrayList<String> timeStamp = new ArrayList<>();


            try {
                urlOne = new URL(chargerSites[0]);
                urlTwo = new URL(chargerSites[1]);
                urlThree = new URL(chargerSites[2]);
                urlFour = new URL(chargerSites[3]);

                connect1 = (HttpURLConnection) urlOne.openConnection();
                connect2 = (HttpURLConnection) urlTwo.openConnection();
                connect3 = (HttpURLConnection) urlThree.openConnection();
                connect4 = (HttpURLConnection) urlFour.openConnection();

                jsonString = new StringBuilder();
                jsonString2 = new StringBuilder();
                jsonString3 = new StringBuilder();
                jsonString4 = new StringBuilder();


                InputStream in = new BufferedInputStream(connect1.getInputStream());
                InputStream in2 = new BufferedInputStream(connect2.getInputStream());
                InputStream in3 = new BufferedInputStream(connect3.getInputStream());
                InputStream in4 = new BufferedInputStream(connect4.getInputStream());


                scanner = new Scanner(in);
                scanner2 = new Scanner(in2);
                scanner3 = new Scanner(in3);
                scanner4 = new Scanner(in4);

                while (scanner.hasNext()) {
                    jsonString.append(scanner.nextLine());
                    jsonString.toString();
                }
                while (scanner2.hasNext()) {
                    jsonString2.append(scanner2.nextLine());
                    jsonString2.toString();
                }
                while (scanner3.hasNext()) {
                    jsonString3.append(scanner3.nextLine());
                    jsonString3.toString();
                }
                while (scanner4.hasNext()) {
                    jsonString4.append(scanner4.nextLine());
                    jsonString4.toString();
                }
                JSONObject obj = new JSONObject(jsonString.toString());
                JSONObject obj2 = new JSONObject(jsonString2.toString());
                JSONObject obj3 = new JSONObject(jsonString3.toString());
                JSONObject obj4 = new JSONObject(jsonString4.toString());


                String Bbuilding = obj.getJSONObject("station_list").getString("time");
                String ResLife = obj2.getJSONObject("station_list").getString("time");
                String ParkingDeck = obj3.getJSONObject("station_list").getString("time");
                String IBuilding = obj4.getJSONObject("station_list").getString("time");


              timeStamp.add(Bbuilding);
              timeStamp.add(ResLife);
              timeStamp.add(ParkingDeck);
              timeStamp.add(IBuilding);

              Collections.sort(timeStamp,Collections.<String>reverseOrder());



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (scanner != null) {
                    scanner.close();
                }
                if (connect1 != null) {
                    connect1.disconnect();
                }
            }

            return timeStamp;


        }


        @Override
        protected void onPostExecute(ArrayList<String> string) {
            super.onPostExecute(string);
            String s = "Result Timestamps " + string;
            Log.e(F_3_C, s);

            for (int i = 0; i<string.size();i++){
                timeStamps[i] = string.get(i).toString();
            }

            //adapter.addAll(timeStamps);
            adapter.notifyDataSetChanged();


        }

    }

    public static class RestOperation1 extends AsyncTask<String[], String, ArrayList<String>> implements com.example.gp189.f3c_electricchargingatggc.RestOperation1 {


        @Override
        protected ArrayList<String> doInBackground(String[]...strings) {
            String[] chargerSites;
            HttpURLConnection connect1 = null;
            HttpURLConnection connect2 = null;
            HttpURLConnection connect3 = null;
            HttpURLConnection connect4 = null;
            BufferedReader bufferedReader;
            String s = "";
            StringBuilder jsonString;
            StringBuilder jsonString2;
            StringBuilder jsonString3;
            StringBuilder jsonString4;

            StringBuilder jsonStringExample;

            Scanner scanner = null;
            Scanner scanner2 = null;
            Scanner scanner3 = null;
            Scanner scanner4 = null;

            URL urlOne = null;
            URL urlTwo = null;
            URL urlThree = null;
            URL urlFour = null;
            chargerSites = new String[4];
            chargerSites[0] = "https://mc.chargepoint.com/map-prod/get?{\"station_list\":{\"ne_lat\":33.9820,\"ne_lon\":-84.0031,\"sw_lat\":33.9811,\"sw_lon\":-84.0048}}";
            chargerSites[1] = "https://mc.chargepoint.com/map-prod/get?{\"station_list\":{\"ne_lat\":33.9805,\"ne_lon\":-84.0063,\"sw_lat\":33.9795,\"sw_lon\":-84.0080}}";
            chargerSites[2] = "https://mc.chargepoint.com/map-prod/get?{\"station_list\":{\"ne_lat\":33.9819,\"ne_lon\":-83.9991,\"sw_lat\":33.9809,\"sw_lon\":-84.0008}}";
            chargerSites[3] = "https://mc.chargepoint.com/map-prod/get?{\"station_list\":{\"ne_lat\":33.9779,\"ne_lon\":-84.0018,\"sw_lat\":33.9770,\"sw_lon\":-84.0035}}";
            ArrayList<String> availability = new ArrayList();

            try {
                urlOne = new URL(chargerSites[0]);

                urlTwo = new URL(chargerSites[1]);

                urlThree = new URL(chargerSites[2]);

                urlFour = new URL(chargerSites[3]);


                connect1 = (HttpURLConnection) urlOne.openConnection();


                connect2 = (HttpURLConnection) urlTwo.openConnection();

                connect3 = (HttpURLConnection) urlThree.openConnection();

                connect4 = (HttpURLConnection) urlFour.openConnection();


                jsonString = new StringBuilder();
                jsonString2 = new StringBuilder();
                jsonString3 = new StringBuilder();
                jsonString4 = new StringBuilder();


                InputStream in = null;
                in = new BufferedInputStream(connect1.getInputStream());

                InputStream in2 = null;
                in2 = new BufferedInputStream(connect2.getInputStream());

                InputStream in3 = null;
                in3 = new BufferedInputStream(connect3.getInputStream());

                InputStream in4 = null;
                in4 = new BufferedInputStream(connect4.getInputStream());


                scanner = new Scanner(in);
                scanner2 = new Scanner(in2);
                scanner3 = new Scanner(in3);
                scanner4 = new Scanner(in4);

                while (scanner.hasNext()) {
                    jsonString.append(scanner.nextLine());
                    jsonString.toString();
                }
                while (scanner2.hasNext()) {
                    jsonString2.append(scanner2.nextLine());
                    jsonString2.toString();
                }
                while (scanner3.hasNext()) {
                    jsonString3.append(scanner3.nextLine());
                    jsonString3.toString();
                }
                while (scanner4.hasNext()) {
                    jsonString4.append(scanner4.nextLine());
                    jsonString4.toString();
                }

                JSONObject obj = new JSONObject(jsonString.toString());

                int Bbuilding = (int)obj.getJSONObject("station_list").getJSONArray("summaries").getJSONObject(0).getJSONObject("port_count").getInt("available");
                String stringBbuilding = Integer.toString(Bbuilding);

                JSONObject obj2 = new JSONObject(jsonString2.toString());

                int ResLife = (int)obj2.getJSONObject("station_list").getJSONArray("summaries").getJSONObject(0).getJSONObject("port_count").getInt("available");
                String stringResLife = Integer.toString(ResLife);

                JSONObject obj3 = new JSONObject(jsonString3.toString());

                int parking = (int)obj3.getJSONObject("station_list").getJSONArray("summaries").getJSONObject(0).getJSONObject("port_count").getInt("available");
                String stringParking = Integer.toString(parking);


                JSONObject obj4 = new JSONObject(jsonString4.toString());

                int iBuilding =obj4.getJSONObject("station_list").getJSONArray("summaries").getJSONObject(0).getJSONObject("port_count").getInt("available");
                 String stringiBuilding = Integer.toString(iBuilding);


                 availability.add(stringBbuilding + " available");
                 availability.add(stringResLife + " available");
                 availability.add(stringParking + " available");
                 availability.add(stringiBuilding + " available");

                 System.out.println(availability);
                 Collections.sort(availability,Collections.<String>reverseOrder());


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return availability;
        }

        @Override
        protected void onPostExecute(ArrayList<String> stringstrings) {
            super.onPostExecute(stringstrings);
            Log.e(F_3_C,"Available " + stringstrings.toString());

            for (int i = 0; i<stringstrings.size();i++) {
                availableChargers[i] = stringstrings.get(i);
            }
            adapter.notifyDataSetChanged();

            }
        }



    }
  class RestOperation2 extends AsyncTask<String[], String, ArrayList<String>> {

      @Override
      protected ArrayList<String> doInBackground(String[]... strings) {
          String[] chargerSites;
          HttpURLConnection connect1 = null;
          HttpURLConnection connect2 = null;
          HttpURLConnection connect3 = null;
          HttpURLConnection connect4 = null;
          BufferedReader bufferedReader;
          String s = "";
          StringBuilder jsonString;
          StringBuilder jsonString2;
          StringBuilder jsonString3;
          StringBuilder jsonString4;

          StringBuilder jsonStringExample;

          Scanner scanner = null;
          Scanner scanner2 = null;
          Scanner scanner3 = null;
          Scanner scanner4 = null;

          URL urlOne = null;
          URL urlTwo = null;
          URL urlThree = null;
          URL urlFour = null;
          chargerSites = new String[4];
          chargerSites[0] = "https://mc.chargepoint.com/map-prod/get?{\"station_list\":{\"ne_lat\":33.9820,\"ne_lon\":-84.0031,\"sw_lat\":33.9811,\"sw_lon\":-84.0048}}";
          chargerSites[1] = "https://mc.chargepoint.com/map-prod/get?{\"station_list\":{\"ne_lat\":33.9805,\"ne_lon\":-84.0063,\"sw_lat\":33.9795,\"sw_lon\":-84.0080}}";
          chargerSites[2] = "https://mc.chargepoint.com/map-prod/get?{\"station_list\":{\"ne_lat\":33.9819,\"ne_lon\":-83.9991,\"sw_lat\":33.9809,\"sw_lon\":-84.0008}}";
          chargerSites[3] = "https://mc.chargepoint.com/map-prod/get?{\"station_list\":{\"ne_lat\":33.9779,\"ne_lon\":-84.0018,\"sw_lat\":33.9770,\"sw_lon\":-84.0035}}";
          ArrayList<String> myLocations = new ArrayList<>();

          try {
              urlOne = new URL(chargerSites[0]);

              urlTwo = new URL(chargerSites[1]);

              urlThree = new URL(chargerSites[2]);

              urlFour = new URL(chargerSites[3]);


              connect1 = (HttpURLConnection) urlOne.openConnection();


              connect2 = (HttpURLConnection) urlTwo.openConnection();

              connect3 = (HttpURLConnection) urlThree.openConnection();

              connect4 = (HttpURLConnection) urlFour.openConnection();


              jsonString = new StringBuilder();
              jsonString2 = new StringBuilder();
              jsonString3 = new StringBuilder();
              jsonString4 = new StringBuilder();


              InputStream in = null;
              in = new BufferedInputStream(connect1.getInputStream());

              InputStream in2 = null;
              in2 = new BufferedInputStream(connect2.getInputStream());

              InputStream in3 = null;
              in3 = new BufferedInputStream(connect3.getInputStream());

              InputStream in4 = null;
              in4 = new BufferedInputStream(connect4.getInputStream());


              scanner = new Scanner(in);
              scanner2 = new Scanner(in2);
              scanner3 = new Scanner(in3);
              scanner4 = new Scanner(in4);

              while (scanner.hasNext()) {
                  jsonString.append(scanner.nextLine());
                  jsonString.toString();
              }
              while (scanner2.hasNext()) {
                  jsonString2.append(scanner2.nextLine());
                  jsonString2.toString();
              }
              while (scanner3.hasNext()) {
                  jsonString3.append(scanner3.nextLine());
                  jsonString3.toString();
              }
              while (scanner4.hasNext()) {
                  jsonString4.append(scanner4.nextLine());
                  jsonString4.toString();
              }

              JSONObject obj = new JSONObject(jsonString.toString());
              JSONObject obj2 = new JSONObject(jsonString2.toString());
              JSONObject obj3 = new JSONObject(jsonString3.toString());
              JSONObject obj4 = new JSONObject(jsonString4.toString());

              String locatB = obj.getJSONObject("station_list").getJSONArray("summaries").getJSONObject(0).getJSONArray("station_name").getString(1);
              String locatRes = obj2.getJSONObject("station_list").getJSONArray("summaries").getJSONObject(0).getJSONArray("station_name").getString(1);
              String Parking = obj3.getJSONObject("station_list").getJSONArray("summaries").getJSONObject(0).getJSONArray("station_name").getString(1);
              String I = obj4.getJSONObject("station_list").getJSONArray("summaries").getJSONObject(0).getJSONArray("station_name").getString(1);
              String locatGGC = obj.getJSONObject("station_list").getJSONArray("summaries").getJSONObject(0).getJSONArray("station_name").getString(0);

              myLocations.add(locatGGC + "/" + locatB);
              myLocations.add(locatGGC + "/" + locatRes);
              myLocations.add(locatGGC + "/" + Parking);
              myLocations.add(locatGGC + "/" + I);

              Collections.sort(myLocations,Collections.<String>reverseOrder());


          } catch (IOException e) {
              e.printStackTrace();
          } catch (JSONException e) {
              e.printStackTrace();
          }


          return myLocations;
      }

      @Override
      protected void onPostExecute(ArrayList<String> strings) {
          super.onPostExecute(strings);
          for (int i = 0; i < strings.size(); i++) {

              myLocations[i] = strings.get(i);
          }
          adapter.notifyDataSetChanged();


      }

      static class RestOperation3 extends AsyncTask<String[], String, ArrayList<String>> {

          @Override
          protected ArrayList<String> doInBackground(String[]... strings) {
              String[] chargerSites;
              HttpURLConnection connect1 = null;
              HttpURLConnection connect2 = null;
              HttpURLConnection connect3 = null;
              HttpURLConnection connect4 = null;
              BufferedReader bufferedReader;
              String s = "";
              StringBuilder jsonString;
              StringBuilder jsonString2;
              StringBuilder jsonString3;
              StringBuilder jsonString4;

              StringBuilder jsonStringExample;

              Scanner scanner = null;
              Scanner scanner2 = null;
              Scanner scanner3 = null;
              Scanner scanner4 = null;

              URL urlOne = null;
              URL urlTwo = null;
              URL urlThree = null;
              URL urlFour = null;
              chargerSites = new String[4];
              chargerSites[0] = "https://mc.chargepoint.com/map-prod/get?{\"station_list\":{\"ne_lat\":33.9820,\"ne_lon\":-84.0031,\"sw_lat\":33.9811,\"sw_lon\":-84.0048}}";
              chargerSites[1] = "https://mc.chargepoint.com/map-prod/get?{\"station_list\":{\"ne_lat\":33.9805,\"ne_lon\":-84.0063,\"sw_lat\":33.9795,\"sw_lon\":-84.0080}}";
              chargerSites[2] = "https://mc.chargepoint.com/map-prod/get?{\"station_list\":{\"ne_lat\":33.9819,\"ne_lon\":-83.9991,\"sw_lat\":33.9809,\"sw_lon\":-84.0008}}";
              chargerSites[3] = "https://mc.chargepoint.com/map-prod/get?{\"station_list\":{\"ne_lat\":33.9779,\"ne_lon\":-84.0018,\"sw_lat\":33.9770,\"sw_lon\":-84.0035}}";
              ArrayList<String> myLocations = new ArrayList();

              try {
                  urlOne = new URL(chargerSites[0]);

                  urlTwo = new URL(chargerSites[1]);

                  urlThree = new URL(chargerSites[2]);

                  urlFour = new URL(chargerSites[3]);


                  connect1 = (HttpURLConnection) urlOne.openConnection();


                  connect2 = (HttpURLConnection) urlTwo.openConnection();

                  connect3 = (HttpURLConnection) urlThree.openConnection();

                  connect4 = (HttpURLConnection) urlFour.openConnection();


                  jsonString = new StringBuilder();
                  jsonString2 = new StringBuilder();
                  jsonString3 = new StringBuilder();
                  jsonString4 = new StringBuilder();


                  InputStream in = null;
                  in = new BufferedInputStream(connect1.getInputStream());

                  InputStream in2 = null;
                  in2 = new BufferedInputStream(connect2.getInputStream());

                  InputStream in3 = null;
                  in3 = new BufferedInputStream(connect3.getInputStream());

                  InputStream in4 = null;
                  in4 = new BufferedInputStream(connect4.getInputStream());


                  scanner = new Scanner(in);
                  scanner2 = new Scanner(in2);
                  scanner3 = new Scanner(in3);
                  scanner4 = new Scanner(in4);

                  while (scanner.hasNext()) {
                      jsonString.append(scanner.nextLine());
                      jsonString.toString();
                  }
                  while (scanner2.hasNext()) {
                      jsonString2.append(scanner2.nextLine());
                      jsonString2.toString();
                  }
                  while (scanner3.hasNext()) {
                      jsonString3.append(scanner3.nextLine());
                      jsonString3.toString();
                  }
                  while (scanner4.hasNext()) {
                      jsonString4.append(scanner4.nextLine());
                      jsonString4.toString();
                  }

                  JSONObject obj = new JSONObject(jsonString.toString());
                  JSONObject obj2 = new JSONObject(jsonString2.toString());
                  JSONObject obj3 = new JSONObject(jsonString3.toString());
                  JSONObject obj4 = new JSONObject(jsonString4.toString());

                  String locatB = obj.getJSONObject("station_list").getJSONArray("summaries").getJSONObject(0).getJSONArray("station_name").getString(1);
                  String locatRes = obj2.getJSONObject("station_list").getJSONArray("summaries").getJSONObject(0).getJSONArray("station_name").getString(1);
                  String Parking = obj3.getJSONObject("station_list").getJSONArray("summaries").getJSONObject(0).getJSONArray("station_name").getString(1);
                  String I = obj4.getJSONObject("station_list").getJSONArray("summaries").getJSONObject(0).getJSONArray("station_name").getString(1);
                  String locatGGC = obj.getJSONObject("station_list").getJSONArray("summaries").getJSONObject(0).getJSONArray("station_name").getString(0);

                  myLocations.add( locatB);
                  myLocations.add( locatRes);
                  myLocations.add( Parking);
                  myLocations.add(I);

                  Collections.sort(myLocations,Collections.<String>reverseOrder());


              } catch (IOException e) {
                  e.printStackTrace();
              } catch (JSONException e) {
                  e.printStackTrace();
              }


              return myLocations;
          }

          @Override
          protected void onPostExecute(ArrayList<String> strings) {
              super.onPostExecute(strings);
              for (int i = 0; i < strings.size(); i++) {

                  titles[i] = strings.get(i);
              }
              adapter.notifyDataSetChanged();
          }
      }

  }




