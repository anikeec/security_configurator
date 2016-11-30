import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;

/**
 * Created by apu on 04.11.2016.
 * test class for answer on configurator query
 */
public class AnswerConfiguration extends SwingWorker{
    @Override
    protected Object doInBackground() throws Exception {

        byte[] data;
        byte[] dataRes;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PacketUnwr pkt = null;

        //main.port.comPortEnableListener(1);

        FileOutputStream fos = new FileOutputStream("log.txt");
        PrintStream prn = new PrintStream(fos);
        System.setOut(prn);

        while(true) {
            data = null;
            baos.reset();
            pkt = null;
            do {
                /*
                main.port.setComPortHasData(false);
                while(main.port.isComPortHasData() == false) {
                    Thread.sleep(10);
                }*/
                if(main.port.getInstance().isOpened()) {
                    data = main.port.read();
                }
                if(data == null)    continue;
                System.out.println("read");
                if(data != null){
                    baos.write(data);
                }
                if(baos.size() < 8)   continue;
                dataRes = baos.toByteArray();
                for(byte all:dataRes) System.out.println(all);
                pkt = new PacketUnwrapper().unwrap(dataRes);
            }while(pkt == null);
            main.mainF.textArea.append(pkt.toString() +"\r\n");

            main.port.write("ok");
            //Thread.sleep(100);
            main.mainF.textArea.append("OK" + "\r\n");
        }
    }
}
