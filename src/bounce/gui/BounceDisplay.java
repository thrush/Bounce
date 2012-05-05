/*
 * This code is copyright.  All rights reserved.
 *
 * The presence of this code on GitHub or any other code hosting service does
 * not imply that it is open source.
 *
 * The author can be found at github.com/thrush.
 */
package bounce.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.Timer;

import bounce.logic.BounceArea;
import bounce.logic.Ball;
import bounce.logic.Converter;
import bounce.logic.Material;
import bounce.logic.Physics;
import bounce.logic.Physics.Direction;

/**
 * A component in which two-dimensional objects are animated so that they appear
 * to bounce under a typical gravitational force.
 */
public class BounceDisplay extends JComponent implements PropertyChangeListener
{
    /** Delay in milliseconds between repaints */
    public static final int DELAY_ANIMATION = 15;

    /** Delay in milliseconds between calculations of the ball's position */
    public static final int DELAY_CALC = 10;

    /** The preferred height of the display area in pixels */
    public static final int PREFERRED_HEIGHT = 250;

    /** The preferred width of the display area in pixels */
    public static final int PREFERRED_WIDTH = 200;

    /** The name of the thread that calculates the ball's position */
    public static final String THREAD_NAME_CALC = "thread-calc";

    /** The time in milliseconds at which the ball starts moving */
    private long displacementStart;

    /** The time elapsed in seconds from the start of a displacement */
    private double displacementTime;

    /** The time it should take to reach a particular apex */
    private double timeToApex;
    
    /** The time at which the "display" clock starts */
    private long clockStart;

    /** The total elapsed time to be displayed */
    private double clockTime;

    /** Flag used to maintain the life of the calculation thread */
    private volatile boolean killCalc = false;

    private Ball ball;
    private Timer animTimer;
    private PropertyChangeListener listener;

    /**
     * Creates an instance of {@code BounceDisplay}.
     */
    public BounceDisplay()
    {
        ball = new Ball(Ball.DEFAULT_DIAMETER);
        animTimer = new Timer(DELAY_ANIMATION, getPaintTimerListener());
    }

