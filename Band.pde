// Customised extend of the Node class

class Band extends Node
{
  String bandName;
  boolean isSelected;
  String bandid;


  Band(String id, String name, float x, float y)
  {
    super(x, y);
    bandName = name;
    bandid = id;
    isSelected = false;
  }

  void draw(PApplet sketch, float x, float y)
  {
    noStroke();
    
    //highlight selected band node and remove it's name
    if (isSelected)
    {
      for(int i=100;i>0;i--)
      {
        fill(255,20);
        ellipse(x,y,i,i);
      }
                  
    }
    else // else draw the node as normal with name
    {
      for(int i=100;i>0;i--)
      {
        fill(255,2);
        ellipse(x,y,i,i);
      }

        fill(255,100);
        textSize(80);
        text(bandName, x, y);
    
    }
  
    //Make the bandnames dim a little when mouse is clicked.
    if (mousePressed != true)
     {
      fill(255,100);
      textSize(80);
      text(bandName, x, y);
     }

  }
}
