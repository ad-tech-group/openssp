package com.atg.openssp.dspSim.view.dsp;

import com.atg.openssp.dspSim.model.MessageNotificationListener;
import com.atg.openssp.dspSim.model.MessageStatus;
import com.atg.openssp.dspSim.model.ModelException;
import com.atg.openssp.dspSim.model.dsp.DspModel;
import com.atg.openssp.dspSim.model.dsp.SimBidder;
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
import java.util.Locale;

/**
 * @author Brian Sorensen
 */
public class SimBidderPanel extends JPanel implements ListSelectionListener, ActionListener, MessageNotificationListener {
    private static final Logger log = LoggerFactory.getLogger(SimBidderPanel.class);
    private final DspModel model;
    private final JList<SimBidder> lBidders;
    private final JLabel lbId = new JLabel("");
    private final JTextField tfPrice = new JTextField(8);
    private final JTextField tfAddPrice = new JTextField(8);
    private final JButton bSave = new JButton("save");
    private final JButton bRemove = new JButton("remove");
    private final JButton bAdd = new JButton("add");
    private final JTextField tfMemo = new JTextField(20);

    public SimBidderPanel(DspModel model) {
        this.model = model;
        lBidders = new JList<>(model.getBidderModel());

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

        lBidders.setVisibleRowCount(10);
        addItem(pTop, "Bidders: ", lBidders);
        lBidders.addListSelectionListener(this);
        bSave.addActionListener(this);
        addItem(pTop, "", bSave);
        bRemove.addActionListener(this);
        addItem(pTop, "", bRemove);

        pMiddle.setBorder(new TitledBorder("Active Bidder"));
        addItem(pMiddle, "ID:", lbId);
        addItem(pMiddle, "Price:", tfPrice);

        pRight.setBorder(new TitledBorder("Add Bidder"));
        addItem(pRight, "Price:", tfAddPrice);
        bAdd.addActionListener(this);
        addItem(pRight, "", bAdd);

        tfMemo.setEditable(false);
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
            lbId.setText(sb.getId());
            DecimalFormat formatter = new DecimalFormat("###,###,###.00");
            tfPrice.setText(formatter.format(sb.getPrice()));
            tfMemo.setText("Bidder selected.");
        }
        model.setMessage("");
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        if (ev.getSource() == bSave) {
            SimBidder sb = lBidders.getSelectedValue();
            if (sb != null) {
                DecimalFormat formatter = new DecimalFormat("###,###,###.00");
                try {
                    float newPrice = formatter.parse(tfPrice.getText()).floatValue();
                    if (sb.getPrice() != newPrice) {
                        model.sendUpdateCommand(sb, newPrice);
                        model.setMessage("Bidder saved.");
                    }
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
                model.sendAddCommand(formatter.parse(tfAddPrice.getText()).floatValue());
                model.setMessage("Bidder added.");
                tfAddPrice.setText("");
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
                    model.sendRemoveCommand(sb);
                } catch (ModelException e) {
                    model.setMessageAsFault(e.getMessage());
                }
            } else {
                model.setMessageAsWarning("No Bidder selected.");
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
