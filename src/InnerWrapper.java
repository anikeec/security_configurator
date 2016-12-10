import java.nio.ByteBuffer;

/**
 * Created by Ksusha on 09.12.2016.
 */
public class InnerWrapper extends Wrapper{
    private InnerProtocol innerProtocol;

    public InnerWrapper(InnerProtocol innerProtocol) {
        this.innerProtocol = innerProtocol;
    }

    public byte[] wrap(InnerPacket srcPkt) throws WrapperException {

        ByteBuffer bb;

        bb = ByteBuffer.allocate(   innerProtocol.getCommandLen() +
                                    innerProtocol.getAddressLen() +
                                    srcPkt.getData().length );

        int protocolCommandLen = innerProtocol.getCommandLen();
        if(protocolCommandLen == 1){
            bb.put((byte)srcPkt.getCommand());
        } else if(protocolCommandLen == 2){
            bb.putShort((short) srcPkt.getCommand());
        } else {
            throw new WrapperException("error. protocolCommandLen = " + protocolCommandLen);
        }

        int protocolAddressLen = innerProtocol.getAddressLen();
        if(protocolAddressLen == 1){
            bb.put((byte)srcPkt.getAddress());
        } else if(protocolAddressLen == 2){
            bb.putShort((short) srcPkt.getAddress());
        } else {
            throw new WrapperException("error. protocolAddressLen = " + protocolAddressLen);
        }

        bb.put(srcPkt.getData());

        return bb.array();
    }

    public InnerPacket unwrap(byte[] srcPkt) throws WrapperException {

        ByteBuffer bb;

        int positionCommand = 0;
        int positionAddress =   positionCommand +
                                innerProtocol.getCommandLen();
        int positionData =  positionAddress +
                            innerProtocol.getAddressLen();

                                                                    // start to unwrap packet
        int tempCommand = 0;
        try {
            tempCommand = findInBuffer(srcPkt, positionCommand, innerProtocol.getCommandLen());
        } catch (WrapperException e){
            throw  new WrapperException("error. Command error.");
        }

        int tempAddress = 0;
        try {
            tempAddress = findInBuffer(srcPkt, positionAddress, innerProtocol.getAddressLen());
        } catch (WrapperException e){
            throw  new WrapperException("error. Address error.");
        }

        bb = createByteBuffer(srcPkt,
                                positionData,
                                srcPkt.length - innerProtocol.getCommandLen() - innerProtocol.getAddressLen());
        byte[] packetData = bb.array();

        InnerPacket innerPacket = new InnerPacket(tempCommand, tempAddress, packetData);

        return innerPacket;
    }

}
