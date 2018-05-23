package com.swarauto.ui.profile.widget;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class BoxPicker extends AbstractPicker<Rectangle> {
    private static final long serialVersionUID = 1L;

    private JLabel labelCurrentBox;

    public BoxPicker(String title, String tooltip) {
        super(title, tooltip);

        setBtnPickIcon("images/ic_box.png");
    }

    @Override
    protected void openPickerDialog() {
        final BoxPickerDialog pickerDialog = new BoxPickerDialog();
        pickerDialog.setTutorialImage(tutorialImage);
        pickerDialog.setData(data);
        final Rectangle selectedBox = pickerDialog.pickBox();
        if (selectedBox != null) {
            setData(selectedBox);
            if (valueListener != null) {
                valueListener.valueChanged(this, selectedBox);
            }
        }
    }

    @Override
    protected Component createCurrentDataView() {
        labelCurrentBox = new JLabel("NOT SET");
        labelCurrentBox.setHorizontalAlignment(SwingConstants.CENTER);
        return labelCurrentBox;
    }

    @Override
    public void setData(Rectangle data) {
        super.setData(data);

        if (data == null) {
            labelCurrentBox.setText("<html>MISSING</html>");
            labelCurrentBox.setForeground(COLOR_MISSING);
        } else {
            labelCurrentBox.setText("<html>OK</html>");
            labelCurrentBox.setForeground(COLOR_OK);
        }
    }

    @Override
    protected void configureLayout() {
        setLayout(new MigLayout("", "[300px][grow,fill][50px]", "[]"));
    }
}
