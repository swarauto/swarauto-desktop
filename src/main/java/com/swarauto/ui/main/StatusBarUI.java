package com.swarauto.ui.main;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.BevelBorder;

public final class StatusBarUI extends JPanel {
    private static final long serialVersionUID = 1L;

    private JLabel statusLabel;
    private JLabel gameStatusLabel;

    public StatusBarUI() {
        initGUI();
    }

    public JLabel getGameStatusLabel() {
        if (gameStatusLabel == null) {
            gameStatusLabel = new JLabel("Current State");
            gameStatusLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            gameStatusLabel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        }
        return gameStatusLabel;
    }

    public JLabel getStatusLabel() {
        if (statusLabel == null) {
            statusLabel = new JLabel("Log");
            statusLabel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        }
        return statusLabel;
    }

    private void initGUI() {
        setLayout(new MigLayout("", "0[grow,fill][150px,fill]0", "0[fill]0"));
        add(getStatusLabel(), "cell 0 0");
        add(getGameStatusLabel(), "cell 1 0");
    }
}
