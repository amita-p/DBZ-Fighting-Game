// The "Haha" class.
import java.applet.*;
import java.awt.*;
import java.util.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;


public class Haha extends Applet implements Runnable, KeyListener
{
    // Place instance variables here
    // Initialization of variables
    int appletsize_x = 300;
    int appletsize_y = 200;
    boolean move = false;
    boolean hit = false;
    double hp = 100;
    double hpRival = 100;
    boolean leftKeyPressed = false;
    boolean rightKeyPressed = false;
    boolean upKeyPressed = false;
    boolean downKeyPressed = false;
    boolean upRival = false;
    boolean downRival = false;
    boolean leftRival = false;
    boolean rightRival = false;

    int x_pos = 50;
    // x - Position of ball
    int x_posRival = 230;
    int y_posRival = appletsize_y / 2;
    int y_pos = appletsize_y / 2;        // y - Position of ball

    int radius = 20;        // Radius of ball

    int x_increment = 0;
    int y_increment = 0;
    int x_incrementRival = 0;
    int y_incrementRival = 0;

    // declare two instance variables at the head of the program
    private Image dbImage;
    private Graphics dbg;

    //timer stuff
    int startTimeMin;
    int currentTime;
    int secondsElapsed = 0;
    // get the supported ids for GMT-08:00 (Pacific Standard Time)
    String[] ids = TimeZone.getAvailableIDs (-8 * 60 * 60 * 1000);
    // if no ids were returned, something is wrong. get out.
    SimpleTimeZone pdt;

