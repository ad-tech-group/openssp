package com.atg.openssp.dspSimUi.view.currency;

import com.atg.openssp.common.core.broker.dto.CurrencyDto;
import com.atg.openssp.common.model.EurRef;
import com.atg.openssp.dspSimUi.model.MessageNotificationListener;
import com.atg.openssp.dspSimUi.model.MessageStatus;
import com.atg.openssp.dspSimUi.model.ModelException;
import com.atg.openssp.dspSimUi.model.currency.CurrencyModel;
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
import java.util.Collections;
import java.util.UUID;

/**
 * @author Brian Sorensen
 */
public class CurrencyMaintenancePanel extends JPanel implements ListSelectionListener, ActionListener, MessageNotificationListener {
    private static final Logger log = LoggerFactory.getLogger(CurrencyMaintenancePanel.class);
    private final CurrencyModel model;
    private final JList<CurrencyDto> lCurrencies;
    
    private final JTextField tfCurrency = new JTextField(4);
    private final DefaultListModel<EurRef> mRates = new DefaultListModel<>();
    private final JList<EurRef> lRates = new JList<>(mRates);

    private final DefaultComboBoxModel<EurRef> mAddRates = new DefaultComboBoxModel<>();
    private final JComboBox<EurRef> cbAddRates = new JComboBox<>(mAddRates);

    private final JTextField tfMemo = new JTextField(20);

    private final JButton bAddRate;
    private final JButton bRemoveRate;
    private final JButton bUpdate;
    private final JButton bRemove;
    private final JButton bAdd;
    private final JButton bLoad;
    private final JButton bImport;
    private final JButton bExport;
    private final JButton bClear;
    private CurrencyDto active;

