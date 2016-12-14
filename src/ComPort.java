import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;
import jssc.*;

/**
 * Created by apu on 06.10.2016.
 */
public class ComPort {
    public String[] portNames;
    private SerialPort comPort = null;
    private SerialPortEventListener portListener;
    private boolean comPortHasData = false;

    public boolean isComPortDataSent() {
        return comPortDataSent;
    }

    public void setComPortDataSent(boolean comPortDataSent) {
        this.comPortDataSent = comPortDataSent;
    }

    private boolean comPortDataSent = false;
    private StringBuilder receiveString = new StringBuilder();

    public void portFind(){
        portNames = SerialPortList.getPortNames();
    }

    public SerialPort getPort(){
        return comPort;
    }

    public boolean open(String portNumber) throws SerialPortException {
        if(portNumber == null)  return false;
        if(comPort != null)        return false;
        comPort = new SerialPort(portNumber);
        try {
            comPort.openPort();
            comPort.setParams(SerialPort.BAUDRATE_57600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            comPort.setEventsMask(SerialPort.MASK_RXCHAR);
            return true;
        } catch (SerialPortException ex){
            comPort = null;
            throw ex;
        }
    }

    public boolean close() throws SerialPortException {
        if(comPort == null)    return false;
        try {
            if(portListener != null){
                comPort.removeEventListener();
            }
            comPort.closePort();
            comPort = null;
            return true;
        } catch (SerialPortException e) {
            comPort = null;
            portListener = null;
            throw e;
            //return false;
        }
    }

    public boolean write(String data) throws SerialPortException {
            write(data.getBytes());
            return true;
    }

    public boolean write(byte[] data) throws SerialPortException {
            comPort.writeBytes(data);
            return true;
    }

    public boolean writeAndWait(byte[] data) throws SerialPortException {
        setComPortDataSent(false);
        comPort.writeBytes(data);
        return true;
    }

    public byte[] read() throws SerialPortException {
        byte[] readData = comPort.readBytes();
        return readData;
    }

    public boolean waitOk() throws SerialPortException {
        byte[] readBuffer;

        try {
            readBuffer = comPort.readBytes();
            if(readBuffer == null)  return false;
            receiveString.append(new String(readBuffer));
            if(receiveString.length()<2) return false;
            if(receiveString.indexOf("ok") != (-1)) {
                receiveString.delete(0,receiveString.length());
                return true;
            }
            return false;
        } catch (SerialPortException e) {
            receiveString.delete(0,receiveString.length());
            throw e;
        }
    }

    public void comPortEnableListener(int waitBytes) throws SerialPortException {
        portListener = new eventListener(waitBytes);
        setComPortHasData(false);
        try {
            comPort.addEventListener(portListener);
        } catch (SerialPortException e) {
            portListener = null;
            throw e;
        }
    }

    public void comPortDisableListener() throws SerialPortException {
        try {
            comPort.removeEventListener();
        } catch (SerialPortException e) {
            throw e;
        } finally {
            portListener = null;
            setComPortHasData(false);
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
            if( serialPortEvent.isTXEMPTY() ) {
                setComPortDataSent(true);
            }
        }
    }
}
