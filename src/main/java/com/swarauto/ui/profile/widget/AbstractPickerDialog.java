package com.swarauto.ui.profile.widget;

import com.swarauto.dependencies.DependenciesRegistry;
import com.swarauto.util.CommandUtil;
import lombok.Setter;
import net.miginfocom.swing.MigLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class AbstractPickerDialog extends JDialog implements ActionListener {
    private static final long serialVersionUID = 1L;

    private final CommandUtil commandUtil;
    protected BufferedImage screenImage;
    @Setter
    private BufferedImage tutorialImage;

    // Screen
    protected JLabel lblScreen = new JLabel("...") {
        private static final long serialVersionUID = 1L;

        @Override
        protected void paintComponent(final Graphics g) {
            super.paintComponent(g);
            customPaint(g);
        }
    };

    // Buttons
    private JButton btnPickData = new JButton("1. Pick data");
    private JButton btnViewPickedData = new JButton("2. View picked data");
    private JButton btnTest = new JButton("3. Test");
    private JButton btnTutorial = new JButton("Tutorial");
    private JButton btnRefreshScreen = new JButton("Refresh screen");
    private JButton btnSave = new JButton("Save");
    private JButton btnCancel = new JButton("Cancel");
    protected JLabel labelInfo = new JLabel("");

    public AbstractPickerDialog() {
        this.commandUtil = DependenciesRegistry.commandUtil;

        setModal(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        setPreferredSize(screenSize);
        getContentPane().setLayout(new MigLayout("", "[grow]", "[grow,fill][]"));

        // Screen
        lblScreen.setFont(new Font("Tahoma", Font.PLAIN, 38));
        JScrollPane scrollPane = new JScrollPane();
        getContentPane().add(scrollPane, "cell 0 0,growx,aligny top");
        scrollPane.setViewportView(lblScreen);

        // Buttons
        JPanel panelBottom = new JPanel();
        getContentPane().add(panelBottom, "cell 0 1,growx,aligny top");
        panelBottom.setLayout(new MigLayout("", "[][200px][300px][300px]", "[][][][]"));

        JLabel lblSteps = new JLabel("Steps:");
        panelBottom.add(lblSteps, "flowx,cell 0 1");
        panelBottom.add(btnPickData, "cell 0 1,growx");
        btnPickData.addActionListener(this);
        panelBottom.add(btnViewPickedData, "cell 0 1,growx");
        btnViewPickedData.addActionListener(this);
        panelBottom.add(btnTest, "cell 0 1,growx");
        btnTest.addActionListener(this);
        panelBottom.add(labelInfo, "cell 0 2");

        panelBottom.add(btnTutorial, "cell 2 1,growx");
        btnTutorial.addActionListener(this);
        panelBottom.add(btnRefreshScreen, "cell 3 1,growx");
        btnRefreshScreen.addActionListener(this);
        panelBottom.add(btnSave, "cell 2 2,growx");
        btnSave.addActionListener(this);
        panelBottom.add(btnCancel, "cell 3 2,growx");
        btnCancel.addActionListener(this);
    }

    protected abstract void customPaint(final Graphics g);

    protected abstract void setData(Object data);

    protected abstract void onCancel();

    protected abstract void onStartPickData();

    protected abstract void onDonePickData();

    protected abstract void testData();

    protected abstract void viewPickedData();

    protected void refreshScreenshot() {
        lblScreen.setText("Loading screenshot from phone, please wait...");
        lblScreen.setIcon(null);
        lblScreen.revalidate();
        lblScreen.repaint();
        screenImage = null;

        // Capture the screenshot from connected phone and allow user to choose point location.
        new SwingWorker<Void, String>() {
            @Override
            protected Void doInBackground() throws Exception {
                setProgress(0);
                publish(commandUtil.capturePhoneScreen());
                setProgress(100);
                return null;
            }

            @Override
            protected void process(final List<String> paths) {
                setScreenshot(paths.get(0));
            }
        }.execute();
    }

    @Override
    public void setVisible(boolean b) {
        if (b) {
            refreshScreenshot();
        }
        super.setVisible(b);
    }

    private void resizeDialogAsScreenSize() {
        if (screenImage != null) {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int finalWidth = (int) Math.min(screenImage.getWidth(), screenSize.getWidth());
            int finalHeight = (int) Math.min(screenImage.getHeight(), screenSize.getHeight());
            setPreferredSize(new Dimension(finalWidth, finalHeight));
            pack();
        }
    }

    private void setScreenshot(final String filename) {
        if (!isVisible()) {
            return;
        }

        if (filename == null) {
            lblScreen.setText("Could not load screenshot, right click to refresh");
        } else {
            lblScreen.setText("");
            Icon newIcon;
            try {
                screenImage = ImageIO.read(new File(filename));
                newIcon = new ImageIcon(screenImage);
                resizeDialogAsScreenSize();
            } catch (final IOException ex) {
                newIcon = new ImageIcon(filename);
            }
            lblScreen.setIcon(newIcon);
        }
        lblScreen.revalidate();
        lblScreen.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnRefreshScreen) {
            refreshScreenshot();
        } else if (e.getSource() == btnCancel) {
            onCancel();
            dispose();
        } else if (e.getSource() == btnPickData) {
            onStartPickData();
        } else if (e.getSource() == btnViewPickedData) {
            viewPickedData();
        } else if (e.getSource() == btnTest) {
            testData();
        } else if (e.getSource() == btnSave) {
            dispose();
        } else if (e.getSource() == btnTutorial) {
            showTutorial();
        }
    }

    protected void showTutorial() {
        if (tutorialImage == null) {
            JOptionPane.showMessageDialog(this,
                    "Sorry, no tutorial",
                    "Empty data",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            final JLabel label = new JLabel();
            label.setIcon(new ImageIcon(tutorialImage));
            JOptionPane.showMessageDialog(this, label, "Tutorial", JOptionPane.PLAIN_MESSAGE);
        }
    }
}
