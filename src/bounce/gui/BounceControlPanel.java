/*
 * This code is copyright.  All rights reserved.
 *
 * The presence of this code on GitHub or any other code hosting service does
 * not imply that it is open source.
 *
 * The author can be found at github.com/thrush.
 */
package bounce.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import bounce.controller.Controller;
import bounce.logic.BounceArea;
import bounce.logic.Converter;
import bounce.logic.Material;

/**
 * A reusable JPanel containing a {@code BounceDisplay} object as well as
 * several components used to control and read from that object.
 */
public class BounceControlPanel extends JPanel implements PropertyChangeListener
{
    private Controller ctrl;
    private BounceDisplay bounceDisplay;
    private JButton startButton;
    private JButton resetButton;
    private JLabel timeField;
    private JComboBox heightCombo;
    private JComboBox materialCombo;
    private DecimalFormat formatter = new DecimalFormat("0.000");

    /**
     * Creates a basic instance of {@code BounceControlPanel}.
     */
    public BounceControlPanel()
    {
        initGUI();

        bounceDisplay.setPropertyChangeListener(this);
        ctrl = new Controller();
        ctrl.registerView(bounceDisplay);
        ctrl.registerView(this);
        BounceArea bounceArea = new BounceArea();
        bounceArea.addListener(ctrl);
        ctrl.registerModel(bounceArea);
        ctrl.initModel(BounceArea.class);

        initListeners();
    }

    /**
     * Initializes the GUI elements and adds them to the layout.
     */
    private void initGUI()
    {
        setLayout(new GridBagLayout());
        GridBagConstraints con = new GridBagConstraints();

        bounceDisplay = new BounceDisplay();
        con.anchor = GridBagConstraints.NORTH;
        con.gridheight = 1;
        con.gridwidth = 3;
        con.gridx = 0;
        con.gridy = 0;
        con.insets = new Insets(5, 5, 0, 5);
        add(bounceDisplay, con);

        JLabel timeLabel = new JLabel("Time (s):");
        Font font = new Font(timeLabel.getFont().getName(), Font.PLAIN,
                timeLabel.getFont().getSize());
        timeLabel.setFont(font);
        con.anchor = GridBagConstraints.NORTHWEST;
        con.gridwidth = 1;
        con.gridy = 1;
        /* TODO add time field when time precision bug is fixed */
//        add(timeLabel, con);

        timeField = new JLabel();
        timeField.setFont(font);
        con.fill = GridBagConstraints.BOTH;
        con.gridwidth = 2;
        con.gridx = 1;
        con.insets = new Insets(5, 0, 0, 5);
//        add(timeField, con);

        JLabel heightLabel = new JLabel("Height (m)");
        heightLabel.setFont(font);
        con.anchor = GridBagConstraints.WEST;
        con.fill = GridBagConstraints.NONE;
        con.gridwidth = 1;
        con.gridx = 0;
        con.gridy = 2;
        con.insets = new Insets(5, 5, 0, 5);
        add(heightLabel, con);

        heightCombo = new JComboBox(
                new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        con.anchor = GridBagConstraints.NORTHWEST;
        con.gridwidth = 2;
        con.gridx = 1;
        add(heightCombo, con);

        JLabel materialLabel = new JLabel("Material");
        materialLabel.setFont(font);
        con.anchor = GridBagConstraints.WEST;
        con.gridwidth = 1;
        con.gridx = 0;
        con.gridy = 3;
        add(materialLabel, con);

        materialCombo = new JComboBox(new Material[]{
                new Material(Material.ID_RUBBER),
                new Material(Material.ID_IRON),
                new Material(Material.ID_STONE)});
        con.anchor = GridBagConstraints.NORTHWEST;
        con.gridwidth = 2;
        con.gridx = 1;
        add(materialCombo, con);

        startButton = new JButton("Start");
        con.fill = GridBagConstraints.NONE;
        con.gridx = 0;
        con.gridy = 4;
        con.insets = new Insets(5, 5, 5, 5);
        add(startButton, con);

        resetButton = new JButton("Reset");
        con.gridwidth = 1;
        con.gridx = 2;
        con.insets = new Insets(5, 0, 5, 5);
        con.weighty = 1;
        add(resetButton, con);
    }

    /**
     * Initialized the listeners that are added to various components on the
     * panel.
     */
    private void initListeners()
    {
        startButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                bounceDisplay.start();
                startButton.setEnabled(false);
            }
        });

        resetButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                bounceDisplay.reset();
                startButton.setEnabled(true);
            }
        });

        heightCombo.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                resizeBounceDisplay();
                startButton.setEnabled(true);
            }
        });

        materialCombo.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                bounceDisplay.changeMaterial(
                        (Material)materialCombo.getSelectedItem());

                startButton.setEnabled(true);
            }
        });
    }

    /**
     * Resizes the BounceDisplay object by updating the presentation model to
     * which it is associated.  Currently, only the height is variable.
     */
    private void resizeBounceDisplay()
    {
        int width = (int)bounceDisplay.getPreferredSize().getWidth();
        int height = ((Integer)heightCombo.getSelectedItem()).intValue() *
                Converter.SCALE_PIXELS_PER_METER;

        ctrl.setModelProperty(
                BounceArea.class, BounceArea.FIELD_SIZE, new Dimension(width, height));
    }

    /**
     * Refreshes the elapsed time displayed.
     */
    private void refreshTimeDisplay()
    {
        timeField.setText(formatter.format(bounceDisplay.getClockTime()));
    }

    /**
     * Receives an event from a subpanel indicating that the time displayed
     * should be refreshed.
     *
     * @param evt currently ignored
     * @see PropertyChangeListener
     * @see PropertyChangeEvent
     */
    public void propertyChange(PropertyChangeEvent evt)
    {
        if(BounceArea.FIELD_SIZE.equals(evt.getPropertyName()))
        {
            Integer i = new Integer(
                    (int)((Dimension)evt.getNewValue()).getHeight());

            heightCombo.setSelectedItem(i.intValue() / Converter.SCALE_PIXELS_PER_METER);
        }
        else
        {
            refreshTimeDisplay();
        }
    }
}