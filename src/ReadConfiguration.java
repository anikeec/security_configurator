import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

import javax.swing.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created by apu on 04.11.2016.
 */
public class ReadConfiguration extends SwingWorker{
    @Override
    protected Object doInBackground() throws InterruptedException {

        byte[] data;
        String message = "";
        boolean error = false;
        String[] strs = new String[2];

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

            message = "Read. Send request. ";

            data = Gui.getValueByName(Gui.getNameById(ptr)).getBytes();
            message +=  Gui.getNameById(ptr);

            try {
                innerPacketToSend = new InnerPacket(ConfigCommand.COMMAND_READ, ptr, data);
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
            strs[0] = "";
            strs[1] = "";

            strs[0] = Gui.getNameById(inputUnwrapInnerPacket.getAddress());
            if(strs[0] != "Error.") {
                strs[1] = new String(inputUnwrapInnerPacket.getData());
                publish(strs);
            }

        }

        if(error){
            publish(message + "\r\n");
        }

        publish(new String("Read OK"));

        return null;
    }

    @Override
    protected void process(List chunks) {
        super.process(chunks);
        String str;
        if(chunks.size() >= 2){
            String key = chunks.get(0).toString();
            Object obj = Gui.getGuiByName(key);
            //Object obj = main.gui.getGui().get(key);
            String text = chunks.get(1).toString();
            main.gui.inputSetText((JTextField)obj,text);
            str = chunks.get(2).toString();
            if(str.indexOf("Read OK") != -1){
                main.gui.buttonRead.setEnabled(true);
            } else {
                main.gui.textArea.append(str);
            }
        } else {
            for(int i=0;i<chunks.size();i++){
                str = chunks.get(i).toString();
                main.gui.textArea.append(str);
            }
        }
    }
}
