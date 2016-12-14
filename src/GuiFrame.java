import jssc.SerialPortException;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * Created by apu on 02.11.2016.
 */
public class GuiFrame extends JFrame{

    private final int COMBO_BOX_SIZE = 150;
    private final int CHECK_BOX_SIZE = 80;
    private final int LABEL_F_SIZE = 100;
    private final int LABEL_B_SIZE = 100;
    private final int FIELD_BIG_SIZE = 150;
    private final int FIELD_SM_SIZE = 120;
    private final int GAP_B_SIZE = 30;
    private final String[] ZONES_STATES = new String[]{"Open","Close"};
    private final String[] OUTS_STATES = new String[]{"On","Off"};

    private WriteConfiguration  writeConfig;
    private AnswerConfiguration answerConfig;
    private ReadConfiguration   readConfig;

    public JPanel           panelTop;
    private JPanel          panelCenter;
    private LayoutManager   layoutMain;
    private GroupLayout     panelCenterLayout;
    private JButton         buttonOpen;
    private JButton         buttonSave;
    public JButton          buttonRead;
    public JButton          buttonWrite;
    public JButton          buttonPortOpenClose;
    private JComboBox       comboBox;
    private JLabel          labelGprsPage;
    private JTextField      inputGprsPage;
    private JScrollPane     scrollpane;

    public JTextArea textArea;

    private ChangeListener  propertyChangeListener;

    ArrayList<JLabel>       labelList = new ArrayList<JLabel>();
    ArrayList<JTextField>   fieldsList = new ArrayList<JTextField>();
    ArrayList<JCheckBox>    checkBoxesList = new ArrayList<JCheckBox>();
    ArrayList<JComboBox>    comboBoxesList = new ArrayList<JComboBox>();


    public void inputSetText(JTextField field, String text){
        field.setText(text);
    }

