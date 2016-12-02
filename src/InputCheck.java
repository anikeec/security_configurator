import javax.swing.*;
import java.awt.*;

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
            settings.getSet().put(main.param.GSM_SERVER,text);
            textField.setForeground(Color.black);
            saveButton.setEnabled(true);
        }

        return true;
    }
}
