import java.util.HashMap;
import java.util.Map;

/**
 * Created by apu on 02.12.2016.
 */
public class settings {
    private static Map<main.param,String> map;

    public static Map<main.param, String> getSet() {
        if(map == null){
            init();
        }
        return map;
    }

    private static void init(){
        map = new HashMap<main.param,String>();

        map.put(main.param.GSM_SERVER,"www.kyivstar.net");
        map.put(main.param.GSM_MONEY_QUERY,"*111#");
        map.put(main.param.GPRS_SERVER,"primula.net.ua");
        map.put(main.param.GPRS_PORT,"80");
    }
}