    public GuiFrame(){
        //super();

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

        for(int i=0;i<20;i++){
            labelList.add(new JLabelBorder());
            labelList.get(i).setText("label" + i);
        }

        for(int i=0;i<20;i++){
            fieldsList.add(new LimitTextField());
            fieldsList.get(i).setText("field" + i);
        }

        for(int i=0;i<20;i++){
            checkBoxesList.add(new JCheckBox());
            checkBoxesList.get(i).setText("check" + i);
        }

        for(int i=0;i<20;i++){
            comboBoxesList.add(new JComboBox());
        }

        panelCenter = new JPanel();

        buttonOpen.setText("Open File");
        buttonSave.setText("Save File");
        buttonRead.setText("Read Data");
        buttonWrite.setText("Write Data");
        buttonPortOpenClose.setText("Port Open");

        initGuiElements();
        initLabels();
        initComboBoxes();
        initCheckBoxes();
        initFields();

        buttonRead.setEnabled(false);
        buttonWrite.setEnabled(false);
        comboBox.setBorder(BorderFactory.createEmptyBorder(5, 25, 5, 5));
        panelTop.setLayout(new FlowLayout(FlowLayout.CENTER));
        textArea.setRows(10);
        panelTop.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panelTop.setPreferredSize(new Dimension(getContentPane().getWidth(),100));
        scrollpane.setAutoscrolls(true);

        buttonOpen.addActionListener(new openButtonAction());
        buttonSave.addActionListener(new saveButtonAction());
        buttonRead.addActionListener(new readButtonAction());
        buttonWrite.addActionListener(new writeButtonAction());
        buttonPortOpenClose.addActionListener(new portOpenCloseButtonAction());

        textArea.addPropertyChangeListener(propertyChangeListener);

        panelTop.add(buttonOpen);
        panelTop.add(buttonSave);
        panelTop.add(buttonRead);
        panelTop.add(buttonWrite);
        panelTop.add(comboBox);
        panelTop.add(buttonPortOpenClose);

        getContentPane().add(panelTop,BorderLayout.NORTH);
        getContentPane().add(scrollpane,BorderLayout.SOUTH);

        panelCenterLayout = new GroupLayout(panelCenter);
        panelCenter.setLayout(panelCenterLayout);
        panelCenter.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        panelCenterLayout.setHorizontalGroup(
                panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelCenterLayout.createSequentialGroup()
                                .addGroup(panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(panelCenterLayout.createSequentialGroup()
                                                .addGroup(panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(panelCenterLayout.createSequentialGroup()
                                                                .addComponent(checkBoxesList.get(1), CHECK_BOX_SIZE, CHECK_BOX_SIZE, CHECK_BOX_SIZE)
                                                                .addComponent(labelList.get(6), LABEL_F_SIZE, LABEL_F_SIZE, LABEL_F_SIZE)
                                                                .addComponent(comboBoxesList.get(0), COMBO_BOX_SIZE, COMBO_BOX_SIZE, COMBO_BOX_SIZE))
                                                        .addGroup(panelCenterLayout.createSequentialGroup()
                                                                .addComponent(checkBoxesList.get(0), CHECK_BOX_SIZE, CHECK_BOX_SIZE, CHECK_BOX_SIZE)
                                                                .addGroup(panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCenterLayout.createSequentialGroup()
                                                                                .addComponent(labelList.get(2), LABEL_F_SIZE, LABEL_F_SIZE, LABEL_F_SIZE)
                                                                                .addComponent(fieldsList.get(2), FIELD_BIG_SIZE, FIELD_BIG_SIZE, FIELD_BIG_SIZE)
                                                                                .addGap(GAP_B_SIZE, GAP_B_SIZE, GAP_B_SIZE)
                                                                                .addComponent(labelList.get(3), LABEL_B_SIZE, LABEL_B_SIZE, LABEL_B_SIZE))
                                                                        .addGroup(panelCenterLayout.createSequentialGroup()
                                                                                .addComponent(labelList.get(4), LABEL_F_SIZE, LABEL_F_SIZE, LABEL_F_SIZE)
                                                                                .addComponent(fieldsList.get(4), FIELD_BIG_SIZE, FIELD_BIG_SIZE, FIELD_BIG_SIZE)
                                                                                .addGap(GAP_B_SIZE, GAP_B_SIZE, GAP_B_SIZE)
                                                                                .addComponent(labelList.get(5), LABEL_B_SIZE, LABEL_B_SIZE, LABEL_B_SIZE)))))
                                                .addGroup(panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(fieldsList.get(5), FIELD_SM_SIZE, FIELD_SM_SIZE, FIELD_SM_SIZE)
                                                        .addComponent(fieldsList.get(3), FIELD_SM_SIZE, FIELD_SM_SIZE, FIELD_SM_SIZE))
                                                )
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelCenterLayout.createSequentialGroup()
                                                        .addGroup(panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelCenterLayout.createSequentialGroup()
                                                                        .addComponent(checkBoxesList.get(9), CHECK_BOX_SIZE, CHECK_BOX_SIZE, CHECK_BOX_SIZE)
                                                                        .addComponent(labelList.get(16), LABEL_F_SIZE, LABEL_F_SIZE, LABEL_F_SIZE)
                                                                        .addComponent(fieldsList.get(10), FIELD_BIG_SIZE, FIELD_BIG_SIZE, FIELD_BIG_SIZE)
                                                                        .addGap(GAP_B_SIZE, GAP_B_SIZE, GAP_B_SIZE)
                                                                        .addComponent(labelList.get(17), LABEL_B_SIZE, LABEL_B_SIZE, LABEL_B_SIZE)
                                                                        .addComponent(fieldsList.get(11), FIELD_SM_SIZE, FIELD_SM_SIZE, FIELD_SM_SIZE))
                                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelCenterLayout.createSequentialGroup()
                                                                        .addComponent(checkBoxesList.get(8), CHECK_BOX_SIZE, CHECK_BOX_SIZE, CHECK_BOX_SIZE)
                                                                        .addComponent(labelList.get(14), LABEL_F_SIZE, LABEL_F_SIZE, LABEL_F_SIZE)
                                                                        .addComponent(fieldsList.get(8), FIELD_BIG_SIZE, FIELD_BIG_SIZE, FIELD_BIG_SIZE)
                                                                        .addGap(GAP_B_SIZE, GAP_B_SIZE, GAP_B_SIZE)
                                                                        .addComponent(labelList.get(15), LABEL_B_SIZE, LABEL_B_SIZE, LABEL_B_SIZE)
                                                                        .addComponent(fieldsList.get(9), FIELD_SM_SIZE, FIELD_SM_SIZE, FIELD_SM_SIZE))
                                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelCenterLayout.createSequentialGroup()
                                                                        .addComponent(checkBoxesList.get(7), CHECK_BOX_SIZE, CHECK_BOX_SIZE, CHECK_BOX_SIZE)
                                                                        .addComponent(labelList.get(12), LABEL_F_SIZE, LABEL_F_SIZE, LABEL_F_SIZE)
                                                                        .addComponent(fieldsList.get(6), FIELD_BIG_SIZE, FIELD_BIG_SIZE, FIELD_BIG_SIZE)
                                                                        .addGap(GAP_B_SIZE, GAP_B_SIZE, GAP_B_SIZE)
                                                                        .addComponent(labelList.get(13), LABEL_B_SIZE, LABEL_B_SIZE, LABEL_B_SIZE)
                                                                        .addComponent(fieldsList.get(7), FIELD_SM_SIZE, FIELD_SM_SIZE, FIELD_SM_SIZE))
                                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelCenterLayout.createSequentialGroup()
                                                                        .addComponent(checkBoxesList.get(2), CHECK_BOX_SIZE, CHECK_BOX_SIZE, CHECK_BOX_SIZE)
                                                                        .addComponent(labelList.get(7), LABEL_F_SIZE, LABEL_F_SIZE, LABEL_F_SIZE)
                                                                        .addComponent(comboBoxesList.get(1), COMBO_BOX_SIZE, COMBO_BOX_SIZE, COMBO_BOX_SIZE))
                                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelCenterLayout.createSequentialGroup()
                                                                        .addComponent(checkBoxesList.get(3), CHECK_BOX_SIZE, CHECK_BOX_SIZE, CHECK_BOX_SIZE)
                                                                        .addComponent(labelList.get(8), LABEL_F_SIZE, LABEL_F_SIZE, LABEL_F_SIZE)
                                                                        .addComponent(comboBoxesList.get(2), COMBO_BOX_SIZE, COMBO_BOX_SIZE, COMBO_BOX_SIZE))
                                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelCenterLayout.createSequentialGroup()
                                                                        .addComponent(checkBoxesList.get(4), CHECK_BOX_SIZE, CHECK_BOX_SIZE, CHECK_BOX_SIZE)
                                                                        .addComponent(labelList.get(9), LABEL_F_SIZE, LABEL_F_SIZE, LABEL_F_SIZE)
                                                                        .addComponent(comboBoxesList.get(3), COMBO_BOX_SIZE, COMBO_BOX_SIZE, COMBO_BOX_SIZE))
                                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelCenterLayout.createSequentialGroup()
                                                                        .addComponent(checkBoxesList.get(5), CHECK_BOX_SIZE, CHECK_BOX_SIZE, CHECK_BOX_SIZE)
                                                                        .addComponent(labelList.get(10), LABEL_F_SIZE, LABEL_F_SIZE, LABEL_F_SIZE)
                                                                        .addComponent(comboBoxesList.get(4), COMBO_BOX_SIZE, COMBO_BOX_SIZE, COMBO_BOX_SIZE))
                                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelCenterLayout.createSequentialGroup()
                                                                        .addComponent(checkBoxesList.get(6), CHECK_BOX_SIZE, CHECK_BOX_SIZE, CHECK_BOX_SIZE)
                                                                        .addComponent(labelList.get(11), LABEL_F_SIZE, LABEL_F_SIZE, LABEL_F_SIZE)
                                                                        .addComponent(comboBoxesList.get(5), COMBO_BOX_SIZE, COMBO_BOX_SIZE, COMBO_BOX_SIZE))
                                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelCenterLayout.createSequentialGroup()
                                                                        .addGap(CHECK_BOX_SIZE, CHECK_BOX_SIZE, CHECK_BOX_SIZE)
                                                                        .addComponent(labelList.get(0), LABEL_F_SIZE, LABEL_F_SIZE, LABEL_F_SIZE)
                                                                        .addComponent(fieldsList.get(0), FIELD_BIG_SIZE, FIELD_BIG_SIZE, FIELD_BIG_SIZE)
                                                                        .addGap(GAP_B_SIZE, GAP_B_SIZE, GAP_B_SIZE)
                                                                        .addComponent(labelList.get(1), LABEL_B_SIZE, LABEL_B_SIZE, LABEL_B_SIZE)
                                                                        .addComponent(fieldsList.get(1), FIELD_SM_SIZE, FIELD_SM_SIZE, FIELD_SM_SIZE)))
                                                )))
        );
        panelCenterLayout.setVerticalGroup(
                panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelCenterLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelList.get(0))
                                        .addComponent(fieldsList.get(0))
                                        .addComponent(labelList.get(1))
                                        .addComponent(fieldsList.get(1)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelList.get(2))
                                        .addComponent(fieldsList.get(2))
                                        .addComponent(labelList.get(3))
                                        .addComponent(fieldsList.get(3))
                                        .addComponent(checkBoxesList.get(0), javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelList.get(4))
                                        .addComponent(fieldsList.get(4))
                                        .addComponent(labelList.get(5))
                                        .addComponent(fieldsList.get(5)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelList.get(6))
                                        .addComponent(checkBoxesList.get(1))
                                        .addComponent(comboBoxesList.get(0)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelList.get(7))
                                        .addComponent(checkBoxesList.get(2))
                                        .addComponent(comboBoxesList.get(1)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelList.get(8))
                                        .addComponent(checkBoxesList.get(3))
                                        .addComponent(comboBoxesList.get(2)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelList.get(9))
                                        .addComponent(checkBoxesList.get(4))
                                        .addComponent(comboBoxesList.get(3)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelList.get(10))
                                        .addComponent(checkBoxesList.get(5))
                                        .addComponent(comboBoxesList.get(4)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelList.get(11))
                                        .addComponent(checkBoxesList.get(6))
                                        .addComponent(comboBoxesList.get(5)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelList.get(12))
                                        .addComponent(fieldsList.get(6))
                                        .addComponent(labelList.get(13))
                                        .addComponent(fieldsList.get(7))
                                        .addComponent(checkBoxesList.get(7)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelList.get(14))
                                        .addComponent(fieldsList.get(8))
                                        .addComponent(labelList.get(15))
                                        .addComponent(fieldsList.get(9))
                                        .addComponent(checkBoxesList.get(8)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelList.get(16))
                                        .addComponent(fieldsList.get(10))
                                        .addComponent(labelList.get(17))
                                        .addComponent(fieldsList.get(11))
                                        .addComponent(checkBoxesList.get(9)))
                                .addContainerGap(137, Short.MAX_VALUE))
        );

        getContentPane().add(panelCenter, BorderLayout.CENTER);

        setVisible(true);
    }

    void initLabels() {
        labelList.get(0).setText("GSM server");
        labelList.get(1).setText("GSM m.query");
        labelList.get(2).setText("GPRS server");
        labelList.get(3).setText("GPRS port");
        labelList.get(4).setText("GPRS page");
        labelList.get(5).setText("GPRS time(s)");
        labelList.get(6).setText("State normal");
        labelList.get(7).setText("State normal");
        labelList.get(8).setText("State normal");
        labelList.get(9).setText("State normal");
        labelList.get(10).setText("State normal");
        labelList.get(11).setText("State normal");
        labelList.get(12).setText("Password");
        labelList.get(13).setText("          Phone");
        labelList.get(14).setText("Password");
        labelList.get(15).setText("          Phone");
        labelList.get(16).setText("Password");
        labelList.get(17).setText("          Phone");
    }
    void initComboBoxes(){
        for(int i=0;i<ZONES_STATES.length;i++) {
            Gui.getComboByName(pktParams.ZONE1_STATE_NORM).addItem(ZONES_STATES[i]);
            Gui.getComboByName(pktParams.ZONE2_STATE_NORM).addItem(ZONES_STATES[i]);
            Gui.getComboByName(pktParams.ZONE3_STATE_NORM).addItem(ZONES_STATES[i]);
        }
        for(int i=0;i<OUTS_STATES.length;i++) {
            Gui.getComboByName(pktParams.OUT1_STATE_NORM).addItem(OUTS_STATES[i]);
            Gui.getComboByName(pktParams.OUT1_STATE_NORM).addItem(OUTS_STATES[i]);
            Gui.getComboByName(pktParams.OUT1_STATE_NORM).addItem(OUTS_STATES[i]);
        }
    }

    void initCheckBoxes(){

        Gui.getCheckboxByName(pktParams.GPRS_ON_OFF).setText("GPRS on");
        Gui.getCheckboxByName(pktParams.ZONE1_ON_OFF).setText("Zone 1");
        Gui.getCheckboxByName(pktParams.ZONE2_ON_OFF).setText("Zone 2");
        Gui.getCheckboxByName(pktParams.ZONE3_ON_OFF).setText("Zone 3");
        Gui.getCheckboxByName(pktParams.OUT1_ON_OFF).setText("Out 1");
        Gui.getCheckboxByName(pktParams.OUT2_ON_OFF).setText("Out 2");
        Gui.getCheckboxByName(pktParams.OUT3_ON_OFF).setText("Out 3");
        Gui.getCheckboxByName(pktParams.USER1_ON_OFF).setText("User 1");
        Gui.getCheckboxByName(pktParams.USER2_ON_OFF).setText("User 2");
        Gui.getCheckboxByName(pktParams.USER3_ON_OFF).setText("User 3");

        if(Gui.getValueByName(pktParams.GPRS_ON_OFF).equals("1")) {
            Gui.getCheckboxByName(pktParams.GPRS_ON_OFF).setSelected(true);
        } else {
            Gui.getCheckboxByName(pktParams.GPRS_ON_OFF).setSelected(false);
        }
        if(Gui.getValueByName(pktParams.ZONE1_ON_OFF).equals("1")) {
            Gui.getCheckboxByName(pktParams.ZONE1_ON_OFF).setSelected(true);
            Gui.getComboByName(pktParams.ZONE1_STATE_NORM).setEnabled(true);
        } else {
            Gui.getCheckboxByName(pktParams.ZONE1_ON_OFF).setSelected(false);
            Gui.getComboByName(pktParams.ZONE1_STATE_NORM).setEnabled(false);
        }
        if(Gui.getValueByName(pktParams.ZONE2_ON_OFF).equals("1")) {
            Gui.getCheckboxByName(pktParams.ZONE2_ON_OFF).setSelected(true);
            Gui.getComboByName(pktParams.ZONE2_STATE_NORM).setEnabled(true);
        } else {
            Gui.getCheckboxByName(pktParams.ZONE2_ON_OFF).setSelected(false);
            Gui.getComboByName(pktParams.ZONE2_STATE_NORM).setEnabled(false);
        }
        if(Gui.getValueByName(pktParams.ZONE3_ON_OFF).equals("1")) {
            Gui.getCheckboxByName(pktParams.ZONE3_ON_OFF).setSelected(true);
            Gui.getComboByName(pktParams.ZONE3_STATE_NORM).setEnabled(true);
        } else {
            Gui.getCheckboxByName(pktParams.ZONE3_ON_OFF).setSelected(false);
            Gui.getComboByName(pktParams.ZONE3_STATE_NORM).setEnabled(false);
        }
        if(Gui.getValueByName(pktParams.OUT1_ON_OFF).equals("1")) {
            Gui.getCheckboxByName(pktParams.OUT1_ON_OFF).setSelected(true);
            Gui.getComboByName(pktParams.OUT1_STATE_NORM).setEnabled(true);
        } else {
            Gui.getCheckboxByName(pktParams.OUT1_ON_OFF).setSelected(false);
            Gui.getComboByName(pktParams.OUT1_STATE_NORM).setEnabled(false);
        }
        if(Gui.getValueByName(pktParams.OUT2_ON_OFF).equals("1")) {
            Gui.getCheckboxByName(pktParams.OUT2_ON_OFF).setSelected(true);
            Gui.getComboByName(pktParams.OUT2_STATE_NORM).setEnabled(true);
        } else {
            Gui.getCheckboxByName(pktParams.OUT2_ON_OFF).setSelected(false);
            Gui.getComboByName(pktParams.OUT2_STATE_NORM).setEnabled(false);
        }
        if(Gui.getValueByName(pktParams.OUT3_ON_OFF).equals("1")) {
            Gui.getCheckboxByName(pktParams.OUT3_ON_OFF).setSelected(true);
            Gui.getComboByName(pktParams.OUT3_STATE_NORM).setEnabled(true);
        } else {
            Gui.getCheckboxByName(pktParams.OUT3_ON_OFF).setSelected(false);
            Gui.getComboByName(pktParams.OUT3_STATE_NORM).setEnabled(false);
        }
        if(Gui.getValueByName(pktParams.USER1_ON_OFF).equals("1")) {
            Gui.getCheckboxByName(pktParams.USER1_ON_OFF).setSelected(true);
            Gui.getFieldByName(pktParams.USER1_PASSW).setEnabled(true);
            Gui.getFieldByName(pktParams.USER1_PHONE).setEnabled(true);
        } else {
            Gui.getCheckboxByName(pktParams.USER1_ON_OFF).setSelected(false);
            Gui.getFieldByName(pktParams.USER1_PASSW).setEnabled(false);
            Gui.getFieldByName(pktParams.USER1_PHONE).setEnabled(false);
        }
        if(Gui.getValueByName(pktParams.USER2_ON_OFF).equals("1")) {
            Gui.getCheckboxByName(pktParams.USER2_ON_OFF).setSelected(true);
            Gui.getFieldByName(pktParams.USER2_PASSW).setEnabled(true);
            Gui.getFieldByName(pktParams.USER2_PHONE).setEnabled(true);
        } else {
            Gui.getCheckboxByName(pktParams.USER2_ON_OFF).setSelected(false);
            Gui.getFieldByName(pktParams.USER2_PASSW).setEnabled(false);
            Gui.getFieldByName(pktParams.USER2_PHONE).setEnabled(false);
        }
        if(Gui.getValueByName(pktParams.USER3_ON_OFF).equals("1")) {
            Gui.getCheckboxByName(pktParams.USER3_ON_OFF).setSelected(true);
            Gui.getFieldByName(pktParams.USER3_PASSW).setEnabled(true);
            Gui.getFieldByName(pktParams.USER3_PHONE).setEnabled(true);
        } else {
            Gui.getCheckboxByName(pktParams.USER3_ON_OFF).setSelected(false);
            Gui.getFieldByName(pktParams.USER3_PASSW).setEnabled(false);
            Gui.getFieldByName(pktParams.USER3_PHONE).setEnabled(false);
        }
    }

    void initFields(){
        Object obj = null;
        JTextField txt = null;
        for(int i=0;i<1000;i++){
            obj = Gui.getGuiById(i);
            if(obj == null) break;
            if(Gui.getGuiTypeById(i) != Gui.guiType.TEXTFIELD)    continue;
            txt = (JTextField)obj;
            txt.setText(Gui.getValueById(i));
            txt.addFocusListener(new inputFocusListener(new InputCheckInfo(txt)));
            txt.addCaretListener(new inputCaretListener(new InputCheckInfo(txt)));
        };
    }

    void initGuiElements(){
        Gui.addGuiByName(pktParams.GSM_SERVER, fieldsList.get(0), Gui.guiType.TEXTFIELD);
        Gui.addGuiByName(pktParams.GSM_MONEY_QUERY, fieldsList.get(1), Gui.guiType.TEXTFIELD);
        Gui.addGuiByName(pktParams.GPRS_SERVER, fieldsList.get(2), Gui.guiType.TEXTFIELD);
        Gui.addGuiByName(pktParams.GPRS_PORT, fieldsList.get(3), Gui.guiType.TEXTFIELD);
        Gui.addGuiByName(pktParams.GPRS_PAGE, fieldsList.get(4), Gui.guiType.TEXTFIELD);
        Gui.addGuiByName(pktParams.GPRS_TIMEOUT, fieldsList.get(5), Gui.guiType.TEXTFIELD);
        Gui.addGuiByName(pktParams.USER1_PASSW, fieldsList.get(6), Gui.guiType.TEXTFIELD);
        Gui.addGuiByName(pktParams.USER1_PHONE, fieldsList.get(7), Gui.guiType.TEXTFIELD);
        Gui.addGuiByName(pktParams.USER2_PASSW, fieldsList.get(8), Gui.guiType.TEXTFIELD);
        Gui.addGuiByName(pktParams.USER2_PHONE, fieldsList.get(9), Gui.guiType.TEXTFIELD);
        Gui.addGuiByName(pktParams.USER3_PASSW, fieldsList.get(10), Gui.guiType.TEXTFIELD);
        Gui.addGuiByName(pktParams.USER3_PHONE, fieldsList.get(11), Gui.guiType.TEXTFIELD);
        Gui.addGuiByName(pktParams.GPRS_ON_OFF, checkBoxesList.get(0), Gui.guiType.CHECKBOX);
        Gui.addGuiByName(pktParams.ZONE1_ON_OFF, checkBoxesList.get(1), Gui.guiType.CHECKBOX);
        Gui.addGuiByName(pktParams.ZONE2_ON_OFF, checkBoxesList.get(2), Gui.guiType.CHECKBOX);
        Gui.addGuiByName(pktParams.ZONE3_ON_OFF, checkBoxesList.get(3), Gui.guiType.CHECKBOX);
        Gui.addGuiByName(pktParams.OUT1_ON_OFF, checkBoxesList.get(4), Gui.guiType.CHECKBOX);
        Gui.addGuiByName(pktParams.OUT2_ON_OFF, checkBoxesList.get(5), Gui.guiType.CHECKBOX);
        Gui.addGuiByName(pktParams.OUT3_ON_OFF, checkBoxesList.get(6), Gui.guiType.CHECKBOX);
        Gui.addGuiByName(pktParams.USER1_ON_OFF, checkBoxesList.get(7), Gui.guiType.CHECKBOX);
        Gui.addGuiByName(pktParams.USER2_ON_OFF, checkBoxesList.get(8), Gui.guiType.CHECKBOX);
        Gui.addGuiByName(pktParams.USER3_ON_OFF, checkBoxesList.get(9), Gui.guiType.CHECKBOX);
        Gui.addGuiByName(pktParams.ZONE1_STATE_NORM, comboBoxesList.get(0), Gui.guiType.COMBOBOX);
        Gui.addGuiByName(pktParams.ZONE2_STATE_NORM, comboBoxesList.get(1), Gui.guiType.COMBOBOX);
        Gui.addGuiByName(pktParams.ZONE3_STATE_NORM, comboBoxesList.get(2), Gui.guiType.COMBOBOX);
        Gui.addGuiByName(pktParams.OUT1_STATE_NORM, comboBoxesList.get(3), Gui.guiType.COMBOBOX);
        Gui.addGuiByName(pktParams.OUT2_STATE_NORM, comboBoxesList.get(4), Gui.guiType.COMBOBOX);
        Gui.addGuiByName(pktParams.OUT3_STATE_NORM, comboBoxesList.get(5), Gui.guiType.COMBOBOX);
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
                    textArea.append("Error. " + ex.getPortName() + " - " + ex.getExceptionType() + "\r\n");
                }
            }
            else{
                try {
                    main.port.close();
                } catch (SerialPortException e1) {
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
            writeConfig = new WriteConfiguration();
            writeConfig.execute();
        }
    }

    class readButtonAction implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            buttonWrite.setEnabled(false);
            buttonRead.setEnabled(false);
            //answerConfig = new AnswerConfiguration();
            //answerConfig.execute();
            readConfig = new ReadConfiguration();
            readConfig.execute();

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
            if( (textField == Gui.getGuiByName(pktParams.GSM_SERVER)) || // servers & page
                (textField == Gui.getGuiByName(pktParams.GPRS_SERVER)) ||
                (textField == Gui.getGuiByName(pktParams.GPRS_PAGE)) ) {
                minLen = 5;
                maxLen = 16;
            }
            if(textField == Gui.getGuiByName(pktParams.GSM_MONEY_QUERY)){     // money query
                minLen = 5;
                maxLen = 5;
            }
            if(textField == Gui.getGuiByName(pktParams.GPRS_PORT)){     // port
                minLen = 2;
                maxLen = 5;
            }
            if(textField == Gui.getGuiByName(pktParams.GPRS_TIMEOUT)){     // timeout
                minLen = 1;
                maxLen = 5;
            }
            if( (textField == Gui.getGuiByName(pktParams.USER1_PASSW)) || // password
                (textField == Gui.getGuiByName(pktParams.USER2_PASSW)) ||
                (textField == Gui.getGuiByName(pktParams.USER3_PASSW)) ) {
                minLen = 6;
                maxLen = 6;
            }
            if( (textField == Gui.getGuiByName(pktParams.USER1_PHONE)) || // phone
                (textField == Gui.getGuiByName(pktParams.USER2_PHONE)) ||
                (textField == Gui.getGuiByName(pktParams.USER3_PHONE)) ) {
                minLen = 13;
                maxLen = 13;
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
