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

    public Packet unwrap(byte[] srcPkt) throws WrapperException {

        ByteBuffer bb;

        int positionMagicByte = 0;
        int positionAddressDst =    positionMagicByte +
                                    protocol.getMagicByteLen();
        int positionPacketLength =  positionAddressDst +
                                    protocol.getAddressDstLen();
        int positionHeaderCrc = positionPacketLength +
                                protocol.getPacketLengthLen();
        int positionData =  positionHeaderCrc +
                            protocol.getHeaderCrcLen();
                                                                    // check header CRC
        bb = createByteBuffer(  srcPkt,
                                positionMagicByte,
                                protocol.getHeaderLen() - protocol.getHeaderCrcLen());

        int tempHeaderCrc = 0;
        int tempHeaderCrcCalc = 0;
        int protocolHeaderCrcLen = protocol.getHeaderCrcLen();
        if(protocolHeaderCrcLen == 1){
            Crc8 crc8 = new Crc8();
            tempHeaderCrcCalc = crc8.calc(bb.array());
            tempHeaderCrc = (int)srcPkt[positionHeaderCrc];
        } else if(protocolHeaderCrcLen == 2){
            Crc16 crc16 = new Crc16();
            tempHeaderCrcCalc = crc16.calc(bb.array());
            bb = createByteBuffer(srcPkt,
                    positionHeaderCrc,
                    protocol.getPacketLengthLen());
            tempHeaderCrc = bb.getInt();
        } else {
            throw new WrapperException("error. protocolHeaderCrcLen = " + protocolHeaderCrcLen);
        }

        if(tempHeaderCrcCalc != tempHeaderCrc){
            throw new WrapperException("error. CRC header error.");
        }
                                                                    // find packet length
        int tempPacketLength = 0;
        try {
            tempPacketLength = findInBuffer(srcPkt, positionPacketLength, protocol.getPacketLengthLen());
        } catch (WrapperException e){
            throw  new WrapperException("error. PacketLength error.");
        }
                                                                    // check full packet CRC
        int tempPacketCrc = 0;
        try {
            tempPacketCrc = findInBuffer(srcPkt,
                                            tempPacketLength -  protocol.getPacketCrcLen(),
                                            protocol.getPacketCrcLen());
        } catch (WrapperException e){
            throw  new WrapperException("error. PacketCrc error.");
        }

        int tempPacketCrcCalc = 0;
        int protocolPacketCrcLen = protocol.getPacketCrcLen();
        if(protocolPacketCrcLen == 1){
            Crc8 crc8 = new Crc8();
            tempPacketCrcCalc = crc8.calc(bb.array());
        } else if(protocolPacketCrcLen == 2){
            bb = createByteBuffer(srcPkt,
                    positionMagicByte,
                    tempPacketLength - protocol.getPacketCrcLen());
            Crc16 crc16 = new Crc16();
            tempPacketCrcCalc = crc16.calc(bb.array());
        } else {
            throw new WrapperException("error. protocolPacketCrcLen = " + protocolPacketCrcLen);
        }

        if(tempPacketCrcCalc != tempPacketCrc){
            throw new WrapperException("error. CRC packet error.");
        }
                                                                    // start to unwrap packet
        int tempMagicByte = 0;
        try {
            tempMagicByte = findInBuffer(srcPkt, positionMagicByte, protocol.getMagicByteLen());
        } catch (WrapperException e){
            throw  new WrapperException("error. MagicByte error.");
        }

        int tempAddressDst = 0;
        try {
            tempAddressDst = findInBuffer(srcPkt, positionAddressDst, protocol.getAddressDstLen());
        } catch (WrapperException e){
            throw  new WrapperException("error. AddressDst error.");
        }

        bb = createByteBuffer(srcPkt,
                positionData,
                tempPacketLength - protocol.getHeaderLen() - protocol.getPacketCrcLen());
        byte[] packetData = bb.array();

        Packet packet = new Packet(tempMagicByte, tempAddressDst, packetData);
        packet.setPacketLength(tempPacketLength);
        packet.setHeaderCrc(tempHeaderCrc);
        packet.setPacketCrc(tempPacketCrc);
        packet.setHeaderLength( protocol.getMagicByteLen() +
                                protocol.getAddressDstLen() +
                                protocol.getPacketLengthLen() +
                                protocol.getHeaderCrcLen());

        return packet;
    }


    int findInBuffer(byte[] buffer, int startPosition, int length) throws WrapperException {
        ByteBuffer bb;
        int tempReturn;

        if(length == 1){
            tempReturn = (int)buffer[startPosition];
        } else if(length == 2){
            bb = createByteBuffer(buffer,
                    startPosition,
                    length);
            bb.position(0);
            tempReturn = bb.getShort();
        } else {
            throw new WrapperException();
        }

        return tempReturn;
    }


    ByteBuffer createByteBuffer(byte[] buffer, int start, int length) {
        ByteBuffer bb = ByteBuffer.allocate(length);
        for(int i=0; i<length; i++){
            bb.put(buffer[start + i]);
        }
        return bb;
    }


    int unwrapHeaderLength(byte[] srcPkt) throws WrapperException {
        ByteBuffer bb;

        int positionMagicByte = 0;
        int positionAddressDst =    positionMagicByte +
                protocol.getMagicByteLen();
        int positionPacketLength =  positionAddressDst +
                protocol.getAddressDstLen();
        int positionHeaderCrc = positionPacketLength +
                protocol.getPacketLengthLen();
                                                                    // check header CRC
        bb = createByteBuffer(  srcPkt,
                                positionMagicByte,
                                protocol.getHeaderLen() - protocol.getHeaderCrcLen());

        int tempHeaderCrc = 0;
        int tempHeaderCrcCalc = 0;
        int protocolHeaderCrcLen = protocol.getHeaderCrcLen();
        if(protocolHeaderCrcLen == 1){
            Crc8 crc8 = new Crc8();
            tempHeaderCrcCalc = crc8.calc(bb.array());
            tempHeaderCrc = (int)srcPkt[positionHeaderCrc];
        } else if(protocolHeaderCrcLen == 2){
            Crc16 crc16 = new Crc16();
            tempHeaderCrcCalc = crc16.calc(bb.array());
            bb = createByteBuffer(  srcPkt,
                                    positionHeaderCrc,
                                    protocol.getPacketLengthLen());
            tempHeaderCrc = bb.getInt();
        } else {
            throw new WrapperException("error. HeaderCrcLen.");
        }

        if(tempHeaderCrcCalc != tempHeaderCrc){
            throw new WrapperException("error. CRC header error.");
        }
                                                                        // find packet length
        int tempPacketLength = 0;
        try {
            tempPacketLength = findInBuffer(srcPkt, positionPacketLength, protocol.getPacketLengthLen());
        } catch (WrapperException e){
            throw  new WrapperException("error. PacketLength error.");
        }

        return tempPacketLength;
    }

}
