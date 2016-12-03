import jssc.SerialPortException;
import jssc.SerialPortList;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by apu on 02.11.2016.
 */
public class GuiFrame extends JFrame{

    private Map<main.param,Object> map;

    private WriteConfiguration  writeConfig;
    private AnswerConfiguration answerConfig;

    public JPanel           panelTop;
    private JPanel          panelCenter;
    private LayoutManager   layoutMain;
    private GroupLayout     panelCenterLayout;
    private JButton         buttonOpen;
    private JButton         buttonSave;
    private JButton         buttonRead;
    public JButton          buttonWrite;
    public JButton          buttonPortOpenClose;
    private JComboBox       comboBox;
    private JLabel          labelGsmServer;
    private JTextField      inputGsmServer;
    private JLabel          labelGsmMoneyQuery;
    private JTextField      inputGsmMoneyQuery;
    private JLabel          labelGprsServer;
    private JTextField      inputGprsServer;
    private JLabel          labelGprsPort;
    private JTextField      inputGprsPort;
    private JLabel          labelGprsPage;
    private JTextField      inputGprsPage;
    private JLabel          labelGprsTimeout;
    private JTextField      inputGprsTimeout;
    private JScrollPane     scrollpane;

    public JTextArea textArea;

    private ChangeListener  propertyChangeListener;

    public Map<main.param, Object> getGui() {
        return map;
    }

    public GuiFrame(){
        //super();

        map = new HashMap<main.param,Object>();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 700);
        setLocation(200,200);
        setResizable(false);
        setTitle("GSM configurator");
        layoutMain = new BorderLayout();
        getContentPane().setLayout(layoutMain);

