package com.swarauto.ui.profile;

import com.swarauto.dependencies.DependenciesRegistry;
import com.swarauto.game.indicator.Indicator;
import com.swarauto.game.profile.Profile;
import com.swarauto.game.profile.ProfileChecker;
import com.swarauto.ui.profile.battle.BattleConfigView;
import com.swarauto.ui.profile.networkproblem.NetworkProblemConfigView;
import com.swarauto.ui.profile.refill.RefillView;
import com.swarauto.ui.profile.rift.RiftConfigView;
import com.swarauto.ui.profile.rune.RuneView;
import com.swarauto.ui.profile.toa.ToaConfigView;
import com.swarauto.ui.profile.widget.AbstractPicker;
import com.swarauto.util.PcUtil;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ProfileEditorView extends JDialog implements ProfileEditorPresenter.View,
        ActionListener,
        ItemListener,
        RefillView.Listener,
        RuneView.Listener,
        BattleConfigView.Listener,
        NetworkProblemConfigView.Listener,
        RiftConfigView.Listener,
        ToaConfigView.Listener {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(ProfileEditorView.class);

    @Getter
    private ProfileEditorPresenter presenter;

    private JLabel lblProfileName = new JLabel("Device name");
    private JTextField textProfileName = new JTextField();

    private final JPanel panelInfo = new JPanel();
    private final JLabel lblSetupInfo = new JLabel("");
    private final JButton btnAutoConfig = new JButton("Run Device Auto Config");

    private JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
    private BattleConfigView battleConfigView = new BattleConfigView(this);
    private JScrollPane battleConfigTab = new JScrollPane(battleConfigView, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private NetworkProblemConfigView networkProblemConfigView = new NetworkProblemConfigView(this);
    private JScrollPane networkProblemConfigTab = new JScrollPane(networkProblemConfigView, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private RiftConfigView riftConfigView = new RiftConfigView(this);
    private JScrollPane riftConfigTab = new JScrollPane(riftConfigView, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private ToaConfigView toaConfigView = new ToaConfigView(this);
    private JScrollPane toaConfigTab = new JScrollPane(toaConfigView, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private RuneView runeView = new RuneView(this);
    private JScrollPane runeTab = new JScrollPane(runeView, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private RefillView refillView = new RefillView(this);
    private JScrollPane refillTab = new JScrollPane(refillView, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    private JButton btnSave = new JButton("Save");
    private JButton btnCancel = new JButton("Cancel");

    public ProfileEditorView() {
        setModal(true);
        setTitle("Device config");

        // Name
        getContentPane().setLayout(new MigLayout("", "[grow]", "[][][grow][]"));
        getContentPane().add(lblProfileName, "cell 0 0,alignx trailing");
        getContentPane().add(textProfileName, "cell 0 0,growx");
        textProfileName.setColumns(10);

        // Info panel
        getContentPane().add(panelInfo, "cell 0 1,grow");
        panelInfo.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Info", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(51, 51, 51)));
        panelInfo.setLayout(new MigLayout("", "[grow]", "[][]"));
        panelInfo.add(lblSetupInfo, "cell 0 0");
        panelInfo.add(btnAutoConfig, "cell 0 1,growx");
        btnAutoConfig.addActionListener(this);

        // Tabs
        getContentPane().add(tabbedPane, "cell 0 2,grow");
        tabbedPane.addTab("Battle", null, battleConfigTab, null);
        tabbedPane.addTab("Rift", null, riftConfigTab, null);
        tabbedPane.addTab("TOA", null, toaConfigTab, null);
        tabbedPane.addTab("Network problem", null, networkProblemConfigTab, null);
        tabbedPane.addTab("Auto Refill", null, refillTab, null);
        tabbedPane.addTab("Runes picking", null, runeTab, null);

        // Buttons
        getContentPane().add(btnSave, "flowx,cell 0 3,growx");
        btnSave.addActionListener(this);
        getContentPane().add(btnCancel, "cell 0 3,growx");
        btnCancel.addActionListener(this);

        presenter = new ProfileEditorPresenter();
        presenter.bindView(this);

        pack();
        setLocationRelativeTo(null);
    }

    @Override
    public void renderAll(Profile profile) {
        textProfileName.setText(profile.getName());
        battleConfigView.populateFromModel(profile);
        networkProblemConfigView.populateFromModel(profile);
        riftConfigView.populateFromModel(profile);
        toaConfigView.populateFromModel(profile);
        refillView.populateFromModel(profile);
        runeView.populateFromModel(profile);
    }

    @Override
    public void renderProfileCheckerInfo(int flags) {
        String info = "<html>";
        info += "Auto Battle (Cairos dungeon, scenario...): " + getColoredString(ProfileChecker.FLAG_RUNE_FARMING, flags);
        info += "<br/>Auto TOA: " + getColoredString(ProfileChecker.FLAG_TOA, flags);
        info += "<br/>Auto Rift: " + getColoredString(ProfileChecker.FLAG_RIFT, flags);
        info += "<br/>Auto retry on network problem: " + getColoredString(ProfileChecker.FLAG_NETWORK_PROBLEM, flags);
        info += "<br/>Auto refill energy by crystal: " + getColoredString(ProfileChecker.FLAG_AUTO_REFILL, flags);
//        info += "<br/>Rune selective picking (auto sell/pick runes dropped): " + getColoredString(ProfileChecker.FLAG_RUNE_PICKING, flags);
        info += "</html>";

        lblSetupInfo.setText(info);
    }

    private String getColoredString(int flag, int flags) {
        String text = "<font color='";

        if (ProfileChecker.flagOn(flag, flags)) {
            text += "#" + Integer.toHexString(AbstractPicker.COLOR_OK.getRGB()).substring(2);
            text += "'>OK";
        } else {
            text += "#" + Integer.toHexString(AbstractPicker.COLOR_MISSING.getRGB()).substring(2);
            text += "'>MISSING CONFIG";
        }

        text += "</font>";
        return text;
    }

    @Override
    public String getInputProfileName() {
        return textProfileName.getText();
    }

    @Override
    public void create() {

    }

    @Override
    public void start() {
        SwingUtilities.invokeLater(() -> {
            setVisible(true);
        });
        presenter.onViewStarted();
    }

    @Override
    public void stop() {
        setVisible(false);
    }

    @Override
    public void destroy() {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSave) {
            presenter.saveProfile();
        } else if (e.getSource() == btnCancel) {
            presenter.cancelEditProfile();
        } else if (e.getSource() == btnAutoConfig) {
            int dialogResult = JOptionPane.showConfirmDialog(this,
                    "<html>SWarAuto can analyzer your device and setup some basic configs (like click location)." +
                            "<br>Currently working best with 16:9 ratio device screen." +
                            "<br>If your device is difference, please config manually." +
                            "<br>" +
                            "<br>Connect your device via USB port and click YES to begin.</html>",
                    "Auto Config device",
                    JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runAutoConfig();
                    }
                }).start();
            }
        }
    }

    private void runAutoConfig() {
        btnSave.setEnabled(false);
        btnCancel.setEnabled(false);
        btnAutoConfig.setEnabled(false);
        tabbedPane.setVisible(false);

        // Begin
        btnAutoConfig.setText("Checking device...");
        if (PcUtil.isDeviceConnected()) {
            String screenShotFilePath = DependenciesRegistry.commandUtil.capturePhoneScreen();
            if (screenShotFilePath != null) {
                try {
                    BufferedImage screenImage = ImageIO.read(new File(screenShotFilePath));
                    int width = Math.max(screenImage.getWidth(), screenImage.getHeight());
                    int height = Math.min(screenImage.getWidth(), screenImage.getHeight());
                    presenter.autoSetupCommandEngine(width, height);
                    JOptionPane.showMessageDialog(this,
                            "You're almost done. Please finish others manually.",
                            "Complete",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException e) {
                    LOG.error("runAutoConfig", e);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "SWartAuto can't see your device screen. Please contact developer.",
                        "Can't see device screen",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Please connect your device via USB cable",
                    "Device not connected",
                    JOptionPane.ERROR_MESSAGE);
        }
        // End

        btnAutoConfig.setText("Run Device Auto Config");
        btnSave.setEnabled(true);
        btnCancel.setEnabled(true);
        btnAutoConfig.setEnabled(true);
        tabbedPane.setVisible(true);
    }

    @Override
    public void onRefillViewChanged() {
        presenter.onRefillViewChanged(refillView);
    }

    @Override
    public void onRuneViewChanged() {
        presenter.onRuneViewChanged(runeView);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
    }

    @Override
    public void onBattleConfigViewLocationsChanged() {
        presenter.saveBattleConfigLocations(battleConfigView);
    }

    @Override
    public void onBattleConfigViewIndicatorChanged(Indicator indicator, BufferedImage bufferedImage) {
        presenter.saveIndicatorImage(indicator, bufferedImage);
    }

    @Override
    public void onNetworkProblemConfigViewLocationsChanged() {
        presenter.saveNetworkProblemConfigLocations(networkProblemConfigView);
    }

    @Override
    public void onNetworkProblemConfigViewIndicatorChanged(Indicator indicator, BufferedImage bufferedImage) {
        presenter.saveIndicatorImage(indicator, bufferedImage);
    }

    @Override
    public void onRiftConfigViewLocationsChanged() {
        presenter.saveRiftConfigLocations(riftConfigView);
    }

    @Override
    public void onRiftConfigViewIndicatorChanged(Indicator indicator, BufferedImage bufferedImage) {
        presenter.saveIndicatorImage(indicator, bufferedImage);
    }

    @Override
    public void onToaConfigViewIndicatorChanged(Indicator indicator, BufferedImage bufferedImage) {
        presenter.saveIndicatorImage(indicator, bufferedImage);
    }
}
