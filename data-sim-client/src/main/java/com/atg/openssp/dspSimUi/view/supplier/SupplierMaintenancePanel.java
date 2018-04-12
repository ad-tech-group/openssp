package com.atg.openssp.dspSimUi.view.supplier;

import com.atg.openssp.dspSimUi.model.MessageNotificationListener;
import com.atg.openssp.dspSimUi.model.MessageStatus;
import com.atg.openssp.dspSimUi.model.ModelException;
import com.atg.openssp.common.demand.Supplier;
import com.atg.openssp.dspSimUi.model.dsp.SupplierModel;
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
import java.text.ParseException;

/**
 * @author Brian Sorensen
 */
public class SupplierMaintenancePanel extends JPanel implements ListSelectionListener, ActionListener, MessageNotificationListener {
    private static final Logger log = LoggerFactory.getLogger(SupplierMaintenancePanel.class);
    private final SupplierModel model;
    private final JList<Supplier> lSuppliers;
    private final JLabel lbId = new JLabel("");
    private final JTextField tfShortName = new JTextField(12);
    private final JTextField tfAddShortName = new JTextField(12);
    private final JTextField tfEndPoint = new JTextField(40);
    private final JTextField tfAddEndPoint = new JTextField(40);
    private final JTextField tfAdId = new JTextField(12);
    private final JTextField tfAddAdId = new JTextField(12);
    private final JTextField tfOpenRtbVersion = new JTextField(5);
    private final JTextField tfAddOpenRtbVersion = new JTextField(5);
    private final JTextField tfContentType = new JTextField(25);
    private final JTextField tfAddContentType = new JTextField(25);
//    private final DefaultListModel<String> mAdomain = new DefaultListModel<String>();
//    private final JList<String> lAdomain = new JList<String>(mAdomain);
//    private final DefaultListModel<String> mAddAdomain = new DefaultListModel<String>();
//    private final JList<String> lAddAdomain = new JList<String>(mAddAdomain);
    private final JTextField tfAcceptEncoding = new JTextField(25);
    private final JTextField tfAddAcceptEncoding = new JTextField(25);
    private final JTextField tfCId = new JTextField(25);
    private final JTextField tfAddCId = new JTextField(25);
    private final JTextField tfCrId = new JTextField(25);
    private final JTextField tfAddCrId = new JTextField(25);
    private final DefaultListModel<ContentCategory> mCat = new DefaultListModel<ContentCategory>();
    private final JList<ContentCategory> lCat = new JList<ContentCategory>(mCat);
    private final DefaultListModel<String> mAddCat = new DefaultListModel<String>();
    private final JList<String> lAddCat = new JList<String>(mAddCat);


    private final JTextField tfAddNewADomain = new JTextField(25);
    private final JTextField tfAddNewCat = new JTextField(25);
    private final JButton bAddADomain = new JButton("add");
    private final JButton bAddCat = new JButton("add");

    private final JButton bUpdate = new JButton("update");
    private final JButton bRemove = new JButton("remove");
    private final JButton bAdd = new JButton("add");
    private final JButton bRefresh = new JButton("refresh");
    private final JTextField tfMemo = new JTextField(20);

