package com.swarauto.ui.profile.widget;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class AbstractPicker<DataType> extends JPanel {
    protected static final Logger LOG = LoggerFactory.getLogger(AbstractPicker.class);
    private static final long serialVersionUID = 1L;
    public static final Color COLOR_MISSING = Color.decode("#AB3D3D");
    public static final Color COLOR_OK = Color.decode("#006F39");

    protected JLabel lblTitle = new JLabel("");
    protected JButton btnPick = new JButton(" Pick");
    @Setter
    protected BufferedImage tutorialImage;

    @Setter
    protected ValueListener valueListener;
    @Getter
    protected DataType data;

    public AbstractPicker(String title, String tooltip) {
        setHtmlTitle(title);
        lblTitle.setToolTipText(tooltip);
        add(lblTitle, "cell 0 0");
        add(createCurrentDataView(), "cell 1 0");
        add(btnPick, "cell 2 0");
        configureLayout();

        btnPick.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openPickerDialog();
            }
        });
    }

    public void setData(DataType data) {
        if (data == null) {
            lblTitle.setForeground(COLOR_MISSING);
        } else {
            lblTitle.setForeground(COLOR_OK);
        }
        this.data = data;
    }

    public void setBtnPickIcon(String path) {
        Image img = null;
        try {
            img = ImageIO.read(new File(path));
            btnPick.setIcon(new ImageIcon(img));
        } catch (IOException e) {
            LOG.error("setBtnPickIcon", e);
        }
    }

    public void setHtmlTitle(String title) {
        lblTitle.setText("<html>" + title + "</html>");
    }

    protected abstract void openPickerDialog();

    protected abstract Component createCurrentDataView();

    protected abstract void configureLayout();
}
