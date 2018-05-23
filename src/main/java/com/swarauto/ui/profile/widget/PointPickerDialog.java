package com.swarauto.ui.profile.widget;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PointPickerDialog extends AbstractPickerDialog {
    private static final long serialVersionUID = 1L;

    private Point point;

    private MouseAdapter mouseAdapter = new MouseAdapter() {
        @Override
        public void mouseClicked(final MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                point = e.getPoint();
                e.consume();
                repaint();
                onDonePickData();
            }
        }
    };

    public PointPickerDialog() {
        super();
        setTitle("Box Picker - click to select point");
    }

    @Override
    protected void customPaint(Graphics g) {
        if (point != null) {
            final Graphics g2 = g.create();
            final int radius = 5;
            final int x = point.x - radius;
            final int y = point.y - radius;
            g2.setColor(Color.GREEN);
            g2.fillOval(x, y, radius * 2, radius * 2);
            g2.drawString("(" + point.x + "," + point.y + ")", point.x + 10, point.y + 10);
            g2.dispose();
        }
    }

    @Override
    protected void setData(Object data) {
        point = (Point) data;
    }

    @Override
    protected void onCancel() {
        point = null;
    }

    @Override
    protected void onStartPickData() {
        lblScreen.addMouseListener(mouseAdapter);
        labelInfo.setText("Now pick a point by clicking on screen");
    }

    @Override
    protected void onDonePickData() {
        lblScreen.removeMouseListener(mouseAdapter);
        labelInfo.setText("");
    }

    @Override
    protected void testData() {
        JOptionPane.showMessageDialog(this,
                "No need to test",
                "Success",
                JOptionPane.PLAIN_MESSAGE);
    }

    @Override
    protected void viewPickedData() {
        if (point == null) {
            JOptionPane.showMessageDialog(this,
                    "Please pick a point first",
                    "Empty data",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "x=" + point.x + "\ny=" + point.y,
                    "Picked point",
                    JOptionPane.PLAIN_MESSAGE);
        }
    }

    public Point pickPoint() {
        pack();
        setVisible(true); // Blocking here until dialog close
        return point;
    }
}
