// The "Haha2" class.
//Amita Pandithan
//June 12, 2014
//Dragon Ball Z-Goten vs. Trunks
/*
This is a fighting game in which the user controls one character through the keyboard and the program controls another character. The movement of the user controlled character is dependent on the value of the
keyPressed variable, which is a string variable which indicates which key was pressed. This variable is also helpful when the program decided which sprites to display (ex. if keyPressed.equals("up") then display the
moving up sprite). The keys 'a' and 's' are used for punching and kicking respectively. Punching and kicking can only occur if the collision method returns true and other requirements are met (ex. one character needs to be facing
the other character to kick or punch, they cannot punch if only they're foot is colliding with the other character, etc.) The ultimate attack, kamehameha does not require collision, however does require for the opponent character to
be within a certain range of the user controlled character to work. Attacking another character uses up something called ki, which is like the power of a character at an instant. Punching/kicking requires 10 ki, while the ultimate attack
requires 50. Ki is increased or decreased depending on the values of the hit variables. If kit/kick (Goten's variables) returns true, Goten's ki goes down progressively until is becomes false. Trunks has a similar variable called gotenHit.
I the runCounter, a counter which counts how many times the run method is executed. Every 10 times it is executed, ki goes down by 1 for basic attacks. A similar mechanism is used for the health of each character when they are attacked.
If a character is in the process of hitting someone, but moves away, the hit variable becomes false. I used a timer to turn the variables for basic attacks to false after 1 second and ultimate attack to false after 2 seconds (basic attacks last 1 second
and ultimate lasts 2). The ultimate attack does not require collision, it has it's effect immediately is Trunks is anywhere in front of Goten, due to the intensity of the light.
In order to increase ki, the character has to charge, which is done by continuously pressed down teh 'c' key. The user cannot move or do anything while charging and if they are hit while charging they,stop charging.
The collision method is a boolean method which checks the 4 corner coordinates of each player and checks whether they are within the coordiates of the other character. The method is used to detect whether a character can do a direct
attack as mentioned above. It is also used to create a barrier so that the user controlled character cannot move through the other character. This will allow them to collide into the character and punch/kick easily without going right past them.
So when collision returns true and ex.the opponent is right in front of the character, the character cannot move forward. This is done through the if statement that look at the keyPressed method to decide which direction the character should move in.
These restrictions are also put into those if statements, so that not only does ex. keyPressed have to equal "right" for the user to move right, but either collision has to be false, or the opponent's x and y should not be directly to the right of
the character. Since the characters can fly, it is possible that a character can fly upward and end up on top of the opponent. I did not want to include a barrier t stop movement in this case because it restricts movement and looks weird. So to ensure that the
character can still move during these situations, I made it so that the character has to be less that 6 pixels on top of the opponent for there to be a barrier(if more than 5 pixels of the character are on the opponent, then they are pretty much overlapping
the opponent. So that's why sometimes the character can go past the opponent. Now...onto the method which controls Trunks. The Trunks method controls all of Trunks' movement with the help of some methods from the fighter class. Basically, Trunks follows
aroung Goten, and how closely he follows him is randomly decided. There are some instances when Trunks will stand directly in front of Goten and trap him and there is another instance when he stays around 60pxls above or below him. Sometimes he moves
100 pxls to the right of Goten. If collision occurs, he will attack Goten 70% of the time and will never attack more than once every two seconds. If his ki reached a critical point, he will move away and charge. All of this is done through if statement, but attacking
is the top priority.
*/
import java.applet.*;
import java.awt.*;
import java.util.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.net.*;


public class Haha2 extends Applet implements Runnable
{
    // Place instance variables here
    // Initialization of variables



