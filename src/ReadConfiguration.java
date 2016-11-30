import javax.swing.*;

/**
 * Created by apu on 04.11.2016.
 */
public class ReadConfiguration extends SwingWorker{
    @Override
    protected Object doInBackground() throws Exception {

        byte[] data;
        String message = "";
        PacketWr packet;
        SwingWorker worker;

        for(int ptr=0;ptr<2;ptr++) {
            switch(ptr){
                case 0:
                        data = main.config.getGsmServer().getBytes();
                        message = "Gsm Server write done.";
                        break;
                case 1:
                        data = main.config.getGsmMoneyQuery().getBytes();
                        message = "Gsm Money Query write done.";
                        break;
                default:
                        data = new byte[]{0};
                        message = "Error.";
            }

            packet = new PacketWrapper().wrap(ptr, ConfigCommand.COMMAND_READ, data);
            main.port.write(packet.data);
            for(int i=0;i<100;i++) {
                Thread.sleep(300);
                if(main.port.waitOk() == true)
                {
                    main.mainF.textArea.append("OK.\r\n");
                    break;

                } else {

                    //main.mainF.textArea.append("NOK.\r\n");
                }
            }

            Thread.sleep(100);
            main.mainF.textArea.append(message + "\r\n");
        }
        return null;
    }
}
