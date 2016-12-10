/**
 * Created by Ksusha on 09.12.2016.
 */
public class TestClass {

    public static void main(String[] args){
        int magicByteLen = 1;
        int addressDstLen = 1;
        int packetLengthLen = 1;
        int headerCrcLen = 1;
        int packetCrcLen = 2;

        OuterProtocol testOuterProtocol = new OuterProtocol(magicByteLen, addressDstLen, packetLengthLen, headerCrcLen, packetCrcLen);

        int commandLen = 1;
        int addressLen = 2;

        InnerProtocol testInnerProtocol = new InnerProtocol(commandLen, addressLen);

        int command = 0x02;
        int address = 0x00;
        byte[] srcBytes = new byte[]{'w','w','w','.','k','y','i','v','s','t','a','r','.','c','o','m'};

        InnerPacket testInnerPacket = new InnerPacket(command, address, srcBytes);

        InnerWrapper testInnerWrapper = new InnerWrapper(testInnerProtocol);

        byte[] testInnerWrapPacket = new byte[0];
        try{
            testInnerWrapPacket = testInnerWrapper.wrap(testInnerPacket);
            System.out.println(testInnerWrapPacket);
        } catch (WrapperException e) {
            System.out.println(e.getMessage());
        }

        int magicByte = 0x53;
        int addressDst = 0x00;

        OuterPacket testOuterPacket = new OuterPacket(magicByte, addressDst, testInnerWrapPacket);

        OuterWrapper testOuterWrapper = new OuterWrapper(testOuterProtocol);

        byte[] testWrapPacket = new byte[0];
        try {
            testWrapPacket = testOuterWrapper.wrap(testOuterPacket);
            System.out.println(testWrapPacket);
        } catch (WrapperException e) {
            System.out.println(e.getMessage());
        }
        System.out.println();

        OuterPacket testUnwrapOuterPacket = null;
        try {
            testUnwrapOuterPacket = testOuterWrapper.unwrap(testWrapPacket);
            System.out.println(testUnwrapOuterPacket);
        } catch (WrapperException e) {
            System.out.println(e.getMessage());
        }

        InnerPacket testUnwrapInnerPacket = null;
        try {
            testUnwrapInnerPacket = testInnerWrapper.unwrap(testUnwrapOuterPacket.getData());
            System.out.println(testUnwrapInnerPacket);
        } catch (WrapperException e) {
            System.out.println(e.getMessage());
        }


    }

}
