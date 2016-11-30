import java.nio.ByteBuffer;

/**
 * Created by apu on 04.11.2016.
 */
public class PacketWrapper {

    public PacketWr wrap(int pkrNumber, int pktCommand, byte[] src){
        PacketRes resTemp = new PacketRes();
        PacketWr res = new PacketWr(resTemp.getPacketCommandByte().length +
                                    resTemp.getPacketNumberByte().length +
                                    resTemp.getPacketLengthByte().length +
                                    src.length +
                                    resTemp.getPacketCrc16Byte().length);

        resTemp.packetCommand = (short)pktCommand;
        resTemp.packetCrc16  = (short) Crc16.countCrc16(src);
        resTemp.packetLength = (short)(src.length + resTemp.getPacketCrc16Byte().length);
        resTemp.packetNumber = (short)pkrNumber;

        int ptr = 0;
        for(int i=0;i<resTemp.getPacketCommandByte().length;i++) {
            res.data[ptr++] = resTemp.getPacketCommandByte()[i];
        }
        for(int i=0;i<resTemp.getPacketNumberByte().length;i++) {
            res.data[ptr++] = resTemp.getPacketNumberByte()[i];
        }
        for(int i=0;i<resTemp.getPacketLengthByte().length;i++) {
            res.data[ptr++] = resTemp.getPacketLengthByte()[i];
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
        PacketWr pkt = new PacketWrapper().wrap(1, ConfigCommand.COMMAND_WRITE, data);
        System.out.println("Short size: "+Short.SIZE);
        for(byte all : pkt.data) System.out.println(all);
    }



    class PacketRes {
        public short    packetCommand;
        public short    packetNumber;
        public short    packetLength;
        public short    packetCrc16;

        public PacketRes() {
            packetCommand = 0;
            packetNumber = 0;
            packetLength = 0;
            packetCrc16 = 0;
        }

        public byte[] getPacketCommandByte(){ return ByteBuffer.allocate(Short.SIZE/8).putShort(packetCommand).array(); }

        public byte[] getPacketNumberByte(){
            return ByteBuffer.allocate(Short.SIZE/8).putShort(packetNumber).array();
        }

        public byte[] getPacketLengthByte(){
            return ByteBuffer.allocate(Short.SIZE/8).putShort(packetLength).array();
        }

        public byte[] getPacketCrc16Byte(){
            return ByteBuffer.allocate(Short.SIZE/8).putShort(packetCrc16).array();
        }
    }
}
