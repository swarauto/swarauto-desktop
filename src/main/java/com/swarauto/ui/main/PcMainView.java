package com.swarauto.ui.main;

import com.swarauto.PcSettings;
import com.swarauto.Settings;
import com.swarauto.dependencies.DependenciesRegistry;
import com.swarauto.game.GameState;
import com.swarauto.game.profile.CommonConfig;
import com.swarauto.game.profile.Profile;
import com.swarauto.game.session.AutoSession;
import lombok.Setter;
import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

public final class PcMainView extends JFrame implements MainView, ActionListener, ItemListener {
    private static final Logger LOG = LoggerFactory.getLogger(PcMainView.class);
    private static final long serialVersionUID = 1L;

    private PcMainPresenter presenter;
    private Settings settings;
    private DeviceInfoRefresher deviceInfoRefresher;

    // UI - Account info
    private final JPanel panelAccountInfo = new JPanel();
    private final JLabel lblDevice = new JLabel("Device: Not connected");
    private final JButton btnInfo = new JButton("Info");

    // UI - Profile
    private final JPanel panelProfile = new JPanel();
    private final JComboBox<String> comboBoxProfiles = new JComboBox<>();
    private final JButton btnNewProfile = new JButton("New Device");
    private final JButton btnEditProfile = new JButton("Edit Device");
    private final JButton btnDeleteProfile = new JButton("Delete Device");

    // UI - Session config
    private final JPanel panelSessionConfig = new JPanel();
    private final JLabel lblScenario = new JLabel("Scenario");
    private final JComboBox<String> comboBoxSessionTypes = new JComboBox<>();
    private final JLabel lblMaxRefills = new JLabel("Max refills");
    private final JTextField textMaxRefills = new JTextField();
    private final JLabel lblMaxRuns = new JLabel("Max runs");
    private final JTextField textMaxRuns = new JTextField();
    private final JCheckBox checkboxRecordSoldRunes = new JCheckBox("Record sold Runes in session");
    private final JButton btnShowSoldRunes = new JButton("View sold runes");
    private final JLabel lblMinRarity = new JLabel("Rune min rarity");
    private final JComboBox<String> comboBoxRunePickingMinRarity = new JComboBox<>();
    private final JLabel lblMinGrade = new JLabel("Rune min grade");
    private final JComboBox<String> comboBoxRunePickingMinGrade = new JComboBox<>();
    private final JButton btnStart = new JButton("Start");
    private final JButton btnStop = new JButton("Stop");

    // UI - Status
    private final JPanel panelStatus = new JPanel();
    private final JLabel lblRefills = new JLabel("Refills: 0/0");
    private final JLabel lblSuccessRuns = new JLabel("Success runs: 0/0");
    private final StatusBarUI statusBar = new StatusBarUI();

