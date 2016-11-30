import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by apu on 04.11.2016.
 */
public class PacketUnwrapper {

    private final int PACKET_PTR_COMMAND = 0;
    private final int PACKET_PTR_COMMAND_LEN = 2;
    private final int PACKET_PTR_NUMBER = 2;
    private final int PACKET_PTR_NUMBER_LEN = 2;
    private final int PACKET_PTR_LENGTH = 4;
    private final int PACKET_PTR_LENGTH_LEN = 2;
    private final int PACKET_PTR_DATA = 6;
    private final int PACKET_PTR_CRC16_LEN = 2;

    private short    packetCommand;
    private short    packetNumber;
    private short    packetLength;
    private short    packetCrc16;
    private byte[]   packetData;

    public PacketUnwr unwrap(byte[] src){

        byte[] bytesTemp = new byte[2];

        if(src.length < (PACKET_PTR_COMMAND_LEN + PACKET_PTR_LENGTH_LEN)) return null;

        for(int i=0;i<PACKET_PTR_COMMAND_LEN;i++)   bytesTemp[i] = src[PACKET_PTR_COMMAND + i];
        packetCommand = bytesToShort(bytesTemp);

        for(int i=0;i<PACKET_PTR_NUMBER_LEN;i++)   bytesTemp[i] = src[PACKET_PTR_NUMBER + i];
        packetNumber = bytesToShort(bytesTemp);

        for(int i=0;i<PACKET_PTR_LENGTH_LEN;i++)   bytesTemp[i] = src[PACKET_PTR_LENGTH + i];
        packetLength = bytesToShort(bytesTemp);

        if(src.length < (PACKET_PTR_COMMAND_LEN + PACKET_PTR_NUMBER_LEN + PACKET_PTR_LENGTH_LEN + packetLength)) return null;

        packetData = new byte[packetLength - PACKET_PTR_CRC16_LEN];
        for(int i=0;i<(packetLength - PACKET_PTR_CRC16_LEN);i++)   packetData[i] = src[PACKET_PTR_DATA + i];

        for(int i=0;i<PACKET_PTR_CRC16_LEN;i++){
            bytesTemp[i] = src[PACKET_PTR_DATA + packetLength - PACKET_PTR_CRC16_LEN + i];
        }
        packetCrc16 = bytesToShort(bytesTemp);

        if(packetCrc16 != (short)Crc16.countCrc16(packetData))  return null;

        PacketUnwr res = new PacketUnwr();

        res.setPacketCommand(packetCommand);
        res.setPacketNumber(packetNumber);
        res.setPacketLength(packetLength);
        res.setPacketData(packetData);
        res.setPacketCrc16(packetCrc16);

        return res;
    }

    public static void main(String[] args){
        byte[] data = {0, 2, 0, 1, 0, 6, 0x25, 0x48, 0x36, (byte) 0x85, 25, 93};
        for(byte all : data) System.out.println(all);
        PacketUnwr pkt = new PacketUnwrapper().unwrap(data);
        System.out.println(pkt.getPacketCommand());
        System.out.println(pkt.getPacketNumber());
        System.out.println(pkt.getPacketLength());
        for(byte all : pkt.getPacketData()) System.out.println(all);
        System.out.println(pkt.getPacketCrc16());
    }

    private short bytesToShort(byte[] src){
        ByteBuffer bb = ByteBuffer.wrap(src);
        return bb.getShort();
    }

    public short getPacketCommand() {
        return packetCommand;
    }

    public short getPacketNumber() {
        return packetNumber;
    }

    public short getPacketLength() {
        return packetLength;
    }

    public short getPacketCrc16() {
        return packetCrc16;
    }

    public byte[] getPacketData() {
        return packetData;
    }
}
