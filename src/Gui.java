import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by apu on 14.12.2016.
 */
public class Gui {
    private static ArrayList<Element> items;
    public enum guiType {COMBOBOX, CHECKBOX, TEXTFIELD};

    public Gui() {
        items = new ArrayList<Element>();
        items.add(new Element(Param.GSM_SERVER));
        items.add(new Element(Param.GSM_MONEY_QUERY));
        items.add(new Element(Param.GPRS_SERVER));
        items.add(new Element(Param.GPRS_PORT));
        items.add(new Element(Param.GPRS_PAGE));
        items.add(new Element(Param.GPRS_TIMEOUT));
        items.add(new Element(Param.GPRS_ON_OFF));
        items.add(new Element(Param.ZONE1_ON_OFF));
        items.add(new Element(Param.ZONE1_STATE_NORM));
        items.add(new Element(Param.ZONE2_ON_OFF));
        items.add(new Element(Param.ZONE2_STATE_NORM));
        items.add(new Element(Param.ZONE3_ON_OFF));
        items.add(new Element(Param.ZONE3_STATE_NORM));
        items.add(new Element(Param.OUT1_ON_OFF));
        items.add(new Element(Param.OUT1_STATE_NORM));
        items.add(new Element(Param.OUT2_ON_OFF));
        items.add(new Element(Param.OUT2_STATE_NORM));
        items.add(new Element(Param.OUT3_ON_OFF));
        items.add(new Element(Param.OUT3_STATE_NORM));
        items.add(new Element(Param.USER1_ON_OFF));
        items.add(new Element(Param.USER1_PASSW));
        items.add(new Element(Param.USER1_PHONE));
        items.add(new Element(Param.USER2_ON_OFF));
        items.add(new Element(Param.USER2_PASSW));
        items.add(new Element(Param.USER2_PHONE));
        items.add(new Element(Param.USER3_ON_OFF));
        items.add(new Element(Param.USER3_PASSW));
        items.add(new Element(Param.USER3_PHONE));
        this.init();
    }

    private void init(){
        addValueByName(Param.GSM_SERVER, "www.kyivstar.net");
        addValueByName(Param.GSM_MONEY_QUERY, "*111#");
        addValueByName(Param.GPRS_SERVER, "primula.net.ua");
        addValueByName(Param.GPRS_PORT,"80");
        addValueByName(Param.GPRS_PAGE,"");
        addValueByName(Param.GPRS_TIMEOUT,"10");
        addValueByName(Param.GPRS_ON_OFF,"1");
        addValueByName(Param.ZONE1_ON_OFF,"1");
        addValueByName(Param.ZONE1_STATE_NORM,"1");
        addValueByName(Param.ZONE2_ON_OFF,"1");
        addValueByName(Param.ZONE2_STATE_NORM,"1");
        addValueByName(Param.ZONE3_ON_OFF,"1");
        addValueByName(Param.ZONE3_STATE_NORM,"1");
        addValueByName(Param.OUT1_ON_OFF,"1");
        addValueByName(Param.OUT1_STATE_NORM,"0");
        addValueByName(Param.OUT2_ON_OFF,"0");
        addValueByName(Param.OUT2_STATE_NORM,"0");
        addValueByName(Param.OUT3_ON_OFF,"0");
        addValueByName(Param.OUT3_STATE_NORM,"0");
        addValueByName(Param.USER1_ON_OFF,"1");
        addValueByName(Param.USER1_PASSW,"123456");
        addValueByName(Param.USER1_PHONE,"+380502103706");
        addValueByName(Param.USER2_ON_OFF,"0");
        addValueByName(Param.USER2_PASSW,"");
        addValueByName(Param.USER2_PHONE,"");
        addValueByName(Param.USER3_ON_OFF,"0");
        addValueByName(Param.USER3_PASSW,"");
        addValueByName(Param.USER3_PHONE,"");
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

    public static JCheckBox getCheckboxByName(String name){
        for(int i=0;i<items.size();i++){
            if(items.get(i).name.equals(name)){
                return (JCheckBox)items.get(i).gui;
            }
        }
        return null;
    }

    public static JTextField getFieldByName(String name){
        for(int i=0;i<items.size();i++){
            if(items.get(i).name.equals(name)){
                return (JTextField)items.get(i).gui;
            }
        }
        return null;
    }

    public static JComboBox getComboByName(String name){
        for(int i=0;i<items.size();i++){
            if(items.get(i).name.equals(name)){
                return (JComboBox)items.get(i).gui;
            }
        }
        return null;
    }

    public static String getValueByName(String name){
        for(int i=0;i<items.size();i++){
            if(items.get(i).name.equals(name)){
                return items.get(i).value;
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

    public static boolean addGuiByName(String name, Object gui, guiType type){
        for(int i=0;i<items.size();i++){
            if(items.get(i).name.equals(name)){
                items.get(i).gui = gui;
                items.get(i).type = type;
                return true;
            }
        }
        return false;
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

    public static String getValueById(int id){
        if(id<items.size()){
            return items.get(id).value;
        }
        return null;
    }

    public static Object getGuiById(int id){
        if(id<items.size()){
            return items.get(id).gui;
        }
        return null;
    }

    public static guiType getGuiTypeById(int id){
        if(id<items.size()){
            return items.get(id).type;
        }
        return null;
    }

    class Element{
        String name;
        String value;
        Object gui;
        guiType type;

        public Element(String name) {
            this.name = name;
            this.gui = null;
            this.value = null;
            this.type = null;
        }
    }
}
