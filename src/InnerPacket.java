import java.util.Arrays;

/**
 * Created by Ksusha on 09.12.2016.
 */
public class InnerPacket extends Packet{
    private int command;
    private int address;

    public InnerPacket(int command, int address, byte[] data) {
        this.command = command;
        this.address = address;
        this.data = data;
    }

    @Override
    public String toString() {
        return "Packet{" +
                "command=" + command +
                ", address=" + address +
                ", data=" + Arrays.toString(data) +
                '}';
    }

    public int getCommand() {
        return command;
    }

    public void setCommand(int command) {
        this.command = command;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

}
