import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.List;

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
                if(main.port.getPort().isOpened()) {
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

            main.port.write("ok");
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
