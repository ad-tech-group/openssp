package com.atg.openssp.dspSimUi.view.dsp;

import com.atg.openssp.dspSimUi.model.MessageNotificationListener;
import com.atg.openssp.dspSimUi.model.MessageStatus;
import com.atg.openssp.dspSimUi.model.ModelException;
import com.atg.openssp.dspSimUi.model.client.ServerCommandType;
import com.atg.openssp.dspSimUi.model.dsp.DspModel;
import com.atg.openssp.dspSimUi.model.dsp.ModeChangeListener;
import com.atg.openssp.dspSimUi.model.dsp.SimBidder;
import openrtb.tables.ContentCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author Brian Sorensen
 */
public class SimBidderPanel extends JPanel implements ListSelectionListener, ActionListener, MessageNotificationListener, ModeChangeListener {
    private static final Logger log = LoggerFactory.getLogger(SimBidderPanel.class);
    private final DspModel model;
    private final JList<SimBidder> lBidders;
    private final JLabel lbId = new JLabel("");
    private final JTextField tfImpId = new JTextField(12);
    private final JTextField tfPrice = new JTextField(8);
    private final JTextField tfAdId = new JTextField(12);
    private final JTextField tfNUrl = new JTextField(20);
    private final JTextField tfAdm = new JTextField(25);
    private final DefaultListModel<String> mAdomain = new DefaultListModel<String>();
    private final JList<String> lAdomain = new JList<String>(mAdomain);
    private final JTextField tfIUrl = new JTextField(25);
    private final JTextField tfCId = new JTextField(25);
    private final JTextField tfCrId = new JTextField(25);
    private final DefaultListModel<ContentCategory> mCat = new DefaultListModel<ContentCategory>();
    private final JList<ContentCategory> lCat = new JList<ContentCategory>(mCat);
//    private final DefaultListModel<ContentCategory> mAddCat = new DefaultListModel<ContentCategory>();
//    private final JList<ContentCategory> lAddCat = new JList<ContentCategory>(mAddCat);


    private final JTextField tfAddNewADomain = new JTextField(25);
    private DefaultComboBoxModel<ContentCategory> mAddNewCat = new DefaultComboBoxModel<ContentCategory>();
    private final JComboBox<ContentCategory> cbAddNewCat = new JComboBox<ContentCategory>(mAddNewCat);
//    private final JButton bAddADomain = new JButton("add");
//    private final JButton bAddCat = new JButton("add");

    private final JButton bUpdate = new JButton("update");
//    private final JButton bRemove = new JButton("remove");
//    private final JButton bAdd = new JButton("add");
    private final JTextField tfMemo = new JTextField(20);
    private final JButton bRestart = new JButton("Restart SIM");
    private final JButton bShutdown = new JButton("Shutdown SIM");
    private final JButton bSendNormal = new JButton("Return Normal Set");
    private final JButton bReturnNone = new JButton("Return None");
    private final JButton bSend400 = new JButton("Return 400");
    private final JButton bSend500 = new JButton("Return 500");

