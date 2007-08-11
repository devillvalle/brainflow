package com.brainflow.application.presentation.forms;

import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import com.jidesoft.hints.ListDataIntelliHints;
import com.jidesoft.swing.FolderChooser;
import com.jidesoft.swing.SelectAllUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Jul 24, 2007
 * Time: 10:51:57 AM
 */
public class SnapshotForm extends JPanel {

    private BufferedImage snapShot;

    private JTextField fileNameField;

    private JComboBox filePathComboBox;

    private JComboBox imageFormatComboBox;

    private JLabel snapShotLabel;

    private List<String> recentDirectories = new ArrayList<String>();

    private JButton browseButton = new JButton("Browse");

    private JButton saveButton = new JButton("Save");

    private JButton cancelButton = new JButton("Cancel");

    private ListDataIntelliHints fileHints;

    private String[] imageFormats;

    private String formatSelection = ".png";

    public SnapshotForm(BufferedImage snapShot) {
        this.snapShot = snapShot;
        String userDir = System.getProperty("user.home");
        recentDirectories.add(userDir);
        initFormats();
        buildGUI();
    }

    private void initFormats() {
        imageFormats = ImageIO.getWriterFileSuffixes();

    }

    private void buildGUI() {
        FormLayout layout = new FormLayout("8dlu, p, 5dlu, l:max(p;80dlu):g, 5dlu, 4dlu, 40dlu, 8dlu", "8dlu, p, 8dlu, p, 10dlu, p, 10dlu, p, 18dlu, p, 8dlu");
        CellConstraints cc = new CellConstraints();
        setLayout(layout);


        ImageIcon icon = new ImageIcon(snapShot);
        snapShotLabel = new JLabel(icon);
        //snapShotLabel.setBackground(Color.LIGHT_GRAY.darker());
        //snapShotLabel.setForeground(Color.LIGHT_GRAY.darker());
        //snapShotLabel.setOpaque(true);

        snapShotLabel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Image Snapshot"), BorderFactory.createEmptyBorder(3, 3, 3, 3)));
        add(snapShotLabel, cc.xyw(2, 2, 6));

        imageFormatComboBox = new JComboBox(imageFormats);
        JLabel formatLabel = new JLabel("Image Format: ");

        add(formatLabel, cc.xy(2, 4));
        add(imageFormatComboBox, cc.xyw(4, 4, 1));

        JLabel fileNameLabel = new JLabel("File Name: ");
        add(fileNameLabel, cc.xy(2, 6));

        fileNameField = new JTextField();
        add(fileNameField, cc.xyw(4, 6, 2));


        JLabel filePathLabel = new JLabel("File Path: ");
        add(filePathLabel, cc.xy(2, 8));

        filePathComboBox = new JComboBox(recentDirectories.toArray());
        filePathComboBox.setEditable(true);
        add(filePathComboBox, cc.xyw(4, 8, 2));

        //add(saveButton, cc.xy(7, 4));
        add(browseButton, cc.xy(7, 8));


        ButtonBarBuilder builder = new ButtonBarBuilder();
        //builder.setHAlignment(CellConstraints.RIGHT);
        builder.addGridded(saveButton);
        builder.addRelatedGap();
        builder.addGridded(cancelButton);
        //builder.setBorder(BorderFactory.createRaisedBevelBorder()); 
        add(builder.getPanel(), cc.xyw(2, 10, 3));

        browseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String path = filePathComboBox.getSelectedItem().toString();
                File fdir = new File(path);

                FolderChooser chooser;

                if (fdir.isDirectory()) {
                    chooser = new FolderChooser(fdir);
                } else {
                    chooser = new FolderChooser(recentDirectories.get(0));
                }

                int ret = chooser.showOpenDialog(SnapshotForm.this);
                if (ret == FolderChooser.APPROVE_OPTION) {
                    String npath = chooser.getSelectedFile().getPath();
                    filePathComboBox.addItem(npath);
                    filePathComboBox.setSelectedItem(npath);
                    updateHints();

                }


            }
        });

        SelectAllUtils.install(fileNameField);
        fileHints = new ListDataIntelliHints(fileNameField, getHintsForPath(filePathComboBox.getSelectedItem().toString()));
        fileHints.setCaseSensitive(false);


        filePathComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                checkValid();
            }

        });

        fileNameField.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void keyReleased(KeyEvent e) {
                checkValid();
            }

            public void keyPressed(KeyEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }


        });

        checkValid();


    }

    private void updateHints() {
        String path = filePathComboBox.getSelectedItem().toString();
        String[] hints = getHintsForPath(path);
        fileHints.setCompletionList(hints);

    }

    private void checkValid() {
        if (isValidFile()) {
            saveButton.setEnabled(true);
        } else {
            saveButton.setEnabled(false);
        }
    }


    private String[] getHintsForPath(String path) {
        File pathFile = new File(path);
        String[] fileNames = pathFile.list();
        return fileNames;
    }

    public boolean isValidFile() {
        File fdir = new File(filePathComboBox.getSelectedItem().toString());
        if (fdir.isDirectory() && (fileNameField.getText().length() > 0)) {
            return true;
        } else {
            return false;
        }
    }

    public String getFilePath() {
        String path = filePathComboBox.getSelectedItem().toString();
        String fileName = fileNameField.getText();

        return path + File.separatorChar + fileName;
    }


    public BufferedImage getSnapShot() {
        return snapShot;
    }

    public JTextField getFileNameField() {
        return fileNameField;
    }

    public JComboBox getFilePathComboBox() {
        return filePathComboBox;
    }

    public JLabel getSnapShotLabel() {
        return snapShotLabel;
    }

    public List<String> getRecentDirectories() {
        return recentDirectories;
    }

    public JButton getBrowseButton() {
        return browseButton;
    }

    public JButton getSaveButton() {
        return saveButton;
    }

    public ListDataIntelliHints getFileHints() {
        return fileHints;
    }

    public static void main(String[] args) {
        try {

            UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
            BufferedImage bimg = ImageIO.read(new File("c:/lddmm/lddmm-coronal-slice-neg-5-shot1.png"));
            SnapshotForm form = new SnapshotForm(bimg);


            String[] formats = ImageIO.getWriterFileSuffixes();
            for (int i = 0; i < formats.length; i++) {
                System.out.println("format: " + formats[i]);
            }


            JFrame frame = new JFrame();
            frame.add(form, BorderLayout.CENTER);
            frame.pack();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
