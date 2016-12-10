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

        InnerPacket innerPacketToSend;
        InnerProtocol packetInnerProtocol = new InnerProtocol(1, 2);
        InnerWrapper packetInnerWrapper = new InnerWrapper(packetInnerProtocol);

        OuterPacket outerPacketToSend;
        OuterProtocol packetOuterProtocol = new OuterProtocol(1, 1, 1, 1, 2);
        OuterWrapper packetOuterWrapper = new OuterWrapper(packetOuterProtocol);

        for(int ptr=0;ptr<2;ptr++) {
            message = "Read. Send request. ";
            switch(ptr){
                case settings.N_GSM_SERVER:
                        data = settings.getSet().get(pktParams.GSM_SERVER).getBytes();
                        message += pktParams.GSM_SERVER;
                        break;
                case settings.N_GSM_MONEY_QUERY:
                        data = settings.getSet().get(pktParams.GSM_MONEY_QUERY).getBytes();
                        message += pktParams.GSM_MONEY_QUERY;
                        break;
                default:
                        data = new byte[]{0};
                        message = "Error.";
            }

            byte[] innerWrappedData = new byte[0];
            innerPacketToSend = new InnerPacket(ConfigCommand.COMMAND_READ, ptr, data);
            try {
                innerWrappedData = packetInnerWrapper.wrap(innerPacketToSend);
            } catch (WrapperException e) {
                e.printStackTrace();
            }

            byte[] outerWrappedData = new byte[0];
            outerPacketToSend = new OuterPacket(0x53, 0x00, innerWrappedData);
            try {
                outerWrappedData = packetOuterWrapper.wrap(outerPacketToSend);
            } catch (WrapperException e) {
                e.printStackTrace();
            }

            try {
                main.port.write(outerWrappedData);
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
