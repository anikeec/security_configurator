import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

import java.io.IOException;

/**
 * Created by Ksusha on 11.12.2016.
 */
public class JsscByteReader implements ByteReader {

    private final SerialPort serialPort;

    public JsscByteReader(SerialPort serialPort) {
        this.serialPort = serialPort;
    }

    @Override
    public void cancelRead() throws IOException {
        try {
            serialPort.purgePort(SerialPort.PURGE_RXABORT);
            serialPort.purgePort(SerialPort.PURGE_RXCLEAR);
        } catch (SerialPortException e) {
            throw new IOException(e);
        }
    }

    @Override
    public byte[] read(int bytesCount) throws IOException {
        try {
            byte[] readBytes = serialPort.readBytes(bytesCount);
            return readBytes;
        } catch (SerialPortException e) {
            throw new IOException(e);
        }
    }

    @Override
    public byte[] read() throws IOException {
        try {
            byte[] readBytes = serialPort.readBytes();
            return readBytes;
        } catch (SerialPortException e) {
            throw new IOException(e);
        }
    }

    @Override
    public byte[] read(int bytesCount, long timeout) throws IOException, InterruptedException, SerialPortException, SerialPortTimeoutException {
        byte[] readBytes;
        if (timeout <= 0) {
            readBytes = this.read(bytesCount);
        }
        else {
            readBytes = serialPort.readBytes(bytesCount,(int)timeout);
        }
        return readBytes;
    }
/*
    @Override
    public byte read(long timeout) throws IOException, InterruptedException {
        if (timeout <= 0) {
            byte readByte = this.read();
            return readByte;
        }
        else {
            return readBytes[0];
        }
    }*/
}
