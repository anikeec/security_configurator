import jssc.SerialPortException;

import javax.swing.*;

/**
 * Created by apu on 02.11.2016.
 */

public class main{

    public static enum  param { GSM_SERVER, GSM_MONEY_QUERY, GPRS_SERVER, GPRS_PORT, GPRS_PAGE, GPRS_TIMEOUT, GPRS_ON_OFF };

    public static ComPort port;
    public static GuiFrame    gui;

    public static void main(String[] args) {
        new main();
        gui = new GuiFrame();
    }

    public main() {
        port = new ComPort();
        port.init();
    }

}