    public CurrencyMaintenancePanel(CurrencyModel model) {
        this.model = model;
        setLayout(new BorderLayout());

        EurRef er = new EurRef();
        er.setCurrency("EUR");
        er.setRate(55);
        mAddRates.addElement(er);

        lCurrencies = new JList<>(model.getCurrencyModel());
        lCurrencies.setSelectedIndex(-1);

        bAddRate = new JButton(model.getTemplateText("RATE_ADD"));
        bRemoveRate = new JButton(model.getTemplateText("RATE_REMOVE"));
        bUpdate = new JButton(model.getTemplateText("CURRENCY_UPDATE"));
        bRemove = new JButton(model.getTemplateText("CURRENCY_REMOVE"));
        bAdd = new JButton(model.getTemplateText("CURRENCY_INIT"));
        bLoad = new JButton(model.getTemplateText("CURRENCY_LOAD"));
        bImport = new JButton(model.getTemplateText("CURRENCY_IMPORT"));
        bExport = new JButton(model.getTemplateText("CURRENCY_EXPORT"));
        bClear = new JButton(model.getTemplateText("CURRENCY_CLEAR"));

        JPanel pTop = new JPanel();
        add(pTop, BorderLayout.NORTH);
        JPanel pBottom = new JPanel();
        pBottom.setLayout(new BoxLayout(pBottom, BoxLayout.Y_AXIS));
        add(pBottom, BorderLayout.SOUTH);
        JPanel p = new JPanel();
        JPanel pMiddle = new JPanel();
        pMiddle.setLayout(new BoxLayout(pMiddle, BoxLayout.Y_AXIS));
        p.add(pMiddle);

        JPanel pCurrency = new JPanel();
        pMiddle.add(pCurrency);
        JPanel pPublisher = new JPanel();
        pMiddle.add(pPublisher);

        add(p, BorderLayout.CENTER);
        JPanel pRight = new JPanel();
        pRight.setLayout(new BoxLayout(pRight, BoxLayout.Y_AXIS));
        add(pRight, BorderLayout.EAST);

        JPanel pCommands = new JPanel();
        pCommands.setLayout(new FlowLayout(FlowLayout.CENTER));
        pBottom.add(pCommands, BorderLayout.SOUTH);

        lCurrencies.setVisibleRowCount(10);
        addItem(pTop, model.getTemplateText("CURRENCIES"), lCurrencies);
        lCurrencies.addListSelectionListener(this);


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

        p = new JPanel();
        p.setBorder(new TitledBorder(model.getTemplateText("MAINTAIN_RATE")));
        addItem(pMiddle, model.getTemplateText("RATES"), p);
        p.add(cbAddRates);

        JPanel p1  = new JPanel();
        p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));
        p1.setBorder(new TitledBorder(model.getTemplateText("RATE")));
        addItem(p, "", p1);

        bAddRate.addActionListener(this);
        bAddRate.setEnabled(false);
        addItem(p1, "", bAddRate);
        bRemoveRate.addActionListener(this);
        bRemoveRate.setEnabled(false);
        addItem(p1, "", bRemoveRate);

        pCurrency.setBorder(new TitledBorder(model.getTemplateText("ACTIVE_CURRENCY")));
        pCurrency.setLayout(new BoxLayout(pCurrency, BoxLayout.Y_AXIS));
        addItem(pCurrency, model.getTemplateText("CURRENCY"), tfCurrency);

        JPanel pRates = new JPanel();
        pRates.setLayout(new BoxLayout(pRates, BoxLayout.X_AXIS));
        addItem(pCurrency, "", pRates);

        addItem(pRates, model.getTemplateText("RATES"), new JScrollPane(lRates));
        lRates.setVisibleRowCount(3);

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
            active = lCurrencies.getSelectedValue();
            setStateForActive();
        }
        model.setMessage("");
    }

    private void setStateForActive() {
        if (active != null) {
            resetActiveDisplay(active);
            bUpdate.setEnabled(true);
            bRemove.setEnabled(true);
            bAddRate.setEnabled(true);
            bRemoveRate.setEnabled(true);
        } else {
            if (lCurrencies.getModel().getSize() == 0) {
                resetActiveDisplay(active);
                bUpdate.setEnabled(false);
                bRemove.setEnabled(false);
                bAddRate.setEnabled(false);
                bRemoveRate.setEnabled(false);
            } else {
                resetActiveDisplay(active);
                lCurrencies.setSelectedIndex(0);
                bUpdate.setEnabled(true);
                bRemove.setEnabled(true);
                bAddRate.setEnabled(true);
                bRemoveRate.setEnabled(true);
            }

        }
    }

    private void resetActiveDisplay(CurrencyDto sb) {
        if (sb == null) {
            tfCurrency.setText("");
            mRates.clear();
            cbAddRates.setSelectedItem(null);
            tfMemo.setText("");
        } else {
            tfCurrency.setText(sb.getCurrency());
            mRates.clear();
            for (EurRef s : sb.getData()) {
                mRates.addElement(s);
            }
            tfMemo.setText("Currency selected.");
        }
    }

    private void populate(CurrencyDto sb) throws ParseException {
        sb.setCurrency(tfCurrency.getText());
        sb.setData(Collections.list(mRates.elements()));
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        if (ev.getSource() == bUpdate) {
            if (active != null) {
                try {
                    CurrencyDto sbN = new CurrencyDto();
                    sbN.setCurrency(active.getCurrency());
                    populate(sbN);
                    model.sendUpdateCommand(sbN);
                    model.setMessage("Currency saved.");
                    active = null;
                    setStateForActive();
                } catch (ModelException e) {
                    model.setMessageAsFault(e.getMessage());
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    e.printStackTrace();
                    model.setMessageAsFault("Could not save Currency due to invalid price.");
                }
            } else {
                model.setMessageAsWarning("No Currency selected.");
            }
            repaint();
        } else if (ev.getSource() == bAdd) {
            lCurrencies.setSelectedIndex(-1);
            active = new CurrencyDto();
            resetActiveDisplay(active);
            setStateForActive();
            repaint();
        } else if (ev.getSource() == bRemove) {
            if (active != null) {
                try {
                    model.sendRemoveCommand(active.getCurrency());
                } catch (ModelException e) {
                    model.setMessageAsFault(e.getMessage());
                }
            } else {
                model.setMessageAsWarning("No Currency selected.");
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
        } else if (ev.getSource() == bAddRate) {
            EurRef value = (EurRef) cbAddRates.getSelectedItem();
            if (value != null && !mRates.contains(value)) {
                mRates.addElement(value);
            }
            cbAddRates.setSelectedItem(null);
            repaint();
        } else if (ev.getSource() == bRemoveRate) {
            EurRef value = lRates.getSelectedValue();
            if (value != null) {
                mRates.removeElement(value);
                lRates.setSelectedIndex(-1);
            } else {
                value = (EurRef) cbAddRates.getSelectedItem();
                if (value != null) {
                    mRates.removeElement(value);
                    cbAddRates.setSelectedIndex(-1);
                } else {
                    model.setMessage("Need to select category to remove.");
                }
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
