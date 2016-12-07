import java.nio.ByteBuffer;

/**
 * Created by apu on 04.11.2016.
 */
public class PacketWrapper {

    public static final byte PACKET_MAGIC_BYTE = 0x53;

    public PacketWr wrap(byte pktAddress, int pkrNumber, byte pktCommand, byte[] src){
        ByteBuffer bbHeader = ByteBuffer.wrap(new byte[3]);
        Crc8 crc8 = new Crc8();

        PacketRes resTemp = new PacketRes();
        PacketWr res = new PacketWr(resTemp.getPacketMagicByte().length +
                                    resTemp.getPacketAddressByte().length +
                                    resTemp.getPacketLengthByte().length +
                                    resTemp.getPacketCrc8Byte().length +
                                    resTemp.getPacketCommandByte().length +
                                    resTemp.getPacketNumberByte().length +
                                    src.length +
                                    resTemp.getPacketCrc16Byte().length);

        resTemp.packetAddress = pktAddress;
        resTemp.packetLength = (byte)(src.length + resTemp.getPacketCrc16Byte().length);
        resTemp.packetCommand = pktCommand;
        resTemp.packetNumber = (short)pkrNumber;
        resTemp.packetCrc16  = (short) Crc16.countCrc16(src);

        //bbHeader.reset();
        bbHeader.put(resTemp.packetMagicByte);
        bbHeader.put(resTemp.packetAddress);
        bbHeader.put(resTemp.packetLength);

        resTemp.packetCrc8 = crc8.calc(bbHeader.array());

        int ptr = 0;
        res.data[ptr++] = resTemp.getPacketMagicByte()[0];

        res.data[ptr++] = resTemp.getPacketAddressByte()[0];

        res.data[ptr++] = resTemp.getPacketLengthByte()[0];

        res.data[ptr++] = resTemp.getPacketCrc8Byte()[0];

        for(int i=0;i<resTemp.getPacketCommandByte().length;i++) {
            res.data[ptr++] = resTemp.getPacketCommandByte()[i];
        }
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
