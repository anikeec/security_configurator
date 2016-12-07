/**
 * Created by apu on 04.11.2016.
 */
public class PacketUnwr {
    private byte     packetMagicByte;
    private byte     packetAddress;
    private byte     packetLength;
    private byte     packetCrc8;
    private short    packetCommand;
    private short    packetNumber;
    private short    packetCrc16;
    private byte[]   packetData;

    public PacketUnwr() {
        packetMagicByte = 0;
        packetAddress = 0;
        packetLength = 0;
        packetCrc8 = 0;
        packetCommand = 0;
        packetNumber = 0;
        packetCrc16 = 0;
        packetData = null;
    }

    public byte getPacketMagicByte() {
        return packetMagicByte;
    }

    public byte getPacketAddress() {
        return packetAddress;
    }

    public byte getPacketCrc8() {
        return packetCrc8;
    }

    public short getPacketCommand(){
        return packetCommand;
    }

    public short getPacketNumber(){
        return packetNumber;
    }

    public byte getPacketLength(){
        return packetLength;
    }

    public short getPacketCrc16(){
        return packetCrc16;
    }

    public byte[] getPacketData() {
        return packetData;
    }

    public void setPacketMagicByte(byte packetMagicByte) {
        this.packetMagicByte = packetMagicByte;
    }

    public void setPacketAddress(byte packetAddress) {
        this.packetAddress = packetAddress;
    }

    public void setPacketCrc8(byte packetCrc8) {
        this.packetCrc8 = packetCrc8;
    }

    public void setPacketCommand(short packetCommand) {
        this.packetCommand = packetCommand;
    }

    public void setPacketNumber(short packetNumber) {
        this.packetNumber = packetNumber;
    }

    public void setPacketLength(byte packetLength) {
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
