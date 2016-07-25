public class Fighter
{

    int x;
    int y;
    int health;
    int width;
    int height;
    int ki;
    String direction; //whether they are facing right or left

    public Fighter (int x, int y, int width, int height, String direction)
    {
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;
	this.health = 100;
	this.ki = 100;
	this.direction = direction;
    }


    public void moveUp (int speed)
    {
	y = y - speed;
    }


    public void moveDown (int speed)
    {
	y = y + speed;
    }


    public void moveLeft (int speed)
    {
	x = x - speed;
	
    }


    public void moveRight (int speed)
    {
	x = x + speed;
    }


    public String moveAway (Fighter Goten, int speed)
    {
	if (Goten.x + Goten.width <= this.x)
	{
	    this.moveRight (speed);
	    this.direction = "right";
	    return "right";
	}
	else if (Goten.x > this.x + this.width)
	{
	    this.moveLeft (speed);
	    this.direction = "left";
	    return "left";
	}
	else
	{
	    this.moveUp (speed);
	    this.direction = "up";
	    return "up";
	}

    }


    //causes a fighter to move towards the other fighter, takes in speed and information on collision
    public String moveCloser (Fighter Goten, int speed, boolean collision,int closeness)
    {
	String trunksMove = "";
	if (Goten.x + Goten.width < this.x)
	{
	    if (((Goten.x + Goten.width - 5 < this.x && Goten.x + Goten.width + 5 > this.x) == false) || collision == false)
	    {
		this.moveLeft (speed);
		trunksMove = "left";
		this.direction = "left";
	    }
	    else
	    {
		trunksMove = "";
	    }
	}


	else if (Goten.x >= this.x + this.width)
	{
	    if ((this.x + this.width > Goten.x - 5 && this.x + this.width < Goten.x + 5) == false || (collision == false))
	    {
		this.moveRight (speed);
		trunksMove = "right";
		this.direction = "right";
	    }
	    else
	    {
		trunksMove = "";
	    }
	}


	else if (Goten.y + closeness < this.y)
	{
	    this.moveUp (speed);
	    trunksMove = "up";

	}


	else if (Goten.y - closeness > this.y)
	{

	    this.moveDown (speed);
	    trunksMove = "down";

	}


	else
	{
	    trunksMove = "";
	}

	return trunksMove;




    }


    public void hover (int runCounter)
    {
	if (runCounter <= 20 && runCounter > 0)
	{
	    this.moveDown (1);
	}
	else if (runCounter > 20 && runCounter <= 40)
	{
	    this.moveUp (1);
	}
	else
	{
	    runCounter = 0;
	}

    }





}


