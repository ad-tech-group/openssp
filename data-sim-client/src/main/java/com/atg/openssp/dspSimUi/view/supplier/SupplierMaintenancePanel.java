package com.atg.openssp.dspSimUi.view.supplier;

import com.atg.openssp.common.demand.SupplierAdFormat;
import com.atg.openssp.common.demand.SupplierAdPlatform;
import com.atg.openssp.dspSimUi.model.MessageNotificationListener;
import com.atg.openssp.dspSimUi.model.MessageStatus;
import com.atg.openssp.dspSimUi.model.ModelException;
import com.atg.openssp.common.demand.Supplier;
import com.atg.openssp.dspSimUi.model.supplier.SupplierModel;
import openrtb.tables.BooleanInt;
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
import java.util.Collections;

/**
 * @author Brian Sorensen
 */
public class SupplierMaintenancePanel extends JPanel implements ListSelectionListener, ActionListener, MessageNotificationListener {
    private static final Logger log = LoggerFactory.getLogger(SupplierMaintenancePanel.class);
    private final SupplierModel model;
    private final JList<Supplier> lSuppliers;
    private final JLabel lbId = new JLabel("");
    private final JTextField tfShortName = new JTextField(12);
    private final JTextField tfEndPoint = new JTextField(40);
    private final DefaultComboBoxModel<Boolean> mConnectionKeepAlive = new DefaultComboBoxModel<>();
    private final JComboBox<Boolean> cbConnectionKeepAlive = new JComboBox<>(mConnectionKeepAlive);
    private final JTextField tfOpenRtbVersion = new JTextField(5);
    private final JTextField tfContentType = new JTextField(25);
    private final JTextField tfAcceptEncoding = new JTextField(25);
    private final JTextField tfContentEncoding = new JTextField(25);
    private final DefaultComboBoxModel<String> mCurrency = new DefaultComboBoxModel<>();
    private final JComboBox<String> cbCurrency = new JComboBox<>(mCurrency);
    private final JTextField tfTmax = new JTextField(5);

    private final DefaultListModel<SupplierAdFormat> mAllowedAdFormat = new DefaultListModel<>();
    private final JList<SupplierAdFormat> lAllowedAdFormat = new JList<>(mAllowedAdFormat);

    private final DefaultComboBoxModel<SupplierAdFormat> mAddAllowedAdFormats = new DefaultComboBoxModel<>();
    private final JComboBox<SupplierAdFormat> cbAddAllowedAdFormat = new JComboBox<>(mAddAllowedAdFormats);

    private final DefaultListModel<SupplierAdPlatform> mAllowedAdPlatform = new DefaultListModel<>();
    private final JList<SupplierAdPlatform> lAllowedAdPlatform = new JList<>(mAllowedAdPlatform);

    private final DefaultComboBoxModel<SupplierAdPlatform> mAddAllowedAdPlatforms = new DefaultComboBoxModel<>();
    private final JComboBox<SupplierAdPlatform> cbAddAllowedAdPlatform = new JComboBox<>(mAddAllowedAdPlatforms);

    private final JTextField tfDemandBrokerFilterClassName = new JTextField(25);
    private final DefaultComboBoxModel<BooleanInt> mUnderTest = new DefaultComboBoxModel<>();
    private final JComboBox<BooleanInt> cbUnderTest = new JComboBox<>(mUnderTest);
    private final DefaultComboBoxModel<BooleanInt> mActive = new DefaultComboBoxModel<>();
    private final JComboBox<BooleanInt> cbActive = new JComboBox<>(mActive);

    private final JTextField tfMemo = new JTextField(20);

    private final JButton bAddAllowedAdFormat;
    private final JButton bRemoveAllowedAdFormat;
    private final JButton bAddAllowedAdPlatform;
    private final JButton bRemoveAllowedAdPlatform;
    private final JButton bUpdate;
    private final JButton bRemove;
    private final JButton bAdd;
    private final JButton bRefresh;
    private final JButton bLoad;
    private final JButton bImport;
    private final JButton bExport;
    private final JButton bClear;
    private Supplier active;

