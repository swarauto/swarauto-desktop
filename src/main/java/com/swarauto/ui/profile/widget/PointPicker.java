package com.swarauto.ui.profile.widget;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class PointPicker extends AbstractPicker<Point> {
    private static final long serialVersionUID = 1L;

    private JLabel labelCurrentPoint;

    public PointPicker(String title, String tooltip) {
        super(title, tooltip);

        setBtnPickIcon("images/ic_point.png");
    }

    @Override
    protected void openPickerDialog() {
        final PointPickerDialog pickerDialog = new PointPickerDialog();
        pickerDialog.setTutorialImage(tutorialImage);
        pickerDialog.setData(data);
        final Point selectedPoint = pickerDialog.pickPoint();

        if (selectedPoint != null) {
            setData(selectedPoint);
            if (valueListener != null) {
                valueListener.valueChanged(this, selectedPoint);
            }
        }
    }

    @Override
    protected Component createCurrentDataView() {
        labelCurrentPoint = new JLabel("MISSING");
        labelCurrentPoint.setHorizontalAlignment(SwingConstants.CENTER);
        return labelCurrentPoint;
    }

    @Override
    public void setData(Point data) {
        super.setData(data);

        if (data == null) {
            labelCurrentPoint.setText("<html>MISSING</html>");
            labelCurrentPoint.setForeground(COLOR_MISSING);
        } else {
            labelCurrentPoint.setText("<html>OK</html>");
            labelCurrentPoint.setForeground(COLOR_OK);
        }
    }

    @Override
    protected void configureLayout() {
        setLayout(new MigLayout("", "[300px][grow,fill][50px]", "[]"));
    }
}
