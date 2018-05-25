package com.atg.openssp.dspSimUi.view.pricelayer;

import com.atg.openssp.dspSimUi.model.MessageNotificationListener;
import com.atg.openssp.dspSimUi.model.MessageStatus;
import com.atg.openssp.dspSimUi.model.ModelException;
import com.atg.openssp.dspSimUi.model.pricelayer.PricelayerModel;
import javafx.scene.control.TextFormatter;
import openrtb.bidrequest.model.Pricelayer;
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
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Collections;
import java.util.UUID;

/**
 * @author Brian Sorensen
 */
public class PricelayerMaintenancePanel extends JPanel implements ListSelectionListener, ActionListener, MessageNotificationListener {
    private static final Logger log = LoggerFactory.getLogger(PricelayerMaintenancePanel.class);
    private final PricelayerModel model;
    private final JList<Pricelayer> lPricelayers;
    
    private final JLabel lbSiteId = new JLabel("");
    private final JTextField tfBidfloor = new JTextField(10);
    private final JTextField tfCurrency = new JTextField(4);

    private final JTextField tfMemo = new JTextField(20);

    private final JButton bUpdate;
    private final JButton bRemove;
    private final JButton bAdd;
    private final JButton bLoad;
    private final JButton bImport;
    private final JButton bExport;
    private final JButton bClear;
    private Pricelayer active;

