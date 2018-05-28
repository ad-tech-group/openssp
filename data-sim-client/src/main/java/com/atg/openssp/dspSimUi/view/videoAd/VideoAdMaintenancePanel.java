package com.atg.openssp.dspSimUi.view.videoAd;

import com.atg.openssp.common.cache.dto.BannerAd;
import com.atg.openssp.common.cache.dto.VideoAd;
import com.atg.openssp.dspSimUi.model.MessageNotificationListener;
import com.atg.openssp.dspSimUi.model.MessageStatus;
import com.atg.openssp.dspSimUi.model.ModelException;
import com.atg.openssp.dspSimUi.model.ad.banner.BannerListener;
import com.atg.openssp.dspSimUi.model.ad.video.VideoAdModel;
import openrtb.bidrequest.model.Banner;
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
import java.util.List;
import java.util.UUID;

/**
 * @author Brian Sorensen
 */
public class VideoAdMaintenancePanel extends JPanel implements ListSelectionListener, ActionListener, MessageNotificationListener, BannerListener {
    private static final Logger log = LoggerFactory.getLogger(VideoAdMaintenancePanel.class);
    private final VideoAdModel model;
    private final JList<VideoAd> lVideoAds;

    private final JLabel lbId = new JLabel("");
    private final JTextField tfVidId = new JTextField(12);
    private final JTextField tfW = new JTextField(5);
    private final JTextField tfH = new JTextField(5);
    private final JTextField tfMinDuration = new JTextField(5);
    private final JTextField tfMaxDuration = new JTextField(5);
    private final JTextField tfStartDelay = new JTextField(5);
    private final DefaultComboBoxModel<VideoLinearity> mLinearity = new DefaultComboBoxModel<>();
    private final JComboBox<VideoLinearity> cbLinearity = new JComboBox<>(mLinearity);

    private final JTextField tfBidfloorCurrency = new JTextField(5);
    private final JTextField tfBidfloorPrice = new JTextField(5);

    private final DefaultListModel<String> mMimes = new DefaultListModel<>();
    private final JList<String> lMimes = new JList<>(mMimes);
    private final JTextField tfAddMimes = new JTextField(10);

    private final DefaultListModel<VideoBidResponseProtocol> mProtocols = new DefaultListModel<>();
    private final JList<VideoBidResponseProtocol> lProtocols = new JList<>(mProtocols);
    private final DefaultComboBoxModel<VideoBidResponseProtocol> mAddProtocols = new DefaultComboBoxModel<>();
    private final JComboBox<VideoBidResponseProtocol> cbAddProtocols = new JComboBox<>(mAddProtocols);

    private final DefaultListModel<CreativeAttribute> mBattrs = new DefaultListModel<>();
    private final JList<CreativeAttribute> lBattrs = new JList<>(mBattrs);
    private final DefaultComboBoxModel<CreativeAttribute> mAddBattrs = new DefaultComboBoxModel<>();
    private final JComboBox<CreativeAttribute> cbAddBattrs = new JComboBox<>(mAddBattrs);

    private final DefaultListModel<Banner> mCompanionAd = new DefaultListModel<>();
    private final JList<Banner> lCompanionAd = new JList<>(mCompanionAd);
    private final DefaultComboBoxModel<Banner> mAddCompanionAd = new DefaultComboBoxModel<>();
    private final JComboBox<Banner> cbAddCompanionAd = new JComboBox<>(mAddCompanionAd);

    private final DefaultListModel<ApiFramework> mApi = new DefaultListModel<>();
    private final JList<ApiFramework> lApi = new JList<>(mApi);
    private final DefaultComboBoxModel<ApiFramework> mAddApi = new DefaultComboBoxModel<>();
    private final JComboBox<ApiFramework> cbAddApi = new JComboBox<>(mAddApi);

//    private Object ext;

    private final JTextField tfMemo = new JTextField(20);

