import jssc.SerialPortException;

import javax.swing.*;
import java.util.List;

/**
 * Created by apu on 04.11.2016.
 */
public class WriteConfiguration extends SwingWorker{
    @Override
    protected Object doInBackground() throws InterruptedException, SerialPortException {

        byte[] data;
        String message = "";
        PacketWr packet;

        try {
            main.port.comPortEnableListener(2);
        } catch (SerialPortException e) {
            publish("Error. " + e.getPortName() + " - " + e.getExceptionType() + "\r\n");
            publish(new String("Write OK"));//to enable write button
            throw e;
        }

        for(int ptr=0;ptr<2;ptr++) {
            switch(ptr){
                case 0:
                        data = settings.getSet().get(main.param.GSM_SERVER).getBytes();
                        message = "Write. Send request. Gsm Server. ";
                        break;
                case 1:
                        data = settings.getSet().get(main.param.GSM_MONEY_QUERY).getBytes();
                        message = "Write. Send request. Gsm Money Query.";
                        break;
                default:
                        data = new byte[]{0};
                        message = "Error.";
            }

            packet = new PacketWrapper().wrap(ptr, ConfigCommand.COMMAND_WRITE, data);
            try {
                main.port.write(packet.data);
                main.port.setComPortHasData(false);
                publish(new String(message + "\r\n"));
                message = "Error. No answer.";
                for(int i=0;i<100;i++) {
                    Thread.sleep(30);
                    if(main.port.isComPortHasData() == true)
                    {
                        if(main.port.waitOk()) {
                            message = "OK";
                            break;
                        }
                    }
                }
                Thread.sleep(200);
                publish(new String(message + "\r\n"));
                if(message.equals("OK") == false)   break;
            } catch (SerialPortException e){
                publish("Error. " + e.getPortName() + " - " + e.getExceptionType() + "\r\n");
                publish(new String("Write OK"));
                throw e;
            } catch (InterruptedException e) {
                publish("Error. Timeout.");
                publish(new String("Write OK"));
                throw e;
            }
        }
        try {
            main.port.comPortDisableListener();
        } catch (SerialPortException e) {
            publish("Error. " + e.getPortName() + " - " + e.getExceptionType() + "\r\n");
            throw e;
        } finally {
            publish(new String("Write OK"));
        }
        return null;
    }

    @Override
    protected void process(List chunks) {
        super.process(chunks);
        String str;
        for(int i=0;i<chunks.size();i++){
            str = chunks.get(i).toString();
            if(str.equals("Write OK")){
                main.gui.buttonWrite.setEnabled(true);
                continue;
            }
            main.gui.textArea.append(str);
        }
    }
}
