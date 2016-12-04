import jssc.SerialPortException;

import javax.swing.*;
import java.io.*;
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
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PacketUnwr pkt = null;
        String[] strs = new String[2];

        //main.port.comPortEnableListener(1);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("log.txt");
            PrintStream prn = new PrintStream(fos);
            System.setOut(prn);
        } catch (FileNotFoundException e) {
            publish("Error. Logging error. " + e.getMessage() + "\r\n");
        }

        while(true) {
            data = null;
            baos.reset();
            pkt = null;
            do {
                data = receiveBytes(PacketUnwrapper.PACKET_HEADER_LENGTH);
                short length = new PacketUnwrapper().packetLength(data);
                if(length == -1){

                }
                baos.write(data);
                data = receiveBytes(length - (data.length - PacketUnwrapper.PACKET_HEADER_LENGTH));
                baos.write(data);
                dataRes = baos.toByteArray();
                for(byte all:dataRes) System.out.println(all);
                pkt = new PacketUnwrapper().unwrap(dataRes);
                strs[0] = "";
                strs[1] = "";
                switch(pkt.getPacketNumber()){
                    case settings.N_GSM_SERVER:
                                                strs[0] = pktParams.GSM_SERVER;
                                                strs[1] = new String(pkt.getPacketData());
                                                break;
                    case settings.N_GSM_MONEY_QUERY:
                                                strs[0] = pktParams.GSM_MONEY_QUERY;
                                                strs[1] = new String(pkt.getPacketData());
                                                break;
                    default:
                                                strs[0] = "";
                                                strs[1] = "";
                                                break;
                }
            }while(pkt == null);
            publish(strs);
            try {
                main.port.write("ok");
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
