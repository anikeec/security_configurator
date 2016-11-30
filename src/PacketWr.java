/**
 * Created by apu on 04.11.2016.
 */
public class PacketWr {
    public byte[]  data;

    public PacketWr(int length) {
        data = new byte[length];
        for(int i=0;i<length;i++)   data[i] = 0;
    }
}
