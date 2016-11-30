/**
 * Created by apu on 04.11.2016.
 */
public class PacketUnwr {
    private short    packetCommand;
    private short    packetNumber;
    private short    packetLength;
    private short    packetCrc16;
    private byte[]   packetData;

    public PacketUnwr() {
        packetCommand = 0;
        packetNumber = 0;
        packetLength = 0;
        packetCrc16 = 0;
        packetData = null;
    }

    public short getPacketCommand(){
        return packetCommand;
    }

    public short getPacketNumber(){
        return packetNumber;
    }

    public short getPacketLength(){
        return packetLength;
    }

    public short getPacketCrc16(){
        return packetCrc16;
    }

    public byte[] getPacketData() {
        return packetData;
    }

    public void setPacketCommand(short packetCommand) {
        this.packetCommand = packetCommand;
    }

    public void setPacketNumber(short packetNumber) {
        this.packetNumber = packetNumber;
    }

    public void setPacketLength(short packetLength) {
        this.packetLength = packetLength;
    }

    public void setPacketCrc16(short packetCrc16) {
        this.packetCrc16 = packetCrc16;
    }

    public void setPacketData(byte[] packetData) {
        this.packetData = packetData;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("packet: ");
        sb.append("com =" + getPacketCommand() + "; ");
        sb.append("num =" + getPacketNumber() + "; ");
        sb.append("len =" + getPacketLength() + "; ");
        for(byte all: getPacketData())  sb.append(all + " ");
        sb.append(";");
        sb.append("Crc16 =" + getPacketCrc16());
        return sb.toString();
    }
}
