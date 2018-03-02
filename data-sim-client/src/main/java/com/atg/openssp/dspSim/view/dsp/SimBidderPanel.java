package com.atg.openssp.dspSim.view.dsp;

import com.atg.openssp.dspSim.model.ModelException;
import com.atg.openssp.dspSim.model.dsp.DspModel;
import com.atg.openssp.dspSim.model.dsp.SimBidder;

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
import java.util.UUID;

public class SimBidderPanel extends JPanel implements ListSelectionListener, ActionListener {
    private final DspModel model;
    private final JList<SimBidder> lBidders;
    private final JLabel lbId = new JLabel("");
    private final JTextField tfPrice = new JTextField(8);
    private final JTextField tfAddPrice = new JTextField(8);
    private final JButton bSave = new JButton("save");
    private final JButton bRemove = new JButton("remove");
    private final JButton bAdd = new JButton("add");
    private final JTextField tfMemo = new JTextField(20);
    private Locale currencyLocale = Locale.GERMANY;

    public SimBidderPanel(DspModel model) {
        this.model = model;
        lBidders = new JList(model.getBidderModel());

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
        tfMemo.setBackground(Color.WHITE);
        pBottom.add(tfMemo);
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
        tfMemo.setBackground(Color.WHITE);
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        if (ev.getSource() == bSave) {
            SimBidder sb = lBidders.getSelectedValue();
            if (sb != null) {
                DecimalFormat formatter = new DecimalFormat("###,###,###.00");
                try {
                    float newPrice = formatter.parse(tfPrice.getText()).floatValue();
                    if (sb.getPrice() != newPrice)
                    tfMemo.setText("Bidder saved.");
                    tfMemo.setBackground(Color.WHITE);
                    model.sendUpdateCommand(sb, newPrice);
                } catch (ModelException e) {
                    tfMemo.setText(e.getMessage());
                    tfMemo.setBackground(Color.RED);
                } catch (Exception e) {
                    e.printStackTrace();
                    tfMemo.setText("Could not save Bidder due to invalid price.");
                    tfMemo.setBackground(Color.RED);
                }
            } else {
                tfMemo.setText("No Bidder selected.");
                tfMemo.setBackground(Color.YELLOW);
            }
            repaint();
        } else if (ev.getSource() == bAdd) {
            DecimalFormat formatter = new DecimalFormat("###,###,###.00");
            try {
                model.sendAddCommand(formatter.parse(tfAddPrice.getText()).floatValue());
                tfMemo.setText("Bidder added.");
                tfMemo.setBackground(Color.WHITE);
                tfAddPrice.setText("");
            } catch (ModelException e) {
                tfMemo.setText(e.getMessage());
                tfMemo.setBackground(Color.RED);
            } catch (ParseException e) {
                tfMemo.setText("Could not add Bidder due to invalid price.");
                tfMemo.setBackground(Color.RED);
            }
            repaint();
        } else if (ev.getSource() == bRemove) {
            SimBidder sb = lBidders.getSelectedValue();
            if (sb != null) {
                try {
                    model.sendRemoveCommand(sb);
                } catch (ModelException e) {
                    tfMemo.setText(e.getMessage());
                    tfMemo.setBackground(Color.RED);
                }
            } else {
                tfMemo.setText("No Bidder selected.");
                tfMemo.setBackground(Color.YELLOW);
            }
            repaint();
        }
    }
}
