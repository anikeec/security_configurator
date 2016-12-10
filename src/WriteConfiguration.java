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

        InnerPacket innerPacketToSend;
        InnerProtocol packetInnerProtocol = new InnerProtocol(1, 2);
        InnerWrapper packetInnerWrapper = new InnerWrapper(packetInnerProtocol);

        OuterPacket outerPacketToSend;
        OuterProtocol packetOuterProtocol = new OuterProtocol(1, 1, 1, 1, 2);
        OuterWrapper packetOuterWrapper = new OuterWrapper(packetOuterProtocol);

        try {
            main.port.comPortEnableListener(2);
        } catch (SerialPortException e) {
            publish("Error. " + e.getPortName() + " - " + e.getExceptionType() + "\r\n");
            publish(new String("Write OK"));//to enable write button
            throw e;
        }

        for(int ptr=0;ptr<2;ptr++) {
            message = "Write. Send request. ";
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
            innerPacketToSend = new InnerPacket(ConfigCommand.COMMAND_WRITE, ptr, data);
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
                main.port.write(outerWrappedData);//packet.data
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
