import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

//IMPORTANT LINKS
// https://open-meteo.com/en/docs


//Giving information to USER by API
public class WeatherApp {
    //fetch weather data for given location
    public static JSONObject getWeatherData(String locationName) {
        //get list of locations with specifed name
        JSONArray locationData = null;
        try {
            locationData = getLocationData(locationName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (locationData == null || locationData.isEmpty()) {
            System.out.println("Error: Location data not found or is empty");
            return null;
        }

        //get first object from list (its almost always what we are looking for)
        //extract latitude and longitude data
        JSONObject location = (JSONObject) locationData.get(0);
        double latitude = (double) location.get("latitude");
        double longitude = (double) location.get("longitude");

        //API request using coordinates
        String urlString = "https://api.open-meteo.com/v1/forecast?" + "latitude=" + latitude + "&longitude=" + longitude + "&hourly=temperature_2m,relative_humidity_2m,weathercode,wind_speed_10m&timezone=auto";

        try {
            //call api and get response
            HttpURLConnection conn = fetchApiResponse(urlString);

            //if response = 200 (OK)
            if (conn.getResponseCode() != 200) {
                System.out.println("Error: Could not connect to API");
                return null;
            }

            //store resulting json data
            StringBuilder resultJson = new StringBuilder();
            Scanner scanner = new Scanner(conn.getInputStream());
            while (scanner.hasNext()) {
                //read and store data into string builder
                resultJson.append(scanner.nextLine());
            }

            //close scanner and url coonection
            scanner.close();
            conn.disconnect();

            //parse data
            JSONParser parser = new JSONParser();
            JSONObject resultJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));

            //retrive hourly data
            JSONObject hourly = (JSONObject) resultJsonObj.get("hourly");

            //take current hour's data
            JSONArray time = (JSONArray) hourly.get("time");
            int index = findIndexOfCurrentTime(time);

            //get temperature using index od hour
            JSONArray temperatureData = (JSONArray) hourly.get("temperature_2m");
            double temperature = (double) temperatureData.get(index);

            //get weather code
            JSONArray weathercode = (JSONArray) hourly.get("weathercode");
            String weatherCondition = convertWeatherCode((long) weathercode.get(index));

            //get humidity
            JSONArray relativeHumidity = (JSONArray) hourly.get("relative_humidity_2m");
            long humidity = (long) relativeHumidity.get(index);

            //get windspeed
            JSONArray windspeedData = (JSONArray) hourly.get("wind_speed_10m");
            double windspeed = (double) windspeedData.get(index);

            //build the eather json data object that we are going to access in our frontend
            JSONObject weatherData = new JSONObject();
            weatherData.put("temperature", temperature);
            weatherData.put("weather_condition", weatherCondition);
            weatherData.put("humidity", humidity);
            weatherData.put("windspeed", windspeed);

            System.out.println(temperature);
            System.out.println(weatherCondition);
            System.out.println(humidity);
            System.out.println(windspeed);

            return weatherData;

        } catch (Exception e) {
            e.printStackTrace();
        }

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

    private static int findIndexOfCurrentTime(JSONArray timeList) {
        String currentTime = getCurrentTime();

        //iterate through the time list and see which one matches our current time
        for (int i = 0; i < timeList.size(); i++)
        {
            String time = (String) timeList.get(i);
            if(time.equalsIgnoreCase(currentTime))
            {
                //return index
                return i;
            }
        }

        return 0;


    }

    public static String getCurrentTime() {
        //get current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();


        // we need data inf format (YYYY-MM-DDTHH:MM)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");

        // format and print the current date and time
        String formattedDateTime = currentDateTime.format(formatter);

        return  formattedDateTime;
    }

    //make weather code more readable
    private static String convertWeatherCode(long weathercode)
    {
//        WMO Weather interpretation codes (WW)
//            Code	Description
//        0	Clear sky
//        1, 2, 3	Mainly clear, partly cloudy, and overcast
//        45, 48	Fog and depositing rime fog
//        51, 53, 55	Drizzle: Light, moderate, and dense intensity
//        56, 57	Freezing Drizzle: Light and dense intensity
//        61, 63, 65	Rain: Slight, moderate and heavy intensity
//        66, 67	Freezing Rain: Light and heavy intensity
//        71, 73, 75	Snow fall: Slight, moderate, and heavy intensity
//        77	Snow grains
//        80, 81, 82	Rain showers: Slight, moderate, and violent
//        85, 86	Snow showers slight and heavy
//        95 *	Thunderstorm: Slight or moderate
//        96, 99 *	Thunderstorm with slight and heavy hail
        String weatherCondition = "";
        if(weathercode == 0L)
        {
            //clear
            weatherCondition = "Clear";
        }
        else if (weathercode > 0L && weathercode <= 3L) {
            //cloudy
            weatherCondition = "Cloudy";
        }
        else if ((weathercode >= 51L && weathercode <= 67L) || (weathercode >= 80L && weathercode <= 99L)) {
            //rain
            weatherCondition = "Rain";
        }
        else if (weathercode >= 71L && weathercode <= 77L) {
            //snow
            weatherCondition = "Snow";
        }

        return weatherCondition;
    }


}


