import java.util.HashMap;
import java.util.Map;

/**
 * Created by apu on 02.12.2016.
 */
public class settings {
    private static Map<String,String> map;

    public static final int N_GSM_SERVER        = 0;
    public static final int N_GSM_MONEY_QUERY   = 1;
    public static final int N_GPRS_SERVER       = 2;
    public static final int N_GPRS_PORT         = 3;
    public static final int N_GPRS_TIMEOUT      = 4;
    public static final int N_GPRS_PAGE         = 5;
    public static final int N_GPRS_ON_OFF       = 6;
    public static final int N_ZONE1_ON_OFF      = 7;
    public static final int N_ZONE1_STATE_NORM  = 8;
    public static final int N_ZONE2_ON_OFF      = 9;
    public static final int N_ZONE2_STATE_NORM  = 10;
    public static final int N_ZONE3_ON_OFF      = 11;
    public static final int N_ZONE3_STATE_NORM  = 12;
    public static final int N_OUT1_ON_OFF       = 13;
    public static final int N_OUT1_STATE_NORM   = 14;
    public static final int N_OUT2_ON_OFF       = 15;
    public static final int N_OUT2_STATE_NORM   = 16;
    public static final int N_OUT3_ON_OFF       = 17;
    public static final int N_OUT3_STATE_NORM   = 18;
    public static final int N_USER1_ON_OFF      = 19;
    public static final int N_USER1_PASSW       = 20;
    public static final int N_USER1_PHONE       = 21;
    public static final int N_USER2_ON_OFF      = 22;
    public static final int N_USER2_PASSW       = 23;
    public static final int N_USER2_PHONE       = 24;
    public static final int N_USER3_ON_OFF      = 25;
    public static final int N_USER3_PASSW       = 26;
    public static final int N_USER3_PHONE       = 27;

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
        map.put(pktParams.GPRS_PAGE,"");
        map.put(pktParams.GPRS_TIMEOUT,"10");
        map.put(pktParams.GPRS_ON_OFF,"1");
        map.put(pktParams.ZONE1_ON_OFF,"1");
        map.put(pktParams.ZONE1_STATE_NORM,"1");
        map.put(pktParams.ZONE2_ON_OFF,"1");
        map.put(pktParams.ZONE2_STATE_NORM,"1");
        map.put(pktParams.ZONE3_ON_OFF,"1");
        map.put(pktParams.ZONE3_STATE_NORM,"1");
        map.put(pktParams.OUT1_ON_OFF,"1");
        map.put(pktParams.OUT1_STATE_NORM,"0");
        map.put(pktParams.OUT2_ON_OFF,"0");
        map.put(pktParams.OUT2_STATE_NORM,"0");
        map.put(pktParams.OUT3_ON_OFF,"0");
        map.put(pktParams.OUT3_STATE_NORM,"0");
        map.put(pktParams.USER1_ON_OFF,"1");
        map.put(pktParams.USER1_PASSW,"123456");
        map.put(pktParams.USER1_PHONE,"+380502103706");
        map.put(pktParams.USER2_ON_OFF,"0");
        map.put(pktParams.USER2_PASSW,"");
        map.put(pktParams.USER2_PHONE,"");
        map.put(pktParams.USER3_ON_OFF,"0");
        map.put(pktParams.USER3_PASSW,"");
        map.put(pktParams.USER3_PHONE,"");
    }

    public static String readParameterName(int parameterNumber){
        String name;

        switch(parameterNumber){
            case settings.N_GSM_SERVER:
                name = pktParams.GSM_SERVER;
                break;
            case settings.N_GSM_MONEY_QUERY:
                name = pktParams.GSM_MONEY_QUERY;
                break;
            case settings.N_GPRS_SERVER:
                name = pktParams.GPRS_SERVER;
                break;
            case settings.N_GPRS_PORT:
                name = pktParams.GPRS_PORT;
                break;
            case settings.N_GPRS_PAGE:
                name = pktParams.GPRS_PAGE;
                break;
            case settings.N_GPRS_TIMEOUT:
                name = pktParams.GPRS_TIMEOUT;
                break;
            case settings.N_GPRS_ON_OFF:
                name = pktParams.GPRS_ON_OFF;
                break;
            case settings.N_ZONE1_ON_OFF:
                name = pktParams.ZONE1_ON_OFF;
                break;
            case settings.N_ZONE1_STATE_NORM:
                name = pktParams.ZONE1_STATE_NORM;
                break;
            case settings.N_ZONE2_ON_OFF:
                name = pktParams.ZONE2_ON_OFF;
                break;
            case settings.N_ZONE2_STATE_NORM:
                name = pktParams.ZONE2_STATE_NORM;
                break;
            case settings.N_ZONE3_ON_OFF:
                name = pktParams.ZONE3_ON_OFF;
                break;
            case settings.N_ZONE3_STATE_NORM:
                name = pktParams.ZONE3_STATE_NORM;
                break;
            case settings.N_OUT1_ON_OFF:
                name = pktParams.OUT1_ON_OFF;
                break;
            case settings.N_OUT1_STATE_NORM:
                name = pktParams.OUT1_STATE_NORM;
                break;
            case settings.N_OUT2_ON_OFF:
                name = pktParams.OUT2_ON_OFF;
                break;
            case settings.N_OUT2_STATE_NORM:
                name = pktParams.OUT2_STATE_NORM;
                break;
            case settings.N_OUT3_ON_OFF:
                name = pktParams.OUT3_ON_OFF;
                break;
            case settings.N_OUT3_STATE_NORM:
                name = pktParams.OUT3_STATE_NORM;
                break;
            case settings.N_USER1_ON_OFF:
                name = pktParams.USER1_ON_OFF;
                break;
            case settings.N_USER1_PASSW:
                name = pktParams.USER1_PASSW;
                break;
            case settings.N_USER1_PHONE:
                name = pktParams.USER1_PHONE;
                break;
            case settings.N_USER2_ON_OFF:
                name = pktParams.USER2_ON_OFF;
                break;
            case settings.N_USER2_PASSW:
                name = pktParams.USER2_PASSW;
                break;
            case settings.N_USER2_PHONE:
                name = pktParams.USER2_PHONE;
                break;
            case settings.N_USER3_ON_OFF:
                name = pktParams.USER3_ON_OFF;
                break;
            case settings.N_USER3_PASSW:
                name = pktParams.USER3_PASSW;
                break;
            case settings.N_USER3_PHONE:
                name = pktParams.USER3_PHONE;
                break;
            default:
                name = "";
        }
        return name;
    }

}
