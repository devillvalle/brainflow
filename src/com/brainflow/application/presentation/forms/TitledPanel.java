package com.brainflow.application.presentation.forms;

import com.brainflow.gui.GradientJPanel;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.PartialLineBorder;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.logging.Logger;


/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 20, 2005
 * Time: 3:23:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class TitledPanel extends JPanel {

    private String title;
    private JLabel titleLabel;
    private GradientJPanel gpanel;

    private Component content;
    private JPanel contentPanel;

    private Color c1 = new Color(87, 124, 182);
    private Color c2 = new Color(92, 175, 246);

    private int titleHeight = 25;
    private int textGap = 5;

    public TitledPanel(String _title, Component _content) {
        title = _title;
        content = _content;

        setLayout(new BorderLayout());

        gpanel = new GradientJPanel(c1, c2, SwingConstants.HORIZONTAL);
        gpanel.setAlpha(1);

        gpanel.setBorder(new PartialLineBorder(Color.GRAY, 1, PartialLineBorder.SOUTH));
        //BorderFactory.createCompoundBorder()
        setBorder(new LineBorder(Color.GRAY));

        titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.white);

        gpanel.setLayout(new BoxLayout(gpanel, BoxLayout.X_AXIS));
        gpanel.add(new Box.Filler(new Dimension(textGap, titleHeight), new Dimension(textGap, titleHeight), new Dimension(textGap, titleHeight)));
        gpanel.add(titleLabel);

        add(gpanel, BorderLayout.NORTH);

        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        //contentPanel.setBorder(new LineBorder(Color.GRAY));

        contentPanel.add(content, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);

    }


    public static void main(String[] args) {
        com.jidesoft.utils.Lm.verifyLicense("UIN", "Brainflow", "7.YTcWgxxjx1xjSnUqG:U1ldgGetfRn1");
        try {
            //UIManager.setLookAndFeel(new com.jidesoft.plaf.eclipse.EclipseWindowsLookAndFeel());
            UIManager.setLookAndFeel(new com.jidesoft.plaf.vsnet.VsnetWindowsLookAndFeel());
            LookAndFeelFactory.installJideExtension();
        } catch (UnsupportedLookAndFeelException e) {
            Logger.getAnonymousLogger().severe("Could not load Look and Feel, aborting");
        }

        JFrame frame = new JFrame();
        JPanel jp = new JPanel();
        JButton jb = new JButton("press me mother fucker");
        jp.add(jb);

        TitledPanel tp = new TitledPanel("Hello", jp);
        frame.add(tp, BorderLayout.CENTER);

        frame.setSize(300, 300);
        frame.setVisible(true);
    }


}
