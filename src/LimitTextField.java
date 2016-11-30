import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

/**
 * Created by apu on 02.11.2016.
 */
class LimitTextField extends JTextField {

    private final int LIMIT = 20;

    @Override
    final protected Document createDefaultModel() {
        return new PlainDocument(){
            @Override
            public void insertString(int offs,
                                     String str,
                                     AttributeSet a)
                    throws BadLocationException {
                if(LimitTextField.this.getText().length()<LIMIT){
                    super.insertString(offs, str, a);
                }
            }

        };
    }
}
