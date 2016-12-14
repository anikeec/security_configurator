import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

import javax.swing.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created by apu on 04.11.2016.
 */
public class WriteConfiguration extends SwingWorker{
    @Override
    protected Object doInBackground() throws InterruptedException {

        byte[] data;
        String message = "";
        boolean error = false;

        byte[] innerWrappedData;
        byte[] outerWrappedData;
        OuterPacket inputUnwrapOuterPacket;
        InnerPacket inputUnwrapInnerPacket;
        InnerPacket innerPacketToSend;
        OuterPacket outerPacketToSend;
        InnerProtocol packetInnerProtocol = new InnerProtocol(1, 2);
        OuterProtocol packetOuterProtocol = new OuterProtocol(1, 1, 1, 1, 2);
        InnerWrapper packetInnerWrapper = new InnerWrapper(packetInnerProtocol);
        OuterWrapper packetOuterWrapper = new OuterWrapper(packetOuterProtocol);

        JsscByteWriter byteWriter = new JsscByteWriter(main.port.getPort());
        JsscByteReader byteReader = new JsscByteReader(main.port.getPort());


        for(int ptr=0;ptr<2;ptr++) {

            message = "Write. Send request. ";

            //data = settings.readParameterField(ptr);
            data = settings.getSet().get(settings.readParameterName(ptr)).getBytes();
            message +=  settings.readParameterName(ptr);

            try {
                innerPacketToSend = new InnerPacket(ConfigCommand.COMMAND_WRITE, ptr, data);
                innerWrappedData = packetInnerWrapper.wrap(innerPacketToSend);
                outerPacketToSend = new OuterPacket(0x53, 0x00, innerWrappedData);
                outerWrappedData = packetOuterWrapper.wrap(outerPacketToSend);
            } catch (WrapperException e) {
                outerWrappedData = null;
                message = ("Error. WrapPacket - " + e.getMessage());
                error = true;
                break;
            }

            try {
                byteWriter.write(outerWrappedData,200);
            } catch (IOException e) {
                message = ("Error. " + e.getMessage() + "\r\n");
                error = true;
                break;
            } catch (InterruptedException e) {
                message = ("Error. Timeout.");
                error = true;
                break;
            }
            publish(new String(message + "\r\n"));


            byte[] packetHeader = new byte[0];
            int length = -1;
            byte[] packetBody = new byte[0];
            try {
                packetHeader = byteReader.read(packetOuterProtocol.getHeaderLen(),2000);
                length = packetOuterWrapper.unwrapHeaderLength(packetHeader);
                packetBody = byteReader.read(length - packetHeader.length,2000);
            } catch (IOException e) {
                message = ("Error. IOException - " + e.getMessage());
                error = true;
                break;
            } catch (InterruptedException e) {
                message = ("Error. InterruptedException.");
                error = true;
                break;
            } catch (SerialPortTimeoutException e) {
                message = ("Error. SerialPortTimeoutException.");
                error = true;
                break;
            } catch (WrapperException e) {
                message = ("Error. UnwrapHeader - " + e.getMessage());
                error = true;
                break;
            } catch (SerialPortException e) {
                message = ("Error. SerialPortException - " + e.getMessage());
                error = true;
                break;
            }

            ByteBuffer bb = ByteBuffer.allocate(length);
            bb.put(packetHeader);
            bb.put(packetBody);
            byte[] packetFull = bb.array();

            try {
                inputUnwrapOuterPacket = packetOuterWrapper.unwrap(packetFull);
                inputUnwrapInnerPacket = packetInnerWrapper.unwrap(inputUnwrapOuterPacket.getData());
            } catch (WrapperException e) {
                inputUnwrapInnerPacket = null;
                message = ("Error. UnwrapPacket - " + e.getMessage());
                error = true;
                break;
            }

            Thread.sleep(200);
            message = new String(inputUnwrapInnerPacket.getData());
            publish(new String(message + "\r\n"));
        }

        if(error){
            publish(message + "\r\n");
        }

        publish(new String("Write OK"));

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
