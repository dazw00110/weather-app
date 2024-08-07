import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

//GUI
public class WeatherAppGui extends JFrame{
    private JSONObject weatherData;

    public WeatherAppGui()
    {
        super("Weather App"); //title
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(450,650); //size
        setLocationRelativeTo(null); //centered position of screen
        setLayout(null); //Manual component layout
        setResizable(false); //resizable off

        addGuiComponents();
    }

    private void addGuiComponents() {
        JTextField searchTextField = new JTextField(); //search field
        searchTextField.setBounds(15,15,351,45);

        searchTextField.setFont(new Font("Dialog",Font.PLAIN,24));

        add(searchTextField);

        //set background
        JLabel weatherConditionImage = new JLabel(loadImage("src/images/cloudy.png"));
        weatherConditionImage.setBounds(0,125,450,217);
        add(weatherConditionImage);

        //creating and centering temperature text
        JLabel temperatureText = new JLabel("10 C");
        temperatureText.setBounds(0,350,450,54);
        temperatureText.setFont(new Font("Dialog",Font.BOLD,48));
        temperatureText.setHorizontalAlignment(SwingConstants.CENTER);
        add(temperatureText);

        //weather description
        JLabel weatherConditionDesc = new JLabel("Cloudy");
        weatherConditionDesc.setBounds(0,405,450,36);
        weatherConditionDesc.setFont(new Font("Dialog",Font.PLAIN,32));
        weatherConditionDesc.setHorizontalAlignment(SwingConstants.CENTER);
        add(weatherConditionDesc);

        //huminidity image
        JLabel humidityImage = new JLabel(loadImage("src/images/humidity.png"));
        humidityImage.setBounds(15,500,74,66);
        add(humidityImage);

        //humidity Text
        JLabel humidityText = new JLabel(("<html><b>Humidity</b> 100%</html>"));
        humidityText.setBounds(90,500,85,55);
        humidityText.setFont(new Font("Dialog",Font.PLAIN,16));
        add(humidityText);

        //windspeed image
        JLabel windspeedImage = new JLabel(loadImage("src/images/windspeed.png"));
        windspeedImage.setBounds(220,500,74,66);
        add(windspeedImage);

        //windspeed text
        JLabel windspeedText = new JLabel("<html><b>Windspeed</b> 15km/h</html>");
        windspeedText.setBounds(310,500,85,55);
        windspeedText.setFont(new Font("Dialog",Font.PLAIN,16));
        add(windspeedText);

        JButton searchButton = new JButton(loadImage("src/images/search.png"));

        //change cursor on search button
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(375,13,47,45);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //get location from user
                String userInput = searchTextField.getText();

                //validate input - remove whitespace
                if(userInput.replaceAll("\\s","").length() <=0)
                {
                    return;
                }

                //retrieve weather data
                weatherData = WeatherApp.getWeatherData(userInput);


                //update weather image
                String weatherCondition = (String) weatherData.get("weather_condition");

                //update weather image depending on weather condition
                switch (weatherCondition)
                {
                    case "Clear":
                        weatherConditionImage.setIcon(loadImage("src/images/clear.png"));
                        break;
                    case "Cloudy":
                        weatherConditionImage.setIcon(loadImage("src/images/cloudy.png"));
                        break;
                    case "Rain":
                        weatherConditionImage.setIcon(loadImage("src/images/rain.png"));
                        break;
                    case "Snow":
                        weatherConditionImage.setIcon(loadImage("src/images/snow.png"));
                        break;
                }

                //update temperature text;
                double temperature = ((Number) weatherData.get("temperature")).doubleValue();
                temperatureText.setText(temperature + " C");

                weatherConditionDesc.setText(weatherCondition);

                //update humidity text
                long humidity = ((Number) weatherData.get("humidity")).longValue();
                humidityText.setText("<html><b>Humidity</b> " + humidity + "%</html>");

                //update windspeed text
                double windspeed = ((Number) weatherData.get("windspeed")).doubleValue();
                windspeedText.setText("<html><b>Windspeed</b> " + windspeed + "km/h</html>");


            }
        });
        add(searchButton);
    }

    private ImageIcon loadImage(String resourcePath)
    {
        try {
            BufferedImage image = ImageIO.read(new File(resourcePath));

            return new ImageIcon(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Could not find resources");
        return null;
    }


}
