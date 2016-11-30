/**
 * Created by apu on 02.11.2016.
 */

public class main {

    public static ComPort port;
    public static MainFrame mainF;
    public static Configuration config;

    public static void main(String[] args) {

        port = new ComPort();
        port.init();

        mainF = new MainFrame();
        mainF.start();
    }
}
