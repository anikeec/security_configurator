import java.nio.ByteBuffer;

/**
 * Created by Ksusha on 09.12.2016.
 */
public class Wrapper {
    private Protocol protocol;

    public Wrapper(Protocol protocol) {
        this.protocol = protocol;
    }

    public byte[] wrap(Packet srcPkt) throws WrapperException {

        ByteBuffer bb;

        bb = ByteBuffer.allocate(   protocol.getHeaderLen() -
                                    protocol.getHeaderCrcLen() );

        int protocolMagicByteLen = protocol.getMagicByteLen();
        if(protocolMagicByteLen == 1){
            bb.put((byte)srcPkt.getMagicByte());
        } else if(protocolMagicByteLen == 2){
            bb.putShort((short) srcPkt.getMagicByte());
        } else {
            throw new WrapperException("error. protocolMagicByteLen = " + protocolMagicByteLen);
        }

        int protocolAddressDstLen = protocol.getAddressDstLen();
        if(protocolAddressDstLen == 1){
            bb.put((byte)srcPkt.getAddressDst());
        } else if(protocolAddressDstLen == 2){
            bb.putShort((short) srcPkt.getMagicByte());
        } else {
            throw new WrapperException("error. protocolAddressDstLen = " + protocolAddressDstLen);
        }

        int tempPacketLength =  protocol.getHeaderLen() +
                                srcPkt.getData().length +
                                protocol.getPacketCrcLen();
        srcPkt.setPacketLength(tempPacketLength);

        int protocolPacketLengthLen = protocol.getPacketLengthLen();
        if(protocolPacketLengthLen == 1){
            bb.put((byte)srcPkt.getPacketLength());
        } else if(protocolPacketLengthLen == 2){
            bb.putShort((short) srcPkt.getMagicByte());
        } else {
            throw new WrapperException("error. protocolPacketLengthLen = " + protocolPacketLengthLen);
        }

        int tempHeaderCrc = 0;
        int protocolHeaderCrcLen = protocol.getHeaderCrcLen();
        if(protocolHeaderCrcLen == 1){
            Crc8 crc8 = new Crc8();
            tempHeaderCrc = crc8.calc(bb.array());
        } else if(protocolHeaderCrcLen == 2){
            Crc16 crc16 = new Crc16();
            tempHeaderCrc = crc16.calc(bb.array());
        } else {
            throw new WrapperException("error. protocolHeaderCrcLen = " + protocolHeaderCrcLen);
        }
        srcPkt.setHeaderCrc(tempHeaderCrc);

        // now we have full header with crc. Time to allocate buffer for full packet
        bb = ByteBuffer.allocate(   srcPkt.getPacketLength() -
                                    protocol.getPacketCrcLen() );

        protocolMagicByteLen = protocol.getMagicByteLen();
        if(protocolMagicByteLen == 1){
            bb.put((byte)srcPkt.getMagicByte());
        } else if(protocolMagicByteLen == 2){
            bb.putShort((short) srcPkt.getMagicByte());
        } else {
            throw new WrapperException("error. protocolMagicByteLen = " + protocolMagicByteLen);
        }

        protocolAddressDstLen = protocol.getAddressDstLen();
        if(protocolAddressDstLen == 1){
            bb.put((byte)srcPkt.getAddressDst());
        } else if(protocolAddressDstLen == 2){
            bb.putShort((short) srcPkt.getMagicByte());
        } else {
            throw new WrapperException("error. protocolAddressDstLen = " + protocolAddressDstLen);
        }

        protocolPacketLengthLen = protocol.getPacketLengthLen();
        if(protocolPacketLengthLen == 1){
            bb.put((byte)srcPkt.getPacketLength());
        } else if(protocolPacketLengthLen == 2){
            bb.putShort((short) srcPkt.getMagicByte());
        } else {
            throw new WrapperException("error. protocolPacketLengthLen = " + protocolPacketLengthLen);
        }

        protocolHeaderCrcLen = protocol.getHeaderCrcLen();
        if(protocolHeaderCrcLen == 1){
            bb.put((byte)srcPkt.getHeaderCrc());
        } else if(protocolHeaderCrcLen == 2){
            bb.putShort((short) srcPkt.getHeaderCrc());
        } else {
            throw new WrapperException("error. protocolHeaderCrcLen = " + protocolHeaderCrcLen);
        }

        bb.put(srcPkt.getData());

        int tempPacketCrc = 0;
        int protocolPacketCrcLen = protocol.getPacketCrcLen();
        if(protocolPacketCrcLen == 1){
            Crc8 crc8 = new Crc8();
            tempPacketCrc = crc8.calc(bb.array());
        } else if(protocolPacketCrcLen == 2){
            Crc16 crc16 = new Crc16();
            tempPacketCrc = crc16.calc(bb.array());
        } else {
            throw new WrapperException("error. protocolPacketCrcLen = " + protocolPacketCrcLen);
        }
        srcPkt.setPacketCrc(tempPacketCrc);

        byte[] currentBuffer = bb.array();
        bb = ByteBuffer.allocate(srcPkt.getPacketLength());

        bb.put(currentBuffer);

        protocolPacketCrcLen = protocol.getPacketCrcLen();
        if(protocolPacketCrcLen == 1){
            bb.put((byte)srcPkt.getPacketCrc());
        } else if(protocolPacketCrcLen == 2){
            bb.putShort((short) srcPkt.getPacketCrc());
        } else {
            throw new WrapperException("error. protocolPacketCrcLen = " + protocolPacketCrcLen);
        }

        return bb.array();
    }

    public Packet unwrap(byte[] srcPkt){
        return null;
    }
}
