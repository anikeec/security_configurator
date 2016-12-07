import java.nio.ByteBuffer;

/**
 * Created by apu on 04.11.2016.
 */
public class PacketWrapper {

    public static final byte PACKET_MAGIC_BYTE = 0x53;

    public PacketWr wrap(byte pktAddress, int pkrNumber, byte pktCommand, byte[] src){
        ByteBuffer bbHeader = ByteBuffer.wrap(new byte[3]);
        Crc8 crc8 = new Crc8();
        short lengthPktFull = 0;

        PacketRes resTemp = new PacketRes();

        lengthPktFull = (short)(resTemp.getPacketMagicByte().length +
                                resTemp.getPacketAddressByte().length +
                                resTemp.getPacketLengthByte().length +
                                resTemp.getPacketCrc8Byte().length +
                                resTemp.getPacketCommandByte().length +
                                resTemp.getPacketNumberByte().length +
                                src.length +
                                resTemp.getPacketCrc16Byte().length);

        PacketWr res = new PacketWr(lengthPktFull);

        resTemp.packetAddress = pktAddress;
        resTemp.packetLength = (byte)(lengthPktFull);
        resTemp.packetCommand = pktCommand;
        resTemp.packetNumber = (short)pkrNumber;

        bbHeader.put(resTemp.packetMagicByte);      // make header array for count crc8
        bbHeader.put(resTemp.packetAddress);
        bbHeader.put(resTemp.packetLength);

        resTemp.packetCrc8 = crc8.calc(bbHeader.array());

        bbHeader = ByteBuffer.allocate(lengthPktFull - resTemp.getPacketCrc16Byte().length);
        bbHeader.put(resTemp.packetMagicByte);      // make array for count crc16
        bbHeader.put(resTemp.packetAddress);
        bbHeader.put(resTemp.packetLength);
        bbHeader.put(resTemp.packetCrc8);
        bbHeader.put(resTemp.packetCommand);
        bbHeader.putShort(resTemp.packetNumber);
        bbHeader.put(src);

        resTemp.packetCrc16  = (short) Crc16.countCrc16(bbHeader.array());

        int ptr = 0;                                // create result array
        res.data[ptr++] = resTemp.getPacketMagicByte()[0];

        res.data[ptr++] = resTemp.getPacketAddressByte()[0];

        res.data[ptr++] = resTemp.getPacketLengthByte()[0];

        res.data[ptr++] = resTemp.getPacketCrc8Byte()[0];

        res.data[ptr++] = resTemp.getPacketCommandByte()[0];

        for(int i=0;i<resTemp.getPacketNumberByte().length;i++) {
            res.data[ptr++] = resTemp.getPacketNumberByte()[i];
        }

        for(int i=0;i<src.length;i++) {
            res.data[ptr++] = src[i];
        }
        for(int i=0;i<resTemp.getPacketCrc16Byte().length;i++) {
            res.data[ptr++] = resTemp.getPacketCrc16Byte()[i];
        }

        return res;
    }

    public static void main(String[] args){
        byte[] data = {0x25, 0x48, 0x36, (byte) 0x85};
        for(byte all : data) System.out.println(all);
        PacketWr pkt = new PacketWrapper().wrap((byte)0, 1, ConfigCommand.COMMAND_WRITE, data);
        System.out.println("Short size: "+Short.SIZE);
        for(byte all : pkt.data) System.out.println(all);
    }



    class PacketRes {
        public byte     packetMagicByte;
        public byte     packetAddress;
        public byte     packetLength;
        public byte     packetCrc8;
        public byte     packetCommand;
        public short    packetNumber;
        public short    packetCrc16;

        public PacketRes() {
            packetMagicByte = PACKET_MAGIC_BYTE;
            packetAddress = 0;
            packetLength = 0;
            packetCrc8 = 0;
            packetCommand = 0;
            packetNumber = 0;
            packetCrc16 = 0;
        }

        public byte[] getPacketMagicByte(){ return ByteBuffer.allocate(Byte.SIZE/8).put(packetMagicByte).array(); }

        public byte[] getPacketAddressByte(){ return ByteBuffer.allocate(Byte.SIZE/8).put(packetAddress).array(); }

        public byte[] getPacketCrc8Byte(){ return ByteBuffer.allocate(Byte.SIZE/8).put(packetCrc8).array(); }

        public byte[] getPacketCommandByte(){ return ByteBuffer.allocate(Byte.SIZE/8).put(packetCommand).array(); }

        public byte[] getPacketNumberByte(){
            return ByteBuffer.allocate(Short.SIZE/8).putShort(packetNumber).array();
        }

        public byte[] getPacketLengthByte(){
            return ByteBuffer.allocate(Byte.SIZE/8).put(packetLength).array();
        }

        public byte[] getPacketCrc16Byte(){
            return ByteBuffer.allocate(Short.SIZE/8).putShort(packetCrc16).array();
        }
    }
}
