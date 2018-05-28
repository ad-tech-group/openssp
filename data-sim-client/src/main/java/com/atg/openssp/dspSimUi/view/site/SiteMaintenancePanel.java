package com.atg.openssp.dspSimUi.view.site;

import com.atg.openssp.dspSimUi.model.MessageNotificationListener;
import com.atg.openssp.dspSimUi.model.MessageStatus;
import com.atg.openssp.dspSimUi.model.ModelException;
import com.atg.openssp.dspSimUi.model.client.ServerCommandType;
import com.atg.openssp.dspSimUi.model.dsp.ModeChangeListener;
import com.atg.openssp.dspSimUi.model.site.SiteModel;
import openrtb.bidrequest.model.Publisher;
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
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Collections;
import java.util.UUID;

/**
 * @author Brian Sorensen
 */
public class SiteMaintenancePanel extends JPanel implements ListSelectionListener, ActionListener, MessageNotificationListener {
    private static final Logger log = LoggerFactory.getLogger(SiteMaintenancePanel.class);
    private final SiteModel model;
    private final JList<Site> lSites;
    
    /*
	private String id;
	private String name;
	private String domain;
	private String page;
	// see product taxonomy -> "http://www.google.com/basepages/producttype/taxonomy.en-US.txt"
	private List<String> cat;
	private List<String> pagecat;
	private List<String> sectioncat;
    private String ref;
	private Publisher publisher;
	private Object ext;
	
	private String id;
	private String name;
	// see factual categories -> "http://developer.factual.com/working-with-categories/"
	private final List<String> cat;
	private String domain;
	private Object ext;
     */
    
    private final JLabel lbId = new JLabel("");
    private final JTextField tfName = new JTextField(12);
    private final JTextField tfDomain = new JTextField(50);
    private final JTextField tfPage = new JTextField(60);
    private final DefaultListModel<ContentCategory> mCat = new DefaultListModel<>();
    private final JList<ContentCategory> lCat = new JList<ContentCategory>(mCat);
    private final DefaultListModel<ContentCategory> mPageCat = new DefaultListModel<>();
    private final JList<ContentCategory> lPageCat = new JList<ContentCategory>(mPageCat);
    private final DefaultListModel<ContentCategory> mSectionCat = new DefaultListModel<>();
    private final JList<ContentCategory> lSectionCat = new JList<ContentCategory>(mSectionCat);
    private final JTextField tfRef = new JTextField(20);
    
    private final JTextField tfPubId = new JTextField(25);
    private final JTextField tfPubName = new JTextField(25);
    private final DefaultListModel<ContentCategory> mPubCat = new DefaultListModel<>();
    private final JList<ContentCategory> lPubCat = new JList<ContentCategory>(mPubCat);
    private final JTextField tfPubDomain = new JTextField(25);

    private final DefaultComboBoxModel<ContentCategory> mAddCat = new DefaultComboBoxModel<>();
    private final JComboBox<ContentCategory> cbAddCat = new JComboBox<ContentCategory>(mAddCat);

    private final JTextField tfMemo = new JTextField(20);

    private final JButton bAddCat;
    private final JButton bRemoveCat;
    private final JButton bAddPageCat;
    private final JButton bRemovePageCat;
    private final JButton bAddSectionCat;
    private final JButton bRemoveSectionCat;
    private final JButton bAddPubCat;
    private final JButton bRemovePubCat;
    private final JButton bUpdate;
    private final JButton bRemove;
    private final JButton bAdd;
    private final JButton bLoad;
    private final JButton bImport;
    private final JButton bExport;
    private final JButton bClear;
    private Site active;

