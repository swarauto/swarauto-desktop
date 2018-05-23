package com.swarauto.ui.profile;

import com.swarauto.dependencies.DependenciesRegistry;
import com.swarauto.game.indicator.Indicator;
import com.swarauto.game.profile.Profile;
import com.swarauto.game.profile.ProfileChecker;
import com.swarauto.ui.BaseView;
import com.swarauto.ui.ViewRegistry;
import com.swarauto.ui.main.PcMainView;
import com.swarauto.ui.profile.battle.BattleConfigView;
import com.swarauto.ui.profile.networkproblem.NetworkProblemConfigView;
import com.swarauto.ui.profile.refill.RefillView;
import com.swarauto.ui.profile.rift.RiftConfigView;
import com.swarauto.ui.profile.rune.RuneView;
import com.swarauto.util.AutoConfig;
import com.swarauto.util.Point;
import com.swarauto.util.Rectangle;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.swarauto.util.PcConverter.fromAwtPoint;
import static com.swarauto.util.PcConverter.fromAwtRectangle;

public class ProfileEditorPresenter {
    protected static final Logger LOG = LoggerFactory.getLogger(ProfileEditorPresenter.class);
    @Setter
    private Profile profile;
    private View view;

    public ProfileEditorPresenter() {
    }

    public void bindView(View view) {
        this.view = view;
    }

    public void onViewStarted() {
        view.renderAll(profile);
        refreshCommandEngineInfo();
    }

    public void saveProfile() {
        profile.setName(view.getInputProfileName());
        DependenciesRegistry.profileManager.saveProfile(profile);
        ViewRegistry.hideView(ProfileEditorView.class);
        ViewRegistry.showView(PcMainView.class);
    }

    public void cancelEditProfile() {
        ViewRegistry.hideView(ProfileEditorView.class);
        ViewRegistry.showView(PcMainView.class);
    }

    public void saveIndicatorImage(Indicator indicator, BufferedImage bufferedImage) {
        // Save to file
        if (bufferedImage != null) {
            try {
                ImageIO.write(bufferedImage, "png", profile.getIndicatorFile(indicator));
            } catch (IOException e) {
                LOG.error("saveIndicatorImage", e);
            }
        }
    }

    public void saveBattleConfigLocations(BattleConfigView battleConfigView) {
        profile.setReplayBattle(fromAwtPoint(battleConfigView.getReplayBattleButtonLocation().getData()));
        profile.setStartBattle(fromAwtPoint(battleConfigView.getStartBattleButtonLocation().getData()));
        profile.setReviveNo(fromAwtPoint(battleConfigView.getNotReviveButtonLocation().getData()));
        profile.setSellRuneLocation(fromAwtPoint(battleConfigView.getSellRuneButtonLocation().getData()));
        profile.setSellRuneConfirmation(fromAwtPoint(battleConfigView.getSellRuneConfirmButtonLocation().getData()));
        profile.setGetRuneLocation(fromAwtPoint(battleConfigView.getGetRuneButtonLocation().getData()));
        profile.setGetRewardLocation(fromAwtPoint(battleConfigView.getGetRewardButtonLocation().getData()));
    }

    public void saveNetworkProblemConfigLocations(NetworkProblemConfigView networkProblemConfigView) {
        profile.setConfirmNetworkDelay(fromAwtPoint(networkProblemConfigView.getNetworkDelayResendButtonLocation().getData()));
        profile.setResendBattleInfo(fromAwtPoint(networkProblemConfigView.getNetworkUnstableResendButtonLocation().getData()));
    }

    public void saveRiftConfigLocations(RiftConfigView riftConfigView) {
        profile.setSellGemLocation(fromAwtPoint(riftConfigView.getSellStoneLocation().getData()));
        profile.setSellStoneConfirmation(fromAwtPoint(riftConfigView.getSellStoneConfirmLocation().getData()));
        profile.setGetGemLocation(fromAwtPoint(riftConfigView.getGetStoneLocation().getData()));
    }

