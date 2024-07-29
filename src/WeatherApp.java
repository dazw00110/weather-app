import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

//Giving information to USER by API
public class WeatherApp {
    //fetch weather data for given location
    public static JSONObject getWeatherData(String locationName)
    {
        JSONArray locationData = getLocationData(locationName);

        return null;
    }

    public static JSONArray getLocationData(String locationName) {
        //if location has a space in between like New York we will change it to +
        //like: new york = new+york, this will be helpfull in links
        locationName = locationName.replaceAll(" ","+");

        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name="+locationName+"&count=10&language=en&format=json";
        try {
            HttpURLConnection connection = fetchApiResponse(urlString);

            //check if response code = 200 (OK)
            if(connection.getResponseCode() != 200)
            {
                System.out.println("Error: Could not connect to API");
                return null;
            }
            else
            {
                //store the API results
                StringBuilder resultJSON = new StringBuilder();
                Scanner scanner = new Scanner(connection.getInputStream());
                //add new data as long as there is any information
                while (scanner.hasNext())
                {
                    resultJSON.append(scanner.nextLine());
                }

                //close scanner and url connection
                scanner.close();
                connection.disconnect();

                //parse JSONString to JSONObject
                JSONParser parser = new JSONParser();
                JSONObject resultsJsonObj = (JSONObject) parser.parse(String.valueOf(resultJSON));

                JSONArray locationData = (JSONArray) resultsJsonObj.get("results");
                return locationData;
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        //couldn't find location
        return null;
    }

    private static HttpURLConnection fetchApiResponse(String urlString) {
        //we are trying to create a connection
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            conn.connect();
            return conn;
        } catch (IOException e) {
            e.printStackTrace();
        }

        //if we cant make a connection return null
        return null;
    }
}
