/*
 * This code is copyright.  All rights reserved.
 *
 * The presence of this code on GitHub or any other code hosting service does
 * not imply that it is open source.
 *
 * The author can be found at github.com/thrush.
 */
package bounce.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

/**
 * A panel designed to display multiple {@code BounceControlPanel} objects.
 */
public class BounceDemoPanel extends JPanel
{
    /**
     * Creates an instance of {@code BounceDemoPanel}.
     */
    public BounceDemoPanel()
    {
        setLayout(new GridBagLayout());
        GridBagConstraints con = new GridBagConstraints();

        con.anchor = GridBagConstraints.SOUTH;
        con.gridheight = 1;
        con.gridwidth = 1;
        con.gridx = 0;
        con.gridy = 0;
        con.insets = new Insets(0, 0, 0, 0);
        add(new BounceControlPanel(), con);

        con.gridx = 1;
        add(new BounceControlPanel(), con);

        con.gridx = 2;
        add(new BounceControlPanel(), con);
    }
}