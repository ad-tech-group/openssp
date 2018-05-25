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
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Collections;
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
    private final DefaultListModel<String> mADomain = new DefaultListModel<String>();
    private final JList<String> lADomain = new JList<String>(mADomain);
    private final JTextField tfIUrl = new JTextField(25);
    private final JTextField tfCId = new JTextField(25);
    private final JTextField tfCrId = new JTextField(25);
    private final DefaultListModel<ContentCategory> mCat = new DefaultListModel<ContentCategory>();
    private final JList<ContentCategory> lCat = new JList<ContentCategory>(mCat);
    private final JTextField tfAddADomain = new JTextField(25);
    private final DefaultComboBoxModel<ContentCategory> mAddCat = new DefaultComboBoxModel<ContentCategory>();
    private final JComboBox<ContentCategory> cbAddCat = new JComboBox<ContentCategory>(mAddCat);
    private final JTextField tfMemo = new JTextField(20);

    private final JButton bAddADomain;
    private final JButton bRemoveADomain;
    private final JButton bAddCat;
    private final JButton bRemoveCat;
    private final JButton bUpdate;
    private final JButton bRemove;
    private final JButton bAdd;
    private final JButton bRestart;
    private final JButton bShutdown;
    private final JButton bSendNormal;
    private final JButton bReturnNone;
    private final JButton bSend400;
    private final JButton bSend500;
    private SimBidder active;

    public SimBidderPanel(DspModel model) {
        this.model = model;
        setLayout(new BorderLayout());

        for (ContentCategory cc : ContentCategory.values()) {
            mAddCat.addElement(cc);
        }
        cbAddCat.setSelectedItem(null);
        lBidders = new JList<>(model.getBidderModel());
        model.addModeChangeListener(this);
        lBidders.setSelectedIndex(-1);

        bAddADomain = new JButton(model.getTemplateText("ADOMAIN_ADD"));
        bRemoveADomain = new JButton(model.getTemplateText("ADOMAIN_REMOVE"));
        bAddCat = new JButton(model.getTemplateText("CAT_ADD"));
        bRemoveCat = new JButton(model.getTemplateText("CAT_REMOVE"));
        bUpdate = new JButton(model.getTemplateText("BIDDER_UPDATE"));
        bRemove = new JButton(model.getTemplateText("BIDDER_REMOVE"));
        bAdd = new JButton(model.getTemplateText("BIDDER_INIT"));
        bRestart = new JButton(model.getTemplateText("SIM_RESTART"));
        bShutdown = new JButton(model.getTemplateText("SIM_SHUTDOWN"));
        bSendNormal = new JButton(model.getTemplateText("RETURN_NORMAL"));
        bReturnNone = new JButton(model.getTemplateText("RETURN_NONE"));
        bSend400 = new JButton(model.getTemplateText("RETURN_400"));
        bSend500 = new JButton(model.getTemplateText("RETURN_500"));

        JPanel pTop = new JPanel();
        add(pTop, BorderLayout.NORTH);
        JPanel pBottom = new JPanel();
        pBottom.setLayout(new BoxLayout(pBottom, BoxLayout.Y_AXIS));
        add(pBottom, BorderLayout.SOUTH);
        JPanel p = new JPanel();
        JPanel pMiddle = new JPanel();
        p.add(pMiddle);
        add(p, BorderLayout.CENTER);
        JPanel pRight = new JPanel();
        pRight.setLayout(new BoxLayout(pRight, BoxLayout.Y_AXIS));
        add(pRight, BorderLayout.EAST);

        JPanel pCommands = new JPanel();
        pCommands.setLayout(new FlowLayout(FlowLayout.CENTER));
        pBottom.add(pCommands, BorderLayout.SOUTH);

        JPanel pAppCommands = new JPanel();
        pAppCommands.setBorder(BorderFactory.createTitledBorder(model.getTemplateText("ADMIN_CONTROLS")));
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
        pInterfaceCommands.setBorder(BorderFactory.createTitledBorder(model.getTemplateText("INTERFACE_CONTROLS")));
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
        addItem(pTop, model.getTemplateText("BIDDERS"), lBidders);
        lBidders.addListSelectionListener(this);


        bAdd.setEnabled(true);
        bAdd.addActionListener(this);
        addItem(pRight, "", bAdd);

        bUpdate.setEnabled(false);
        bUpdate.addActionListener(this);
        addItem(pRight, "", bUpdate);
        bRemove.setEnabled(false);
        bRemove.addActionListener(this);
        addItem(pRight, "", bRemove);

        pMiddle.setBorder(new TitledBorder(model.getTemplateText("ACTIVE_BIDDER")));
        pMiddle.setLayout(new BoxLayout(pMiddle, BoxLayout.Y_AXIS));
        addItem(pMiddle, model.getTemplateText("BIDDER_ID"), lbId);
        addItem(pMiddle, model.getTemplateText("IMPRESSION_ID"), tfImpId);
        addItem(pMiddle, model.getTemplateText("PRICE"), tfPrice);
        addItem(pMiddle, model.getTemplateText("AD_ID"), tfAdId);
        addItem(pMiddle, model.getTemplateText("N_URL"),  tfNUrl);
        addItem(pMiddle, model.getTemplateText("ADM"), tfAdm);
        addItem(pMiddle, model.getTemplateText("A_DOMAIN"), new JScrollPane(lADomain));
        lADomain.setVisibleRowCount(3);
        addItem(pMiddle, model.getTemplateText("I_URL"), tfIUrl);
        addItem(pMiddle, model.getTemplateText("C_ID"), tfCId);
        addItem(pMiddle, model.getTemplateText("CR_ID"), tfCrId);
        addItem(pMiddle, model.getTemplateText("CAT"), new JScrollPane(lCat));
        lCat.setVisibleRowCount(3);

        p = new JPanel();
        p.setBorder(new TitledBorder(model.getTemplateText("MAINTAIN_A_DOMAIN")));
        addItem(pMiddle, "", p);
        p.add(tfAddADomain);
        bAddADomain.addActionListener(this);
        bAddADomain.setEnabled(false);
        addItem(p, "", bAddADomain);
        bRemoveADomain.addActionListener(this);
        bRemoveADomain.setEnabled(false);
        addItem(p, "", bRemoveADomain);

        p = new JPanel();
        p.setBorder(new TitledBorder(model.getTemplateText("MAINTAIN_CATEGORY")));
        addItem(pMiddle, model.getTemplateText("CATATORIES"), p);
        p.add(cbAddCat);
        bAddCat.addActionListener(this);
        bAddCat.setEnabled(false);
        addItem(p, "", bAddCat);
        bRemoveCat.addActionListener(this);
        bRemoveCat.setEnabled(false);
        addItem(p, "", bRemoveCat);

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
            active = lBidders.getSelectedValue();
            setStateForActive();
        }
        model.setMessage("");
    }

    private void setStateForActive() {
        if (active != null) {
            lbId.setText(active.getId());
            resetActiveDisplay(active);
            bUpdate.setEnabled(true);
            bRemove.setEnabled(true);
            bAddCat.setEnabled(true);
            bRemoveCat.setEnabled(true);
            bAddADomain.setEnabled(true);
            bRemoveADomain.setEnabled(true);
        } else {
            if (lBidders.getModel().getSize() == 0) {
                resetActiveDisplay(active);
                bUpdate.setEnabled(false);
                bRemove.setEnabled(false);
                bAddCat.setEnabled(false);
                bRemoveCat.setEnabled(false);
                bAddADomain.setEnabled(false);
                bRemoveADomain.setEnabled(false);
            } else {
                resetActiveDisplay(active);
                lBidders.setSelectedIndex(0);
                bUpdate.setEnabled(true);
                bRemove.setEnabled(true);
                bAddCat.setEnabled(true);
                bRemoveCat.setEnabled(true);
                bAddADomain.setEnabled(true);
                bRemoveADomain.setEnabled(true);
            }

        }
    }

    private void resetActiveDisplay(SimBidder sb) {
        if (sb == null) {
            tfImpId.setText("");
            tfPrice.setText("");
            tfAdId.setText("");
            tfNUrl.setText("");
            tfAdm.setText("");
            mADomain.clear();
            tfAddADomain.setText("");
            tfCId.setText("");
            tfCrId.setText("");
            mCat.clear();
            cbAddCat.setSelectedItem(null);
            tfMemo.setText("");
        } else {
            tfImpId.setText(sb.getImpid());
            DecimalFormat formatter = new DecimalFormat("###,###,###.00");
            tfPrice.setText(formatter.format(sb.getPrice()));
            tfAdId.setText(sb.getAdid());
            tfNUrl.setText(sb.getNurl());
            tfAdm.setText(sb.getAdm());
            mADomain.clear();
            for (String s : sb.getAdomain()) {
                mADomain.addElement(s);
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

    private void populate(SimBidder sb) throws ParseException {
        DecimalFormat formatter = new DecimalFormat("###,###,###.00");
        float newPrice = formatter.parse(tfPrice.getText()).floatValue();
        sb.setImpid(tfImpId.getText());
        sb.setPrice(newPrice);
        sb.setAdid(tfAdId.getText());
        sb.setNurl(tfNUrl.getText());
        sb.setAdm(tfAdm.getText());
        sb.setAdomain(Collections.list(mADomain.elements()));
        sb.setIurl(tfIUrl.getText());
        sb.setCid(tfCId.getText());
        sb.setCrid(tfCrId.getText());
        sb.setCats(Collections.list(mCat.elements()));
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        if (ev.getSource() == bUpdate) {
            if (active != null) {
                try {
                    SimBidder sbN = new SimBidder(active.getId());
                    populate(sbN);
                    model.sendUpdateCommand(sbN);
                    model.setMessage("Bidder saved.");
                    active = null;
                    setStateForActive();
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
            lBidders.setSelectedIndex(-1);
            active = new SimBidder(UUID.randomUUID().toString());
            resetActiveDisplay(active);
            setStateForActive();
            repaint();
        } else if (ev.getSource() == bRemove) {
            if (active != null) {
                try {
                    model.sendRemoveCommand(active.getId());
                } catch (ModelException e) {
                    model.setMessageAsFault(e.getMessage());
                }
            } else {
                model.setMessageAsWarning("No Bidder selected.");
            }
            repaint();
        } else if (ev.getSource() == bAddADomain) {
            String value = tfAddADomain.getText();
            if (value != null && !mADomain.contains(value)) {
                mADomain.addElement(value);
            }
            tfAddADomain.setText(null);
            repaint();
        } else if (ev.getSource() == bRemoveADomain) {
            String value = lADomain.getSelectedValue();
            if (value != null) {
                mADomain.removeElement(value);
                lADomain.setSelectedIndex(-1);
            } else {
                model.setMessage("Need to select a domain to remove.");
            }
            repaint();
        } else if (ev.getSource() == bAddCat) {
            ContentCategory value = (ContentCategory) cbAddCat.getSelectedItem();
            if (value != null && !mCat.contains(value)) {
                mCat.addElement(value);
            }
            cbAddCat.setSelectedItem(null);
            repaint();
        } else if (ev.getSource() == bRemoveCat) {
            ContentCategory value = lCat.getSelectedValue();
            if (value != null) {
                mCat.removeElement(value);
                lCat.setSelectedIndex(-1);
            } else {
                value = (ContentCategory) cbAddCat.getSelectedItem();
                if (value != null) {
                    mCat.removeElement(value);
                    cbAddCat.setSelectedIndex(-1);
                } else {
                    model.setMessage("Need to select category to remove.");
                }
            }
            repaint();
        } else if (ev.getSource() == bShutdown) {
            try {
                model.sendShutdownCommand();
            } catch (ModelException e) {
                model.setMessageAsFault(e.getMessage());
            }
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
