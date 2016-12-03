import jssc.SerialPortException;

import javax.swing.*;
import java.io.*;
import java.nio.ByteBuffer;
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
                if(baos.size() < 8)   continue;
                dataRes = baos.toByteArray();
                for(byte all:dataRes) System.out.println(all);
                pkt = new PacketUnwrapper().unwrap(dataRes);
            }while(pkt == null);

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

    @Override
    protected void process(List chunks) {
        super.process(chunks);
        for(int i=0;i<chunks.size();i++){
            main.gui.textArea.append(chunks.get(i).toString());
        }
    }
}