    public PcMainView() {
        this.settings = DependenciesRegistry.settings;

        textMaxRuns.setColumns(10);
        textMaxRefills.setColumns(10);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("SWarAuto - v" + PcSettings.PC_VERSION);

        getContentPane().setLayout(new MigLayout("", "[grow]", "[][][][][][grow][]"));

        // UI - Account info
        getContentPane().add(panelAccountInfo, "cell 0 0,growx");
        panelAccountInfo.setLayout(new MigLayout("", "[200px,left][200px,right][grow,fill]", "[25px]"));
        panelAccountInfo.add(lblDevice, "cell 0 0,alignx left");
        panelAccountInfo.add(btnInfo, "cell 2 0,growx");
        btnInfo.addActionListener(this);

        // UI - Profile
        getContentPane().add(panelProfile, "cell 0 2,growx");
        panelProfile.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Device config", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(51, 51, 51)));
        panelProfile.setLayout(new MigLayout("", "[grow,fill]", "[][]"));
        panelProfile.add(comboBoxProfiles, "cell 0 0,growx");
        comboBoxProfiles.addItemListener(this);
        panelProfile.add(btnNewProfile, "flowx,cell 0 1");
        btnNewProfile.addActionListener(this);
        panelProfile.add(btnEditProfile, "cell 0 1");
        btnEditProfile.addActionListener(this);
        btnEditProfile.setEnabled(false);
        panelProfile.add(btnDeleteProfile, "cell 0 1");
        btnDeleteProfile.addActionListener(this);
        btnDeleteProfile.setEnabled(false);

        // UI - Session config
        getContentPane().add(panelSessionConfig, "cell 0 4,growx");
        panelSessionConfig.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Run config", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(51, 51, 51)));
        panelSessionConfig.setLayout(new MigLayout("", "[left][grow]", "[][][][][][][]"));
        panelSessionConfig.add(lblScenario, "cell 0 0");
        panelSessionConfig.add(comboBoxSessionTypes, "flowx,cell 1 0,growx");
        comboBoxSessionTypes.addItemListener(this);
        panelSessionConfig.add(lblMaxRefills, "cell 0 1");
        panelSessionConfig.add(textMaxRefills, "cell 1 1,growx");
        textMaxRefills.setInputVerifier(new NumberTextFieldVerifier());
        panelSessionConfig.add(lblMaxRuns, "cell 0 2");
        panelSessionConfig.add(textMaxRuns, "cell 1 2,growx");
        textMaxRuns.setInputVerifier(new NumberTextFieldVerifier());
        panelSessionConfig.add(checkboxRecordSoldRunes, "cell 1 3");
        checkboxRecordSoldRunes.setToolTipText("All old records will be deleted when start new session");
        panelSessionConfig.add(btnShowSoldRunes, "cell 1 3");
        btnShowSoldRunes.addActionListener(this);
        panelSessionConfig.add(lblMinRarity, "cell 0 4");
        panelSessionConfig.add(comboBoxRunePickingMinRarity, "flowx,cell 1 4,growx");
//        panelSessionConfig.add(lblMinGrade, "cell 0 5");
//        panelSessionConfig.add(comboBoxRunePickingMinGrade, "flowx,cell 1 5,growx");
        panelSessionConfig.add(btnStart, "cell 0 6 2 1,growx");
        btnStart.addActionListener(this);
        panelSessionConfig.add(btnStop, "cell 0 6 2 1,growx");
        btnStop.addActionListener(this);
        btnStop.setEnabled(false);

        // UI - Status
        getContentPane().add(panelStatus, "cell 0 5,grow");
        panelStatus.setLayout(new MigLayout("", "[]", "[][]"));
        panelStatus.add(lblRefills, "cell 0 0");
        panelStatus.add(lblSuccessRuns, "cell 0 1");

        getContentPane().add(statusBar, "cell 0 6,growx,aligny top");

        presenter = new PcMainPresenter();
        presenter.bindView(this);

        pack();
        setLocationRelativeTo(null);
    }

