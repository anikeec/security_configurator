import javax.swing.*;
import java.util.List;

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
                        data = settings.getSet().get(main.param.GSM_SERVER).getBytes();
                        message = "Gsm Server write done.";
                        break;
                case 1:
                        data = settings.getSet().get(main.param.GSM_MONEY_QUERY).getBytes();
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
                    publish(new String("OK.\r\n"));
                    break;

                } else {
                    publish(new String("NOK.\r\n"));
                }
            }

            Thread.sleep(100);
            publish(new String(message + "\r\n"));
        }
        return null;
    }

    @Override
    protected void process(List chunks) {
        super.process(chunks);
        for(int i=0;i<chunks.size();i++){
            main.gui.textArea.append(chunks.get(i).toString());
        }
    }
}
