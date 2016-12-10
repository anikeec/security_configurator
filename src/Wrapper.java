import java.nio.ByteBuffer;

/**
 * Created by Ksusha on 10.12.2016.
 */
public abstract class Wrapper {

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
}