    public SupplierMaintenancePanel(SupplierModel model) {
        this.model = model;
        setLayout(new BorderLayout());

        mConnectionKeepAlive.addElement(Boolean.TRUE);
        mConnectionKeepAlive.addElement(Boolean.FALSE);
        mCurrency.addElement("USD");
        for (SupplierAdFormat cc : SupplierAdFormat.values()) {
            mAddAllowedAdFormats.addElement(cc);
        }
        cbAddAllowedAdFormat.setSelectedItem(null);
        for (SupplierAdPlatform cc : SupplierAdPlatform.values()) {
            mAddAllowedAdPlatforms.addElement(cc);
        }
        cbAddAllowedAdPlatform.setSelectedItem(null);

        for (BooleanInt cc : BooleanInt.values()) {
            mUnderTest.addElement(cc);
        }
        cbUnderTest.setSelectedItem(null);

        for (BooleanInt cc : BooleanInt.values()) {
            mActive.addElement(cc);
        }
        cbActive.setSelectedItem(null);

        lSuppliers = new JList<>(model.getSupplierModel());
        lSuppliers.setSelectedIndex(-1);

        bAddAllowedAdFormat = new JButton(model.getTemplateText("ALLOWED_AD_FORMAT_ADD"));
        bRemoveAllowedAdFormat = new JButton(model.getTemplateText("ALLOWED_AD_FORMAT_REMOVE"));
        bAddAllowedAdPlatform = new JButton(model.getTemplateText("ALLOWED_AD_PLATFORM_ADD"));
        bRemoveAllowedAdPlatform = new JButton(model.getTemplateText("ALLOWED_AD_PLATFORM_REMOVE"));
        bUpdate = new JButton(model.getTemplateText("SUPPLIER_UPDATE"));
        bRemove = new JButton(model.getTemplateText("SUPPLIER_REMOVE"));
        bAdd = new JButton(model.getTemplateText("SUPPLIER_INIT"));
        bRefresh = new JButton(model.getTemplateText("SUPPLIER_LIST_REFRESH"));
        bLoad = new JButton(model.getTemplateText("SUPPLIER_LOAD"));
        bImport = new JButton(model.getTemplateText("SUPPLIER_IMPORT"));
        bExport = new JButton(model.getTemplateText("SUPPLIER_EXPORT"));
        bClear = new JButton(model.getTemplateText("SUPPLIER_CLEAR"));

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

        lSuppliers.setVisibleRowCount(6);
        addItem(pTop, model.getTemplateText("SUPPLIERS"), lSuppliers);
        lSuppliers.addListSelectionListener(this);

        bRefresh.addActionListener(this);
        addItem(pRight, "", bRefresh);

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

        pMiddle.setBorder(new TitledBorder(model.getTemplateText("ACTIVE_SUPPLIER")));
        pMiddle.setLayout(new BoxLayout(pMiddle, BoxLayout.Y_AXIS));
        addItem(pMiddle, "ID:", lbId);
        addItem(pMiddle, "Short Name:", tfShortName);
        addItem(pMiddle, "End Point:", tfEndPoint);
        addItem(pMiddle, "Connection Keep Alive:", cbConnectionKeepAlive);
        addItem(pMiddle, "Open Rtb Version:", tfOpenRtbVersion);
        addItem(pMiddle, "Content Type:", tfContentType);
        addItem(pMiddle, "Accept Encoding:", tfAcceptEncoding);
        addItem(pMiddle, "Content Encoding:", tfContentEncoding);
        addItem(pMiddle, "Currency:", cbCurrency);
        addItem(pMiddle, "T Max:", tfTmax);
        addItem(pMiddle, model.getTemplateText("SUPPLIER_AD_FORMAT"), lAllowedAdFormat);
        lAllowedAdFormat.setVisibleRowCount(3);
        addItem(pMiddle, model.getTemplateText("SUPPLIER_AD_PLATFORM"), lAllowedAdPlatform);
        lAllowedAdPlatform.setVisibleRowCount(3);
        addItem(pMiddle, model.getTemplateText("DEMAND_BROKER_FILTER_CLASS_NAME"), tfDemandBrokerFilterClassName);
        addItem(pMiddle, model.getTemplateText("UNDER_TEST"), cbUnderTest);
        addItem(pMiddle, model.getTemplateText("ACTIVE"), cbActive);

        p = new JPanel();
        p.setBorder(new TitledBorder(model.getTemplateText("MAINTAIN_SUPPLIER_AD_FORMAT")));
        addItem(pRight, model.getTemplateText("SUPPLIER_AD_FORMATS"), p);
        p.add(cbAddAllowedAdFormat);
        bAddAllowedAdFormat.addActionListener(this);
        bAddAllowedAdFormat.setEnabled(false);
        addItem(p, "", bAddAllowedAdFormat);
        bRemoveAllowedAdFormat.addActionListener(this);
        bRemoveAllowedAdFormat.setEnabled(false);
        addItem(p, "", bRemoveAllowedAdFormat);

        p = new JPanel();
        p.setBorder(new TitledBorder(model.getTemplateText("MAINTAIN_SUPPLIER_AD_PLATFORM")));
        addItem(pRight, model.getTemplateText("SUPPLIER_AD_PLATFORMS"), p);
        p.add(cbAddAllowedAdPlatform);
        bAddAllowedAdPlatform.addActionListener(this);
        bAddAllowedAdPlatform.setEnabled(false);
        addItem(p, "", bAddAllowedAdPlatform);
        bRemoveAllowedAdPlatform.addActionListener(this);
        bRemoveAllowedAdPlatform.setEnabled(false);
        addItem(p, "", bRemoveAllowedAdPlatform);

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
            active = lSuppliers.getSelectedValue();
            setStateForActive();
        }
        model.setMessage("");
    }

    private void setStateForActive() {
        if (active != null) {
            lbId.setText(Long.toString(active.getSupplierId()));
            resetActiveDisplay(active);
            bUpdate.setEnabled(true);
            bRemove.setEnabled(true);
            bAddAllowedAdFormat.setEnabled(true);
            bRemoveAllowedAdFormat.setEnabled(true);
            bAddAllowedAdPlatform.setEnabled(true);
            bRemoveAllowedAdPlatform.setEnabled(true);
        } else {
            if (lSuppliers.getModel().getSize() == 0) {
                resetActiveDisplay(active);
                bUpdate.setEnabled(false);
                bRemove.setEnabled(false);
                bAddAllowedAdFormat.setEnabled(false);
                bRemoveAllowedAdFormat.setEnabled(false);
                bAddAllowedAdPlatform.setEnabled(false);
                bRemoveAllowedAdPlatform.setEnabled(false);
            } else {
                resetActiveDisplay(active);
                lSuppliers.setSelectedIndex(0);
                bUpdate.setEnabled(true);
                bRemove.setEnabled(true);
                bAddAllowedAdFormat.setEnabled(true);
                bRemoveAllowedAdFormat.setEnabled(true);
                bAddAllowedAdPlatform.setEnabled(true);
                bRemoveAllowedAdPlatform.setEnabled(true);
            }

        }
    }

    private void resetActiveDisplay(Supplier sb) {
        if (sb == null) {
            tfShortName.setText("");
            tfEndPoint.setText("");
            cbConnectionKeepAlive.setSelectedIndex(-1);
            tfOpenRtbVersion.setText("");
            tfContentType.setText("");
            tfAcceptEncoding.setText("");
            tfContentEncoding.setText("");
            cbCurrency.setSelectedIndex(-1);
            tfTmax.setText("");
            mAllowedAdFormat.clear();
            cbAddAllowedAdFormat.setSelectedItem(null);
            mAllowedAdPlatform.clear();
            cbAddAllowedAdPlatform.setSelectedItem(null);
            tfDemandBrokerFilterClassName.setText("");
            cbUnderTest.setSelectedIndex(-1);
            cbActive.setSelectedIndex(-1);
            tfMemo.setText("");
        } else {
            tfShortName.setText(sb.getShortName());
            tfEndPoint.setText(sb.getEndPoint());
            cbConnectionKeepAlive.setSelectedItem(sb.isConnectionKeepAlive());
            tfOpenRtbVersion.setText(sb.getOpenRtbVersion());
            tfContentType.setText(sb.getContentType());
            tfAcceptEncoding.setText(sb.getAcceptEncoding());
            tfContentEncoding.setText(sb.getContentEncoding());
            cbCurrency.setSelectedItem(sb.getCurrency());
            Integer max = sb.getTmax();
            if (max == null) {
                tfTmax.setText("");
            } else {
                tfTmax.setText(Integer.toString(max));
            }
            mAllowedAdFormat.clear();
            for (SupplierAdFormat s : sb.getAllowedAdFormats()) {
                mAllowedAdFormat.addElement(s);
            }
            mAllowedAdPlatform.clear();
            for (SupplierAdPlatform s : sb.getAllowedAdPlatforms()) {
                mAllowedAdPlatform.addElement(s);
            }
            tfDemandBrokerFilterClassName.setText(sb.getDemandBrokerFilterClassName());
            cbUnderTest.setSelectedItem(sb.getUnderTest());
            cbActive.setSelectedItem(sb.getActive());
            tfMemo.setText("Supplier selected.");
        }
    }

    private void populate(Supplier s) throws ParseException {
        s.setShortName(tfShortName.getText());
        s.setEndPoint(tfEndPoint.getText());
        s.setConnectionKeepAlive((Boolean) cbConnectionKeepAlive.getSelectedItem());
        s.setOpenRtbVersion(tfOpenRtbVersion.getText());
        s.setContentType(tfContentType.getText());
        s.setAcceptEncoding(tfAcceptEncoding.getText());
        s.setContentEncoding(tfContentEncoding.getText());
        s.setCurrency((String) cbCurrency.getSelectedItem());
        if ("".equals(tfTmax.getText())) {
            s.setTmax(null);
        } else {
            s.setTmax(Integer.parseInt(tfTmax.getText()));
        }
        s.setAllowedAdFormats(Collections.list(mAllowedAdFormat.elements()));
        s.setAllowedAdPlatforms(Collections.list(mAllowedAdPlatform.elements()));
        s.setDemandBrokerFilterClassName(tfDemandBrokerFilterClassName.getText());
        s.setUnderTest((BooleanInt) cbUnderTest.getSelectedItem());
        s.setActive((BooleanInt) cbActive.getSelectedItem());
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        if (ev.getSource() == bUpdate) {
            Supplier sb = lSuppliers.getSelectedValue();
            if (sb != null) {
                try {
                    Supplier sbN = new Supplier();
                    sbN.setSupplierId(sb.getSupplierId());
                    populate(sbN);
                    model.sendUpdateCommand(sbN);
                    model.setMessage("Supplier saved.");
                    active=null;
                    setStateForActive();
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
        } else if (ev.getSource() == bAdd) {
            lSuppliers.setSelectedIndex(-1);
            active = new Supplier();
            active.setSupplierId(666L);
            //TODO: fix
            resetActiveDisplay(active);
            setStateForActive();
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
        } else if (ev.getSource() == bAddAllowedAdFormat) {
            SupplierAdFormat value = (SupplierAdFormat) cbAddAllowedAdFormat.getSelectedItem();
            if (value != null && !mAllowedAdFormat.contains(value)) {
                mAllowedAdFormat.addElement(value);
            }
            cbAddAllowedAdFormat.setSelectedItem(null);
            repaint();
        } else if (ev.getSource() == bRemoveAllowedAdFormat) {
            SupplierAdFormat value = lAllowedAdFormat.getSelectedValue();
            if (value != null) {
                mAllowedAdFormat.removeElement(value);
                lAllowedAdFormat.setSelectedIndex(-1);
            } else {
                value = (SupplierAdFormat) cbAddAllowedAdFormat.getSelectedItem();
                if (value != null) {
                    mAllowedAdFormat.removeElement(value);
                    cbAddAllowedAdFormat.setSelectedIndex(-1);
                } else {
                    model.setMessage("Need to select category to remove.");
                }
            }
            repaint();
        } else if (ev.getSource() == bAddAllowedAdPlatform) {
            SupplierAdPlatform value = (SupplierAdPlatform) cbAddAllowedAdPlatform.getSelectedItem();
            if (value != null && !this.mAllowedAdPlatform.contains(value)) {
                mAllowedAdPlatform.addElement(value);
            }
            cbAddAllowedAdPlatform.setSelectedItem(null);
            repaint();
        } else if (ev.getSource() == bRemoveAllowedAdPlatform) {
            SupplierAdPlatform value = lAllowedAdPlatform.getSelectedValue();
            if (value != null) {
                mAllowedAdPlatform.removeElement(value);
                lAllowedAdPlatform.setSelectedIndex(-1);
            } else {
                value = (SupplierAdPlatform) cbAddAllowedAdPlatform.getSelectedItem();
                if (value != null) {
                    mAllowedAdPlatform.removeElement(value);
                    cbAddAllowedAdPlatform.setSelectedIndex(-1);
                } else {
                    model.setMessage("Need to select category to remove.");
                }
            }
            repaint();
        } else if (ev.getSource() == bRefresh) {
            try {
                model.sendListCommand();
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

}
