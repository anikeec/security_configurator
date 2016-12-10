import java.util.Arrays;

/**
 * Created by Ksusha on 09.12.2016.
 */
public class OuterPacket extends Packet{
    private int magicByte;
    private int addressDst;
    private int packetLength;
    private int headerCrc;
    private int headerLength;
    private int packetCrc;

    public OuterPacket(int magicByte, int addressDst, byte[] data) {
        this.magicByte = magicByte;
        this.addressDst = addressDst;
        packetLength = 0;
        headerCrc = 0;
        headerLength = 0;
        this.data = data;
        packetCrc = 0;
    }

    @Override
    public String toString() {
        return "Packet{" +
                "magicByte=" + magicByte +
                ", addressDst=" + addressDst +
                ", packetLength=" + packetLength +
                ", headerCrc=" + headerCrc +
                ", headerLength=" + headerLength +
                ", data=" + Arrays.toString(data) +
                ", packetCrc=" + packetCrc +
                '}';
    }

    public int getMagicByte() {
        return magicByte;
    }

    public void setMagicByte(int magicByte) {
        this.magicByte = magicByte;
    }

    public int getAddressDst() {
        return addressDst;
    }

    public void setAddressDst(int addressDst) {
        this.addressDst = addressDst;
    }

    public int getPacketLength() {
        return packetLength;
    }

    public void setPacketLength(int packetLength) {
        this.packetLength = packetLength;
    }

    public int getHeaderCrc() {
        return headerCrc;
    }

    public void setHeaderCrc(int headerCrc) {
        this.headerCrc = headerCrc;
    }

    public int getHeaderLength() {
        return headerLength;
    }

    public void setHeaderLength(int headerLength) {
        this.headerLength = headerLength;
    }

    public int getPacketCrc() {
        return packetCrc;
    }

    public void setPacketCrc(int packetCrc) {
        this.packetCrc = packetCrc;
    }
}
