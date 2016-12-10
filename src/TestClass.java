/**
 * Created by Ksusha on 09.12.2016.
 */
public class TestClass {

    public static void main(String[] args){
        int magicByteLen = 1;
        int addressLen = 1;
        int packetLengthLen = 1;
        int headerCrcLen = 1;
        int packetCrcLen = 2;

        OuterProtocol testOuterProtocol = new OuterProtocol(magicByteLen, addressLen, packetLengthLen, headerCrcLen, packetCrcLen);

        int magicByte = 0x53;
        int addressDst = 0x00;
        byte[] srcBytes = new byte[]{0x02, 0x00, 0x00, 'w','w','w','.','k','y','i','v','s','t','a','r','.','c','o','m'};

        OuterPacket testOuterPacket = new OuterPacket(magicByte, addressDst, srcBytes);

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


    }

}
