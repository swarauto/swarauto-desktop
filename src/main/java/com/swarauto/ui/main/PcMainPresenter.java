package com.swarauto.ui.main;

import com.swarauto.game.profile.Profile;
import com.swarauto.ui.ViewRegistry;
import com.swarauto.ui.profile.ProfileEditorView;
import com.swarauto.util.PcUtil;

public class PcMainPresenter extends MainPresenter {
    public void refreshDevice() {
        ((PcMainView) view).renderDeviceInfo(PcUtil.getConnectedDeviceName());
    }

    @Override
    protected boolean startAuto() {
        // Check condition so that we can start the auto
        if (PcUtil.getConnectedDeviceName() == null) {
            view.renderMessage("Error: Connect your phone/tablet first...");
            refreshDevice();
            return false;
        }
        return super.startAuto();
    }

    public void requestCreateNewProfile(String profileName) {
        Profile profile = profileManager.createEmptyProfile();
        profile.setName(profileName);
        profileManager.saveProfile(profile);

        refreshProfileList();
        openProfileEditor(profile);
    }

    public void onBtnEditProfileClicked() {
        String profileId = model.getSelectedProfileId();
        if (profileId != null) {
            Profile profile = profileManager.loadProfile(profileId);
            if (profile != null) {
                openProfileEditor(profile);
            }
        }
    }

    public void onBtnDeleteProfileClicked() {
        String profileId = model.getSelectedProfileId();
        if (profileId != null) {
            Profile profile = profileManager.loadProfile(profileId);
            if (profile != null) {
                profileManager.deleteProfile(profile.getId());
                refreshProfileList();
            }
        }
    }

    private void openProfileEditor(Profile profile) {
        ProfileEditorView profileEditorView = (ProfileEditorView) ViewRegistry.get(ProfileEditorView.class);
        profileEditorView.getPresenter().setProfile(profile);
        ViewRegistry.showView(ProfileEditorView.class);
    }
}
