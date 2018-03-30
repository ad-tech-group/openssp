package com.atg.openssp.dspSimUi.view.site;

import com.atg.openssp.dspSimUi.model.MessageNotificationListener;
import com.atg.openssp.dspSimUi.model.MessageStatus;
import com.atg.openssp.dspSimUi.model.ModelException;
import com.atg.openssp.dspSimUi.model.site.SiteModel;
import openrtb.bidrequest.model.Site;
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

/**
 * @author Brian Sorensen
 */
public class SiteMaintenancePanel extends JPanel implements ListSelectionListener, ActionListener, MessageNotificationListener {
    private static final Logger log = LoggerFactory.getLogger(SiteMaintenancePanel.class);
    private final SiteModel model;
    private final JList<Site> lSites;
    private final JLabel lbId = new JLabel("");
    private final JTextField tfShortName = new JTextField(20);
    private final JTextField tfAddShortName = new JTextField(12);
    private final JTextField tfPrice = new JTextField(8);
    private final JTextField tfAddPrice = new JTextField(8);
    private final JTextField tfAdId = new JTextField(12);
    private final JTextField tfAddAdId = new JTextField(12);
//    private final JTextField tfNUrl = new JTextField(20);
    private final JTextField tfAddNUrl = new JTextField(20);
    private final JTextField tfAdm = new JTextField(25);
    private final JTextField tfAddAdm = new JTextField(25);
    private final DefaultListModel<String> mAdomain = new DefaultListModel<String>();
    private final JList<String> lAdomain = new JList<String>(mAdomain);
    private final DefaultListModel<String> mAddAdomain = new DefaultListModel<String>();
    private final JList<String> lAddAdomain = new JList<String>(mAddAdomain);
    private final JTextField tfIUrl = new JTextField(25);
    private final JTextField tfAddIUrl = new JTextField(25);
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
    private final JTextField tfMemo = new JTextField(20);