        buttonOpen = new JButton();
        buttonSave = new JButton();
        buttonRead = new JButton();
        buttonWrite = new JButton();
        buttonPortOpenClose = new JButton();
        comboBox = new JComboBox(main.port.portNames);
        panelTop = new JPanel();
        textArea = new JTextArea();
        scrollpane = new JScrollPane(textArea,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        labelGsmServer = new JLabelBorder();
        inputGsmServer = new LimitTextField();
        labelGsmMoneyQuery = new JLabelBorder();
        inputGsmMoneyQuery = new LimitTextField();
        labelGprsServer = new JLabelBorder();
        inputGprsServer = new LimitTextField();
        labelGprsPort = new JLabelBorder();
        inputGprsPort = new LimitTextField();
        labelGprsTimeout = new JLabelBorder();
        inputGprsTimeout = new LimitTextField();
        panelCenter = new JPanel();

        map.put(main.param.GSM_SERVER,inputGsmServer);
        map.put(main.param.GSM_MONEY_QUERY,inputGsmMoneyQuery);
        map.put(main.param.GPRS_SERVER,inputGprsServer);
        map.put(main.param.GPRS_PORT,inputGprsPort);

        buttonOpen.setText("Open File");
        buttonSave.setText("Save File");
        buttonRead.setText("Read Data");
        buttonWrite.setText("Write Data");
        buttonPortOpenClose.setText("Port Open");
        labelGsmServer.setText("GSM server address (www.kyivstar.net):  ");
        inputGsmServer.setText(settings.getSet().get(main.param.GSM_SERVER));
        labelGsmMoneyQuery.setText("GSM check money query (*111#):  ");
        inputGsmMoneyQuery.setText(settings.getSet().get(main.param.GSM_MONEY_QUERY));
        labelGprsServer.setText("GPRS server address:  ");
        inputGprsServer.setText(settings.getSet().get(main.param.GPRS_SERVER));
        labelGprsPort.setText("GPRS port number:  ");
        inputGprsPort.setText(settings.getSet().get(main.param.GPRS_PORT));
        labelGprsTimeout.setText("GPRS update timeout(seconds):  ");
        inputGprsTimeout.setText("0");

        buttonRead.setEnabled(false);
        buttonWrite.setEnabled(false);
        comboBox.setBorder(BorderFactory.createEmptyBorder(5, 25, 5, 5));
        panelTop.setLayout(new FlowLayout(FlowLayout.CENTER));
        textArea.setRows(10);
        panelTop.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panelTop.setPreferredSize(new Dimension(getContentPane().getWidth(),100));
        scrollpane.setAutoscrolls(true);
        panelCenterLayout = new GroupLayout(panelCenter);
        panelCenter.setLayout(panelCenterLayout);
        panelCenter.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        buttonOpen.addActionListener(new openButtonAction());
        buttonSave.addActionListener(new saveButtonAction());
        buttonRead.addActionListener(new readButtonAction());
        buttonWrite.addActionListener(new writeButtonAction());
        buttonPortOpenClose.addActionListener(new portOpenCloseButtonAction());
        //comboBox.addActionListener(new comboBoxAction());
        //comboBox.addItemListener(new comboBoxItem());


        textArea.addPropertyChangeListener(propertyChangeListener);
        inputGsmServer.addFocusListener(new inputFocusListener(new InputCheckInfo(inputGsmServer)));
        inputGsmServer.addCaretListener(new inputCaretListener(new InputCheckInfo(inputGsmServer)));
        inputGsmMoneyQuery.addFocusListener(new inputFocusListener(new InputCheckInfo(inputGsmMoneyQuery)));
        inputGsmMoneyQuery.addCaretListener(new inputCaretListener(new InputCheckInfo(inputGsmMoneyQuery)));

        panelTop.add(buttonOpen);
        panelTop.add(buttonSave);
        panelTop.add(buttonRead);
        panelTop.add(buttonWrite);
        panelTop.add(comboBox);
        panelTop.add(buttonPortOpenClose);

        getContentPane().add(panelTop,BorderLayout.NORTH);
        getContentPane().add(scrollpane,BorderLayout.SOUTH);

        panelCenterLayout.setHorizontalGroup(panelCenterLayout.createSequentialGroup()
                .addGroup(panelCenterLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(panelCenterLayout.createSequentialGroup()
                                        .addGroup(panelCenterLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addComponent(labelGsmServer)
                                                .addComponent(labelGsmMoneyQuery)
                                                .addComponent(labelGprsServer)
                                                .addComponent(labelGprsPort)
                                                .addComponent(labelGprsTimeout))
                                        .addGroup(panelCenterLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addComponent(inputGsmServer)
                                                .addComponent(inputGsmMoneyQuery)
                                                .addComponent(inputGprsServer)
                                                .addComponent(inputGprsPort)
                                                .addComponent(inputGprsTimeout))
                        )));
        //panelCenterLayout.linkSize(SwingConstants.HORIZONTAL, inputGsmServer, inputGsmMoneyQuery);
        panelCenterLayout.setVerticalGroup(panelCenterLayout.createSequentialGroup()
                        .addGroup(panelCenterLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(panelCenterLayout.createSequentialGroup()
                                        .addGroup(panelCenterLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(labelGsmServer)
                                                .addComponent(inputGsmServer))
                                        .addGroup(panelCenterLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(labelGsmMoneyQuery)
                                                .addComponent(inputGsmMoneyQuery))
                                        .addGroup(panelCenterLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(labelGprsServer)
                                                .addComponent(inputGprsServer))
                                        .addGroup(panelCenterLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(labelGprsPort)
                                                .addComponent(inputGprsPort))
                                        .addGroup(panelCenterLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(labelGprsTimeout)
                                                .addComponent(inputGprsTimeout))))
        );

        getContentPane().add(panelCenter,BorderLayout.CENTER);

        setVisible(true);
    }

    class ChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String pName = evt.getPropertyName();
            Object pValue = evt.getNewValue();
            if(pName.equals("TEXT_AREA")){
                textArea.append((String)pValue);
            }
        }
    }

    class portOpenCloseButtonAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(buttonPortOpenClose.getText().equals("Port Open")){
                String item = (String) comboBox.getSelectedItem();
                try {
                    if (main.port.open(item)) {
                        buttonPortOpenClose.setText("Port Close");
                        comboBox.setEnabled(false);
                        buttonRead.setEnabled(true);
                        buttonWrite.setEnabled(true);
                        textArea.append("Port " + item + " open.\r\n");
                    }
                } catch (SerialPortException ex){
                    textArea.append(ex.getPortName() + " " + ex.getExceptionType() + "\r\n");
                }
            }
            else{
                try {
                    main.port.close();
                } catch (SerialPortException e1) {
                    //e1.printStackTrace();
                    textArea.append("Error. " + e1.getPortName() + " - " + e1.getExceptionType() + "\r\n");
                } finally {
                    if(writeConfig != null) writeConfig.cancel(true);
                    if(answerConfig != null) answerConfig.cancel(true);
                    buttonPortOpenClose.setText("Port Open");
                    comboBox.setEnabled(true);
                    buttonRead.setEnabled(false);
                    buttonWrite.setEnabled(false);
                    textArea.append("Port close.\r\n");
                }
            }
        }
    }

    class comboBoxAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