    public SiteMaintenancePanel(SiteModel model) {
        this.model = model;
        setLayout(new BorderLayout());

        for (ContentCategory cc : ContentCategory.values()) {
            mAddCat.addElement(cc);
        }
        cbAddCat.setSelectedItem(null);
        lSites = new JList<>(model.getSiteModel());
        lSites.setSelectedIndex(-1);

        bAddCat = new JButton(model.getTemplateText("CAT_ADD"));
        bRemoveCat = new JButton(model.getTemplateText("CAT_REMOVE"));
        bAddPageCat = new JButton(model.getTemplateText("PAGE_CAT_ADD"));
        bRemovePageCat = new JButton(model.getTemplateText("PAGE_CAT_REMOVE"));
        bAddSectionCat = new JButton(model.getTemplateText("SECTION_CAT_ADD"));
        bRemoveSectionCat = new JButton(model.getTemplateText("SECTION_CAT_REMOVE"));
        bAddPubCat = new JButton(model.getTemplateText("PUB_CAT_ADD"));
        bRemovePubCat = new JButton(model.getTemplateText("PUB_CAT_REMOVE"));
        bUpdate = new JButton(model.getTemplateText("SITE_UPDATE"));
        bRemove = new JButton(model.getTemplateText("SITE_REMOVE"));
        bAdd = new JButton(model.getTemplateText("SITE_INIT"));
        bLoad = new JButton(model.getTemplateText("SITE_LOAD"));
        bImport = new JButton(model.getTemplateText("SITE_IMPORT"));
        bExport = new JButton(model.getTemplateText("SITE_EXPORT"));
        bClear = new JButton(model.getTemplateText("SITE_CLEAR"));

        JPanel pTop = new JPanel();
        add(pTop, BorderLayout.NORTH);
        JPanel pBottom = new JPanel();
        pBottom.setLayout(new BoxLayout(pBottom, BoxLayout.Y_AXIS));
        add(pBottom, BorderLayout.SOUTH);
        JPanel p = new JPanel();
        JPanel pMiddle = new JPanel();
        pMiddle.setLayout(new BoxLayout(pMiddle, BoxLayout.Y_AXIS));
        p.add(pMiddle);

        JPanel pSite = new JPanel();
        pMiddle.add(pSite);
        JPanel pPublisher = new JPanel();
        pMiddle.add(pPublisher);

        add(p, BorderLayout.CENTER);
        JPanel pRight = new JPanel();
        pRight.setLayout(new BoxLayout(pRight, BoxLayout.Y_AXIS));
        add(pRight, BorderLayout.EAST);

        JPanel pCommands = new JPanel();
        pCommands.setLayout(new FlowLayout(FlowLayout.CENTER));
        pBottom.add(pCommands, BorderLayout.SOUTH);

        lSites.setVisibleRowCount(10);
        addItem(pTop, model.getTemplateText("SITES"), lSites);
        lSites.addListSelectionListener(this);


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

        pSite.setBorder(new TitledBorder(model.getTemplateText("ACTIVE_SITE")));
        pSite.setLayout(new BoxLayout(pSite, BoxLayout.Y_AXIS));
        addItem(pSite, model.getTemplateText("ID"), lbId);
        addItem(pSite, model.getTemplateText("NAME"), tfName);
        addItem(pSite, model.getTemplateText("DOMAIN"), tfDomain);
        addItem(pSite, model.getTemplateText("PAGE"), tfPage);

        JPanel pCats = new JPanel();
        pCats.setLayout(new BoxLayout(pCats, BoxLayout.X_AXIS));
        addItem(pSite, "", pCats);

        addItem(pCats, model.getTemplateText("CAT"), new JScrollPane(lCat));
        lCat.setVisibleRowCount(3);
        addItem(pCats, model.getTemplateText("PAGECAT"), new JScrollPane(lPageCat));
        lPageCat.setVisibleRowCount(3);
        addItem(pCats, model.getTemplateText("SECTIONCAT"), new JScrollPane(lSectionCat));
        lSectionCat.setVisibleRowCount(3);
        addItem(pSite, model.getTemplateText("REF"),  tfRef);

        pPublisher.setBorder(new TitledBorder(model.getTemplateText("ACTIVE_PUBLISHER")));
        pPublisher.setLayout(new BoxLayout(pPublisher, BoxLayout.Y_AXIS));
        addItem(pPublisher, model.getTemplateText("PUBLISHERID"), tfPubId);
        addItem(pPublisher, model.getTemplateText("PUBLISHERNAME"), tfPubName);
        addItem(pPublisher, model.getTemplateText("PUBLISHERDOMAIN"), tfPubDomain);
        addItem(pPublisher, model.getTemplateText("PUBCAT"), new JScrollPane(lPubCat));
        lPubCat.setVisibleRowCount(3);


        p = new JPanel();
        p.setBorder(new TitledBorder(model.getTemplateText("MAINTAIN_CATEGORY")));
        addItem(pMiddle, model.getTemplateText("CATATORIES"), p);
        p.add(cbAddCat);

        JPanel p1  = new JPanel();
        p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));
        p1.setBorder(new TitledBorder(model.getTemplateText("CAT")));
        addItem(p, "", p1);
        JPanel p2  = new JPanel();
        p2.setBorder(new TitledBorder(model.getTemplateText("PAGECAT")));
        p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));
        addItem(p, "", p2);
        JPanel p3  = new JPanel();
        p3.setLayout(new BoxLayout(p3, BoxLayout.Y_AXIS));
        p3.setBorder(new TitledBorder(model.getTemplateText("SECTIONCAT")));
        addItem(p, "", p3);
        JPanel p4  = new JPanel();
        p4.setLayout(new BoxLayout(p4, BoxLayout.Y_AXIS));
        p4.setBorder(new TitledBorder(model.getTemplateText("PUBCAT")));
        addItem(p, "", p4);

        bAddCat.addActionListener(this);
        bAddCat.setEnabled(false);
        addItem(p1, "", bAddCat);
        bRemoveCat.addActionListener(this);
        bRemoveCat.setEnabled(false);
        addItem(p1, "", bRemoveCat);

        bAddPageCat.addActionListener(this);
        bAddPageCat.setEnabled(false);
        addItem(p2, "", bAddPageCat);
        bRemovePageCat.addActionListener(this);
        bRemovePageCat.setEnabled(false);
        addItem(p2, "", bRemovePageCat);

        bAddSectionCat.addActionListener(this);
        bAddSectionCat.setEnabled(false);
        addItem(p3, "", bAddSectionCat);
        bRemoveSectionCat.addActionListener(this);
        bRemoveSectionCat.setEnabled(false);
        addItem(p3, "", bRemoveSectionCat);

        bAddPubCat.addActionListener(this);
        bAddPubCat.setEnabled(false);
        addItem(p4, "", bAddPubCat);
        bRemovePubCat.addActionListener(this);
        bRemovePubCat.setEnabled(false);
        addItem(p4, "", bRemovePubCat);

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
            active = lSites.getSelectedValue();
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
            bAddPageCat.setEnabled(true);
            bRemovePageCat.setEnabled(true);
            bAddSectionCat.setEnabled(true);
            bRemoveSectionCat.setEnabled(true);
            bAddPubCat.setEnabled(true);
            bRemovePubCat.setEnabled(true);
        } else {
            if (lSites.getModel().getSize() == 0) {
                resetActiveDisplay(active);
                bUpdate.setEnabled(false);
                bRemove.setEnabled(false);
                bAddCat.setEnabled(false);
                bRemoveCat.setEnabled(false);
                bAddPageCat.setEnabled(false);
                bRemovePageCat.setEnabled(false);
                bAddSectionCat.setEnabled(false);
                bRemoveSectionCat.setEnabled(false);
                bAddPubCat.setEnabled(false);
                bRemovePubCat.setEnabled(false);
            } else {
                resetActiveDisplay(active);
                lSites.setSelectedIndex(0);
                bUpdate.setEnabled(true);
                bRemove.setEnabled(true);
                bAddCat.setEnabled(true);
                bRemoveCat.setEnabled(true);
                bAddPageCat.setEnabled(true);
                bRemovePageCat.setEnabled(true);
                bAddSectionCat.setEnabled(true);
                bRemoveSectionCat.setEnabled(true);
                bAddPubCat.setEnabled(true);
                bRemovePubCat.setEnabled(true);
            }

        }
    }

    private void resetActiveDisplay(Site sb) {
        if (sb == null) {
            tfName.setText("");
            tfDomain.setText("");
            tfPage.setText("");
            tfRef.setText("");
            tfPubId.setText("");
            tfPubDomain.setText("");
            mCat.clear();
            mPageCat.clear();
            mSectionCat.clear();
            mPubCat.clear();
            cbAddCat.setSelectedItem(null);
            tfMemo.setText("");
        } else {
            tfName.setText(sb.getName());
            tfDomain.setText(sb.getDomain());
            tfPage.setText(sb.getPage());
            tfRef.setText(sb.getRef());
            mCat.clear();
            for (ContentCategory s : sb.getCat()) {
                mCat.addElement(s);
            }
            mPageCat.clear();
            for (ContentCategory s : sb.getPagecat()) {
                mPageCat.addElement(s);
            }
            mSectionCat.clear();
            for (ContentCategory s : sb.getSectioncat()) {
                mSectionCat.addElement(s);
            }
            mPubCat.clear();
            if (sb.getPublisher() == null) {
                tfPubId.setText("");
                tfPubName.setText("");
                tfPubDomain.setText("");
            } else {
                tfPubId.setText(sb.getPublisher().getId());
                tfPubName.setText(sb.getPublisher().getName());
                tfPubDomain.setText(sb.getPublisher().getDomain());
                for (ContentCategory s : sb.getPublisher().getCat()) {
                    mPubCat.addElement(s);
                }
            }
            tfMemo.setText("Site selected.");
        }
    }

    private void populate(Site sb) throws ParseException {
        sb.setName(tfName.getText());
        sb.setDomain(tfDomain.getText());
        sb.setPage(tfPage.getText());
        sb.setRef(tfRef.getText());
        sb.getPublisher().setId(tfPubId.getText());
        sb.getPublisher().setName(tfPubName.getText());
        sb.getPublisher().setDomain(tfPubDomain.getText());
        sb.setCat(Collections.list(mCat.elements()));
        sb.setPagecat(Collections.list(mCat.elements()));
        sb.setSectioncat(Collections.list(mCat.elements()));
        sb.getPublisher().setCat(Collections.list(mPubCat.elements()));
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        if (ev.getSource() == bUpdate) {
            if (active != null) {
                try {
                    Site sbN = new Site();
                    sbN.setId(active.getId());
                    populate(sbN);
                    model.sendUpdateCommand(sbN);
                    model.setMessage("Site saved.");
                    active = null;
                    setStateForActive();
                } catch (ModelException e) {
                    model.setMessageAsFault(e.getMessage());
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    e.printStackTrace();
                    model.setMessageAsFault("Could not save Site due to invalid price.");
                }
            } else {
                model.setMessageAsWarning("No Site selected.");
            }
            repaint();
        } else if (ev.getSource() == bAdd) {
            lSites.setSelectedIndex(-1);
            active = new Site();
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
                model.setMessageAsWarning("No Site selected.");
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
        } else if (ev.getSource() == bAddPageCat) {
            ContentCategory value = (ContentCategory) cbAddCat.getSelectedItem();
            if (value != null && !mCat.contains(value)) {
                mPageCat.addElement(value);
            }
            cbAddCat.setSelectedItem(null);
            repaint();
        } else if (ev.getSource() == bRemovePageCat) {
            ContentCategory value = lPageCat.getSelectedValue();
            if (value != null) {
                mPageCat.removeElement(value);
                lPageCat.setSelectedIndex(-1);
            } else {
                value = (ContentCategory) cbAddCat.getSelectedItem();
                if (value != null) {
                    mPageCat.removeElement(value);
                    cbAddCat.setSelectedIndex(-1);
                } else {
                    model.setMessage("Need to select category to remove.");
                }
            }
            repaint();
        } else if (ev.getSource() == bAddSectionCat) {
            ContentCategory value = (ContentCategory) cbAddCat.getSelectedItem();
            if (value != null && !mSectionCat.contains(value)) {
                mSectionCat.addElement(value);
            }
            cbAddCat.setSelectedItem(null);
            repaint();
        } else if (ev.getSource() == bRemoveSectionCat) {
            ContentCategory value = lSectionCat.getSelectedValue();
            if (value != null) {
                mSectionCat.removeElement(value);
                lSectionCat.setSelectedIndex(-1);
            } else {
                value = (ContentCategory) cbAddCat.getSelectedItem();
                if (value != null) {
                    mSectionCat.removeElement(value);
                    cbAddCat.setSelectedIndex(-1);
                } else {
                    model.setMessage("Need to select category to remove.");
                }
            }
            repaint();
        } else if (ev.getSource() == bAddPubCat) {
            ContentCategory value = (ContentCategory) cbAddCat.getSelectedItem();
            if (value != null && !mPubCat.contains(value)) {
                mPubCat.addElement(value);
            }
            cbAddCat.setSelectedItem(null);
            repaint();
        } else if (ev.getSource() == bRemovePubCat) {
            ContentCategory value = lPubCat.getSelectedValue();
            if (value != null) {
                mPubCat.removeElement(value);
                lPubCat.setSelectedIndex(-1);
            } else {
                value = (ContentCategory) cbAddCat.getSelectedItem();
                if (value != null) {
                    mPubCat.removeElement(value);
                    cbAddCat.setSelectedIndex(-1);
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
