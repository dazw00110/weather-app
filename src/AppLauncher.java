import javax.swing.*;

public class AppLauncher {
    public static void main(String[] args) {
        // Ensure GUI updates are run on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //new WeatherAppGui().setVisible(true);

                System.out.println(WeatherApp.getLocationData("Tokyo"));
            }
        });
    }
}