/*
    class comboBoxItem implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            int state = e.getStateChange();
            if(state == ItemEvent.SELECTED) {
                comboBox.removeAllItems();
                String[] strs = SerialPortList.getPortNames();
                for (String str : strs) {
                    comboBox.addItem(str);
                }
            }
        }
    }
*/
    class saveButtonAction  implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            /*
            buttonSave.setEnabled(false);
            try {
                File file = new File("./test.xml");
                JAXBContext context = JAXBContext.newInstance(Configuration.class);
                Marshaller marshaller = context.createMarshaller();
                marshaller.marshal(main.config,file);
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                textArea.append(sdf.format(Calendar.getInstance().getTime()) + " Configuration saved to file.\r\n");
            } catch (JAXBException e1) {
                e1.printStackTrace();
            } finally {
                buttonSave.setEnabled(true);
            }*/
        }
    }

    class openButtonAction implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            /*
            buttonOpen.setEnabled(false);
            JAXBContext context = null;
            try {
                context = JAXBContext.newInstance(Configuration.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                File file = new File("./test.xml");
                main.config = (Configuration)unmarshaller.unmarshal(file);
                inputGsmServer.setText(main.config.getGsmServer());
                inputGsmMoneyQuery.setText(main.config.getGsmMoneyQuery());
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                textArea.append(sdf.format(Calendar.getInstance().getTime()) + " Configuration read from file.\r\n");
            }catch (JAXBException e1) {
                if(e1.getLinkedException() instanceof FileNotFoundException) {
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    textArea.append(sdf.format(Calendar.getInstance().getTime()) + " Error. Configuration file not found.\r\n");
                    buttonOpen.setEnabled(true);
                }else {
                    e1.printStackTrace();
                }
            }finally
            {
                buttonOpen.setEnabled(true);
            }
            */
        }
    }

    class writeButtonAction implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            buttonWrite.setEnabled(false);
            buttonRead.setEnabled(false);
            //buttonPortOpenClose.setEnabled(false);
            writeConfig = new WriteConfiguration();
            writeConfig.execute();

            //buttonWrite.setEnabled(true);
            //buttonRead.setEnabled(true);
        }
    }

    class readButtonAction implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            buttonWrite.setEnabled(false);
            buttonRead.setEnabled(false);
            //buttonPortOpenClose.setEnabled(false);
            answerConfig = new AnswerConfiguration();
            answerConfig.execute();

            //buttonWrite.setEnabled(true);
            //buttonRead.setEnabled(true);
        }
    }

    class InputCheckInfo {
        JTextField textField;
        int minLen;
        int maxLen;

        public InputCheckInfo(JTextField textField) {
            this.textField = textField;
            if(textField == inputGsmServer){
                minLen = 5;
                maxLen = 16;
            }
            if(textField == inputGsmMoneyQuery){
                minLen = 5;
                maxLen = 6;
            }
        }
    }

    class inputCaretListener implements CaretListener {

        InputCheckInfo inputCheckInfo;

        public inputCaretListener(InputCheckInfo inputCheckInfo) {
            this.inputCheckInfo = inputCheckInfo;
        }

        @Override
        public void caretUpdate(CaretEvent e) {
            InputCheck.checkLength(inputCheckInfo.textField, inputCheckInfo.minLen, inputCheckInfo.maxLen, buttonSave);
        }
    }

    class inputFocusListener implements FocusListener {

        InputCheckInfo inputCheckInfo;

        public inputFocusListener(InputCheckInfo inputCheckInfo) {
            this.inputCheckInfo = inputCheckInfo;
        }

        @Override
        public void focusGained(FocusEvent e) {
            inputCheckInfo.textField.setForeground(Color.black);
        }

        @Override
        public void focusLost(FocusEvent e) {
            InputCheck.checkLength(inputCheckInfo.textField, inputCheckInfo.minLen, inputCheckInfo.maxLen, buttonSave);
        }
    }

}
