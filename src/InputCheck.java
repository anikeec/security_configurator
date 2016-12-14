import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.Map;

public class InputCheck {
    public static boolean checkLength(JTextField textField, int minLen, int maxLen, JButton saveButton){
        boolean error = false;
        String text = textField.getText();

        if((text.length()<minLen)||(text.length() > maxLen)) {
            textField.setForeground(Color.red);
            saveButton.setEnabled(false);
            error = true;
        }
        if(error == false){
            //main.config.setGsmServer(text);
            String key = "";
            Map<String,Object> map = main.gui.getGui();
            Iterator<Map.Entry<String,Object>> it = map.entrySet().iterator();
            Map.Entry<String,Object> entry;
            while(it.hasNext()){
                entry = it.next();
                if(entry.getValue() == textField){
                    key = entry.getKey();
                    break;
                }
            }
            if(key != "") {

            }
            textField.setForeground(Color.black);
            saveButton.setEnabled(true);
        }

        return true;
    }
}
