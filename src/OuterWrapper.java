import java.nio.ByteBuffer;

/**
 * Created by Ksusha on 09.12.2016.
 */
public class OuterWrapper extends Wrapper{
    private OuterProtocol outerProtocol;

    public OuterWrapper(OuterProtocol outerProtocol) {
        this.outerProtocol = outerProtocol;
    }

    public byte[] wrap(OuterPacket srcPkt) throws WrapperException {

        ByteBuffer bb;

        bb = ByteBuffer.allocate(   outerProtocol.getHeaderLen() -
                                    outerProtocol.getHeaderCrcLen() );

        int protocolMagicByteLen = outerProtocol.getMagicByteLen();
        if(protocolMagicByteLen == 1){
            bb.put((byte)srcPkt.getMagicByte());
        } else if(protocolMagicByteLen == 2){
            bb.putShort((short) srcPkt.getMagicByte());
        } else {
            throw new WrapperException("error. protocolMagicByteLen = " + protocolMagicByteLen);
        }

        int protocolAddressDstLen = outerProtocol.getAddressDstLen();
        if(protocolAddressDstLen == 1){
            bb.put((byte)srcPkt.getAddressDst());
        } else if(protocolAddressDstLen == 2){
            bb.putShort((short) srcPkt.getMagicByte());
        } else {
            throw new WrapperException("error. protocolAddressDstLen = " + protocolAddressDstLen);
        }

        int tempPacketLength =  outerProtocol.getHeaderLen() +
                                srcPkt.getData().length +
                                outerProtocol.getPacketCrcLen();
        srcPkt.setPacketLength(tempPacketLength);

        int protocolPacketLengthLen = outerProtocol.getPacketLengthLen();
        if(protocolPacketLengthLen == 1){
            bb.put((byte)srcPkt.getPacketLength());
        } else if(protocolPacketLengthLen == 2){
            bb.putShort((short) srcPkt.getMagicByte());
        } else {
            throw new WrapperException("error. protocolPacketLengthLen = " + protocolPacketLengthLen);
        }

        int tempHeaderCrc = 0;
        int protocolHeaderCrcLen = outerProtocol.getHeaderCrcLen();
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
                                    outerProtocol.getPacketCrcLen() );

        protocolMagicByteLen = outerProtocol.getMagicByteLen();
        if(protocolMagicByteLen == 1){
            bb.put((byte)srcPkt.getMagicByte());
        } else if(protocolMagicByteLen == 2){
            bb.putShort((short) srcPkt.getMagicByte());
        } else {
            throw new WrapperException("error. protocolMagicByteLen = " + protocolMagicByteLen);
        }

        protocolAddressDstLen = outerProtocol.getAddressDstLen();
        if(protocolAddressDstLen == 1){
            bb.put((byte)srcPkt.getAddressDst());
        } else if(protocolAddressDstLen == 2){
            bb.putShort((short) srcPkt.getAddressDst());
        } else {
            throw new WrapperException("error. protocolAddressDstLen = " + protocolAddressDstLen);
        }

        protocolPacketLengthLen = outerProtocol.getPacketLengthLen();
        if(protocolPacketLengthLen == 1){
            bb.put((byte)srcPkt.getPacketLength());
        } else if(protocolPacketLengthLen == 2){
            bb.putShort((short) srcPkt.getPacketLength());
        } else {
            throw new WrapperException("error. protocolPacketLengthLen = " + protocolPacketLengthLen);
        }

        protocolHeaderCrcLen = outerProtocol.getHeaderCrcLen();
        if(protocolHeaderCrcLen == 1){
            bb.put((byte)srcPkt.getHeaderCrc());
        } else if(protocolHeaderCrcLen == 2){
            bb.putShort((short) srcPkt.getHeaderCrc());
        } else {
            throw new WrapperException("error. protocolHeaderCrcLen = " + protocolHeaderCrcLen);
        }

        bb.put(srcPkt.getData());

        int tempPacketCrc = 0;
        int protocolPacketCrcLen = outerProtocol.getPacketCrcLen();
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

        protocolPacketCrcLen = outerProtocol.getPacketCrcLen();
        if(protocolPacketCrcLen == 1){
            bb.put((byte)srcPkt.getPacketCrc());
        } else if(protocolPacketCrcLen == 2){
            bb.putShort((short) srcPkt.getPacketCrc());
        } else {
            throw new WrapperException("error. protocolPacketCrcLen = " + protocolPacketCrcLen);
        }