    public PricelayerMaintenancePanel(PricelayerModel model) {
        this.model = model;
        setLayout(new BorderLayout());

        lPricelayers = new JList<>(model.getPricelayerModel());
        lPricelayers.setSelectedIndex(-1);

        bUpdate = new JButton(model.getTemplateText("PRICELAYER_UPDATE"));
        bRemove = new JButton(model.getTemplateText("PRICELAYER_REMOVE"));
        bAdd = new JButton(model.getTemplateText("PRICELAYER_INIT"));
        bLoad = new JButton(model.getTemplateText("PRICELAYER_LOAD"));
        bImport = new JButton(model.getTemplateText("PRICELAYER_IMPORT"));
        bExport = new JButton(model.getTemplateText("PRICELAYER_EXPORT"));
        bClear = new JButton(model.getTemplateText("PRICELAYER_CLEAR"));

        JPanel pTop = new JPanel();
        add(pTop, BorderLayout.NORTH);
        JPanel pBottom = new JPanel();
        pBottom.setLayout(new BoxLayout(pBottom, BoxLayout.Y_AXIS));
        add(pBottom, BorderLayout.SOUTH);
        JPanel p = new JPanel();
        JPanel pMiddle = new JPanel();
        pMiddle.setLayout(new BoxLayout(pMiddle, BoxLayout.Y_AXIS));
        p.add(pMiddle);

        JPanel pPricelayer = new JPanel();
        pMiddle.add(pPricelayer);
        JPanel pPublisher = new JPanel();
        pMiddle.add(pPublisher);

        add(p, BorderLayout.CENTER);
        JPanel pRight = new JPanel();
        pRight.setLayout(new BoxLayout(pRight, BoxLayout.Y_AXIS));
        add(pRight, BorderLayout.EAST);

        JPanel pCommands = new JPanel();
        pCommands.setLayout(new FlowLayout(FlowLayout.CENTER));
        pBottom.add(pCommands, BorderLayout.SOUTH);

        lPricelayers.setVisibleRowCount(10);
        addItem(pTop, model.getTemplateText("PRICELAYERS"), lPricelayers);
        lPricelayers.addListSelectionListener(this);


        bAdd.setEnabled(true);
        bAdd.addActionListener(this);
        addItem(pRight, "", bAdd);

        bUpdate.setEnabled(false);
        bUpdate.addActionListener(this);
        addItem(pRight, "", bUpdate);
        bRemove.setEnabled(false);
        bRemove.addActionListener(this);
        addItem(pRight, "", bRemove);
        bLoad.addActionListener(this);
        addItem(pRight, "", bLoad);
        bImport.addActionListener(this);
        addItem(pRight, "", bImport);
        bExport.addActionListener(this);
        addItem(pRight, "", bExport);
        bClear.addActionListener(this);
        addItem(pRight, "", bClear);

        pPricelayer.setBorder(new TitledBorder(model.getTemplateText("ACTIVE_PRICELAYER")));
        pPricelayer.setLayout(new BoxLayout(pPricelayer, BoxLayout.Y_AXIS));
        addItem(pPricelayer, model.getTemplateText("SITEID"), lbSiteId);
        addItem(pPricelayer, model.getTemplateText("BIDFLOOR"), tfBidfloor);
        addItem(pPricelayer, model.getTemplateText("CURRENCY"), tfCurrency);

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
            active = lPricelayers.getSelectedValue();
            setStateForActive();
        }
        model.setMessage("");
    }

    private void setStateForActive() {
        if (active != null) {
            lbSiteId.setText(active.getSiteid());
            resetActiveDisplay(active);
            bUpdate.setEnabled(true);
            bRemove.setEnabled(true);
        } else {
            if (lPricelayers.getModel().getSize() == 0) {
                resetActiveDisplay(active);
                bUpdate.setEnabled(false);
                bRemove.setEnabled(false);
            } else {
                resetActiveDisplay(active);
                lPricelayers.setSelectedIndex(0);
                bUpdate.setEnabled(true);
                bRemove.setEnabled(true);
            }

        }
    }

    private void resetActiveDisplay(Pricelayer sb) {
        if (sb == null) {
            tfBidfloor.setText("");
            tfCurrency.setText("");
            tfMemo.setText("");
        } else {
            NumberFormat tf = NumberFormat.getNumberInstance();
            tf.setMinimumFractionDigits(2);
            tfBidfloor.setText(tf.format(sb.getBidfloor()));
            tfCurrency.setText(sb.getCurrency());
            tfMemo.setText("Pricelayer selected.");
        }
    }

    private void populate(Pricelayer sb) throws ParseException {
        sb.setBidfloor((float)Double.parseDouble(tfBidfloor.getText()));
        sb.setCurrency(tfCurrency.getText());
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        if (ev.getSource() == bUpdate) {
            if (active != null) {
                try {
                    Pricelayer sbN = new Pricelayer();
                    sbN.setSiteid(active.getSiteid());
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
            lPricelayers.setSelectedIndex(-1);
            active = new Pricelayer();
            active.setSiteid("fixme");
            resetActiveDisplay(active);
            setStateForActive();
            repaint();
        } else if (ev.getSource() == bRemove) {
            if (active != null) {
                try {
                    model.sendRemoveCommand(active.getSiteid());
                } catch (ModelException e) {
                    model.setMessageAsFault(e.getMessage());
                }
            } else {
                model.setMessageAsWarning("No Bidder selected.");
            }
            repaint();
        } else if (ev.getSource() == bLoad) {
            try {
                model.sendLoadCommand();
            } catch (ModelException e) {
                model.setMessageAsFault(e.getMessage());
            }
            repaint();
        } else if (ev.getSource() == bExport) {
            try {
                model.sendExportCommand();
            } catch (ModelException e) {
                model.setMessageAsFault(e.getMessage());
            }
            repaint();
        } else if (ev.getSource() == bImport) {
            try {
                model.sendImportCommand();
            } catch (ModelException e) {
                model.setMessageAsFault(e.getMessage());
            }
            repaint();
        } else if (ev.getSource() == bClear) {
            try {
                model.sendClearCommand();
            } catch (ModelException e) {
                model.setMessageAsFault(e.getMessage());
            }
            repaint();
        }
    }


    @Override
    public void sendMessage(MessageStatus s, String m) {
        tfMemo.setText(m);
        tfMemo.setBackground(s.getColor());
    }

}