    private void populateComboBox(JComboBox<String> comboBox, List<String> values) {
        final Runnable runnable = () -> {
            final DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) comboBox.getModel();
            model.removeAllElements();
            values.forEach(model::addElement);
        };
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(runnable);
        } else {
            runnable.run();
        }
    }

    public void renderDeviceInfo(String deviceName) {
        if (deviceName == null || deviceName.length() == 0) {
            lblDevice.setText("Device: NOT FOUND");
            lblDevice.setForeground(Color.red);
        } else {
            lblDevice.setText("Device: CONNECTED");
            lblDevice.setForeground(Color.green);
        }

    }

    @Override
    public void renderProfileList(List<String> profileList) {
        populateComboBox(comboBoxProfiles, profileList);
    }

    @Override
    public void renderSessionTypeList(List<String> sessionTypeList) {
        populateComboBox(comboBoxSessionTypes, sessionTypeList);
    }

    @Override
    public void renderCommonConfig(CommonConfig commonConfig) {
        textMaxRefills.setText(String.valueOf(commonConfig.getMaxRefills()));
        textMaxRuns.setText(String.valueOf(commonConfig.getMaxRuns()));
        checkboxRecordSoldRunes.setSelected(commonConfig.isRecordSoldRunes());
        populateComboBox(comboBoxRunePickingMinRarity, CommonConfig.RunePickingRarity.getTexts());
        comboBoxRunePickingMinRarity.setSelectedIndex(CommonConfig.RunePickingRarity.indexOf(commonConfig.getRunePickingMinRarity()));
        populateComboBox(comboBoxRunePickingMinGrade, CommonConfig.RunePickingGrade.getTexts());
        comboBoxRunePickingMinGrade.setSelectedIndex(CommonConfig.RunePickingGrade.indexOf(commonConfig.getRunePickingMinGrade()));
    }

    @Override
    public void renderUIForSessionState(AutoSession.State sessionState) {
        switch (sessionState) {
            case INIT:
                btnStart.setText("Start");
                btnStop.setEnabled(false);
                comboBoxProfiles.setEnabled(true);
                comboBoxSessionTypes.setEnabled(true);
                textMaxRefills.setEnabled(true);
                textMaxRuns.setEnabled(true);
                break;
            case RUNNING:
                btnStart.setText("Pause");
                btnStop.setEnabled(true);
                comboBoxProfiles.setEnabled(false);
                comboBoxSessionTypes.setEnabled(false);
                textMaxRefills.setEnabled(false);
                textMaxRuns.setEnabled(false);
                break;
            case PAUSED:
                btnStart.setText("Resume");
                btnStop.setEnabled(true);
                comboBoxProfiles.setEnabled(false);
                comboBoxSessionTypes.setEnabled(false);
                textMaxRefills.setEnabled(false);
                textMaxRuns.setEnabled(false);
                break;
            case STOPPED:
                btnStart.setText("Start");
                btnStop.setEnabled(false);
                comboBoxProfiles.setEnabled(true);
                comboBoxSessionTypes.setEnabled(true);
                textMaxRefills.setEnabled(true);
                textMaxRuns.setEnabled(true);
                break;
        }
    }

    @Override
    public void renderSessionReport(AutoSession.Report report) {
        lblRefills.setText("Refill: " + report.getRefillTimes());
        lblSuccessRuns.setText("Success runs: " + report.getSuccessRuns() + "/" + report.getCompletedRuns());
    }

    @Override
    public void renderGameState(GameState gameState) {
        SwingUtilities
                .invokeLater(() -> statusBar.getGameStatusLabel().setText(gameState == null ? "" : gameState.name()));
    }

    @Override
    public void renderError(String error) {
        renderMessage(error);
    }

    @Override
    public void renderMessage(String message) {
        SwingUtilities.invokeLater(() -> statusBar.getStatusLabel().setText(message));
    }

    @Override
    public CommonConfig getCommonConfig() {
        CommonConfig commonConfig = new CommonConfig();
        commonConfig.setMaxRefills(Integer.parseInt(textMaxRefills.getText()));
        commonConfig.setMaxRuns(Integer.parseInt(textMaxRuns.getText()));
        commonConfig.setRecordSoldRunes(checkboxRecordSoldRunes.isSelected());
        commonConfig.setRunePickingMinRarity(CommonConfig.RunePickingRarity.byIndex(comboBoxRunePickingMinRarity.getSelectedIndex()));
        commonConfig.setRunePickingMinGrade(CommonConfig.RunePickingGrade.byIndex(comboBoxRunePickingMinGrade.getSelectedIndex()));
        return commonConfig;
    }

    @Override
    public void renderProfileCheckerError(String error) {
        JOptionPane.showMessageDialog(this,
                error,
                "Missing device config",
                JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void showInfoDialog(AutoSession.Report sessionReport) {
        StringBuilder sb = new StringBuilder();

        if (sessionReport != null) {
            if (sb.length() > 0) sb.append("\n");
            sb.append("Current session:");
            sb.append("\n - Refills done: ").append(sessionReport.getRefillTimes());
            sb.append("\n - Completed runs: ").append(sessionReport.getCompletedRuns());
            sb.append("\n - Victory runs: ").append(sessionReport.getSuccessRuns())
                    .append("/").append(sessionReport.getCompletedRuns());
        }

        if (sb.length() == 0) sb.append("No info yet");

        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                sb.toString(),
                "Info",
                JOptionPane.INFORMATION_MESSAGE));
    }

    @Override
    public void renderSelectedProfile(Profile profile) {
        // NO THING
    }

    @Override
    public void showSoldRunes() {
        try {
            File soldRunesDir = new File(settings.getSoldRunesFolderPath());
            soldRunesDir.mkdirs();
            Desktop.getDesktop().open(soldRunesDir);
        } catch (IOException e) {
            LOG.error("showSoldRunes", e);
        }
    }

    // Swing events
    @Override
    public void actionPerformed(final ActionEvent e) {
        final Object source = e.getSource();
        if (source == btnInfo) {
            presenter.onBtnInfoClicked();
        } else if (source == btnStart) {
            presenter.onBtnStartClicked();
        } else if (source == btnShowSoldRunes) {
            presenter.onBtnShowSoldRunesClicked();
        } else if (source == btnStop) {
            presenter.onBtnStopClicked();
        } else if (source == btnNewProfile) {
            String newProfileName = JOptionPane.showInputDialog(this,
                    "Name your device, e.g. Galaxy S9",
                    "New Device",
                    JOptionPane.QUESTION_MESSAGE);

            // Validate
            if (newProfileName == null || newProfileName.length() == 0) {
                JOptionPane.showMessageDialog(this,
                        "Invalid device name",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            presenter.requestCreateNewProfile(newProfileName);
        } else if (source == btnEditProfile) {
            presenter.onBtnEditProfileClicked();
        } else if (source == btnDeleteProfile) {
            int dialogResult = JOptionPane.showConfirmDialog(this,
                    "Are you sure want to delete this device?", "Delete device",
                    JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                presenter.onBtnDeleteProfileClicked();
            }
        }
    }

    @Override
    public void itemStateChanged(final ItemEvent e) {
        if (e.getStateChange() != ItemEvent.SELECTED) {
            return;
        }

        if (e.getSource() == comboBoxProfiles) {
            presenter.onProfileSelected(comboBoxProfiles.getSelectedIndex());
            btnEditProfile.setEnabled(true);
            btnDeleteProfile.setEnabled(true);
        } else if (e.getSource() == comboBoxSessionTypes) {
            presenter.onSessionTypeSelected(comboBoxSessionTypes.getSelectedIndex());
        }
    }

    @Override
    public void create() {

    }

    @Override
    public void start() {
        setVisible(true);
        if (deviceInfoRefresher != null) {
            deviceInfoRefresher.setRunning(false);
            deviceInfoRefresher = null;
        }
        deviceInfoRefresher = new DeviceInfoRefresher();
        deviceInfoRefresher.start();
        presenter.refreshProfileList();
        presenter.refreshSessionTypeList();
        presenter.refreshCommonConfig();
    }

    @Override
    public void stop() {
        setVisible(false);
        deviceInfoRefresher.setRunning(false);
        deviceInfoRefresher = null;
    }

    @Override
    public void destroy() {

    }

    public class NumberTextFieldVerifier extends InputVerifier {
        @Override
        public boolean verify(JComponent input) {
            return validNumberInputText(((JTextField) input).getText());
        }
    }

    private boolean validNumberInputText(String text) {
        try {
            int num = Integer.parseInt(text);
            if (num >= 0 && num < Integer.MAX_VALUE) {
                return true;
            }
        } catch (Exception ignored) {

        }
        return false;
    }

    public class DeviceInfoRefresher extends Thread {
        @Setter
        private boolean running = true;

        @Override
        public void run() {
            while (running) {
                presenter.refreshDevice();

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    LOG.error("DeviceInfoRefresher", e);
                }
            }
        }
    }
}
