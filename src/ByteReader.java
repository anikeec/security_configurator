import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

import java.io.IOException;
/**
 * Created by Ksusha on 11.12.2016.
 */
public interface ByteReader {

        byte[] read(int bytesCount) throws IOException;

        byte[] read() throws IOException;

        byte[] read(int bytesCount, long timeout) throws IOException, InterruptedException, SerialPortException, SerialPortTimeoutException;

        //byte read(long timeout) throws IOException, InterruptedException;

        void cancelRead() throws IOException;

}