    public void onRefillViewChanged(RefillView refillTab) {
        profile.setRechargeEnergyYes(fromAwtPoint(refillTab.getRechargeEnergyYes().getData()));
        profile.setRechargeEnergyNo(fromAwtPoint(refillTab.getRechargeEnergyNo().getData()));
        profile.setRechargeEnergy(fromAwtPoint(refillTab.getEnergyLocationInShop().getData()));
        profile.setConfirmRechargeEnergy(fromAwtPoint(refillTab.getConfirmUseCrystal().getData()));
        profile.setAckRechargeEnergyOk(fromAwtPoint(refillTab.getAckRechargeSuccess().getData()));
        profile.setCloseRechargeEnergy(fromAwtPoint(refillTab.getCloseShop().getData()));
    }

    public void onRuneViewChanged(RuneView runeTab) {
        profile.setRareLevelBox(fromAwtRectangle(runeTab.getRarityBox().getData()));
//        saveIndicatorImage(runeTab.getFiveStarRuneConfig().getIndicator(), runeTab.getFiveStarRuneConfig().getData());
//        saveIndicatorImage(runeTab.getSixStarRuneConfig().getIndicator(), runeTab.getSixStarRuneConfig().getData());
    }

    public void autoSetupCommandEngine(int screenWidth, int screenHeight) {
        AutoConfig autoConfig = AutoConfig.findBestMatch(screenWidth, screenHeight);
        double scale = (double) screenWidth / autoConfig.width;

        profile.setStartBattle(scalePoint(scale, autoConfig.startBattleButton));
        profile.setConfirmNetworkDelay(scalePoint(scale, autoConfig.networkDelayResend));
        profile.setResendBattleInfo(scalePoint(scale, autoConfig.networkUnstableResend));
        profile.setEnableAutoMode(scalePoint(scale, autoConfig.toggleAutoAttackButton));
        profile.setReviveNo(scalePoint(scale, autoConfig.reviveNoButton));
        profile.setGetRewardLocation(scalePoint(scale, autoConfig.getOtherReward));
        profile.setSellRuneLocation(scalePoint(scale, autoConfig.runeSellButton));
        profile.setSellRuneConfirmation(scalePoint(scale, autoConfig.runeSellConfirmButton));
        profile.setGetRuneLocation(scalePoint(scale, autoConfig.runeGetButton));
        profile.setReplayBattle(scalePoint(scale, autoConfig.replayButton));

        profile.setRechargeEnergyYes(scalePoint(scale, autoConfig.rechargeEnergyYes));
        profile.setRechargeEnergyNo(scalePoint(scale, autoConfig.rechargeEnergyNo));
        profile.setRechargeEnergy(scalePoint(scale, autoConfig.rechargeEnergy));
        profile.setConfirmRechargeEnergy(scalePoint(scale, autoConfig.confirmRechargeEnergy));
        profile.setAckRechargeEnergyOk(scalePoint(scale, autoConfig.ackRechargeEnergyOk));
        profile.setCloseRechargeEnergy(scalePoint(scale, autoConfig.closeRechargeEnergy));

        profile.setRareLevelBox(scaleRectangle(scale, autoConfig.rareLevelBox));

        view.renderAll(profile);
        refreshCommandEngineInfo();
    }

    private Rectangle scaleRectangle(double scale, Rectangle org) {
        return new Rectangle((int) (org.x * scale), (int) (org.y * scale), (int) (org.width * scale), (int) (org.height * scale));
    }

    private Point scalePoint(double scale, Point org) {
        return new Point((int) (org.x * scale), (int) (org.y * scale));
    }

    private void refreshCommandEngineInfo() {
        view.renderProfileCheckerInfo(ProfileChecker.checkAll(profile));
    }

    public interface View extends BaseView {
        void renderAll(Profile profile);

        void renderProfileCheckerInfo(int flags);

        String getInputProfileName();
    }
}