    //Image variable
    Image gotenDash;
    Image trunksDash;
    Image kaiPlanet; //background
    Image gotenDashLeft;
    Image trunksDashRight;
    Image gotenRise;
    Image gotenStand;
    Image gotenStandLeft;
    Image trunksStand;
    Image trunksHurt;
    Image gotenPunch;
    Image gotenPunchLeft;
    Image gotenCharge;
    Image trunksRise;
    Image gotenRapidKick;
    Image trunksStandRight;
    Image trunksPortrait;
    Image gotenPortrait;
    Image opening;
    Image logo;
    Image gotenHurt;
    Image trunksPunch;
    Image gotenRapidKickLeft;
    Image trunksPunchRight;
    Image trunksCharge;
    Image kamehameha;
    Image kameStance;
    Image gotenBlast;
    Image iceShelf;
    Image purpleHaze;
    Image gotenDead;
    Image trunksDead;
    URL base;
    MediaTracker mt;

    //audio stuff
    AudioClip introSong;
    AudioClip gameSong;


    int appletsize_x = 450;
    int appletsize_y = 250;
    //creating two fighters
    Fighter Goten = new Fighter (50, appletsize_y / 2, 45, 60, "right");
    Fighter Trunks = new Fighter (230, appletsize_y / 2, 45, 60, "left");
    int kaiPlanetx = -346;
    //variables which indicate whether a player is hurt
    boolean hit = false;
    boolean kick = false;
    boolean kameAttack = false;
    boolean kameHit = false;
    boolean gotenHit = false;
    boolean gotenKick = false;
    //decision variables for Trunks' ai
    double decision = -5.0;
    double collisionDecision = 0.0;
    //these strings are used to keep track of what kind of movement a player is doing
    //needed for the program to know which sprite to use
    String keyPressed = "";
    String trunksMove = "";
    //variables which indicate whether a player is charging
    boolean charge = false;
    boolean trunksCharging = false;
    //boolean variables for screens
    boolean instructionsScreen = false;
    boolean introScreen = true;
    boolean fightDoneScreen = true;
    int collideCounter = 0;
    //counters which keep track of how many times the run method runs
    int trunksRunCounter = 0;
    int runCounter = 0;
    //string to keep track of which direction a collision occurred in
    String collisionDir = "";





    // declare two instance variables at the head of the program
    private Image dbImage;
    private Graphics dbg;

    //timer stuff
    int startTimeMin;
    int currentTime;
    int secondsElapsed = 0;
    int secondsElapsedSinceLastHit = 0;
    int realSecondsElapsed;
    // get the supported ids for GMT-08:00 (Pacific Standard Time)
    String[] ids = TimeZone.getAvailableIDs (-8 * 60 * 60 * 1000);
    // if no ids were returned, something is wrong. get out.
    SimpleTimeZone pdt;

