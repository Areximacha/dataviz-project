import org.gicentre.utils.network.*;
import traer.animation.*;
import traer.physics.*;

// interactive network sketch representing bands that played together in festivals during 2011
// J Tang, 2012

private ParticleViewer<Band,PlayedWith> pViewer;
private Band[] bands;
private Band selected;
private PlayedWith[] playedWith;
Table festPlayed; // table for fastivals bands have played in
PFont agencyBold, agencyReg; // imported fonts
PImage[] bandPics; //array for band images


void setup()
{
  size(800,680);
  smooth();
  textAlign(LEFT,TOP);
  agencyBold = loadFont("AgencyFB-Bold-25.vlw"); //bold font
  agencyReg = loadFont("AgencyFB-Reg-25.vlw"); //regular font
  
  bandPics = new PImage[182];  //create and populate array of artists images
  for (int i = 0; i<bandPics.length;i++)
  {
    bandPics[i] = loadImage(i+".jpg");
  }
  
  pViewer = new ParticleViewer(this,width,height);
  readData();
}

void draw()
{
  background(24,34,62);
  pViewer.draw();
  
  //Draw the interaction information box on mouse click
  if (mousePressed == true && selected !=null)
  {
    fill(0,100);
    noStroke();
    
    String bandid = selected.bandid; // band id for referencing festPlayed table
    int picid = int(bandid); // convert string to int for referencing bandPics array
    String festivals = festPlayed.getString(bandid,1);
    textFont(agencyReg);
    
    // adaptive box with for longer artist names
    float nWidth = max(150, textWidth(selected.bandName)+15);
    
    //box position depending on mouse position      
    if(mouseX > width-nWidth && mouseY > height-250)
    {
      rect(mouseX,mouseY,-nWidth,-250);
      fill(255);
      text(selected.bandName,mouseX-(nWidth-10),mouseY-240);
      fill(0,100);
      
      rect(mouseX-(nWidth-13),mouseY-197,60,60); //shadow for the image, 3px offset
      image(bandPics[picid],mouseX-(nWidth-10),mouseY-200,60,60); //band image
      
      fill(255);
      text("Festivals Played",mouseX-(nWidth-10),mouseY-130);
      textSize(16);
      text(festivals,mouseX-(nWidth-10),mouseY-100,130,100);
    }
    else if(mouseY > height-250)
    {
      rect(mouseX,mouseY,nWidth,-250);
      fill(255);
      text(selected.bandName,mouseX+10,mouseY-240);
      
      fill(0,100);
      rect(mouseX+13,mouseY-197,60,60);
      image(bandPics[picid],mouseX+10,mouseY-200,60,60);
      
      fill(255);
      text("Festivals Played",mouseX+10,mouseY-130);
      textSize(16);
      text(festivals,mouseX+10,mouseY-100,130,100);
    }
    else if(mouseX > width-nWidth)
    {
      rect(mouseX,mouseY,-nWidth,250);
      fill(255);
      text(selected.bandName,mouseX-(nWidth-10),mouseY+10);
      
      fill(0,100);
      rect(mouseX-(nWidth-13),mouseY+53,60,60);
      image(bandPics[picid],mouseX-(nWidth-10),mouseY+50,60,60);
      
      fill(255);
      text("Festivals Played",mouseX-(nWidth-10),mouseY+120);
      textSize(16);
      text(festivals,mouseX-(nWidth-10),mouseY+150,130,100);
    }
    else
    {
      rect(mouseX,mouseY,nWidth,250);
      fill(255);
      text(selected.bandName,mouseX+10,mouseY+10);
      
      fill(0,100);
      rect(mouseX+13,mouseY+53,60,60);
      image(bandPics[picid],mouseX+10,mouseY+50,60,60);
      
      fill(255);
      text("Festivals Played",mouseX+10,mouseY+120);
      textSize(16);
      text(festivals,mouseX+10,mouseY+150,130,100);
    }
  }
  
  //  draw title and instruction boxes
  fill(0,100);
  noStroke();
  rect(0,0,width,40);
  rect(0,height-40,width,40);
  fill(255,200);
  textFont(agencyBold);
  //textSize(14);
  text("How Music Artists are Related by the Festivals they Played in 2011",10,5);
  textFont(agencyReg);
  String clickText = "Click anywhere to begin";
  textSize(20);
  text(clickText,(width-textWidth(clickText))-5,10);
  text("Zoom = Shift + LClick/MouseWheel      Pan = Shift + RClick",10,height-35);
  String reset = "Hit 'R' to reset view";
  text(reset,(width-textWidth(clickText))-5,height-35);
}

// interact with nodes and pass back selected node and out edges info
public void mousePressed()
{
  pViewer.selectNearestWithMouse();
  
  selected = pViewer.getSelectedNode();
  
  if (selected !=null)
  {
      selected.isSelected = true;
      for(Edge edge : selected.getOutEdges())
      {
         ((PlayedWith)edge).isSelected = true;
      }
  }
}

// unselect nodes and edges
public void mouseReleased()
{
  selected = pViewer.getSelectedNode();
  if(selected != null)
  {
    selected.isSelected = false;
    for(Edge edge : selected.getOutEdges())
      {
         ((PlayedWith)edge).isSelected = false;
      }
  }
  pViewer.dropSelected();
}

//reset zoompan
void keyPressed()
{
    
  if (key=='r')
  {
     pViewer.resetView();
  }
}
