package com.atg.openssp.dspSimUi.view.bannerAd;

import com.atg.openssp.common.cache.dto.BannerAd;
import com.atg.openssp.dspSimUi.model.MessageNotificationListener;
import com.atg.openssp.dspSimUi.model.MessageStatus;
import com.atg.openssp.dspSimUi.model.ModelException;
import com.atg.openssp.dspSimUi.model.ad.banner.BannerAdModel;
import openrtb.tables.*;
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
public class BannerAdMaintenancePanel extends JPanel implements ListSelectionListener, ActionListener, MessageNotificationListener {
    private static final Logger log = LoggerFactory.getLogger(BannerAdMaintenancePanel.class);
    private final BannerAdModel model;
    private final JList<BannerAd> lBannerAds;
    
    private final JLabel lbId = new JLabel("");
    private final JTextField tfPlacementId = new JTextField(12);
    private final JTextField tfW = new JTextField(5);
    private final JTextField tfH = new JTextField(5);
    private final JTextField tfWMax = new JTextField(5);
    private final JTextField tfHMax = new JTextField(5);
    private final JTextField tfWMin = new JTextField(5);
    private final JTextField tfHMin = new JTextField(5);
    private final JTextField tfAdUnitCode = new JTextField(5);
    private final JTextField tfSize = new JTextField(5);
    private final JTextField tfPromoSizes = new JTextField(5);
    private final JTextField tfTopframe = new JTextField(5);
    private final JTextField tfBidfloorCurrency = new JTextField(5);
    private final JTextField tfBidfloorPrice = new JTextField(5);

    private final DefaultListModel<String> mMimes = new DefaultListModel<>();
    private final JList<String> lMimes = new JList<>(mMimes);
    private final JTextField tfAddMimes = new JTextField(10);

    private final DefaultListModel<BannerAdType> mBtype = new DefaultListModel<>();
    private final JList<BannerAdType> lBtype = new JList<>(mBtype);
    private final DefaultComboBoxModel<BannerAdType> mAddBtype = new DefaultComboBoxModel<>();
    private final JComboBox<BannerAdType> cbAddBtype = new JComboBox<>(mAddBtype);

    private final DefaultListModel<CreativeAttribute> mBattrs = new DefaultListModel<>();
    private final JList<CreativeAttribute> lBattrs = new JList<>(mBattrs);
    private final DefaultComboBoxModel<CreativeAttribute> mAddBattrs = new DefaultComboBoxModel<>();
    private final JComboBox<CreativeAttribute> cbAddBattrs = new JComboBox<>(mAddBattrs);

    private final DefaultListModel<ExpandableDirectionType> mExpdir = new DefaultListModel<>();
    private final JList<ExpandableDirectionType> lExpdir = new JList<>(mExpdir);
    private final DefaultComboBoxModel<ExpandableDirectionType> mAddExpdir = new DefaultComboBoxModel<>();
    private final JComboBox<ExpandableDirectionType> cbAddExpdir = new JComboBox<>(mAddExpdir);

    private final DefaultListModel<ApiFramework> mApi = new DefaultListModel<>();
    private final JList<ApiFramework> lApi = new JList<>(mApi);
    private final DefaultComboBoxModel<ApiFramework> mAddApi = new DefaultComboBoxModel<>();
    private final JComboBox<ApiFramework> cbAddApi = new JComboBox<>(mAddApi);

//    private Object ext;

    private final JTextField tfMemo = new JTextField(20);

    private final JButton bAddMime;
    private final JButton bRemoveMime;
    private final JButton bAddBtype;
    private final JButton bRemoveBtype;
    private final JButton bAddBattrs;
    private final JButton bRemoveBattrs;
    private final JButton bAddExpdir;
    private final JButton bRemoveExpdir;
    private final JButton bAddApi;
    private final JButton bRemoveApi;
    private final JButton bUpdate;
    private final JButton bRemove;
    private final JButton bAdd;
    private final JButton bLoad;
    private final JButton bImport;
    private final JButton bExport;
    private final JButton bClear;
    private BannerAd active;

