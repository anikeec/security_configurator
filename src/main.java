/**
 * Created by apu on 02.11.2016.
 */

public class main{

    public static ComPort port;
    public static GuiFrame    gui;

    public static void main(String[] args) {
        new main();
        gui = new GuiFrame();
    }

    public main() {
        port = new ComPort();
        port.portFind();
    }

}