    public SiteMaintenancePanel(SiteModel model) {
        this.model = model;
        lSites = new JList<>(model.getSiteModel());

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

        lSites.setVisibleRowCount(10);
        addItem(pTop, "Sites: ", lSites);
        lSites.addListSelectionListener(this);
        bUpdate.setEnabled(false);
        bUpdate.addActionListener(this);
        addItem(pTop, "", bUpdate);
        bRemove.setEnabled(false);
        bRemove.addActionListener(this);
        addItem(pTop, "", bRemove);

        pMiddle.setBorder(new TitledBorder("Active Site"));
        addItem(pMiddle, "ID:", lbId);
        addItem(pMiddle, "Short Name:", tfShortName);
        addItem(pMiddle, "Price:", tfPrice);
        addItem(pMiddle, "AD ID:", tfAdId);
//        addItem(pMiddle, "N Url:", tfNUrl);
        addItem(pMiddle, "ADM:", tfAdm);
        addItem(pMiddle, "A DOMAIN:", lAdomain);
        addItem(pMiddle, "I URL:", tfIUrl);
        addItem(pMiddle, "C ID:", tfCId);
        addItem(pMiddle, "CR ID:", tfCrId);
        addItem(pMiddle, "CAT:", lCat);

        pRight.setBorder(new TitledBorder("Add Site"));
        addItem(pRight, "Short Name:", tfAddShortName);
        addItem(pRight, "Price:", tfAddPrice);
        addItem(pRight, "AD ID:", tfAddAdId);
        addItem(pRight, "N URL:", tfAddNUrl);
        addItem(pRight, "ADM:", tfAddAdm);
        addItem(pRight, "A DOMAIN:", lAddAdomain);
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        addItem(pRight, "NEW A DOMAIN:", p);
        p.add(tfAddNewADomain);
        p.add(bAddADomain);

        addItem(pRight, "I URL:", tfAddIUrl);
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
            Site sb = lSites.getSelectedValue();
            if (sb != null) {
                lbId.setText(sb.getId());
                resetActiveDisplay(sb);
                bUpdate.setEnabled(true);
                bRemove.setEnabled(true);
            } else {
                if (lSites.getModel().getSize() == 0) {
                    resetActiveDisplay(sb);
                    bUpdate.setEnabled(false);
                    bRemove.setEnabled(false);
                } else {
                    resetActiveDisplay(sb);
                    lSites.setSelectedIndex(0);
                    bUpdate.setEnabled(true);
                    bRemove.setEnabled(true);
                }

    }
}
        model.setMessage("");
    }

    private void resetActiveDisplay(Site sb) {
        if (sb == null) {
            tfShortName.setText("");
            tfPrice.setText("");
            tfAdId.setText("");
//            tfNUrl.setText("");
            tfAdm.setText("");
            mAdomain.clear();
            tfCId.setText("");
            tfCrId.setText("");
            mCat.clear();
            tfMemo.setText("");
        } else {
            //tfShortName.setText(sb.getShortName());
            
            /*
            DecimalFormat formatter = new DecimalFormat("###,###,###.00");
            tfPrice.setText(formatter.format(sb.getPrice()));
            tfAdId.setText(sb.getAdId());
//            tfNUrl.setText(sb.getNUrl());
            tfAdm.setText(sb.getAdm());
            mAdomain.clear();
            for (String s : sb.getAdomain()) {
                mAdomain.addElement(s);
            }
            tfIUrl.setText(sb.getIUrl());
            tfCId.setText(sb.getCId());
            tfCrId.setText(sb.getCrId());
            mCat.clear();
            for (ContentCategory s : sb.getCats()) {
                mCat.addElement(s);
            }
            */
            tfMemo.setText("Site selected.");
        }
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        if (ev.getSource() == bUpdate) {
            Site sb = lSites.getSelectedValue();
            if (sb != null) {
                /*
                DecimalFormat formatter = new DecimalFormat("###,###,###.00");
                try {
                    float newPrice = formatter.parse(tfPrice.getText()).floatValue();

                    Site sbN = new Site(sb.getId());
//                    sbN.setImpId(tfImpId.getText());
                    sbN.setPrice(newPrice);
                    sbN.setAdId(tfAdId.getText());
//                    sbN.setNUrl(tfNUrl.getText());
                    sbN.setAdm(tfAdm.getText());
                    sbN.setAdomain(Collections.list(mAdomain.elements()));
                    sbN.setIUrl(tfIUrl.getText());
                    sbN.setCId(tfCId.getText());
                    sbN.setCrId(tfCrId.getText());
                    sbN.setCats(Collections.list(mCat.elements()));
                    model.sendUpdateCommand(sbN);
                    model.setMessage("Site saved.");
                } catch (ModelException e) {
                    model.setMessageAsFault(e.getMessage());
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    e.printStackTrace();

                    model.setMessageAsFault("Could not save Site due to invalid price.");
                }
                */
            } else {
                model.setMessageAsWarning("No Site selected.");
            }
            repaint();
        } else if (ev.getSource() == bAdd) {
            /*
            DecimalFormat formatter = new DecimalFormat("###,###,###.00");
            try {
                float newPrice = formatter.parse(tfAddPrice.getText()).floatValue();
                Site sbN = new Site(UUID.randomUUID().toString());
//                sbN.setImpId(tfAddImpId.getText());
                sbN.setPrice(newPrice);
                sbN.setAdId(tfAddAdId.getText());
//                sbN.setNUrl(tfAddNUrl.getText());
                sbN.setAdm(tfAddAdm.getText());
                sbN.setAdomain(Collections.list(mAddAdomain.elements()));
                sbN.setIUrl(tfAddIUrl.getText());
                sbN.setCId(tfAddCId.getText());
                sbN.setCrId(tfAddCrId.getText());
                sbN.setCats(Collections.list(mCat.elements()));

                model.sendAddCommand(sbN);
                model.setMessage("Site added.");
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
                model.setMessageAsFault("Could not add Site due to invalid price.");
            }
            */
            repaint();
        } else if (ev.getSource() == bRemove) {
            Site sb = lSites.getSelectedValue();
            if (sb != null) {
                try {
                    model.sendRemoveCommand(sb.getId());
                } catch (ModelException e) {
                    model.setMessageAsFault(e.getMessage());
                }
            } else {
                model.setMessageAsWarning("No Site selected.");
            }
            repaint();
        } else if (ev.getSource() == bAddADomain) {
            String value = tfAddNewADomain.getText();
            if (!"".equals(value.trim())) {
                mAddAdomain.addElement(value);
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