    public BannerAdMaintenancePanel(BannerAdModel model) {
        this.model = model;
        setLayout(new BorderLayout());

        for (BannerAdType cc : BannerAdType.values()) {
            mAddBtype.addElement(cc);
        }
        cbAddBtype.setSelectedItem(null);
        for (CreativeAttribute cc : CreativeAttribute.values()) {
            mAddBattrs.addElement(cc);
        }
        cbAddBattrs.setSelectedItem(null);
        for (ExpandableDirectionType cc : ExpandableDirectionType.values()) {
            mAddExpdir.addElement(cc);
        }
        cbAddExpdir.setSelectedItem(null);
        for (ApiFramework cc : ApiFramework.values()) {
            mAddApi.addElement(cc);
        }
        cbAddApi.setSelectedItem(null);

        lBannerAds = new JList<>(model.getBannerAdModel());
        lBannerAds.setSelectedIndex(-1);

        bAddMime = new JButton(model.getTemplateText("MIME_ADD"));
        bRemoveMime = new JButton(model.getTemplateText("MIME_REMOVE"));
        bAddBtype = new JButton(model.getTemplateText("BANNER_AD_TYPE_ADD"));
        bRemoveBtype = new JButton(model.getTemplateText("BANNER_AD_TYPE_REMOVE"));
        bAddBattrs = new JButton(model.getTemplateText("B_ATTRS_ADD"));
        bRemoveBattrs = new JButton(model.getTemplateText("B_ATTRS_REMOVE"));
        bAddExpdir = new JButton(model.getTemplateText("EXP_DIR_ADD"));
        bRemoveExpdir = new JButton(model.getTemplateText("EXP_DIR_REMOVE"));
        bAddApi = new JButton(model.getTemplateText("API_ADD"));
        bRemoveApi = new JButton(model.getTemplateText("API_REMOVE"));
        bUpdate = new JButton(model.getTemplateText("BANNER_AD_UPDATE"));
        bRemove = new JButton(model.getTemplateText("BANNER_AD_REMOVE"));
        bAdd = new JButton(model.getTemplateText("BANNER_AD_INIT"));
        bLoad = new JButton(model.getTemplateText("BANNER_AD_LOAD"));
        bImport = new JButton(model.getTemplateText("BANNER_AD_IMPORT"));
        bExport = new JButton(model.getTemplateText("BANNER_AD_EXPORT"));
        bClear = new JButton(model.getTemplateText("BANNER_AD_CLEAR"));

        JPanel pTop = new JPanel();
        add(new JScrollPane(pTop), BorderLayout.NORTH);
        JPanel pBottom = new JPanel();
        pBottom.setLayout(new BoxLayout(pBottom, BoxLayout.Y_AXIS));
        add(pBottom, BorderLayout.SOUTH);
        JPanel p = new JPanel();
        JPanel pMiddle = new JPanel();
        pMiddle.setLayout(new BoxLayout(pMiddle, BoxLayout.Y_AXIS));
        p.add(pMiddle);

        JPanel pBannerAd = new JPanel();
        pMiddle.add(pBannerAd);
        JPanel pPublisher = new JPanel();
        pMiddle.add(pPublisher);

        add(new JScrollPane(p), BorderLayout.CENTER);
        JPanel pRight = new JPanel();
        pRight.setLayout(new BoxLayout(pRight, BoxLayout.Y_AXIS));
        add(pRight, BorderLayout.EAST);

        JPanel pCommands = new JPanel();
        pCommands.setLayout(new FlowLayout(FlowLayout.CENTER));
        pBottom.add(pCommands, BorderLayout.SOUTH);

        lBannerAds.setVisibleRowCount(3);
        addItem(pTop, model.getTemplateText("BANNER_ADS"), new JScrollPane(lBannerAds));
        lBannerAds.addListSelectionListener(this);


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

        pBannerAd.setBorder(new TitledBorder(model.getTemplateText("ACTIVE_BANNER_AD")));
        pBannerAd.setLayout(new BoxLayout(pBannerAd, BoxLayout.Y_AXIS));
        addItem(pBannerAd, model.getTemplateText("ID"), lbId);
        addItem(pBannerAd, model.getTemplateText("PLACEMENT_ID"), tfPlacementId);

        JPanel pSize = new JPanel();
        pSize.setBorder(new TitledBorder(model.getTemplateText("SIZES")));
        addItem(pBannerAd, "", pSize);

        JPanel pG1 = new JPanel();
        addItem(pBannerAd, "", pG1);

        addItem(pSize, model.getTemplateText("W"), tfW);
        addItem(pSize, model.getTemplateText("H"), tfH);
        addItem(pSize, model.getTemplateText("W_Min"), tfWMin);
        addItem(pSize, model.getTemplateText("W_Max"), tfWMax);
        addItem(pSize, model.getTemplateText("H_Min"), tfHMin);
        addItem(pSize, model.getTemplateText("H_Max"), tfHMax);
        addItem(pG1, model.getTemplateText("AD_UNIT_CODE"), tfAdUnitCode);
        addItem(pSize, model.getTemplateText("SIZE"), tfSize);
        addItem(pSize, model.getTemplateText("PROMO_SIZES"), tfPromoSizes);
        addItem(pG1, model.getTemplateText("TOPFRAME"), tfTopframe);
        addItem(pG1, model.getTemplateText("BIDFLOOR_CURRENCY"), tfBidfloorCurrency);
        addItem(pG1, model.getTemplateText("BIDFLOOR_PRICE"), tfBidfloorPrice);

        JPanel pMimes = new JPanel();
        addItem(pBannerAd, "", pMimes);
        addItem(pMimes, model.getTemplateText("MIME"), new JScrollPane(lMimes));
        lMimes.setVisibleRowCount(3);
        addItem(pMimes, "", tfAddMimes);
        addItem(pMimes, "", bAddMime);
        bAddMime.addActionListener(this);
        bAddMime.setEnabled(false);
        addItem(pMimes, "", bRemoveMime);
        bRemoveMime.addActionListener(this);
        bRemoveMime.setEnabled(false);

        JPanel pAdTypes = new JPanel();
        addItem(pBannerAd, "", pAdTypes);
        addItem(pAdTypes, model.getTemplateText("BANNER_AD_TYPE"), new JScrollPane(lBtype));
        lBtype.setVisibleRowCount(3);
        addItem(pAdTypes, "", cbAddBtype);
        addItem(pAdTypes, "", bAddBtype);
        bAddBtype.addActionListener(this);
        bAddBtype.setEnabled(false);
        addItem(pAdTypes, "", bRemoveBtype);
        bRemoveBtype.addActionListener(this);
        bRemoveBtype.setEnabled(false);

        JPanel pAttrs = new JPanel();
        addItem(pBannerAd, "", pAttrs);
        addItem(pAttrs, model.getTemplateText("B_ATTRS"), new JScrollPane(lBattrs));
        lBattrs.setVisibleRowCount(3);
        addItem(pAttrs, "", cbAddBattrs);
        addItem(pAttrs, "", bAddBattrs);
        bAddBattrs.addActionListener(this);
        bAddBattrs.setEnabled(false);
        addItem(pAttrs, "", bRemoveBattrs);
        bRemoveBattrs.addActionListener(this);
        bRemoveBattrs.setEnabled(false);

        JPanel pExpDir = new JPanel();
        addItem(pBannerAd, "", pExpDir);
        addItem(pExpDir, model.getTemplateText("EXP_DIR"), new JScrollPane(lExpdir));
        lExpdir.setVisibleRowCount(3);
        addItem(pExpDir, "", cbAddExpdir);
        addItem(pExpDir, "", bAddExpdir);
        bAddExpdir.addActionListener(this);
        bAddExpdir.setEnabled(false);
        addItem(pExpDir, "", bRemoveExpdir);
        bRemoveExpdir.addActionListener(this);
        bRemoveExpdir.setEnabled(false);

        JPanel pApi = new JPanel();
        addItem(pBannerAd, "", pApi);
        addItem(pApi, model.getTemplateText("API"), new JScrollPane(lApi));
        lApi.setVisibleRowCount(3);
        addItem(pApi, "", cbAddApi);
        addItem(pApi, "", bAddApi);
        bAddApi.addActionListener(this);
        bAddApi.setEnabled(false);
        addItem(pApi, "", bRemoveApi);
        bRemoveApi.addActionListener(this);
        bRemoveApi.setEnabled(false);

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
            active = lBannerAds.getSelectedValue();
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
            bAddMime.setEnabled(true);
            bRemoveMime.setEnabled(true);
            bAddBtype.setEnabled(true);
            bRemoveBtype.setEnabled(true);
            bAddBattrs.setEnabled(true);
            bRemoveBattrs.setEnabled(true);
            bAddExpdir.setEnabled(true);
            bRemoveExpdir.setEnabled(true);
            bAddApi.setEnabled(true);
            bRemoveApi.setEnabled(true);

        } else {
            if (lBannerAds.getModel().getSize() == 0) {
                resetActiveDisplay(active);
                bUpdate.setEnabled(false);
                bRemove.setEnabled(false);
                bAddMime.setEnabled(false);
                bRemoveMime.setEnabled(false);
                bAddBtype.setEnabled(false);
                bRemoveBtype.setEnabled(false);
                bAddBattrs.setEnabled(false);
                bRemoveBattrs.setEnabled(false);
                bAddExpdir.setEnabled(false);
                bRemoveExpdir.setEnabled(false);
                bAddApi.setEnabled(false);
                bRemoveApi.setEnabled(false);

            } else {
                resetActiveDisplay(active);
                lBannerAds.setSelectedIndex(0);
                bUpdate.setEnabled(true);
                bRemove.setEnabled(true);
                bAddMime.setEnabled(true);
                bRemoveMime.setEnabled(true);
                bAddBtype.setEnabled(true);
                bRemoveBtype.setEnabled(true);
                bAddBattrs.setEnabled(true);
                bRemoveBattrs.setEnabled(true);
                bAddExpdir.setEnabled(true);
                bRemoveExpdir.setEnabled(true);
                bAddApi.setEnabled(true);
                bRemoveApi.setEnabled(true);
            }

        }
    }

    private void resetActiveDisplay(BannerAd sb) {
        if (sb == null) {
            tfPlacementId.setText("");
            tfW.setText("");
            tfH.setText("");
            tfWMin.setText("");
            tfWMax.setText("");
            tfHMin.setText("");
            tfHMax.setText("");
            tfAdUnitCode.setText("");
            tfSize.setText("");
            tfPromoSizes.setText("");
            tfTopframe.setText("");
            tfBidfloorCurrency.setText("");
            tfBidfloorPrice.setText("");

            mMimes.clear();
            mBtype.clear();
            mBattrs.clear();
            mExpdir.clear();
            mApi.clear();
            tfMemo.setText("");
        } else {
            tfPlacementId.setText(sb.getPlacementId());
            tfW.setText(transform(sb.getW()));
            tfH.setText(transform(sb.getH()));
            tfWMin.setText(transform(sb.getWmin()));
            tfWMax.setText(transform(sb.getWmax()));
            tfHMin.setText(transform(sb.getHmin()));
            tfHMax.setText(transform(sb.getHmax()));
            tfAdUnitCode.setText(sb.getAdUnitCode());
            tfSize.setText(sb.getSize());
            tfPromoSizes.setText(sb.getPromoSizes());
            tfTopframe.setText(transform(sb.getTopframe()));
            tfBidfloorCurrency.setText(sb.getBidfloorCurrency());
            tfBidfloorPrice.setText(transform(sb.getBidfloorPrice()));

            mMimes.clear();
            for (String s : sb.getMimes()) {
                mMimes.addElement(s);
            }
            mBtype.clear();
            for (BannerAdType s : sb.getBtypes()) {
                mBtype.addElement(s);
            }
            mBattrs.clear();
            for (CreativeAttribute s : sb.getBattrs()) {
                mBattrs.addElement(s);
            }
            mExpdir.clear();
            for (ExpandableDirectionType s : sb.getExpdir()) {
                mExpdir.addElement(s);
            }
            mApi.clear();
            for (ApiFramework s : sb.getApi()) {
                mApi.addElement(s);
            }
            tfMemo.setText("BannerAd selected.");
        }
    }

    private String transform(Integer i) {
        if (i == null) {
            return "";
        } else {
            return i.toString();
        }
    }

    private String transform(int i) {
        return Integer.toString(i);
    }

    private String transform(float f) {
        DecimalFormat formatter = new DecimalFormat("###,###,###.00");
        return formatter.format(f);
    }

    private void populate(BannerAd sb) throws ParseException {
        sb.setPlacementId(tfPlacementId.getText());
        sb.setW(transformInteger(tfW.getText()));
        sb.setH(transformInteger(tfH.getText()));
        sb.setWmin(transformInt(tfWMin.getText()));
        sb.setWmax(transformInt(tfWMax.getText()));
        sb.setHmin(transformInt(tfHMin.getText()));
        sb.setHmax(transformInt(tfHMax.getText()));
        sb.setAdUnitCode(tfAdUnitCode.getText());
        sb.setSize(tfSize.getText());
        sb.setPromoSizes(tfPromoSizes.getText());
        sb.setTopframe(transformInt(tfTopframe.getText()));
        sb.setBidfloorCurrency(tfBidfloorCurrency.getText());
        sb.setBidfloorPrice(transformFloat(tfBidfloorPrice.getText()));

        sb.setMimes(Collections.list(mMimes.elements()));
        sb.setBtypes(Collections.list(mBtype.elements()));
        sb.setBattrs(Collections.list(mBattrs.elements()));
        sb.setExpdir(Collections.list(mExpdir.elements()));
        sb.setApi(Collections.list(mApi.elements()));

    }

    private int transformInt(String s) {
        if (s == null || "".equals(s)) {
            return 0;
        } else {
            return Integer.parseInt(s);
        }
    }

    private Integer transformInteger(String s) {
        if (s == null || "".equals(s)) {
            return null;
        } else {
            return new Integer(s);
        }
    }

    private float transformFloat(String s) throws ParseException {
        DecimalFormat formatter = new DecimalFormat("###,###,###.00");
        return formatter.parse(s).floatValue();
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        if (ev.getSource() == bUpdate) {
            if (active != null) {
                try {
                    BannerAd sbN = new BannerAd();
                    sbN.setId(active.getId());
                    populate(sbN);
                    model.sendUpdateCommand(sbN);
                    model.setMessage("BannerAd saved.");
                    active = null;
                    setStateForActive();
                } catch (ModelException e) {
                    model.setMessageAsFault(e.getMessage());
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    e.printStackTrace();
                    model.setMessageAsFault("Could not save BannerAd due to invalid price.");
                }
            } else {
                model.setMessageAsWarning("No BannerAd selected.");
            }
            repaint();
        } else if (ev.getSource() == bAdd) {
            lBannerAds.setSelectedIndex(-1);
            active = new BannerAd();
            active.setId(UUID.randomUUID().toString());
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
                model.setMessageAsWarning("No BannerAd selected.");
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
        } else if (ev.getSource() == bAddMime) {
            String value = tfAddMimes.getText();
            if (value != null && !mMimes.contains(value)) {
                mMimes.addElement(value);
            }
            tfAddMimes.setText("");
            repaint();
        } else if (ev.getSource() == bRemoveMime) {
            String value = (String) lMimes.getSelectedValue();
            if (value == null) {
                value = tfAddMimes.getText();
            }
            if (value != null && mMimes.contains(value)) {
                mMimes.removeElement(value);
            }
            tfAddMimes.setText("");
            repaint();
        } else if (ev.getSource() == bAddBtype) {
            BannerAdType value = (BannerAdType) cbAddBtype.getSelectedItem();
            if (value != null && !mBtype.contains(value)) {
                mBtype.addElement(value);
            }
            cbAddBtype.setSelectedItem(null);
            repaint();
        } else if (ev.getSource() == bRemoveBtype) {
            BannerAdType value = lBtype.getSelectedValue();
            if (value == null) {
                value = (BannerAdType) cbAddBtype.getSelectedItem();
            }
            if (value != null && mBtype.contains(value)) {
                mBtype.removeElement(value);
            }
            cbAddBtype.setSelectedItem(null);
            repaint();
        } else if (ev.getSource() == bAddBattrs) {
            CreativeAttribute value = (CreativeAttribute) cbAddBattrs.getSelectedItem();
            if (value != null && !mBattrs.contains(value)) {
                mBattrs.addElement(value);
            }
            cbAddBattrs.setSelectedItem(null);
            repaint();
        } else if (ev.getSource() == bRemoveBattrs) {
            CreativeAttribute value = lBattrs.getSelectedValue();
            if (value == null) {
                value = (CreativeAttribute) cbAddBattrs.getSelectedItem();
            }
            if (value != null && mBattrs.contains(value)) {
                mBattrs.removeElement(value);
            }
            cbAddBattrs.setSelectedItem(null);
            repaint();
        } else if (ev.getSource() == bAddExpdir) {
            ExpandableDirectionType value = (ExpandableDirectionType) cbAddExpdir.getSelectedItem();
            if (value != null && !mExpdir.contains(value)) {
                mExpdir.addElement(value);
            }
            cbAddExpdir.setSelectedItem(null);
            repaint();
        } else if (ev.getSource() == bRemoveExpdir) {
            ExpandableDirectionType value = lExpdir.getSelectedValue();
            if (value == null) {
                value = (ExpandableDirectionType) cbAddExpdir.getSelectedItem();
            }
            if (value != null && mExpdir.contains(value)) {
                mExpdir.removeElement(value);
            }
            cbAddExpdir.setSelectedItem(null);
            repaint();
        } else if (ev.getSource() == bAddApi) {
            ApiFramework value = (ApiFramework) cbAddApi.getSelectedItem();
            if (value != null && !mApi.contains(value)) {
                mApi.addElement(value);
            }
            cbAddApi.setSelectedItem(null);
            repaint();
        } else if (ev.getSource() == bRemoveApi) {
            ApiFramework value = lApi.getSelectedValue();
            if (value == null) {
                value = (ApiFramework) cbAddApi.getSelectedItem();
            }
            if (value != null && mApi.contains(value)) {
                mApi.removeElement(value);
            }
            cbAddApi.setSelectedItem(null);
            repaint();
        }
    }

    @Override
    public void sendMessage(MessageStatus s, String m) {
        tfMemo.setText(m);
        tfMemo.setBackground(s.getColor());
    }

}
