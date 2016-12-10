/**
 * Created by Ksusha on 09.12.2016.
 */
public class OuterProtocol {
    private int magicByteLen;
    private int addressDstLen;
    private int packetLengthLen;
    private int headerCrcLen;
    private int packetCrcLen;

    public OuterProtocol() {
        this.magicByteLen = 1;
        this.addressDstLen = 1;
        this.packetLengthLen = 1;
        this.headerCrcLen = 1;
        this.packetCrcLen = 2;
    }

    public OuterProtocol(int magicByteLen, int addressDstLen, int packetLengthLen, int headerCrcLen, int packetCrcLen) {
        this.magicByteLen = magicByteLen;
        this.addressDstLen = addressDstLen;
        this.packetLengthLen = packetLengthLen;
        this.headerCrcLen = headerCrcLen;
        this.packetCrcLen = packetCrcLen;
    }

    public int getHeaderLen(){
        return (magicByteLen + addressDstLen + packetLengthLen + headerCrcLen);
    }

    public int getMagicByteLen() {
        return magicByteLen;
    }

    public void setMagicByteLen(int magicByteLen) {
        this.magicByteLen = magicByteLen;
    }

    public int getAddressDstLen() {
        return addressDstLen;
    }

    public void setAddressDstLen(int addressDstLen) {
        this.addressDstLen = addressDstLen;
    }

    public int getPacketLengthLen() {
        return packetLengthLen;
    }

    public void setPacketLengthLen(int packetLengthLen) {
        this.packetLengthLen = packetLengthLen;
    }

    public int getHeaderCrcLen() {
        return headerCrcLen;
    }

    public void setHeaderCrcLen(int headerCrcLen) {
        this.headerCrcLen = headerCrcLen;
    }

    public int getPacketCrcLen() {
        return packetCrcLen;
    }

    public void setPacketCrcLen(int packetCrcLen) {
        this.packetCrcLen = packetCrcLen;
    }
}
