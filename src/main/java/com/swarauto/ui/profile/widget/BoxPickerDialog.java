/**
 * Copyright (C) 2017, Justin Nguyen
 */
package com.swarauto.ui.profile.widget;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author tuan3.nguyen@gmail.com
 */
public class BoxPickerDialog extends AbstractPickerDialog {
    private static final long serialVersionUID = 1L;

    private Point startPoint;
    private Point endPoint;
    protected Rectangle pickedBox;

    final MouseAdapter mouseAdapter = new MouseAdapter() {
        @Override
        public void mouseClicked(final MouseEvent e) {
        }

        @Override
        public void mouseDragged(final MouseEvent e) {
            if (startPoint != null) {
                endPoint = e.getPoint();
                e.consume();
                repaint();
            }
        }

        @Override
        public void mousePressed(final MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
//        System.out.println("L Mouse DOWN");
                startPoint = e.getPoint();
                e.consume();
            }
        }

        @Override
        public void mouseReleased(final MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
//        System.out.println("L Mouse UP");
                endPoint = e.getPoint();
                e.consume();
                onDonePickData();
            }
        }
    };

    public BoxPickerDialog() {
        super();
        setTitle("Box Picker - drag to select rectangle");
    }

    public Rectangle pickBox() {
        pack();
        setVisible(true); // Blocking here until dialog close
        return pickedBox;
    }

    @Override
    protected void customPaint(final Graphics g) {
        if (startPoint != null && endPoint != null) {
            final Graphics g2 = g.create();
            final int x = Math.min(startPoint.x, endPoint.x);
            final int y = Math.min(startPoint.y, endPoint.y);
            final int width = Math.abs(startPoint.x - endPoint.x);
            final int height = Math.abs(startPoint.y - endPoint.y);
            g2.setColor(Color.GREEN);
            g2.drawRect(x, y, width, height);
            g2.dispose();
        }
    }

    @Override
    protected void setData(Object data) {
        pickedBox = (Rectangle) data;
    }

    @Override
    protected void onCancel() {
        startPoint = null;
        endPoint = null;
        pickedBox = null;
    }

    @Override
    protected void onStartPickData() {
        lblScreen.addMouseListener(mouseAdapter);
        lblScreen.addMouseMotionListener(mouseAdapter);

        labelInfo.setText("Now pick screen area by drawing a box in screen");
    }

    @Override
    protected void onDonePickData() {
        lblScreen.removeMouseListener(mouseAdapter);
        lblScreen.removeMouseMotionListener(mouseAdapter);

        labelInfo.setText("");

        if (startPoint == null || endPoint == null) {
            pickedBox = null;
        } else {
            final int x = Math.min(startPoint.x, endPoint.x);
            final int y = Math.min(startPoint.y, endPoint.y);
            final int width = Math.abs(startPoint.x - endPoint.x);
            final int height = Math.abs(startPoint.y - endPoint.y);
            if (width == 0 || height == 0) {
                pickedBox = null;
            } else {
                pickedBox = new Rectangle(x, y, width, height);
            }
        }
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
        if (pickedBox == null) {
            JOptionPane.showMessageDialog(this,
                    "Please pick a rectangle first",
                    "Empty data",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "x=" + pickedBox.x + "\ny=" + pickedBox.y + "\nwidth=" + pickedBox.width + "\nheight=" + pickedBox.height,
                    "Picked area",
                    JOptionPane.PLAIN_MESSAGE);
        }
    }
}