    private ActionListener getPaintTimerListener()
    {
        return new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
                repaint();
            }
        };
    }

    /**
     * Draws a horizontal dashed line.  The blank spaces makes up one third of
     * the length of the line.  One blank segment is 3% of the total blank
     * space.  One solid segment is twice the length of a blank.
     * <p>Therefore, if the total length of the line is 300 pixels, there will
     * be 100 pixels of blank space, with each blank segment measuring 3 pixels,
     * and each solid measuring 6 pixels.</p>
     *
     * @param g the {@code Graphics2D} object used to draw the line
     * @param x1 the starting point
     * @param x2 the ending point
     * @param y the y position of the line
     */
    private void drawHorizontalDashedLine(Graphics2D g, int x1, int x2, int y)
    {
        int width = x2 - x1;
        int blank = (int)(width / 3 * 0.03);
        int solid = blank * 2;

        for(int i = 0; i < width; i += (solid + blank))
        {
            g.drawLine(i, y, i + solid - 1, y);
        }
    }

    /**
     * Calculates the scale height of the mark in meters based on the supplied
     * y position in pixels.
     *
     * @param pixels the y position of the mark in pixels
     *
     * @return the scale height in meters of the corresponding mark
     */
    private int calcRuleMark(int pixels)
    {
        return ((int)getPreferredSize().getHeight() / 
                Converter.SCALE_PIXELS_PER_METER) -
                ((pixels + 1) / Converter.SCALE_PIXELS_PER_METER);
    }

    /**
     * Gets a string with the number of meters and meter abbreviation to be used
     * when painting the scaled rule, e.g. "5 m".
     *
     * @param pixels y position on the component where the mark will be painted
     *
     * @return the text to be written above or below a scale mark
     */
    private String getRuleMarkText(int pixels)
    {
        return Integer.toString(calcRuleMark(pixels)) + Converter.ABBREV_METER;
    }

    /**
     * Paints a scaled rule along the left side of the component indicating,
     * marked off in meters.
     *
     * @param g the {@code Graphics2D} object on which the scale is painted
     */
    private void paintRule(Graphics2D g)
    {
        g.setColor(Color.BLACK);

        for(int i = 0; i < getPreferredSize().getHeight(); i++)
        {
            if(i == 0 || ((i + 1) % Converter.SCALE_PIXELS_PER_METER) == 0)
            {
                drawHorizontalDashedLine(
                        g, 0, (int)getPreferredSize().getWidth() - 1, i);

                if(i + 1 < getPreferredSize().getHeight())
                {
                    g.drawString(getRuleMarkText(i), 1,
                            i + g.getFontMetrics().getHeight());
                }
                else
                {
                    g.drawString(getRuleMarkText(i), 1, i - 2);
                }
            }
        }
    }

    /**
     * Paints the component, including a background, scaled rule, and falling
     * object.
     *
     * @param graphics the {@code Grpahics2D} object used to paint
     */
    @Override
    public void paintComponent(Graphics graphics)
    {
        super.paintComponent(graphics);

        Graphics2D g = (Graphics2D)graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // Paint background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        paintRule(g);

        ball.paint(g, (int)getPreferredSize().getHeight());
    }

    /**
     * Gets the time elapsed in seconds since the start of the current bounce
     * movement.  (One movement being either a free fall from the apex, or a
     * rise to the apex from the ground.)
     *
     * @return the time elapsed in seconds
     */
    private double getElapsedBounceTime()
    {
        return (System.currentTimeMillis() - displacementStart) / 1000d;
    }

    /**
     * Resets the time at which one bounce movement begins.  The current system
     * time is used to reset the value. (One movement being either a free fall
     * from the apex, or a rise to the apex from the ground.)
     */
    private void resetDisplacementStartTime()
    {
        displacementStart = System.currentTimeMillis();
    }

    /**
     * Calculates the new position of the falling object based on its current
     * direction of travel, and the time at which it started moving.
     */
    private void calculateNewPosition()
    {
        displacementTime = getElapsedBounceTime();
        double displacement = Physics.calcDisplacement(
                ball.getDirection(), displacementTime, ball.getVelocity());

        switch(ball.getDirection())
        {
            case UP:
            {
                ball.setY(displacement);

                if(ball.getY() >= ball.getApex() ||
                        displacementTime >= timeToApex)
                {
                    ball.setY(ball.getApex());
                    ball.setVelocity(0);
                    ball.setDirection(Direction.DOWN);

                    resetDisplacementStartTime();
                }
                break;
            }
            case DOWN:
            {
                ball.setY(ball.getApex() - displacement);

                if(ball.getY() < 0)
                {
                    ball.setY(0);
                }

                if(ball.getY() == 0 && getHeight() > 0)
                {
                    ball.setVelocity(Physics.calcVelocity(ball.getApex()) *
                            ball.getMaterial().getCor());
                    ball.setApex(Physics.calcBounceApex(ball.getVelocity()));
                    ball.setDirection(Direction.UP);

                    timeToApex = Physics.calcTimeToApex(
                            ball.getApex(), ball.getVelocity());

                    resetDisplacementStartTime();

                    if(Converter.toPixels(ball.getApex()) <= 0)
                    {
                        ball.setDirection(Direction.NONE);
                    }
                }
                break;
            }
            default:
            {
            }
        }
    }

    /**
     * Updates the time that is displayed on the screen as the time elapsed
     * since the beginning of the bounce.
     */
    private void updateClockTime()
    {
        clockTime = (System.currentTimeMillis() - clockStart) / 1000d;
        notifyListener();
    }

    /**
     * Gets the time elapsed in seconds since the very beginning of the bounce.
     *
     * @return the time elapsed in seconds
     */
    public double getClockTime()
    {
        return clockTime;
    }

    /**
     * Begins the animation by starting the timer that calculates the falling
     * objects position.
     */
    public void start()
    {
        resetDisplacementStartTime();
        clockStart = System.currentTimeMillis();
        killCalc = false;

        Thread t = new Thread(THREAD_NAME_CALC)
        {
            @Override
            public void run()
            {
                while(!killCalc && ball.getDirection() != Direction.NONE)
                {
                    /* TODO replace when precision time bug is fixed */
//                    updateClockTime()
                    calculateNewPosition();

                    try
                    {
                        Thread.sleep(DELAY_CALC);
                    }
                    catch(InterruptedException e)
                    {
                    }
                }
            }
        };

        t.start();
        animTimer.start();
    }

    /**
     * Stops the animation and repositions the falling object to its initial
     * drop height.
     */
    public void reset()
    {
        killCalc = true;
        animTimer.stop();

        double height = Converter.toMeters((int)getPreferredSize().getHeight());

        ball.setDiameter(Ball.DEFAULT_DIAMETER);
        ball.setX((Converter.toMeters((int)getPreferredSize().getWidth()) / 2) -
                (ball.getDiameter() / 2));
        ball.setY(height - ball.getDiameter());
        ball.setApex(ball.getY());
        ball.setVelocity(0);
        ball.setDirection(Direction.DOWN);

        clockTime = 0;

        notifyListener();
        repaint();
    }

    /**
     * Sets the listener that listens for updates to the elapsed time that gets
     * displayed on the screen.
     *
     * @param listener the listener that monitors the time value
     */
    public void setPropertyChangeListener(PropertyChangeListener listener)
    {
        this.listener = listener;
    }

    /**
     * Notifies the listener when the elapsed time value has been updated.
     */
    private void notifyListener()
    {
        if(listener != null)
        {
            listener.propertyChange(
                    new PropertyChangeEvent(this, null, null, null));
        }
    }

    /**
     * Resizes the component to the specified size and resets it.  The top-level
     * ancestor, in this case, a {@code JFrame}, is packed in the event that the
     * resizing of the component requires a resizing of the entire window.
     *
     * @param size the size to which the component will be set
     */
    public void resizeDisplay(Dimension size)
    {
        setPreferredSize(size);
        setSize(size);
        reset();
        
        Container container = getTopLevelAncestor();
        if(container instanceof Main)
        {
            /* Set it so that the frame appears to be resized using the top
             * border of the frame rather than the bottom.
             */
            Main m = (Main)container;
            int oldHeight = (int)m.getSize().getHeight();
            m.pack();
            int diff = (int)m.getSize().getHeight() - oldHeight;
            Point p = m.getLocation();
            p.move((int)p.getX(), (int)p.getY() - diff);
            m.setLocation(p);
        }
    }

    /**
     * Changes the material of the ball.
     *
     * @param material the new material
     */
    public void changeMaterial(Material material)
    {
        ball.setMaterial(material);
        reset();
    }

    /**
     * Receives events from a controller when values to a relevant model are
     * updated.
     *
     * @param evt the event received
     *
     * @see PropertyChangeListener
     * @see PropertyChangeEvent
     */
    public void propertyChange(PropertyChangeEvent evt)
    {
        if(BounceArea.FIELD_SIZE.equals(evt.getPropertyName()))
        {
            resizeDisplay((Dimension)evt.getNewValue());
        }
    }
}