    public SimBidderPanel(DspModel model) {
        this.model = model;
        lBidders = new JList<>(model.getBidderModel());
        model.addModeChangeListener(this);

        setLayout(new BorderLayout());

        JPanel pTop = new JPanel();
        add(pTop, BorderLayout.NORTH);
        JPanel pBottom = new JPanel();
        pBottom.setLayout(new BoxLayout(pBottom, BoxLayout.Y_AXIS));
        add(pBottom, BorderLayout.SOUTH);
        JPanel pMiddle = new JPanel();
        add(pMiddle, BorderLayout.CENTER);
        JPanel pRight = new JPanel();
        pRight.setLayout(new BoxLayout(pRight, BoxLayout.Y_AXIS));
        add(pRight, BorderLayout.EAST);

        JPanel pCommands = new JPanel();
        pCommands.setLayout(new FlowLayout(FlowLayout.CENTER));
        pBottom.add(pCommands, BorderLayout.SOUTH);

        JPanel pAppCommands = new JPanel();
        pAppCommands.setBorder(BorderFactory.createTitledBorder("Admin Controls:"));
        pAppCommands.setLayout(new BoxLayout(pAppCommands, BoxLayout.X_AXIS));
        pCommands.add(pAppCommands);

        bShutdown.addActionListener(this);
        bShutdown.setBackground(Color.RED);
        bShutdown.setOpaque(true);
        addItem(pAppCommands, "", bShutdown);
        bRestart.addActionListener(this);
        bRestart.setBackground(Color.YELLOW);
        bRestart.setOpaque(true);
        addItem(pAppCommands, "", bRestart);


        JPanel pInterfaceCommands = new JPanel();
        pInterfaceCommands.setBorder(BorderFactory.createTitledBorder("Interface Controls:"));
        pInterfaceCommands.setLayout(new BoxLayout(pInterfaceCommands, BoxLayout.X_AXIS));
        pCommands.add(pInterfaceCommands);
        bSendNormal.addActionListener(this);
        bSendNormal.setBackground(getBackground());
        bSendNormal.setOpaque(true);
        addItem(pInterfaceCommands, "", bSendNormal);
        bReturnNone.addActionListener(this);
        bReturnNone.setBackground(getBackground());
        bReturnNone.setOpaque(true);
        addItem(pInterfaceCommands, "", bReturnNone);
        bSend400.addActionListener(this);
        bSend400.setBackground(getBackground());
        bSend400.setOpaque(true);
        addItem(pInterfaceCommands, "", bSend400);
        bSend500.addActionListener(this);
        bSend500.setBackground(getBackground());
        bSend500.setOpaque(true);
        addItem(pInterfaceCommands, "", bSend500);

        lBidders.setVisibleRowCount(10);
        addItem(pTop, "Bidders: ", lBidders);
        lBidders.addListSelectionListener(this);
        /*
        bUpdate.setEnabled(false);
        bUpdate.addActionListener(this);
        addItem(pTop, "", bUpdate);
        bRemove.setEnabled(false);
        bRemove.addActionListener(this);
        addItem(pTop, "", bRemove);
*/

        pMiddle.setBorder(new TitledBorder("Active Bidder"));
        addItem(pMiddle, "ID:", lbId);
        addItem(pMiddle, "IMP ID:", tfImpId);
        tfImpId.setEditable(false);
        addItem(pMiddle, "Price:", tfPrice);
        addItem(pMiddle, "AD ID:", tfAdId);
        addItem(pMiddle, "N Url:", tfNUrl);
        tfNUrl.setEditable(false);
        addItem(pMiddle, "ADM:", tfAdm);
        addItem(pMiddle, "A DOMAIN:", lAdomain);
        addItem(pMiddle, "I URL:", tfIUrl);
        addItem(pMiddle, "C ID:", tfCId);
        addItem(pMiddle, "CR ID:", tfCrId);
        addItem(pMiddle, "CAT:", lCat);

//        bAddADomain.addActionListener(this);
//        bAddCat.addActionListener(this);
//        bAdd.addActionListener(this);
//        addItem(pRight, "", bAdd);

        tfMemo.setEditable(false);
        tfMemo.setBackground(Color.GREEN);
        tfMemo.setOpaque(true);
        pBottom.add(tfMemo);
        model.addMessageNotificationListener(this);
        model.setMessage("");
    }