    public SupplierMaintenancePanel(SupplierModel model) {
        this.model = model;
        lSuppliers = new JList<>(model.getSupplierModel());

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

        lSuppliers.setVisibleRowCount(10);
        addItem(pTop, "Suppliers: ", lSuppliers);
        lSuppliers.addListSelectionListener(this);
        bUpdate.setEnabled(false);
        bUpdate.addActionListener(this);
        addItem(pTop, "", bUpdate);
        bRemove.setEnabled(false);
        bRemove.addActionListener(this);
        addItem(pTop, "", bRemove);

        pMiddle.setBorder(new TitledBorder("Active Supplier"));
        addItem(pMiddle, "ID:", lbId);
        addItem(pMiddle, "Short Name:", tfShortName);
        addItem(pMiddle, "End Point:", tfEndPoint);
        addItem(pMiddle, "AD ID:", tfAdId);
        addItem(pMiddle, "Open Rtb Version:", tfOpenRtbVersion);
        addItem(pMiddle, "Content Type:", tfContentType);
//        addItem(pMiddle, "A DOMAIN:", lAdomain);
        addItem(pMiddle, "Accept Encoding:", tfAcceptEncoding);
        addItem(pMiddle, "C ID:", tfCId);
        addItem(pMiddle, "CR ID:", tfCrId);
        addItem(pMiddle, "CAT:", lCat);

        pRight.setBorder(new TitledBorder("Add Supplier"));
        addItem(pRight, "Short Name:", tfAddShortName);
        addItem(pRight, "End Point:", tfAddEndPoint);
        addItem(pRight, "AD ID:", tfAddAdId);
        addItem(pRight, "Open Rtb Version:", tfAddOpenRtbVersion);
        addItem(pRight, "Content Type:", tfContentType);
//        addItem(pRight, "A DOMAIN:", lAddAdomain);
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        addItem(pRight, "NEW A DOMAIN:", p);
        p.add(tfAddNewADomain);
        p.add(bAddADomain);

        addItem(pRight, "Accept Encoding:", tfAddAcceptEncoding);
        addItem(pRight, "C ID:", tfAddCId);
        addItem(pRight, "CR ID:", tfAddCrId);
        addItem(pRight, "CAT:", lAddCat);
        p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        addItem(pRight, "NEW CAT:", p);
        p.add(tfAddNewCat);
        p.add(bAddCat);


        bAddADomain.addActionListener(this);
        bAddCat.addActionListener(this);
        bAdd.addActionListener(this);
        addItem(pRight, "", bAdd);
        bRefresh.addActionListener(this);
        addItem(pRight, "", bRefresh);

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
            Supplier sb = lSuppliers.getSelectedValue();
            if (sb != null) {
                lbId.setText(Long.toString(sb.getSupplierId()));
                resetActiveDisplay(sb);
                bUpdate.setEnabled(true);
                bRemove.setEnabled(true);
            } else {
                if (lSuppliers.getModel().getSize() == 0) {
                    resetActiveDisplay(sb);
                    bUpdate.setEnabled(false);
                    bRemove.setEnabled(false);
                } else {
                    resetActiveDisplay(sb);
                    lSuppliers.setSelectedIndex(0);
                    bUpdate.setEnabled(true);
                    bRemove.setEnabled(true);
                }

    }
}
        model.setMessage("");
    }

    private void resetActiveDisplay(Supplier sb) {
        if (sb == null) {
            tfShortName.setText("");
            tfEndPoint.setText("");
            tfAdId.setText("");
            tfOpenRtbVersion.setText("");
            tfContentType.setText("");
//            mAdomain.clear();
            tfAcceptEncoding.setText("");
            tfCId.setText("");
            tfCrId.setText("");
            mCat.clear();
            tfMemo.setText("");
        } else {
            tfShortName.setText(sb.getShortName());
            tfEndPoint.setText(sb.getEndPoint());
//            tfAdId.setText(sb.getAdId());
            tfOpenRtbVersion.setText(sb.getOpenRtbVersion());
            tfContentType.setText(sb.getContentType());
//            mAdomain.clear();
//            for (String s : sb.getAdomain()) {
//                mAdomain.addElement(s);
//            }
            tfAcceptEncoding.setText(sb.getAcceptEncoding());
//            tfCId.setText(sb.getCId());
//            tfCrId.setText(sb.getCrId());
            mCat.clear();
//            for (ContentCategory s : sb.getCats()) {
//                mCat.addElement(s);
//            }
            tfMemo.setText("Supplier selected.");
        }
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        if (ev.getSource() == bUpdate) {
            Supplier sb = lSuppliers.getSelectedValue();
            if (sb != null) {
                try {
                    Supplier sbN = new Supplier();
                    sbN.setSupplierId(sb.getSupplierId());
                    sbN.setShortName(tfShortName.getText());
                    sbN.setEndPoint(tfEndPoint.getText());
//                    sbN.setAdId(tfAdId.getText());
                    sbN.setOpenRtbVersion(tfOpenRtbVersion.getText());
                    sbN.setContentType(tfContentType.getText());
//                    sbN.setAdomain(Collections.list(mAdomain.elements()));
                    sbN.setAcceptEncoding(tfAcceptEncoding.getText());
//                    sbN.setCId(tfCId.getText());
//                    sbN.setCrId(tfCrId.getText());
//                    sbN.setCats(Collections.list(mCat.elements()));
                    model.sendUpdateCommand(sbN);
                    model.setMessage("Supplier saved.");
                } catch (ModelException e) {
                    model.setMessageAsFault(e.getMessage());
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    e.printStackTrace();
                    model.setMessageAsFault("Could not save Supplier due to invalid price.");
                }
            } else {
                model.setMessageAsWarning("No Supplier selected.");
            }
            repaint();
        } else if (ev.getSource() == bRefresh) {
            try {
                model.sendListCommand();
            } catch (ModelException e) {
                model.setMessageAsFault(e.getMessage());
            }
        } else if (ev.getSource() == bAdd) {
            try {
//                float newPrice = formatter.parse(tfAddPrice.getText()).floatValue();
                Supplier sbN = new Supplier();
                sbN.setSupplierId(0L);
                sbN.setShortName(tfAddShortName.getText());
                sbN.setEndPoint(tfAddEndPoint.getText());
//                sbN.setAdId(tfAddAdId.getText());
                sbN.setOpenRtbVersion(tfAddOpenRtbVersion.getText());
                sbN.setContentType(tfAddContentType.getText());
//                sbN.setAdomain(Collections.list(mAddAdomain.elements()));
                sbN.setAcceptEncoding(tfAddAcceptEncoding.getText());
//                sbN.setCId(tfAddCId.getText());
//                sbN.setCrId(tfAddCrId.getText());
//                sbN.setCats(Collections.list(mCat.elements()));

                model.sendAddCommand(sbN);
                model.setMessage("Supplier added.");
                tfAddShortName.setText("");
                tfAddEndPoint.setText("");
                tfAddAdId.setText("");
                tfAddOpenRtbVersion.setText("");
                tfAddContentType.setText("");
//                mAddAdomain.clear();
                tfAddAcceptEncoding.setText("");
                tfAddCId.setText("");
                tfAddCrId.setText("");
                mAddCat.clear();
            } catch (ModelException e) {
                model.setMessageAsFault(e.getMessage());
            }
            repaint();
        } else if (ev.getSource() == bRemove) {
            Supplier sb = lSuppliers.getSelectedValue();
            if (sb != null) {
                try {
                    model.sendRemoveCommand(sb.getSupplierId());
                } catch (ModelException e) {
                    model.setMessageAsFault(e.getMessage());
                }
            } else {
                model.setMessageAsWarning("No Supplier selected.");
            }
            repaint();
        } else if (ev.getSource() == bAddADomain) {
            String value = tfAddNewADomain.getText();
            if (!"".equals(value.trim())) {
//                mAddAdomain.addElement(value);
                tfAddNewADomain.setText("");
            }
        } else if (ev.getSource() == bAddCat) {
            String value = tfAddNewCat.getText();
            if (!"".equals(value.trim())) {
                mAddCat.addElement(value);
                tfAddNewCat.setText("");
            }
        }

    }

    @Override
    public void sendMessage(MessageStatus s, String m) {
        tfMemo.setText(m);
        tfMemo.setBackground(s.getColor());
    }

}
