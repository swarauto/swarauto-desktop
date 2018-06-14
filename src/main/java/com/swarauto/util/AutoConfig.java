package com.swarauto.util;

import java.util.ArrayList;
import java.util.List;

public class AutoConfig {
    public static final List<AutoConfig> PRESETS = new ArrayList<>();

    static {
        AutoConfig preset169 = new AutoConfig();
        preset169.width = 1920;
        preset169.height = 1080;
        preset169.startBattleButton = new Point(1620, 765);
        preset169.networkDelayResend = new Point(791, 717);
        preset169.networkUnstableResend = new Point(785, 652);
        preset169.toggleAutoAttackButton = new Point(355, 1010);
        preset169.reviveNoButton = new Point(1240, 705);
        preset169.getOtherReward = new Point(960, 830);
        preset169.runeSellButton = new Point(800, 875);
        preset169.runeSellConfirmButton = new Point(792, 657);
        preset169.runeGetButton = new Point(1130, 875);
        preset169.replayButton = new Point(600, 585);
        preset169.rechargeEnergyYes = new Point(795, 655);
        preset169.rechargeEnergyNo = new Point(1130, 655);
        preset169.rechargeEnergy = new Point(800, 590);
        preset169.confirmRechargeEnergy = new Point(800, 660);
        preset169.ackRechargeEnergyOk = new Point(960, 655);
        preset169.closeRechargeEnergy = new Point(1800, 115);
        preset169.rareLevelBox = new Rectangle(1186, 364, 128, 29);

        PRESETS.add(preset169);
    }

    public int width;
    public int height;

    public Point startBattleButton;
    public Point networkDelayResend;
    public Point networkUnstableResend;
    public Point toggleAutoAttackButton;
    public Point reviveNoButton;
    public Point getOtherReward;
    public Point runeSellButton;
    public Point runeSellConfirmButton;
    public Point runeGetButton;
    public Point replayButton;

    // Refill
    public Point rechargeEnergyYes;
    public Point rechargeEnergyNo;
    public Point rechargeEnergy;
    public Point confirmRechargeEnergy;
    public Point ackRechargeEnergyOk;
    public Point closeRechargeEnergy;

    public Rectangle rareLevelBox;
    public Rectangle grindstoneStatBox;

    public double getRatio() {
        return (double) width / height;
    }

    public static AutoConfig findBestMatch(int width, int height) {
        double ratio = (double) width / height;
        double different = Double.MAX_VALUE;
        AutoConfig autoConfig = null;

        for (AutoConfig preset : PRESETS) {
            if (Math.abs(preset.getRatio() - ratio) < different) {
                different = Math.abs(preset.getRatio() - ratio);
                autoConfig = preset;
            }
        }

        return autoConfig;
    }
}
