import java.util.HashMap;
import java.util.Map;

/**
 * Created by apu on 02.12.2016.
 */
public class settings {
    private static Map<String,String> map;

    public static final int N_GSM_SERVER = 0;
    public static final int N_GSM_MONEY_QUERY = 1;

    public static Map<String, String> getSet() {
        if(map == null){
            init();
        }
        return map;
    }

    private static void init(){
        map = new HashMap<String,String>();

        map.put(pktParams.GSM_SERVER,"www.kyivstar.net");
        map.put(pktParams.GSM_MONEY_QUERY,"*111#");
        map.put(pktParams.GPRS_SERVER,"primula.net.ua");
        map.put(pktParams.GPRS_PORT,"80");
    }
}
