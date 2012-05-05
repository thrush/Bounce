/*
 * This code is copyright.  All rights reserved.
 *
 * The presence of this code on GitHub or any other code hosting service does
 * not imply that it is open source.
 *
 * The author can be found at github.com/thrush.
 */
package bounce.gui;

import javax.swing.JFrame;

/**
 * A basic frame that houses one or more displays used to animate bouncing
 * objects.
 */
public class Main extends JFrame
{
    /**
     * Creates a basic instance of {@code Main}.
     */
    public Main()
    {
        super("Bounce 0.1.1 (alpha)");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().add(new BounceDemoPanel());
        pack();
    }

    /**
     * Creates and displays an instance of {@code Main}.
     *
     * @param args optional command-line arguments (currently ignored)
     */
    public static void main(String[] args)
    {
        Main m = new Main();

        // Center the frame
        m.setLocationRelativeTo(null);

        m.setVisible(true);
    }
}