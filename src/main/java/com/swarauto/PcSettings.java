package com.swarauto;

public class PcSettings extends Settings {
    public static final String TUTORIALS_DIR_NAME = "tutorial";
    public static final String PC_VERSION = "1.0.0";

    @Override
    public String getHomeFolderPath() {
        return "./";
    }

    public String getTutorialsFolderPath() {
        return this.getHomeFolderPath() + "/" + TUTORIALS_DIR_NAME;
    }
}
