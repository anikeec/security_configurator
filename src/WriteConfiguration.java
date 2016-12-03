import javax.swing.*;
import java.util.List;

/**
 * Created by apu on 04.11.2016.
 */
public class WriteConfiguration extends SwingWorker{
    @Override
    protected Object doInBackground() throws Exception {

        byte[] data;
        String message = "";
        PacketWr packet;
        SwingWorker worker;

        main.port.comPortEnableListener(2);

        for(int ptr=0;ptr<2;ptr++) {
            switch(ptr){
                case 0:
                        data = settings.getSet().get(main.param.GSM_SERVER).getBytes();
                        message = "Gsm Server write done.";
                        break;
                case 1:
                        data = settings.getSet().get(main.param.GSM_MONEY_QUERY).getBytes();
                        message = "Gsm Money Query write done.";
                        break;
                default:
                        data = new byte[]{0};
                        message = "Error.";
            }

            packet = new PacketWrapper().wrap(ptr, ConfigCommand.COMMAND_WRITE, data);
            main.port.write(packet.data);
            main.port.setComPortHasData(false);
            for(int i=0;i<100;i++) {
                Thread.sleep(300);
                if(main.port.isComPortHasData() == true)
                {
                    if(main.port.waitOk()) {
                        publish(new String("OK.\r\n"));
                        break;
                    }
                }
            }

            Thread.sleep(200);
            publish(new String(message + "\r\n"));
        }
        main.port.comPortDisableListener();
        //main.mainF.buttonPortOpenClose.setEnabled(true);
        publish(new String("Write OK"));
        return null;
    }

    @Override
    protected void process(List chunks) {
        super.process(chunks);
        String str;
        for(int i=0;i<chunks.size();i++){
            str = chunks.get(i).toString();
            if(str.equals("Write OK")){
                main.gui.buttonWrite.setEnabled(true);
                continue;
            }
            main.gui.textArea.append(str);
        }
    }
}