    public void init ()
    {
	// Place the body of the initialization method here
	resize (450, 250);

	//sound stuff
	introSong = getAudioClip (getDocumentBase (), "introSong.au");
	
	//image stuff
	mt = new MediaTracker (this);
	try
	{
	    base = getDocumentBase ();
	}
	catch (Exception e)
	{
	}
	gotenDash = getImage (base, "Gotendash.gif");
	trunksDash = getImage (base, "Trunksdash.gif");
	kaiPlanet = getImage (base, "kaiPlanet.jpg");
	gotenDashLeft = getImage (base, "gotenDashLeft.gif");
	trunksDashRight = getImage (base, "trunksDashRight.gif");
	gotenRise = getImage (base, "gotenUp.gif");
	gotenStand = getImage (base, "Gotenstand.gif");
	trunksStand = getImage (base, "Trunksstand.gif");
	trunksHurt = getImage (base, "Trunkshurt.gif");
	gotenPunch = getImage (base, "GotenPunch.gif");
	gotenStandLeft = getImage (base, "GotenStandLeft.gif");
	gotenPunchLeft = getImage (base, "GotenPunchLeft.gif");
	gotenCharge = getImage (base, "GotenCharge.gif");
	trunksRise = getImage (base, "trunksUp.gif");
	gotenRapidKick = getImage (base, "gotenRapidKick.gif");
	trunksStandRight = getImage (base, "trunksStandRight.gif");
	trunksPortrait = getImage (base, "trunksPortrait.gif");
	gotenPortrait = getImage (base, "gotenPortrait.gif");
	opening = getImage (base, "ringaroundtherosie.gif");
	logo = getImage (base, "logo2.gif");
	gotenHurt = getImage (base, "gotenHurt.gif");
	trunksPunch = getImage (base, "trunksPunch.gif");
	gotenRapidKickLeft = getImage (base, "gotenRapidKickLeft.gif");
	trunksPunchRight = getImage (base, "trunksPunchRight.gif");
	trunksCharge = getImage (base, "trunksCharge.gif");
	kamehameha = getImage (base, "Kamehameha.gif");
	kameStance = getImage (base, "kameStance.gif");
	gotenBlast = getImage (base, "gotenBlast.gif");
	iceShelf = getImage (base, "iceshelf.gif");
	purpleHaze = getImage (base, "purpleHaze.jpg");
	gotenDead = getImage (base, "gotenunconcious.gif");
	trunksDead = getImage (base, "trunksunconsious.gif");

	mt.addImage (gotenDash, 1);
	mt.addImage (trunksDash, 2);
	mt.addImage (kaiPlanet, 3);
	mt.addImage (gotenDashLeft, 4);
	mt.addImage (trunksDashRight, 5);
	mt.addImage (gotenRise, 6);
	mt.addImage (gotenStand, 7);
	mt.addImage (trunksStand, 8);
	mt.addImage (trunksHurt, 9);
	mt.addImage (gotenPunch, 10);
	mt.addImage (gotenStandLeft, 11);
	mt.addImage (gotenPunchLeft, 12);
	mt.addImage (gotenCharge, 13);
	mt.addImage (trunksRise, 14);
	mt.addImage (gotenRapidKick, 15);
	mt.addImage (trunksStandRight, 16);
	mt.addImage (trunksPortrait, 17);
	mt.addImage (gotenPortrait, 18);
	mt.addImage (opening, 19);
	mt.addImage (logo, 20);
	mt.addImage (gotenHurt, 21);
	mt.addImage (trunksPunch, 22);
	mt.addImage (gotenRapidKickLeft, 23);
	mt.addImage (trunksPunchRight, 24);
	mt.addImage (trunksCharge, 25);
	mt.addImage (kamehameha, 26);
	mt.addImage (kameStance, 27);
	mt.addImage (gotenBlast, 28);
	mt.addImage (iceShelf, 29);
	mt.addImage (purpleHaze, 30);
	mt.addImage (gotenDead, 31);
	mt.addImage (trunksDead, 32);

	try
	{
	    mt.waitForAll ();
	}
	catch (InterruptedException e)
	{
	}

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

	    introSong.play ();


	    //timer stuff
	    Calendar calendar = new GregorianCalendar (pdt);
	    Date trialTime = new Date ();
	    calendar.setTime (trialTime);
	    int timeRightNow = calendar.get (Calendar.SECOND);

	    if (introScreen == false)
	    { //time stops when the instructions screen is displayed
		if (timeRightNow != currentTime && instructionsScreen == false)
		{
		    secondsElapsed++;
		    secondsElapsedSinceLastHit++;
		    realSecondsElapsed++;
		    currentTime = timeRightNow;
		}

		if (Goten.health == 0 || Trunks.health == 0 && hit == false && kameHit == false && kick == false && gotenHit == false)
		{
		    fightDoneScreen = true;
		}


		//after 1 second, if the enemy was hit make the hit and kick variables false
		//the hitting and kicking actions occur for 1 second
		//the enemy remains unable to fight for one second
		if (secondsElapsed == 1 && hit == true || secondsElapsed == 1 && kick == true)
		{
		    hit = false;
		    kick = false;
		    Goten.ki = Goten.ki - 10;
		}
		if (secondsElapsed == 1 && gotenHit == true)
		{
		    gotenHit = false;
		    gotenKick = false;
		    Trunks.ki = Trunks.ki - 10;
		}
		if (secondsElapsed == 2 && kameAttack == true)
		{
		    kameAttack = false;
		    kameHit = false;
		    Goten.ki = Goten.ki - 50;
		}
		if (gotenHit == true)
		{
		    kameAttack = false;
		    kameHit = false;
		}




		//as long as these variables remain true, the enemy's health decreases by 1 every 5 times the run method is executed
		if (hit == true || kick == true)
		{
		    if (runCounter % 10 == 0)
		    {
			Trunks.health--;
		    }
		}
		if (kameAttack == true && Trunks.y >= Goten.y - 40 && Trunks.y + Trunks.height <= Goten.y + 80)
		{

		    kameHit = true;
		    if (runCounter % 2 == 0 && secondsElapsed >= 1)
		    {
			Trunks.health--;
		    }


		}
		//same thing for the player
		if (gotenHit == true)
		{
		    if (runCounter % 10 == 0)
		    {
			Goten.health--;
		    }

		}

		if (charge == true && Goten.ki < 100)
		{
		    if (runCounter % 10 == 0)
		    {
			Goten.ki++;
		    }
		}
		if (trunksCharging == true && Trunks.ki < 100)
		{
		    if (runCounter % 10 == 0)
		    {
			Trunks.ki++;
		    }
		}

		Trunks ();

		collision ();

		//keyboard controls for moving the character around, depending on the value of keyPressed from the keyDown method
		//the character can only move if they aren't hit and aren't charging

		if (gotenHit == false && charge == false && instructionsScreen == false && Goten.health > 0 && Trunks.health > 0)
		{
		    if (keyPressed.equals ("right") && Goten.x + Goten.width < 450)
		    {
			if ((Goten.x + Goten.width > Trunks.x - 6 && Goten.x + Goten.width < Trunks.x + 6) == false || (collision () == false))
			{
			    Goten.moveRight (6);
			    kaiPlanetx--;
			}
		    }
		    else if (keyPressed.equals ("left") && Goten.x > 0)
		    {
			if (((Trunks.x + Trunks.width - 6 < Goten.x && Trunks.x + Trunks.width + 6 > Goten.x) == false) || collision () == false)
			{
			    Goten.moveLeft (6);
			    kaiPlanetx++;
			}
		    }
		    else if (keyPressed.equals ("up") && Goten.y > 70)
		    {
			Goten.moveUp (6);
		    }
		    else if (keyPressed.equals ("down") && Goten.y < 170)
		    {
			Goten.moveDown (6);
		    }
		    else if (keyPressed.equals ("")) //no movement means that they hover in midair
		    {
			Goten.hover (runCounter);
		    }
		}

		if (Goten.health <= 0 && Goten.y < 190)
		{
		    Goten.moveDown (6);
		}


		//Trunks's AI method is called to consistently update his movement


		runCounter++;
		trunksRunCounter++;
		if (runCounter > 40)
		{
		    runCounter = 0;
		}
	    }

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



    //method that controls the behavior of Trunks
    public void Trunks ()
    {
	//changes the direction that he faces based on Goten's location
	if (Goten.x < Trunks.x)
	{
	    Trunks.direction = "left";
	}
	else if (Goten.x >= Trunks.x)
	{
	    Trunks.direction = "right";
	}

	if (trunksRunCounter % 100 == 0)
	{
	    decision = Math.random ();
	    collisionDecision = Math.random ();
	}

	if (Trunks.health <= 0 && Trunks.y < 190)
	{
	    Trunks.moveDown (6);
	}


	if (instructionsScreen == false && Trunks.health > 0 && Goten.health > 0)
	{
	    //if collision occurs he hits Goten 70% of the time
	    //in order to maintain ki he only hits him once every 3 seconds
	    if (collision () == true && secondsElapsedSinceLastHit >= 3 && Trunks.y >= Goten.y - 20 && Trunks.y <= Goten.y + 20 && trunksCharging == false && decision < 0.7)
	    {
		if ((collisionDecision < 1 || charge == true) && Trunks.ki >= 10 && hit == false && kick == false && kameHit == false)
		{
		    gotenHit = true;
		    secondsElapsed = 0;
		    secondsElapsedSinceLastHit = 0;
		}
	    }

	    //if his ki is less than 20, he goes away to charge before attacking again
	    else if (Trunks.ki <= 20)
	    {
		//he moves 100 pxls away from Goten before charging
		if (Math.abs (Goten.x - Trunks.x) < 100 && trunksCharging == false && hit == false && kick == false && kameHit == false && gotenHit == false)
		{
		    Trunks.moveRight (4);
		    trunksMove = "right";
		}
		else
		{
		    trunksMove = "";
		    if (hit == false && kick == false && kameHit == false && gotenHit == false)
		    {
			trunksCharging = true;
		    }

		}
	    }
	    else
	    {
		trunksCharging = false;
		if (decision < 0.7 && hit == false && kick == false && kameHit == false)
		{
		    trunksMove = Trunks.moveCloser (Goten, 6, collision (), 65);
		}
		else if (((decision >= 0.7 && decision <= 0.9) || charge == true) && hit == false && kick == false && kameHit == false)
		{
		    trunksMove = Trunks.moveCloser (Goten, 6, collision (), 5);
		}
		else
		{
		    if (Math.abs (Goten.x - Trunks.x) < 100 && trunksCharging == false && hit == false && kick == false && kameHit == false && gotenHit == false)
		    {
			Trunks.moveRight (5);
			trunksMove = "right";
		    }
		    else
		    {
			trunksMove = "";
		    }

		}

	    }

	}

	if (trunksMove.equals ("") && Trunks.health > 0)
	{
	    //hovering movement
	    Trunks.hover (runCounter);
	}

    }


    //method to detect collision...
    public boolean collision ()
    {
	boolean collide = false;
	if (Goten.x >= Trunks.x && Goten.x <= Trunks.x + Trunks.width)
	{
	    if (Goten.y >= Trunks.y && Goten.y <= Trunks.y + Trunks.height)
	    {
		collide = true;

	    }
	}


	if (Goten.x >= Trunks.x && Goten.x <= Trunks.x + Trunks.width)
	{
	    if (Goten.y + Goten.height >= Trunks.y && Goten.y + Goten.height <= Trunks.y + Trunks.height)
	    {

		collide = true;

	    }
	}


	if (Goten.x + Goten.width >= Trunks.x && Goten.x + Goten.width <= Trunks.x + Trunks.width)
	{
	    if (Goten.y >= Trunks.y && Goten.y <= Trunks.y + Trunks.height)
	    {
		collide = true;
	    }
	}


	if (Goten.x + Goten.width >= Trunks.x && Goten.x + Goten.width <= Trunks.x + Trunks.width)
	{
	    if (Goten.y + Goten.height >= Trunks.y && Goten.y + Goten.height <= Trunks.y + Trunks.height)
	    {
		collide = true;
	    }
	}


	if (collide == true)
	{

	    if (collideCounter == 0)
	    {
		collisionDir = keyPressed;
	    }
	    collideCounter++;
	}


	else
	{
	    collisionDir = "";
	    collideCounter = 0;
	}


	return collide;
    }


    //the string value of variable keyPressed changes depending on keyboard input
    //the value of keyPresseed indicates which direction the character should move in in the run method
    public boolean keyDown (Event e, int key)
    {
	//moving around the screen
	//the user can only move if they're not in the process of hitting/kicking
	if (introScreen == false && instructionsScreen == false)
	{
	    if (key == Event.LEFT && hit == false && kick == false)
	    {
		keyPressed = "left";
		Goten.direction = "left";
	    }


	    if (key == Event.RIGHT && hit == false && kick == false)
	    {
		keyPressed = "right";
		Goten.direction = "right";
	    }


	    if (key == Event.UP && hit == false && kick == false)
	    {
		keyPressed = "up";
	    }


	    if (key == Event.DOWN && hit == false && kick == false)
	    {
		keyPressed = "down";
	    }


	    //keys for attacking (direct punches and kicks, so collision must occur for these)
	    //the secondsElapsed is set to 0, so that when secondsElapsed turns into 1, we know that the attack happened for 1 second and can end it
	    if (key == 97 && collision () == true && Goten.y >= Trunks.y - 20 && Goten.y <= Trunks.y + 20 && Goten.health > 0)  //'a' key for punching
	    {
		if (Goten.direction.equals (Trunks.direction) == false && gotenHit == false && Goten.ki >= 10) //Goten has to face Trunks in order to punch him (they can't be facing teh same direction)
		{
		    hit = true;
		    secondsElapsed = 0;
		}
	    }



	    else if (key == 115 && collision () == true && ((Goten.y + Goten.height <= Trunks.y + Trunks.height && Goten.y + Goten.height >= Trunks.y) || (Goten.y + Goten.height <= Trunks.y + Trunks.height + 10))) //'s' key for kicking
	    {
		if (Goten.direction.equals (Trunks.direction) == false && gotenHit == false && Goten.ki >= 10 && Goten.health > 0) //Goten has to face Trunks in order to kick him(they can't be facing the same direction)
		{
		    kick = true;
		    secondsElapsed = 0;
		}
	    }

	    else if (key == 107 && Goten.ki >= 50 && Goten.health > 0)
	    {
		kameAttack = true;
		secondsElapsed = 0;
	    }


	    //key for charging
	    //charging increases the characters ki(power) and stops all movement of the character (charging requires a lot of concentration, that's why)
	    if (key == 99) //'c' key is for charging
	    {
		keyPressed = "";
		charge = true;
	    }


	}

	if (key == 10)
	{
	    introScreen = false;
	}
	if (key == 105 && instructionsScreen == false)
	{
	    instructionsScreen = true;

	}
	//if the instructionsScreen is displayed and 'i' is pressed then, that means the user
	//doesn't want to see the instructions screen anymore
	else if (key == 105 && instructionsScreen == true)
	{
	    instructionsScreen = false;
	}

	return true;

    }


    public boolean keyUp (Event e, int key)
    {
	//when a directional key isn't being pressed, keyPressed becomes and empty screen, which results in no movement
	if (key == Event.LEFT || key == Event.RIGHT || key == Event.UP || key == Event.DOWN)
	{
	    keyPressed = "";
	}


	//when the charging key is let go, charging stops
	if (key == 99)
	{
	    charge = false;
	}


	return true;
    }











    public void paint (Graphics g)
    {
	// Place the body of the drawing method here


	// draw the background
	g.drawImage (purpleHaze, kaiPlanetx, 0, 1142, 250, this);
	g.drawImage (iceShelf, kaiPlanetx, 0, 1142, 250, this);

	if (keyPressed.equals ("") && hit == false && kick == false && kameAttack == false && gotenHit == false)
	{
	    if (charge == true && Goten.health > 0 && Trunks.health > 0)
	    {
		g.drawImage (gotenCharge, Goten.x - 50, Goten.y - 50, 144, 117, this);
	    }
	}

	//trunks
	if (Trunks.health <= 0)
	{
	    g.drawImage (trunksDead, Trunks.x, Trunks.y, 62, 34, this);
	}
	else if (hit == true || kick == true || kameHit == true)
	{
	    g.drawImage (trunksHurt, Trunks.x, Trunks.y, Trunks.width + 10, Trunks.height, this);
	}

	else if (trunksCharging == true)
	{
	    g.drawImage (trunksCharge, Trunks.x - 50, Trunks.y - 50, 144, 117, this);
	}


	else if (trunksMove.equals ("left"))
	{
	    g.drawImage (trunksDash, Trunks.x, Trunks.y, Trunks.width, Trunks.height, this);
	}


	else if (trunksMove.equals ("right"))
	{
	    g.drawImage (trunksDashRight, Trunks.x, Trunks.y, Trunks.width, Trunks.height, this);
	}


	else if (trunksMove.equals ("up") || trunksMove.equals ("down"))
	{
	    g.drawImage (trunksRise, Trunks.x, Trunks.y, Trunks.width, Trunks.height, this);
	}


	else if (trunksMove.equals ("") && gotenHit == false)
	{
	    if (Trunks.direction.equals ("left"))
	    {
		g.drawImage (trunksStand, Trunks.x, Trunks.y, Trunks.width - 10, Trunks.height, this);
	    }
	    else
	    {
		g.drawImage (trunksStandRight, Trunks.x, Trunks.y, Trunks.width - 10, Trunks.height, this);
	    }
	}

	if (gotenHit == true && Trunks.health > 0)
	{
	    if (Trunks.direction.equals ("left"))
	    {
		g.drawImage (trunksPunch, Trunks.x, Trunks.y, Trunks.width, Trunks.height, this);
	    }
	    else
	    {
		g.drawImage (trunksPunchRight, Trunks.x, Trunks.y, Trunks.width, Trunks.height, this);
	    }
	}


	//goten
	if (Goten.health <= 0)
	{
	    g.drawImage (gotenDead, Goten.x, Goten.y, 62, 34, this);

	}
	else if (gotenHit == true)
	{
	    g.drawImage (gotenHurt, Goten.x, Goten.y, Goten.width, Goten.height, this);
	}

	else if (keyPressed.equals ("right"))
	{
	    g.drawImage (gotenDash, Goten.x, Goten.y, Goten.width, Goten.height, this);
	}


	else if (keyPressed.equals ("left"))
	{
	    g.drawImage (gotenDashLeft, Goten.x, Goten.y, Goten.width, Goten.height, this);
	}


	else if (keyPressed.equals ("up") || keyPressed.equals ("down"))
	{
	    g.drawImage (gotenRise, Goten.x, Goten.y, Goten.width, Goten.height, this);
	}


	else if (keyPressed.equals ("") && hit == false && kick == false && kameAttack == false && gotenHit == false && charge == false)
	{

	    if (Goten.direction.equals ("right")) //depending on whether they use pressed the right or left key last, goten faces different directions
	    {
		g.drawImage (gotenStand, Goten.x, Goten.y, Goten.width, Goten.height, this);
	    }
	    else
	    {
		g.drawImage (gotenStandLeft, Goten.x, Goten.y, Goten.width, Goten.height, this);
	    }
	}




	if (Goten.health > 0 && Trunks.health > 0)
	{
	    if (hit == true)
	    {
		if (Goten.direction.equals ("right"))
		{
		    g.drawImage (gotenPunch, Goten.x, Goten.y, Goten.width, Goten.height, this);
		}
		else
		{
		    g.drawImage (gotenPunchLeft, Goten.x, Goten.y, Goten.width, Goten.height, this);
		}
	    }


	    else if (kick == true)
	    {
		if (Goten.direction.equals ("right"))
		{
		    g.drawImage (gotenRapidKick, Goten.x, Goten.y, Goten.width + 10, Goten.height, this);
		}
		else
		{
		    g.drawImage (gotenRapidKickLeft, Goten.x, Goten.y, 55, Goten.height, this);
		}
	    }

	    else if (kameAttack == true)
	    {
		if (secondsElapsed < 1)
		{
		    g.drawImage (kameStance, Goten.x, Goten.y, Goten.width, Goten.height, this);
		}
		else if (secondsElapsed >= 1 && secondsElapsed <= 1.5)
		{
		    g.drawImage (gotenBlast, Goten.x, Goten.y, Goten.width, Goten.height, this);
		    g.drawImage (kamehameha, Goten.x + Goten.width, Goten.y + 2, 300, Goten.height, this);
		}
	    }
	}



	g.drawImage (gotenPortrait, 10, 10, 40, 56, this);
	g.drawImage (trunksPortrait, 390, 10, 40, 56, this);
	g.setColor (Color.green);
	g.fillRect (60, 20, Goten.health, 15);
	g.fillRect (280 + (100 - Trunks.health), 20, Trunks.health, 15);
	g.drawString (Integer.toString (Trunks.health), 300, 15);
	g.drawString (Integer.toString (Goten.health), 100, 15);
	g.fillRect (60, 20, Goten.health, 15);
	g.fillRect (280 + (100 - Trunks.health), 20, Trunks.health, 15);
	g.setColor (Color.blue);
	g.fillRect (60, 40, Goten.ki, 10);
	g.fillRect (280 + (100 - Trunks.ki), 40, Trunks.ki, 10);

	if (introScreen == true)
	{
	    g.drawImage (opening, 0, 0, 450, 250, this);
	    g.drawImage (logo, 450 - 220 - 10, 250 - 94 - 10, 220, 94, this);
	}


	if (instructionsScreen == true)
	{
	    g.setColor (Color.black);
	    g.fillRect (0, 0, appletsize_x, appletsize_y);
	}






    } // paint method


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
} // Haha class