    private void addItem(JPanel pMain, String txt, JComponent c) {
        JPanel p0 = new JPanel();
        pMain.add(p0);
        p0.setLayout(new FlowLayout());
        JPanel p1 = new JPanel();
        p0.add(p1);
        p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));
        p1.add(new JLabel(txt));
        p1.add(c);
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            SimBidder sb = lBidders.getSelectedValue();
            if (sb != null) {
                lbId.setText(sb.getId());
                resetActiveDisplay(sb);
  //              bUpdate.setEnabled(true);
  //              bRemove.setEnabled(true);
            } else {
                if (lBidders.getModel().getSize() == 0) {
                    resetActiveDisplay(sb);
  //                  bUpdate.setEnabled(false);
  //                  bRemove.setEnabled(false);
                } else {
                    resetActiveDisplay(sb);
                    lBidders.setSelectedIndex(0);
  //                  bUpdate.setEnabled(true);
  //                  bRemove.setEnabled(true);
                }

            }
        }
        model.setMessage("");
    }

    private void resetActiveDisplay(SimBidder sb) {
        if (sb == null) {
            tfImpId.setText("");
            tfPrice.setText("");
            tfAdId.setText("");
            tfNUrl.setText("");
            tfAdm.setText("");
            mAdomain.clear();
            tfCId.setText("");
            tfCrId.setText("");
            mCat.clear();
            tfMemo.setText("");
        } else {
            tfImpId.setText(sb.getImpid());
            DecimalFormat formatter = new DecimalFormat("###,###,###.00");
            tfPrice.setText(formatter.format(sb.getPrice()));
            tfAdId.setText(sb.getAdid());
            tfNUrl.setText(sb.getNurl());
            tfAdm.setText(sb.getAdm());
            mAdomain.clear();
            for (String s : sb.getAdomain()) {
                mAdomain.addElement(s);
            }
            tfIUrl.setText(sb.getIurl());
            tfCId.setText(sb.getCid());
            tfCrId.setText(sb.getCrid());
            mCat.clear();
            for (ContentCategory s : sb.getCats()) {
                mCat.addElement(s);
            }
            tfMemo.setText("Bidder selected.");
        }
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        if (ev.getSource() == bUpdate) {
        /*
            SimBidder sb = lBidders.getSelectedValue();
            if (sb != null) {
                DecimalFormat formatter = new DecimalFormat("###,###,###.00");
                try {
                    float newPrice = formatter.parse(tfAddPrice.getText()).floatValue();

                    SimBidder sbN = new SimBidder(sb.getId());
                    sbN.setImpid(tfAddImpId.getText());
                    sbN.setPrice(newPrice);
                    sbN.setAdid(tfAdId.getText());
                    sbN.setNurl(tfNUrl.getText());
                    sbN.setAdm(tfAdm.getText());
                    sbN.setAdomain(Collections.list(mAdomain.elements()));
                    sbN.setIurl(tfIUrl.getText());
                    sbN.setCid(tfCId.getText());
                    sbN.setCrid(tfCrId.getText());
                    sbN.setCats(Collections.list(mCat.elements()));
                    model.sendUpdateCommand(sbN);
                    model.setMessage("Bidder saved.");
                } catch (ModelException e) {
                    model.setMessageAsFault(e.getMessage());
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    e.printStackTrace();
                    model.setMessageAsFault("Could not save Bidder due to invalid price.");
                }
            } else {
                model.setMessageAsWarning("No Bidder selected.");
            }
            repaint();
        } else if (ev.getSource() == bAdd) {
            DecimalFormat formatter = new DecimalFormat("###,###,###.00");
            try {
                float newPrice = formatter.parse(tfAddPrice.getText()).floatValue();
                SimBidder sbN = new SimBidder(UUID.randomUUID().toString());
                sbN.setImpid(tfAddImpId.getText());
                sbN.setPrice(newPrice);
                sbN.setAdid(tfAddAdId.getText());
                sbN.setNurl(tfAddNUrl.getText());
                sbN.setAdm(tfAddAdm.getText());
                sbN.setAdomain(Collections.list(mAddAdomain.elements()));
                sbN.setIurl(tfAddIUrl.getText());
                sbN.setCid(tfAddCId.getText());
                sbN.setCrid(tfAddCrId.getText());
                sbN.setCats(Collections.list(mCat.elements()));

                model.sendAddCommand(sbN);
                model.setMessage("Bidder added.");
                tfAddImpId.setText("");
                tfAddPrice.setText("");
                tfAddAdId.setText("");
                tfAddNUrl.setText("");
                tfAddAdm.setText("");
                mAddAdomain.clear();
                tfAddIUrl.setText("");
                tfAddCId.setText("");
                tfAddCrId.setText("");
                mAddCat.clear();
            } catch (ModelException e) {
                model.setMessageAsFault(e.getMessage());
            } catch (ParseException e) {
                model.setMessageAsFault("Could not add Bidder due to invalid price.");
            }
            repaint();
        } else if (ev.getSource() == bRemove) {
            SimBidder sb = lBidders.getSelectedValue();
            if (sb != null) {
                try {
                    model.sendRemoveCommand(sb.getId());
                } catch (ModelException e) {
                    model.setMessageAsFault(e.getMessage());
                }
            } else {
                model.setMessageAsWarning("No Bidder selected.");
            }
            repaint();
        } else if (ev.getSource() == bAddADomain) {
            String value = tfAddNewADomain.getText();
            if (!"".equals(value.trim())) {
                mAddAdomain.addElement(value);
                tfAddNewADomain.setText("");
            }
        } else if (ev.getSource() == bAddCat) {
            ContentCategory value = (ContentCategory) cbAddNewCat.getSelectedItem();
            mAddCat.addElement(value);
            cbAddNewCat.setSelectedItem(null);
        } else if (ev.getSource() == bShutdown) {
            try {
                model.sendShutdownCommand();
            } catch (ModelException e) {
                model.setMessageAsFault(e.getMessage());
            }
            */
        } else if (ev.getSource() == bRestart) {
            try {
                model.sendRestartCommand();
            } catch (ModelException e) {
                model.setMessageAsFault(e.getMessage());
            }
        } else if (ev.getSource() == bSendNormal) {
            try {
                model.sendNormalCommand();
            } catch (ModelException e) {
                model.setMessageAsFault(e.getMessage());
            }
        } else if (ev.getSource() == bReturnNone) {
            try {
                model.sendReturnNoneCommand();
            } catch (ModelException e) {
                model.setMessageAsFault(e.getMessage());
            }
        } else if (ev.getSource() == bSend400) {
            try {
                model.send400Command();
            } catch (ModelException e) {
                model.setMessageAsFault(e.getMessage());
            }
        } else if (ev.getSource() == bSend500) {
            try {
                model.send500Command();
            } catch (ModelException e) {
                model.setMessageAsFault(e.getMessage());
            }
        }
    }

    @Override
    public void sendMessage(MessageStatus s, String m) {
        tfMemo.setText(m);
        tfMemo.setBackground(s.getColor());
    }

    @Override
    public void updateMode(ServerCommandType mode) {
        if (mode == ServerCommandType.ONLY_400) {
            bSendNormal.setBackground(getBackground());
            bReturnNone.setBackground(getBackground());
            bSend400.setBackground(Color.GREEN);
            bSend500.setBackground(getBackground());
        } else if (mode == ServerCommandType.ONLY_500) {
            bSendNormal.setBackground(getBackground());
            bReturnNone.setBackground(getBackground());
            bSend400.setBackground(getBackground());
            bSend500.setBackground(Color.GREEN);
        } else if (mode == ServerCommandType.RETURN_NONE) {
            bSendNormal.setBackground(getBackground());
            bReturnNone.setBackground(Color.GREEN);
            bSend400.setBackground(getBackground());
            bSend500.setBackground(getBackground());
        } else if (mode == ServerCommandType.RETURN_NORMAL) {
            bSendNormal.setBackground(Color.GREEN);
            bReturnNone.setBackground(getBackground());
            bSend400.setBackground(getBackground());
            bSend500.setBackground(getBackground());
        }
    }
}
