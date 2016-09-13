// Customised version of the Edge class

class PlayedWith extends Edge
{
  int numPlayedWith;
  boolean isSelected;
  
  PlayedWith(Band band1, Band band2, int numPlayed)
  {
    super(band1,band2);
    numPlayedWith = numPlayed;
  }  
  
  void draw(PApplet applet, float p1x, float p1y, float p2x, float p2y)
  {
    //highlight all edges that are connected to the selected node
    if (isSelected)
    {
      for(int i=10;i>0;i--)
      {
        stroke(255,numPlayedWith*50);
        strokeWeight(10);
        drawCurve(p1x,p1y,p2x,p2y);
      }
    }
    else
    {
      stroke(255,numPlayedWith*50);  //transparancy depends on strength of connection
      strokeWeight(numPlayedWith*2);  //thickness of edge depends on strength of connection
      drawCurve(p1x,p1y,p2x,p2y);
    }
  }
  
  //draw curve method
  void drawCurve(float x1,float y1, float x2, float y2)
  {  
    float curveAngle = radians(-90);
    float x = (x1-x2)/4f;
    float y = (y1-y2)/4f;

    float cx = x2 + x*cos(curveAngle) - y*sin(curveAngle);
    float cy = y2 + y*cos(curveAngle) + x*sin(curveAngle);
    bezier(x1,y1,cx,cy,x2,y2,x2,y2);
  }
}
