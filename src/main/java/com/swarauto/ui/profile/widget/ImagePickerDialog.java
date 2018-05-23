package com.swarauto.ui.profile.widget;

import com.swarauto.util.ImageUtil;
import com.swarauto.util.PcUtil;
import org.bytedeco.javacpp.opencv_core;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static com.swarauto.util.PcConverter.toAwtRectangle;

public class ImagePickerDialog extends BoxPickerDialog {
    private static final long serialVersionUID = 1L;

    protected BufferedImage pickedImage;

    public ImagePickerDialog() {
        super();

        setTitle("Image Picker - drag to select rectangle");
    }

    @Override
    protected void setData(Object data) {
        pickedImage = (BufferedImage) data;
    }

    public Image pickImage() {
        pickBox();
        return pickedImage;
    }

    @Override
    protected void onDonePickData() {
        super.onDonePickData();

        if (pickedBox == null || screenImage == null) {
            pickedImage = null;
        } else {
            pickedImage = screenImage.getSubimage(pickedBox.x, pickedBox.y, pickedBox.width, pickedBox.height);
        }
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        pickedImage = null;
    }

    @Override
    protected void viewPickedData() {
        if (pickedImage == null) {
            JOptionPane.showMessageDialog(this,
                    "Please pick image first",
                    "Empty data",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            final JLabel label = new JLabel();
            label.setIcon(new ImageIcon(pickedImage));
            JOptionPane.showMessageDialog(this, label, "Picked image", JOptionPane.PLAIN_MESSAGE);
        }
    }

    @Override
    protected void testData() {
        if (pickedImage == null) {
            JOptionPane.showMessageDialog(this,
                    "Please pick image first",
                    "Empty data",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            opencv_core.Mat screen = PcUtil.bufferedImageToMat(screenImage);
            opencv_core.Mat template = PcUtil.bufferedImageToMat(pickedImage);

            com.swarauto.util.Rectangle rectangle = ImageUtil.contains(screen, template, 99);
            template.release();
            screen.release();

            if (rectangle != null) {
                JOptionPane.showMessageDialog(this,
                        "This screen can be recognized",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                pickedBox = toAwtRectangle(rectangle);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to recognize this screen",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
