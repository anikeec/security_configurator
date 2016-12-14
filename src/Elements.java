import java.util.ArrayList;

/**
 * Created by apu on 14.12.2016.
 */
public class Elements {
    private static ArrayList<Element> items;

    public Elements() {
        items = new ArrayList<Element>();
        items.add(new Element(pktParams.GSM_SERVER));
        items.add(new Element(pktParams.GSM_MONEY_QUERY));
        items.add(new Element(pktParams.GPRS_SERVER));
        items.add(new Element(pktParams.GPRS_PORT));
        items.add(new Element(pktParams.GPRS_PAGE));
        items.add(new Element(pktParams.GPRS_TIMEOUT));
        items.add(new Element(pktParams.GPRS_ON_OFF));
        items.add(new Element(pktParams.ZONE1_ON_OFF));
        items.add(new Element(pktParams.ZONE1_STATE_NORM));
        items.add(new Element(pktParams.ZONE2_ON_OFF));
        items.add(new Element(pktParams.ZONE2_STATE_NORM));
        items.add(new Element(pktParams.ZONE3_ON_OFF));
        items.add(new Element(pktParams.ZONE3_STATE_NORM));
        items.add(new Element(pktParams.OUT1_ON_OFF));
        items.add(new Element(pktParams.OUT1_STATE_NORM));
        items.add(new Element(pktParams.OUT2_ON_OFF));
        items.add(new Element(pktParams.OUT2_STATE_NORM));
        items.add(new Element(pktParams.OUT3_ON_OFF));
        items.add(new Element(pktParams.OUT3_STATE_NORM));
        items.add(new Element(pktParams.USER1_ON_OFF));
        items.add(new Element(pktParams.USER1_PASSW));
        items.add(new Element(pktParams.USER1_PHONE));
        items.add(new Element(pktParams.USER2_ON_OFF));
        items.add(new Element(pktParams.USER2_PASSW));
        items.add(new Element(pktParams.USER2_PHONE));
        items.add(new Element(pktParams.USER3_ON_OFF));
        items.add(new Element(pktParams.USER3_PASSW));
        items.add(new Element(pktParams.USER3_PHONE));
        this.init();
    }

    private void init(){
        addValueByName(pktParams.GSM_SERVER, "www.kyivstar.net");
        addValueByName(pktParams.GSM_MONEY_QUERY, "*111#");
        addValueByName(pktParams.GPRS_SERVER, "primula.net.ua");
        addValueByName(pktParams.GPRS_PORT,"80");
        addValueByName(pktParams.GPRS_PAGE,"");
        addValueByName(pktParams.GPRS_TIMEOUT,"10");
        addValueByName(pktParams.GPRS_ON_OFF,"1");
        addValueByName(pktParams.ZONE1_ON_OFF,"1");
        addValueByName(pktParams.ZONE1_STATE_NORM,"1");
        addValueByName(pktParams.ZONE2_ON_OFF,"1");
        addValueByName(pktParams.ZONE2_STATE_NORM,"1");
        addValueByName(pktParams.ZONE3_ON_OFF,"1");
        addValueByName(pktParams.ZONE3_STATE_NORM,"1");
        addValueByName(pktParams.OUT1_ON_OFF,"1");
        addValueByName(pktParams.OUT1_STATE_NORM,"0");
        addValueByName(pktParams.OUT2_ON_OFF,"0");
        addValueByName(pktParams.OUT2_STATE_NORM,"0");
        addValueByName(pktParams.OUT3_ON_OFF,"0");
        addValueByName(pktParams.OUT3_STATE_NORM,"0");
        addValueByName(pktParams.USER1_ON_OFF,"1");
        addValueByName(pktParams.USER1_PASSW,"123456");
        addValueByName(pktParams.USER1_PHONE,"+380502103706");
        addValueByName(pktParams.USER2_ON_OFF,"0");
        addValueByName(pktParams.USER2_PASSW,"");
        addValueByName(pktParams.USER2_PHONE,"");
        addValueByName(pktParams.USER3_ON_OFF,"0");
        addValueByName(pktParams.USER3_PASSW,"");
        addValueByName(pktParams.USER3_PHONE,"");
    }

    private static ArrayList<Element> Items() {
        return items;
    }

    public static Object getGuiByName(String name){
        for(int i=0;i<items.size();i++){
            if(items.get(i).name.equals(name)){
                return items.get(i).gui;
            }
        }
        return null;
    }

    public static boolean addGuiByName(String name, Object gui){
        for(int i=0;i<items.size();i++){
            if(items.get(i).name.equals(name)){
                items.get(i).gui = gui;
                return true;
            }
        }
        return false;
    }

    public static String getValueByName(String name){
        for(int i=0;i<items.size();i++){
            if(items.get(i).name.equals(name)){
                return items.get(i).value;
            }
        }
        return null;
    }

    public static boolean addValueByName(String name, String value){
        for(int i=0;i<items.size();i++){
            if(items.get(i).name.equals(name)){
                items.get(i).value = value;
                return true;
            }
        }
        return false;
    }

    public static String getNameByGui(Object obj){
        for(int i=0;i<items.size();i++){
            if(items.get(i).gui == obj){
                return items.get(i).name;
            }
        }
        return null;
    }

    public static String getNameById(int id){
        if(id<items.size()){
                return items.get(id).name;
        }
        return null;
    }

    class Element{
        String name;
        String value;
        Object gui;

        public Element(String name) {
            this.name = name;
            this.gui = null;
            this.value = null;
        }
    }
}
