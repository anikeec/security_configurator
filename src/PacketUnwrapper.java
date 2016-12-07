import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by apu on 04.11.2016.
 */
public class PacketUnwrapper {

    private static final int PACKET_PTR_MAGICBYTE = 0;
    private static final int PACKET_PTR_MAGICBYTE_LEN = 1;
    private static final int PACKET_PTR_ADDRESS = 1;
    private static final int PACKET_PTR_ADDRESS_LEN = 1;
    private static final int PACKET_PTR_LENGTH = 2;
    private static final int PACKET_PTR_LENGTH_LEN = 1;
    private static final int PACKET_PTR_CRC8 = 3;
    private static final int PACKET_PTR_CRC8_LEN = 1;
    private static final int PACKET_PTR_COMMAND = 4;
    public static final int PACKET_PTR_COMMAND_LEN = 1;
    private static final int PACKET_PTR_NUMBER = 5;
    public static final int PACKET_PTR_NUMBER_LEN = 2;
    private static final int PACKET_PTR_DATA = 7;
    private static final int PACKET_PTR_CRC16_LEN = 2;
    public  static final int PACKET_HEADER_LENGTH = 4;
    public  static final int PACKET_LENGTH_MAX = 100;
    public  static final short PACKET_LENGTH_ERROR = -1;

    private byte     packetMagicByte;
    private byte     packetAddress;
    private byte     packetLength;
    private byte     packetCrc8;
    private short    packetCommand;
    private short    packetNumber;
    private short    packetCrc16;
    private byte[]   packetData;

    public short packetLength(byte[] src){
        short packetLength;

        if(src.length < (PACKET_HEADER_LENGTH)) return PACKET_LENGTH_ERROR;
        if(checkHeaderCrc(src) != true)         return PACKET_LENGTH_ERROR;
        if(checkMagicByte(src) != true)         return PACKET_LENGTH_ERROR;

        packetLength = (short)src[PACKET_PTR_LENGTH];
        if(packetLength > PACKET_LENGTH_MAX)  packetLength = PACKET_LENGTH_ERROR;

        return packetLength;
    }

    public boolean checkMagicByte(byte[] src){
        if(src[PACKET_PTR_MAGICBYTE] == PacketWrapper.PACKET_MAGIC_BYTE)    return true;
        else    return false;
    }

    public boolean checkHeaderCrc(byte[] src){
        byte    packetCrc8;
        byte    countCrc8;
        byte[] bytesTemp = new byte[3];
        Crc8 crc8 = new Crc8();

        packetCrc8 = src[PACKET_PTR_CRC8];

        bytesTemp[0] = src[PACKET_PTR_MAGICBYTE];
        bytesTemp[1] = src[PACKET_PTR_ADDRESS];
        bytesTemp[2] = src[PACKET_PTR_LENGTH];
        countCrc8 = crc8.calc(bytesTemp);

        if(packetCrc8 == countCrc8) return true;
        else    return false;
    }

    public PacketUnwr unwrap(byte[] src){

        byte[] bytesTemp = new byte[2];

        if(src.length < (PACKET_HEADER_LENGTH)) return null;

        packetMagicByte = src[PACKET_PTR_MAGICBYTE];

        packetAddress = src[PACKET_PTR_ADDRESS];

        packetLength = src[PACKET_PTR_LENGTH];

        packetCrc8 = src[PACKET_PTR_CRC8];

        packetCommand = src[PACKET_PTR_COMMAND];

        for(int i=0;i<PACKET_PTR_NUMBER_LEN;i++)   bytesTemp[i] = src[PACKET_PTR_NUMBER + i];
        packetNumber = bytesToShort(bytesTemp);

        if(src.length < (PACKET_PTR_COMMAND_LEN + PACKET_PTR_NUMBER_LEN + PACKET_HEADER_LENGTH + packetLength)) return null;

        packetData = new byte[packetLength - PACKET_PTR_CRC16_LEN];
        for(int i=0;i<(packetLength - PACKET_PTR_CRC16_LEN);i++)   packetData[i] = src[PACKET_PTR_DATA + i];

        for(int i=0;i<PACKET_PTR_CRC16_LEN;i++){
            bytesTemp[i] = src[PACKET_PTR_DATA + packetLength - PACKET_PTR_CRC16_LEN + i];
        }
        packetCrc16 = bytesToShort(bytesTemp);

        if(packetCrc16 != (short)Crc16.countCrc16(packetData))  return null;

        PacketUnwr res = new PacketUnwr();

        res.setPacketMagicByte(packetMagicByte);
        res.setPacketAddress(packetAddress);
        res.setPacketLength(packetLength);
        res.setPacketCrc8(packetCrc8);
        res.setPacketCommand(packetCommand);
        res.setPacketNumber(packetNumber);
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