    private final JButton bAddMime;
    private final JButton bRemoveMime;
    private final JButton bAddProtocol;
    private final JButton bRemoveProtocol;
    private final JButton bAddBattrs;
    private final JButton bRemoveBattrs;
    private final JButton bAddCompanionAd;
    private final JButton bRemoveCompanionAd;
    private final JButton bAddApi;
    private final JButton bRemoveApi;
    private final JButton bUpdate;
    private final JButton bRemove;
    private final JButton bAdd;
    private final JButton bLoad;
    private final JButton bImport;
    private final JButton bExport;
    private final JButton bClear;
    private VideoAd active;

    public VideoAdMaintenancePanel(VideoAdModel model) {
        this.model = model;
        setLayout(new BorderLayout());

        for (VideoBidResponseProtocol cc : VideoBidResponseProtocol.values()) {
            mAddProtocols.addElement(cc);
        }
        cbAddProtocols.setSelectedItem(null);
        for (CreativeAttribute cc : CreativeAttribute.values()) {
            mAddBattrs.addElement(cc);
        }
        cbAddBattrs.setSelectedItem(null);
        cbAddCompanionAd.setSelectedItem(null);
        for (ApiFramework cc : ApiFramework.values()) {
            mAddApi.addElement(cc);
        }
        cbAddApi.setSelectedItem(null);
        mLinearity.addElement(null);
        for (VideoLinearity cc : VideoLinearity.values()) {
            mLinearity.addElement(cc);
        }
        model.addBannerListener(this);

        lVideoAds = new JList<>(model.getVideoAdModel());
        lVideoAds.setSelectedIndex(-1);

        bAddMime = new JButton(model.getTemplateText("MIME_ADD"));
        bRemoveMime = new JButton(model.getTemplateText("MIME_REMOVE"));
        bAddProtocol = new JButton(model.getTemplateText("PROTOCOL_ADD"));
        bRemoveProtocol = new JButton(model.getTemplateText("PROTOCOL_REMOVE"));
        bAddBattrs = new JButton(model.getTemplateText("B_ATTRS_ADD"));
        bRemoveBattrs = new JButton(model.getTemplateText("B_ATTRS_REMOVE"));
        bAddCompanionAd = new JButton(model.getTemplateText("COMPANION_AD_ADD"));
        bRemoveCompanionAd = new JButton(model.getTemplateText("COMPANION_AD_REMOVE"));
        bAddApi = new JButton(model.getTemplateText("API_ADD"));
        bRemoveApi = new JButton(model.getTemplateText("API_REMOVE"));
        bUpdate = new JButton(model.getTemplateText("VIDEO_AD_UPDATE"));
        bRemove = new JButton(model.getTemplateText("VIDEO_AD_REMOVE"));
        bAdd = new JButton(model.getTemplateText("VIDEO_AD_INIT"));
        bLoad = new JButton(model.getTemplateText("VIDEO_AD_LOAD"));
        bImport = new JButton(model.getTemplateText("VIDEO_AD_IMPORT"));
        bExport = new JButton(model.getTemplateText("VIDEO_AD_EXPORT"));
        bClear = new JButton(model.getTemplateText("VIDEO_AD_CLEAR"));

        JPanel pTop = new JPanel();
        add(new JScrollPane(pTop), BorderLayout.NORTH);
        JPanel pBottom = new JPanel();
        pBottom.setLayout(new BoxLayout(pBottom, BoxLayout.Y_AXIS));
        add(pBottom, BorderLayout.SOUTH);
        JPanel p = new JPanel();
        JPanel pMiddle = new JPanel();
        pMiddle.setLayout(new BoxLayout(pMiddle, BoxLayout.Y_AXIS));
        p.add(pMiddle);

        JPanel pVideoAd = new JPanel();
        pMiddle.add(pVideoAd);
        JPanel pPublisher = new JPanel();
        pMiddle.add(pPublisher);

        add(new JScrollPane(p), BorderLayout.CENTER);
        JPanel pRight = new JPanel();
        pRight.setLayout(new BoxLayout(pRight, BoxLayout.Y_AXIS));
        add(pRight, BorderLayout.EAST);

        JPanel pCommands = new JPanel();
        pCommands.setLayout(new FlowLayout(FlowLayout.CENTER));
        pBottom.add(pCommands, BorderLayout.SOUTH);

        lVideoAds.setVisibleRowCount(3);
        addItem(pTop, model.getTemplateText("VIDEO_ADS"), new JScrollPane(lVideoAds));
        lVideoAds.addListSelectionListener(this);


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

        pVideoAd.setBorder(new TitledBorder(model.getTemplateText("ACTIVE_VIDEO_AD")));
        pVideoAd.setLayout(new BoxLayout(pVideoAd, BoxLayout.Y_AXIS));
        addItem(pVideoAd, model.getTemplateText("ID"), lbId);
        addItem(pVideoAd, model.getTemplateText("VIDEO_ID"), tfVidId);

        JPanel pSize = new JPanel();
        pSize.setBorder(new TitledBorder(model.getTemplateText("SIZES")));
        addItem(pVideoAd, "", pSize);

        JPanel pG1 = new JPanel();
        addItem(pVideoAd, "", pG1);

        addItem(pSize, model.getTemplateText("W"), tfW);
        addItem(pSize, model.getTemplateText("H"), tfH);
        addItem(pSize, model.getTemplateText("MIN_DURATION"), tfMinDuration);
        addItem(pSize, model.getTemplateText("MAX_DURATION"), tfMaxDuration);
        addItem(pSize, model.getTemplateText("START_DELAY"), tfStartDelay);
        addItem(pG1, model.getTemplateText("LINEARITY"), cbLinearity);
        addItem(pG1, model.getTemplateText("BIDFLOOR_CURRENCY"), tfBidfloorCurrency);
        addItem(pG1, model.getTemplateText("BIDFLOOR_PRICE"), tfBidfloorPrice);

        JPanel pMimes = new JPanel();
        addItem(pVideoAd, "", pMimes);
        addItem(pMimes, model.getTemplateText("MIME"), new JScrollPane(lMimes));
        lMimes.setVisibleRowCount(3);
        addItem(pMimes, "", tfAddMimes);
        addItem(pMimes, "", bAddMime);
        bAddMime.addActionListener(this);
        bAddMime.setEnabled(false);
        addItem(pMimes, "", bRemoveMime);
        bRemoveMime.addActionListener(this);
        bRemoveMime.setEnabled(false);

        JPanel pProtocols = new JPanel();
        addItem(pVideoAd, "", pProtocols);
        addItem(pProtocols, model.getTemplateText("PROTOCOLS"), new JScrollPane(lProtocols));
        lProtocols.setVisibleRowCount(3);
        addItem(pProtocols, "", cbAddProtocols);
        addItem(pProtocols, "", bAddProtocol);
        bAddProtocol.addActionListener(this);
        bAddProtocol.setEnabled(false);
        addItem(pProtocols, "", bRemoveProtocol);
        bRemoveProtocol.addActionListener(this);
        bRemoveProtocol.setEnabled(false);

        JPanel pAttrs = new JPanel();
        addItem(pVideoAd, "", pAttrs);
        addItem(pAttrs, model.getTemplateText("B_ATTRS"), new JScrollPane(lBattrs));
        lBattrs.setVisibleRowCount(3);
        addItem(pAttrs, "", cbAddBattrs);
        addItem(pAttrs, "", bAddBattrs);
        bAddBattrs.addActionListener(this);
        bAddBattrs.setEnabled(false);
        addItem(pAttrs, "", bRemoveBattrs);
        bRemoveBattrs.addActionListener(this);
        bRemoveBattrs.setEnabled(false);

        /*
        JPanel pBanners = new JPanel();
        addItem(pVideoAd, "", pBanners);
        addItem(pBanners, model.getTemplateText("COMPANION_AD"), new JScrollPane(lCompanionAd));
        lCompanionAd.setVisibleRowCount(3);
        addItem(pBanners, "", cbAddCompanionAd);
        addItem(pBanners, "", bAddCompanionAd);
        bAddCompanionAd.addActionListener(this);
        bAddCompanionAd.setEnabled(false);
        addItem(pBanners, "", bRemoveCompanionAd);
        bRemoveCompanionAd.addActionListener(this);
        bRemoveCompanionAd.setEnabled(false);
        */

        JPanel pApi = new JPanel();
        addItem(pVideoAd, "", pApi);
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
            active = lVideoAds.getSelectedValue();
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
            bAddProtocol.setEnabled(true);
            bRemoveProtocol.setEnabled(true);
            bAddBattrs.setEnabled(true);
            bRemoveBattrs.setEnabled(true);
            bAddCompanionAd.setEnabled(true);
            bRemoveCompanionAd.setEnabled(true);
            bAddApi.setEnabled(true);
            bRemoveApi.setEnabled(true);

        } else {
            if (lVideoAds.getModel().getSize() == 0) {
                resetActiveDisplay(active);
                bUpdate.setEnabled(false);
                bRemove.setEnabled(false);
                bAddMime.setEnabled(false);
                bRemoveMime.setEnabled(false);
                bAddProtocol.setEnabled(false);
                bRemoveProtocol.setEnabled(false);
                bAddBattrs.setEnabled(false);
                bRemoveBattrs.setEnabled(false);
                bAddCompanionAd.setEnabled(false);
                bRemoveCompanionAd.setEnabled(false);
                bAddApi.setEnabled(false);
                bRemoveApi.setEnabled(false);

            } else {
                resetActiveDisplay(active);
                lVideoAds.setSelectedIndex(0);
                bUpdate.setEnabled(true);
                bRemove.setEnabled(true);
                bAddMime.setEnabled(true);
                bRemoveMime.setEnabled(true);
                bAddProtocol.setEnabled(true);
                bRemoveProtocol.setEnabled(true);
                bAddBattrs.setEnabled(true);
                bRemoveBattrs.setEnabled(true);
                bAddCompanionAd.setEnabled(true);
                bRemoveCompanionAd.setEnabled(true);
                bAddApi.setEnabled(true);
                bRemoveApi.setEnabled(true);
            }

        }
    }

    private void resetActiveDisplay(VideoAd sb) {
        if (sb == null) {
            tfVidId.setText("");
            tfW.setText("");
            tfH.setText("");
            tfMinDuration.setText("");
            tfMaxDuration.setText("");
            tfStartDelay.setText("");
            cbLinearity.setSelectedItem(null);
            tfBidfloorCurrency.setText("");
            tfBidfloorPrice.setText("");

            mMimes.clear();
            mProtocols.clear();
            mBattrs.clear();
            mCompanionAd.clear();
            mApi.clear();
            tfMemo.setText("");
        } else {
            tfVidId.setText(sb.getVidId());
            tfW.setText(transform(sb.getW()));
            tfH.setText(transform(sb.getH()));
            tfMinDuration.setText(transform(sb.getMinDuration()));
            tfMaxDuration.setText(transform(sb.getMaxDuration()));
            tfStartDelay.setText(transform(sb.getStartDelay()));
            cbLinearity.setSelectedItem(sb.getLinearity());
            tfBidfloorCurrency.setText(sb.getBidfloorCurrency());
            tfBidfloorPrice.setText(transform(sb.getBidfloorPrice()));

            mMimes.clear();
            for (String s : sb.getMimes()) {
                mMimes.addElement(s);
            }
            mProtocols.clear();
            for (VideoBidResponseProtocol s : sb.getProtocols()) {
                mProtocols.addElement(s);
            }
            mBattrs.clear();
            for (CreativeAttribute s : sb.getBattr()) {
                mBattrs.addElement(s);
            }
            mCompanionAd.clear();
            List<Banner> banners = sb.getCompanionad();
            if (banners != null) {
                for (Banner s : banners) {
                    mCompanionAd.addElement(s);
                }
            }
            mApi.clear();
            for (ApiFramework s : sb.getApi()) {
                mApi.addElement(s);
            }
            tfMemo.setText("VideoAd selected.");
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

    private void populate(VideoAd sb) throws ParseException {
        sb.setVidId(tfVidId.getText());
        sb.setW(transformInteger(tfW.getText()));
        sb.setH(transformInteger(tfH.getText()));
        sb.setMinDuration(transformInt(tfMinDuration.getText()));
        sb.setMaxDuration(transformInt(tfMaxDuration.getText()));
        sb.setStartDelay(transformInt(tfStartDelay.getText()));
        sb.setLinearity((VideoLinearity) cbLinearity.getSelectedItem());
        sb.setBidfloorCurrency(tfBidfloorCurrency.getText());
        sb.setBidfloorPrice(transformFloat(tfBidfloorPrice.getText()));

        sb.setMimes(Collections.list(mMimes.elements()));
        sb.setProtocols(Collections.list(mProtocols.elements()));
        sb.setBattr(Collections.list(mBattrs.elements()));
        sb.setCompanionad(Collections.list(mCompanionAd.elements()));
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
                    VideoAd sbN = new VideoAd();
                    sbN.setId(active.getId());
                    populate(sbN);
                    model.sendUpdateCommand(sbN);
                    model.setMessage("VideoAd saved.");
                    active = null;
                    setStateForActive();
                } catch (ModelException e) {
                    model.setMessageAsFault(e.getMessage());
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    e.printStackTrace();
                    model.setMessageAsFault("Could not save VideoAd due to invalid entry.");
                }
            } else {
                model.setMessageAsWarning("No VideoAd selected.");
            }
            repaint();
        } else if (ev.getSource() == bAdd) {
            lVideoAds.setSelectedIndex(-1);
            active = new VideoAd();
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
                model.setMessageAsWarning("No VideoAd selected.");
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
        } else if (ev.getSource() == bAddProtocol) {
            VideoBidResponseProtocol value = (VideoBidResponseProtocol) cbAddProtocols.getSelectedItem();
            if (value != null && !mProtocols.contains(value)) {
                mProtocols.addElement(value);
            }
            cbAddProtocols.setSelectedItem(null);
            repaint();
        } else if (ev.getSource() == bRemoveProtocol) {
            VideoBidResponseProtocol value = lProtocols.getSelectedValue();
            if (value == null) {
                value = (VideoBidResponseProtocol) cbAddProtocols.getSelectedItem();
            }
            if (value != null && mProtocols.contains(value)) {
                mProtocols.removeElement(value);
            }
            cbAddProtocols.setSelectedItem(null);
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
        } else if (ev.getSource() == bAddCompanionAd) {
            Banner value = (Banner) cbAddCompanionAd.getSelectedItem();
            if (value != null && !mCompanionAd.contains(value)) {
                mCompanionAd.addElement(value);
            }
            cbAddCompanionAd.setSelectedItem(null);
            repaint();
        } else if (ev.getSource() == bRemoveCompanionAd) {
            Banner value = lCompanionAd.getSelectedValue();
            if (value == null) {
                value = (Banner) cbAddCompanionAd.getSelectedItem();
            }
            if (value != null && mCompanionAd.contains(value)) {
                mCompanionAd.removeElement(value);
            }
            cbAddCompanionAd.setSelectedItem(null);
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

    @Override
    public void added(BannerAd value) {
//        mAddCompanionAd.addElement(value);
    }

    @Override
    public void removed(BannerAd value) {
        mAddCompanionAd.removeElement(value);
    }
}
