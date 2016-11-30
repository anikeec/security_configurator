import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;
import jssc.*;

import javax.swing.*;
import java.util.EventListener;

/**
 * Created by apu on 06.10.2016.
 */
public class ComPort {
    public String[] portNames;
    private SerialPort port = null;
    private SerialPortEventListener portListener;
    private boolean comPortHasData = false;

    public void init(){
        portNames = SerialPortList.getPortNames();
    }

    public SerialPort getInstance(){
        return port;
    }

    public boolean open(String portNumber) throws SerialPortException {
        if(portNumber == null)  return false;
        if(port != null)        return false;
        port = new SerialPort(portNumber);
        try {
            port.openPort();
            port.setParams(SerialPort.BAUDRATE_57600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            port.setEventsMask(SerialPort.MASK_RXCHAR);
            return true;
        } catch (SerialPortException ex){
            port = null;
            throw ex;
        }
    }

    public boolean close(){
        if(port == null)    return false;
        try {
            //byte[] readBuffer = port.readBytes();
            if(portListener != null){
                port.removeEventListener();
            }
            port.closePort();
            port = null;
            return true;
        } catch (SerialPortException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean write(String data){
        try {
            port.writeBytes(data.getBytes());
            return true;
        } catch (SerialPortException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean write(byte[] data){
        try {
            port.writeBytes(data);
            return true;
        } catch (SerialPortException e) {
            e.printStackTrace();
            return false;
        }
    }

    public byte[] read(){
        byte[] readData = new byte[0];
        try {
            readData = port.readBytes();
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
        return readData;
    }

    public boolean waitOk(){
        byte[] readBuffer;

        try {
            readBuffer = port.readBytes();
            if(readBuffer == null)  return false;
            if(readBuffer.length<2) return false;
            if(new String(readBuffer).equals("ok")) return true;
            else    return false;
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void comPortEnableListener(int waitBytes){
        portListener = new eventListener(waitBytes);
        setComPortHasData(false);
        try {
            port.addEventListener(portListener);
        } catch (SerialPortException e) {
            e.printStackTrace();
            portListener = null;
            return;
        }
    }

    public boolean comPortDisableListener(){
        try {
            port.removeEventListener();
            portListener = null;
            setComPortHasData(false);
            return true;
        } catch (SerialPortException e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean isComPortHasData() {
        return comPortHasData;
    }

    public void setComPortHasData(boolean comPortHasData) {
        this.comPortHasData = comPortHasData;
    }

    private class eventListener implements SerialPortEventListener{

        int waitBytes = 0;

        public eventListener(int waitBytes) {
            this.waitBytes = waitBytes;
        }

        @Override
        public void serialEvent(SerialPortEvent serialPortEvent) {
            if( serialPortEvent.isRXCHAR() && (serialPortEvent.getEventValue() >= waitBytes)){
                setComPortHasData(true);
            }
        }
    }
}
