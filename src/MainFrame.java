import jssc.SerialPortException;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by apu on 02.11.2016.
 */
public class MainFrame extends JFrame{
    public JPanel          panelTop;
    private JPanel          panelCenter;
    private LayoutManager   layoutMain;
    private GroupLayout     panelCenterLayout;
    private JButton         buttonOpen;
    private JButton         buttonSave;
    private JButton         buttonRead;
    private JButton         buttonWrite;
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

    public JTextArea textArea;

    private WriteConfiguration  writeConfig;
    private AnswerConfiguration answerConfig;

    public MainFrame(){
        super();
        main.config = new Configuration();
    }

    public void start(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 700);
        setLocation(200,200);
        setResizable(false);
        setTitle("GSM configurator");
        layoutMain = new BorderLayout();
        getContentPane().setLayout(layoutMain);

        buttonOpen = new JButton();
        buttonOpen.setText("Open File");
        buttonOpen.addActionListener(new openButtonAction());

        buttonSave = new JButton();
        buttonSave.setText("Save File");
        buttonSave.addActionListener(new saveButtonAction());

        buttonRead = new JButton();
        buttonRead.setText("Read Data");
        buttonRead.setEnabled(false);
        buttonRead.addActionListener(new readButtonAction());

        buttonWrite = new JButton();
        buttonWrite.setText("Write Data");
        buttonWrite.setEnabled(false);
        buttonWrite.addActionListener(new writeButtonAction());

        buttonPortOpenClose = new JButton();
        buttonPortOpenClose.setText("Port Open");
        buttonPortOpenClose.addActionListener(new portOpenCloseButtonAction());

        comboBox = new JComboBox(main.port.portNames);
        comboBox.addActionListener(new comboBoxAction());
        comboBox.setBorder(BorderFactory.createEmptyBorder(5, 25, 5, 5));

        panelTop = new JPanel();
        panelTop.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelTop.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panelTop.setPreferredSize(new Dimension(getContentPane().getWidth(),100));

        panelTop.add(buttonOpen);
        panelTop.add(buttonSave);
        panelTop.add(buttonRead);
        panelTop.add(buttonWrite);
        panelTop.add(comboBox);
        panelTop.add(buttonPortOpenClose);

        getContentPane().add(panelTop,BorderLayout.NORTH);

        textArea = new JTextArea();
        textArea.setRows(10);
        JScrollPane scrollpane = new JScrollPane(textArea,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollpane.setAutoscrolls(true);

        getContentPane().add(scrollpane,BorderLayout.SOUTH);

        labelGsmServer = new JLabelBorder();
        labelGsmServer.setText("GSM server address (www.kyivstar.net):  ");

        inputGsmServer = new LimitTextField();
        inputGsmServer.setText(main.config.getGsmServer());
        inputGsmServer.addFocusListener(new inputFocusListener(new InputCheckInfo(inputGsmServer)));
        inputGsmServer.addCaretListener(new inputCaretListener(new InputCheckInfo(inputGsmServer)));

        labelGsmMoneyQuery = new JLabelBorder();
        labelGsmMoneyQuery.setText("GSM check money query (*111#):  ");

        inputGsmMoneyQuery = new LimitTextField();
        inputGsmMoneyQuery.setText(main.config.getGsmMoneyQuery());
        inputGsmMoneyQuery.addFocusListener(new inputFocusListener(new InputCheckInfo(inputGsmMoneyQuery)));
        inputGsmMoneyQuery.addCaretListener(new inputCaretListener(new InputCheckInfo(inputGsmMoneyQuery)));

        labelGprsServer = new JLabelBorder();
        labelGprsServer.setText("GPRS server address:  ");

        inputGprsServer = new LimitTextField();
        inputGprsServer.setText(main.config.getGprsServer());

        labelGprsPort = new JLabelBorder();
        labelGprsPort.setText("GPRS port number:  ");

        inputGprsPort = new LimitTextField();
        inputGprsPort.setText(String.valueOf(main.config.getGprsPort()));

        labelGprsTimeout = new JLabelBorder();
        labelGprsTimeout.setText("GPRS update timeout(seconds):  ");

        inputGprsTimeout = new LimitTextField();
        inputGprsTimeout.setText(String.valueOf(main.config.getGprsTimeout()));


        panelCenter = new JPanel();
        panelCenterLayout = new GroupLayout(panelCenter);
        panelCenter.setLayout(panelCenterLayout);
        panelCenter.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

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


    class portOpenCloseButtonAction implements ActionListener{

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
                if(main.port.close()) {
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

    class saveButtonAction  implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
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
            }
        }
    }

    class openButtonAction implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
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

    class inputCaretListener implements CaretListener{

        InputCheckInfo inputCheckInfo;

        public inputCaretListener(InputCheckInfo inputCheckInfo) {
            this.inputCheckInfo = inputCheckInfo;
        }

        @Override
        public void caretUpdate(CaretEvent e) {
            InputCheck.checkLength(inputCheckInfo.textField, inputCheckInfo.minLen, inputCheckInfo.maxLen, buttonSave);
        }
    }

    class inputFocusListener implements FocusListener{

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
