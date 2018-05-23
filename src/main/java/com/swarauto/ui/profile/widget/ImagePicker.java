package com.swarauto.ui.profile.widget;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePicker extends AbstractPicker<BufferedImage> {
    private static final long serialVersionUID = 1L;

    private JLabel lblStatus;

    public ImagePicker(String title, String tooltip) {
        super(title, tooltip);

        setBtnPickIcon("images/ic_box.png");
    }

    @Override
    protected void openPickerDialog() {
        final ImagePickerDialog pickerDialog = new ImagePickerDialog();
        pickerDialog.setTutorialImage(tutorialImage);
        pickerDialog.setData(data);
        final Image selectedBox = pickerDialog.pickImage();
        if (selectedBox != null) {
            data = (BufferedImage) selectedBox;
            if (valueListener != null) {
                valueListener.valueChanged(this, selectedBox);
            }
        }
    }

    @Override
    protected Component createCurrentDataView() {
        lblStatus = new JLabel("READY");
        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
        return lblStatus;
    }

    @Override
    public void setData(BufferedImage data) {
        super.setData(data);

        lblStatus.setText(data != null ? "<html>OK</html>" : "<html>MISSING</html>");
        lblStatus.setForeground(data != null ? COLOR_OK : COLOR_MISSING);
    }

    @Override
    protected void configureLayout() {
        setLayout(new MigLayout("", "[300px][grow,fill][50px]", "[]"));
    }

}
