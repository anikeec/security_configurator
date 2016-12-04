import jssc.SerialPortException;

import javax.swing.*;
import java.util.List;

/**
 * Created by apu on 04.11.2016.
 */
public class ReadConfiguration extends SwingWorker{
    @Override
    protected Object doInBackground() throws SerialPortException, InterruptedException {

        byte[] data;
        String message = "";
        PacketWr packet;

        for(int ptr=0;ptr<2;ptr++) {
            switch(ptr){
                case 0:
                        data = settings.getSet().get(pktParams.GSM_SERVER).getBytes();
                        message = "Read. Send request. Gsm Server.";
                        break;
                case 1:
                        data = settings.getSet().get(pktParams.GSM_MONEY_QUERY).getBytes();
                        message = "Read. Send request. Gsm Money Query.";
                        break;
                default:
                        data = new byte[]{0};
                        message = "Error.";
            }

            packet = new PacketWrapper().wrap(ptr, ConfigCommand.COMMAND_READ, data);
            try {
                main.port.write(packet.data);
                publish(new String(message + "\r\n"));
                message = "Error. No answer.";
                for(int i=0;i<100;i++) {
                    Thread.sleep(30);
                    if(main.port.waitOk() == true)
                    {
                        message = "OK";
                        break;
                    }
                }
                Thread.sleep(100);
                publish(new String(message + "\r\n"));
            } catch (SerialPortException e) {
                publish("Error. " + e.getPortName() + " - " + e.getExceptionType() + "\r\n");
                publish(new String("Write OK"));
                throw e;
            } catch (InterruptedException e) {
                publish("Error. Timeout.");
                publish(new String("Write OK"));
                throw e;
            }

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
