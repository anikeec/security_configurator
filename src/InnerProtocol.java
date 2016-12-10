/**
 * Created by Ksusha on 09.12.2016.
 */
public class InnerProtocol extends Protocol{
    private int commandLen;
    private int addressLen;

    public InnerProtocol() {
        this.commandLen = 1;
        this.addressLen = 2;
    }

    public InnerProtocol(int commandLen, int addressLen) {
        this.commandLen = commandLen;
        this.addressLen = addressLen;
    }

    public int getCommandLen() {
        return commandLen;
    }

    public void setCommandLen(int commandLen) {
        this.commandLen = commandLen;
    }

    public int getAddressLen() {
        return addressLen;
    }

    public void setAddressLen(int addressLen) {
        this.addressLen = addressLen;
    }
}
