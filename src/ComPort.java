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
            //byte[] readBuffer = comPort.readBytes();
            if(portListener != null){
                comPort.removeEventListener();
            }
            comPort.closePort();
            comPort = null;
            return true;
        } catch (SerialPortException e) {
            comPort = null;
            portListener = null;
            //e.printStackTrace();
            throw new SerialPortException(e.getPortName(),e.getMethodName(),e.getExceptionType());
            //return false;
        }
    }

    public boolean write(String data){
        try {
            comPort.writeBytes(data.getBytes());
            return true;
        } catch (SerialPortException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean write(byte[] data){
        try {
            comPort.writeBytes(data);
            return true;
        } catch (SerialPortException e) {
            e.printStackTrace();
            return false;
        }
    }

    public byte[] read(){
        byte[] readData = new byte[0];
        try {
            readData = comPort.readBytes();
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
        return readData;
    }

    public boolean waitOk(){
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
            //if(new String(readBuffer).equals("ok")) return true;
            //else    return false;
            return false;
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void comPortEnableListener(int waitBytes){
        portListener = new eventListener(waitBytes);
        setComPortHasData(false);
        try {
            comPort.addEventListener(portListener);
        } catch (SerialPortException e) {
            e.printStackTrace();
            portListener = null;
            return;
        }
    }

    public boolean comPortDisableListener(){
        try {
            comPort.removeEventListener();
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