    public void init ()
    {
	// Place the body of the initialization method here

	//timer stuff
	if (ids.length == 0)
	{
	    System.exit (0);
	}
	pdt = new SimpleTimeZone (-8 * 60 * 60 * 1000, ids [0]);
	pdt.setStartRule (Calendar.APRIL, 1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
	pdt.setEndRule (Calendar.OCTOBER, -1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
	Calendar calendar = new GregorianCalendar (pdt);
	Date trialTime = new Date ();
	calendar.setTime (trialTime);
	startTimeMin = calendar.get (Calendar.MINUTE);
	currentTime = calendar.get (Calendar.SECOND);


	setBackground (Color.blue);
	addKeyListener (this);

    } // init method


    public void start ()
    {

	// define a new thread
	Thread th = new Thread (this);

	// start this thread
	th.start ();


    }


    public void stop ()
    {
	//nothing right now
    }


    public void destroy ()
    {
	//nothing right now
    }


    public void run ()
    {

	// lower ThreadPriority
	Thread.currentThread ().setPriority (Thread.MIN_PRIORITY);

	// run a long while (true) this means in our case "always"
	while (true)
	{
	    //timer stuff
	    Calendar calendar = new GregorianCalendar (pdt);
	    Date trialTime = new Date ();
	    calendar.setTime (trialTime);
	    int timeRightNow = calendar.get (Calendar.SECOND);
	    if (timeRightNow != currentTime)
	    {
		secondsElapsed++;
		currentTime = timeRightNow;
	    }

	    if (secondsElapsed == 1 && hit == true)
	    {
		hp -= 5;
		hit = false;
	    }


	    //if the user collides with the foe from the right
	    if (collision ().equals ("rightCollide"))
	    {
		//allow them to move in the opposite direction
		if (leftKeyPressed == true)
		{
		    x_increment = -1;
		}
		//else keep the x increment at 0 for collision
		else
		{
		    x_increment = 0;
		    x_incrementRival = 0;
		}
	    }

	    //if the user collides with the foe from the left
	    else if (collision ().equals ("leftCollide"))
	    {
		//allow them to move in the opposite direction
		if (rightKeyPressed == true)
		{
		    x_increment = 1;
		}
		else
		{
		    x_increment = 0;
		    x_incrementRival = 0;
		}
	    }
	    else if (collision ().equals ("downCollide"))
	    {
		//allow the user to go upwards and move away
		if (upKeyPressed == true)
		{
		    y_increment = -1;
		}
		else
		{
		    y_increment = 0;
		    y_incrementRival = 0;
		}
	    }
	    else if (collision ().equals ("upCollide"))
	    {
		//allow the user to go downwards and move away
		if (downKeyPressed == true)
		{
		    y_increment = 1;
		}
		else
		{
		    y_increment = 0;
		    y_incrementRival = 0;
		}
	    }
	    //no collision, carry on with normal stuff

	    if (collision ().equals (""))
	    {
		Rival ();
	    }

	    //update position on the screen
	    x_pos = x_pos + x_increment;
	    y_pos = y_pos + y_increment;
	    x_posRival = x_posRival + x_incrementRival;
	    y_posRival = y_posRival + y_incrementRival;




	    // repaint the applet
	    repaint ();

	    try
	    {
		// Stop thread for 20 milliseconds
		Thread.sleep (20);
	    }
	    catch (InterruptedException ex)
	    {
		// do nothing
	    }

	    // set ThreadPriority to maximum value
	    Thread.currentThread ().setPriority (Thread.MAX_PRIORITY);

	}


    }


    public void Rival ()
    {
	if (rightRival == true)
	{
	    x_incrementRival = 1;
	    y_incrementRival = 0;
	}
	else if (leftRival == true)
	{
	    x_incrementRival = -1;
	    y_incrementRival = 0;
	}
	else if (upRival == true)
	{
	    y_incrementRival = -1;
	    x_incrementRival = 0;
	}
	else if (downRival == true)
	{
	    y_incrementRival = 1;
	    x_incrementRival = 0;
	}

	leftRival = true;

    }


    //method to check for collision between the fighters
    //returns a string indicating what kind of collision took place
    public String collision ()
    {
	String rightCollide = "rightCollide";
	String leftCollide = "leftCollide";
	String downCollide = "downCollide";
	String upCollide = "upCollide";

	//user colliding from the right or rival colliding from the left
	if ((x_pos + radius * 2) == x_posRival && y_pos >= y_posRival && y_pos <= y_posRival + radius * 2)
	{

	    return rightCollide;

	}
	else if ((x_pos + radius * 2) == x_posRival && y_pos + radius * 2 >= y_posRival && y_pos + radius * 2 <= y_posRival + radius * 2)
	{
	    return rightCollide;
	}
	//colliding from the left or rival colliding from the right
	else if (x_pos == (x_posRival + radius * 2) && y_pos >= y_posRival && y_pos <= y_posRival + radius * 2)
	{

	    return leftCollide;


	}
	else if (x_pos == (x_posRival + radius * 2) && y_pos + radius * 2 >= y_posRival && y_pos + radius * 2 <= y_posRival + radius * 2)
	{
	    return leftCollide;

	}
	//colliding down
	if ((y_pos + radius * 2) == y_posRival && x_pos >= x_posRival && x_pos <= x_posRival + radius * 2)
	{

	    return downCollide;


	}
	else if ((y_pos + radius * 2) == y_posRival && x_pos + radius * 2 >= x_posRival && x_pos + radius * 2 <= x_posRival + radius * 2)
	{

	    return downCollide;


	}
	//colliding up
	if (y_pos == (y_posRival + radius * 2) && x_pos >= x_posRival && x_pos <= x_posRival + radius * 2)
	{

	    return upCollide;

	}
	else if (y_pos == (y_posRival + radius * 2) && x_pos + radius * 2 >= x_posRival && x_pos + radius * 2 <= x_posRival + radius * 2)
	{

	    return upCollide;


	}
	//no collision
	else
	{
	    return "";
	}




    }


    public void keyPressed (KeyEvent e)
    {
	int key = e.getKeyCode ();
	if (key == KeyEvent.VK_LEFT)
	{
	    x_increment = -1;
	    y_increment = 0;
	    leftKeyPressed = true;
	    rightKeyPressed = false;
	    upKeyPressed = false;
	    downKeyPressed = false;
	}
	if (key == KeyEvent.VK_RIGHT)
	{
	    x_increment = 1;
	    y_increment = 0;
	    leftKeyPressed = false;
	    rightKeyPressed = true;
	    upKeyPressed = false;
	    downKeyPressed = false;
	}
	if (key == KeyEvent.VK_UP)
	{
	    y_increment = -1;
	    x_increment = 0;
	    leftKeyPressed = false;
	    rightKeyPressed = false;
	    downKeyPressed = false;
	    upKeyPressed = true;
	}

	if (key == KeyEvent.VK_DOWN)
	{
	    y_increment = 1;
	    x_increment = 0;
	    leftKeyPressed = false;
	    rightKeyPressed = false;
	    upKeyPressed = false;
	    downKeyPressed = true;
	}

	//the user tried to attack, check for collision first then set hit variable to true
	if (key == KeyEvent.VK_A && collision ().equals ("") == false)
	{
	    hit = true;
	    //set seconds elapsed to 0
	    secondsElapsed = 0;

	}
    }


    public void keyReleased (KeyEvent e)
    {
	int key = e.getKeyCode ();
	//stop player motion when the corresponding keys are released
	if (key == KeyEvent.VK_LEFT)
	{
	    x_increment = 0;
	    leftKeyPressed = false;
	}
	if (key == KeyEvent.VK_RIGHT)
	{
	    x_increment = 0;
	    rightKeyPressed = false;
	}
	if (key == KeyEvent.VK_UP)
	{
	    y_increment = 0;
	    upKeyPressed = false;
	}
	if (key == KeyEvent.VK_DOWN)
	{
	    y_increment = 0;
	    downKeyPressed = false;
	}


    }


    public void keyTyped (KeyEvent e)
    {


    }


    /** Update - Method, implements double buffering */
    public void update (Graphics g)
    {

	// initialize buffer
	if (dbImage == null)
	{
	    dbImage = createImage (this.getSize ().width, this.getSize ().height);
	    dbg = dbImage.getGraphics ();
	}

	// clear screen in background
	dbg.setColor (getBackground ());
	dbg.fillRect (0, 0, this.getSize ().width, this.getSize ().height);

	// draw elements in background
	dbg.setColor (getForeground ());
	paint (dbg);

	// draw image on the screen
	g.drawImage (dbImage, 0, 0, this);

    }


    // method to handle key - down events



    public void paint (Graphics g)
    {
	// Place the body of the drawing method here

	// set color
	g.setColor (Color.green);
	// paint a filled colored circle
	g.fillRect (x_pos, y_pos, 2 * radius, 2 * radius);
	g.setColor (Color.white);
	g.fillRect (x_posRival, y_posRival, 2 * radius, 2 * radius);
	if (hit == true)
	{
	    g.setColor (Color.red);
	    g.fillRect (x_posRival, y_posRival, 2 * radius, 2 * radius);

	}
	if (hit == false)
	{
	    g.setColor (Color.white);
	    g.fillRect (x_posRival, y_posRival, 2 * radius, 2 * radius);
	}

	g.drawString ((Integer.toString ((int) hp)), 10, 20);

    } // paint method
} // Haha class
