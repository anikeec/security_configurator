import jssc.SerialPortException;

import javax.swing.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by apu on 04.11.2016.
 * test class for answer on configurator query
 */
public class AnswerConfiguration extends SwingWorker{
    @Override
    protected Object doInBackground() throws IOException, SerialPortException {

        byte[] data;
        byte[] dataRes;
        byte[] packetAnswer = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String[] strs = new String[2];
        String message = "";
        boolean error = false;

        InnerPacket innerPacketToSend;
        OuterPacket outerPacketToSend;
        InnerPacket inputUnwrapInnerPacket = null;
        OuterPacket inputUnwrapOuterPacket = null;
        InnerProtocol packetInnerProtocol = new InnerProtocol(1, 2);
        OuterProtocol packetOuterProtocol = new OuterProtocol(1, 1, 1, 1, 2);
        InnerWrapper packetInnerWrapper = new InnerWrapper(packetInnerProtocol);
        OuterWrapper packetOuterWrapper = new OuterWrapper(packetOuterProtocol);


        FileInputStream fis = null;
        FileOutputStream fos = null;
        byte[] tempBuffer = null;
        synchronized (this.getClass()) {
            try {
                fis = new FileInputStream("log.txt");
                if (fis.available() != 0) {
                    tempBuffer = new byte[fis.available()];
                    fis.read(tempBuffer);
                }
                if (fis != null) fis.close();

                fos = new FileOutputStream("log.txt");
                PrintStream prn = new PrintStream(fos);
                System.setOut(prn);
            } catch (FileNotFoundException e) {
            }

            if (tempBuffer != null) {
                System.out.println(tempBuffer);
            }
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        System.out.println(dateFormat.format( new Date() ));

        while(true) {
            data = null;
            baos.reset();
            inputUnwrapInnerPacket = null;
            do {
                System.out.println("start receive header");
                data = receiveBytes(packetOuterProtocol.getHeaderLen());
                int length = -1;
                try {
                    length = packetOuterWrapper.unwrapHeaderLength(data);
                } catch (WrapperException e) {
                    System.out.println("Error. UnwrapHeader. " + e.getMessage());
                }

                if(length == -1) {
                    data = null;
                    baos.reset();
                    inputUnwrapInnerPacket = null;
                    continue;
                }
                baos.write(data);
                System.out.println("received " + data.length + " bytes");
                System.out.println("receive other " + (length  - data.length) + " bytes");

                data = receiveBytes(length  - data.length);
                baos.write(data);
                dataRes = baos.toByteArray();
                for(byte all:dataRes) System.out.println(all);

                inputUnwrapOuterPacket = null;
                inputUnwrapInnerPacket = null;
                try {
                    inputUnwrapOuterPacket = packetOuterWrapper.unwrap(dataRes);
                    inputUnwrapInnerPacket = packetInnerWrapper.unwrap(inputUnwrapOuterPacket.getData());
                } catch (WrapperException e) {
                    inputUnwrapOuterPacket = null;
                    inputUnwrapInnerPacket = null;
                    System.out.println("Error. UnwrapPacket. " + e.getMessage());
                    break;
                }
                System.out.println("unwrap packet.");

                strs[0] = "";
                strs[1] = "";
                packetAnswer = new byte[]{'o','k'};

                strs[0] = Elements.getNameById(inputUnwrapInnerPacket.getAddress());
                if(strs[0] != "Error.") {
                    strs[1] = new String(inputUnwrapInnerPacket.getData());
                    packetAnswer = Elements.getValueByName(strs[0]).getBytes();
                }

            } while(inputUnwrapInnerPacket == null);

            int address = inputUnwrapInnerPacket.getAddress();
            int command = inputUnwrapInnerPacket.getCommand();

            if(command == ConfigCommand.COMMAND_WRITE) {
                publish(strs);
            }
            System.out.println("publish");
            while(main.port.isComPortHasData() == true){
                receiveBytes(1);
            }

            byte[] innerWrappedData = new byte[0];
            innerPacketToSend = new InnerPacket(ConfigCommand.COMMAND_ANSWER, address, packetAnswer);
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
            } catch (SerialPortException e) {
                publish("Error. " + e.getMessage() + "\r\n");
                throw e;
            }
            //Thread.sleep(100);
            publish(new String("OK.\r\n"));
        }
    }

    private byte[] receiveBytes(int length) throws SerialPortException, IOException {
        byte[] data;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        data = null;
        baos.reset();
        do {
            if(main.port.getPort().isOpened()) {
                try {
                    data = main.port.read();
                } catch (SerialPortException e) {
                    publish("Error. " + e.getPortName() + " - " + e.getExceptionType() + "\r\n");
                    throw e;
                }
            }
            if(data == null)    continue;
            System.out.println("read");
            if(data != null){
                try {
                    baos.write(data);
                } catch (IOException e) {
                    publish("Error. " + e.getMessage() + "\r\n");
                    throw e;
                }
            }
        } while(baos.size() < length);
        return baos.toByteArray();
    }

    @Override
    protected void process(List chunks) {
        super.process(chunks);
        if(chunks.size() == 3){
            String key = chunks.get(0).toString();
            Object obj = main.gui.getGui().get(key);
            String text = chunks.get(1).toString();
            main.gui.inputSetText((JTextField)obj,text);
            main.gui.textArea.append(chunks.get(2).toString());
        } else {
            for (int i = 0; i < chunks.size(); i++) {
                main.gui.textArea.append(chunks.get(i).toString());
            }
        }
    }
}