        return bb.array();
    }

    public OuterPacket unwrap(byte[] srcPkt) throws WrapperException {

        ByteBuffer bb;

        int positionMagicByte = 0;
        int positionAddressDst =    positionMagicByte +
                                    outerProtocol.getMagicByteLen();
        int positionPacketLength =  positionAddressDst +
                                    outerProtocol.getAddressDstLen();
        int positionHeaderCrc = positionPacketLength +
                                outerProtocol.getPacketLengthLen();
        int positionData =  positionHeaderCrc +
                            outerProtocol.getHeaderCrcLen();
                                                                    // check header CRC
        bb = createByteBuffer(  srcPkt,
                                positionMagicByte,
                                outerProtocol.getHeaderLen() - outerProtocol.getHeaderCrcLen());

        int tempHeaderCrc = 0;
        int tempHeaderCrcCalc = 0;
        int protocolHeaderCrcLen = outerProtocol.getHeaderCrcLen();
        if(protocolHeaderCrcLen == 1){
            Crc8 crc8 = new Crc8();
            tempHeaderCrcCalc = crc8.calc(bb.array());
            tempHeaderCrc = (int)srcPkt[positionHeaderCrc];
        } else if(protocolHeaderCrcLen == 2){
            Crc16 crc16 = new Crc16();
            tempHeaderCrcCalc = crc16.calc(bb.array());
            bb = createByteBuffer(srcPkt,
                    positionHeaderCrc,
                    outerProtocol.getPacketLengthLen());
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
            tempPacketLength = findInBuffer(srcPkt, positionPacketLength, outerProtocol.getPacketLengthLen());
        } catch (WrapperException e){
            throw  new WrapperException("error. PacketLength error.");
        }
                                                                    // check full packet CRC
        int tempPacketCrc = 0;
        try {
            tempPacketCrc = findInBuffer(srcPkt,
                                            tempPacketLength -  outerProtocol.getPacketCrcLen(),
                                            outerProtocol.getPacketCrcLen());
        } catch (WrapperException e){
            throw  new WrapperException("error. PacketCrc error.");
        }

        int tempPacketCrcCalc = 0;
        int protocolPacketCrcLen = outerProtocol.getPacketCrcLen();
        if(protocolPacketCrcLen == 1){
            Crc8 crc8 = new Crc8();
            tempPacketCrcCalc = crc8.calc(bb.array());
        } else if(protocolPacketCrcLen == 2){
            bb = createByteBuffer(srcPkt,
                    positionMagicByte,
                    tempPacketLength - outerProtocol.getPacketCrcLen());
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
            tempMagicByte = findInBuffer(srcPkt, positionMagicByte, outerProtocol.getMagicByteLen());
        } catch (WrapperException e){
            throw  new WrapperException("error. MagicByte error.");
        }

        int tempAddressDst = 0;
        try {
            tempAddressDst = findInBuffer(srcPkt, positionAddressDst, outerProtocol.getAddressDstLen());
        } catch (WrapperException e){
            throw  new WrapperException("error. AddressDst error.");
        }

        bb = createByteBuffer(srcPkt,
                positionData,
                tempPacketLength - outerProtocol.getHeaderLen() - outerProtocol.getPacketCrcLen());
        byte[] packetData = bb.array();

        OuterPacket outerPacket = new OuterPacket(tempMagicByte, tempAddressDst, packetData);
        outerPacket.setPacketLength(tempPacketLength);
        outerPacket.setHeaderCrc(tempHeaderCrc);
        outerPacket.setPacketCrc(tempPacketCrc);
        outerPacket.setHeaderLength( outerProtocol.getMagicByteLen() +
                                outerProtocol.getAddressDstLen() +
                                outerProtocol.getPacketLengthLen() +
                                outerProtocol.getHeaderCrcLen());

        return outerPacket;
    }


    int unwrapHeaderLength(byte[] srcPkt) throws WrapperException {
        ByteBuffer bb;

        int positionMagicByte = 0;
        int positionAddressDst =    positionMagicByte +
                outerProtocol.getMagicByteLen();
        int positionPacketLength =  positionAddressDst +
                outerProtocol.getAddressDstLen();
        int positionHeaderCrc = positionPacketLength +
                outerProtocol.getPacketLengthLen();
                                                                    // check header CRC
        bb = createByteBuffer(  srcPkt,
                                positionMagicByte,
                                outerProtocol.getHeaderLen() - outerProtocol.getHeaderCrcLen());

        int tempHeaderCrc = 0;
        int tempHeaderCrcCalc = 0;
        int protocolHeaderCrcLen = outerProtocol.getHeaderCrcLen();
        if(protocolHeaderCrcLen == 1){
            Crc8 crc8 = new Crc8();
            tempHeaderCrcCalc = crc8.calc(bb.array());
            tempHeaderCrc = (int)srcPkt[positionHeaderCrc];
        } else if(protocolHeaderCrcLen == 2){
            Crc16 crc16 = new Crc16();
            tempHeaderCrcCalc = crc16.calc(bb.array());
            bb = createByteBuffer(  srcPkt,
                                    positionHeaderCrc,
                                    outerProtocol.getPacketLengthLen());
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
            tempPacketLength = findInBuffer(srcPkt, positionPacketLength, outerProtocol.getPacketLengthLen());
        } catch (WrapperException e){
            throw  new WrapperException("error. PacketLength error.");
        }

        return tempPacketLength;
    }

}
