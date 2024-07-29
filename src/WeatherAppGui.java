import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WeatherAppGui extends JFrame{
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

        JButton searchButton = new JButton(loadImage("src/images/search.png"));

        //change cursor on search button
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(375,13,47,45);
        add(searchButton);

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
        JLabel windseepdText = new JLabel("<html><b>Windspeed</b> 15km/h</html>");
        windseepdText.setBounds(310,500,85,55);
        windseepdText.setFont(new Font("Dialog",Font.PLAIN,16));
        add(windseepdText);

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
