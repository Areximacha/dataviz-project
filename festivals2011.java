import processing.core.*; 
import processing.xml.*; 

import org.gicentre.utils.network.*; 
import traer.animation.*; 
import traer.physics.*; 

import org.gicentre.utils.colour.*; 
import org.gicentre.utils.io.*; 
import org.gicentre.utils.gui.*; 
import org.gicentre.utils.move.*; 
import org.gicentre.utils.multisketch.*; 
import org.gicentre.utils.stat.*; 
import org.gicentre.utils.*; 
import org.gicentre.utils.network.*; 
import org.gicentre.utils.spatial.*; 
import org.gicentre.utils.geom.*; 
import traer.physics.*; 
import traer.animation.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class festivals2011 extends PApplet {





// interactive network sketch representing bands that played together in festivals during 2011
// J Tang, 2012

private ParticleViewer<Band,PlayedWith> pViewer;
private Band[] bands;
private Band selected;
private PlayedWith[] playedWith;
Table festPlayed; // table for fastivals bands have played in
PFont agencyBold, agencyReg; // imported fonts
PImage[] bandPics; //array for band images


public void setup()
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

public void draw()
{
  background(24,34,62);
  pViewer.draw();
  
  //Draw the interaction information box on mouse click
  if (mousePressed == true && selected !=null)
  {
    fill(0,100);
    noStroke();
    
    String bandid = selected.bandid; // band id for referencing festPlayed table
    int picid = PApplet.parseInt(bandid); // convert string to int for referencing bandPics array
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
public void keyPressed()
{
    
  if (key=='r')
  {
     pViewer.resetView();
  }
}
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

  public void draw(PApplet sketch, float x, float y)
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
  
  public void draw(PApplet applet, float p1x, float p1y, float p2x, float p2y)
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
  public void drawCurve(float x1,float y1, float x2, float y2)
  {  
    float curveAngle = radians(-90);
    float x = (x1-x2)/4f;
    float y = (y1-y2)/4f;

    float cx = x2 + x*cos(curveAngle) - y*sin(curveAngle);
    float cy = y2 + y*cos(curveAngle) + x*sin(curveAngle);
    bezier(x1,y1,cx,cy,x2,y2,x2,y2);
  }
}
// Class for representing and query in a table.
// Written by Ben Fry (http://ben.fry.com/writing/map/Table.pde) with modifications by Jo Wood
// Version 2.2, 27th January, 2012.

class Table
{
  // These are 'global' variables visible to all methods in Table.
  String[][] data;
  int rowCount;
  
  // Create a new empty 10 by 10 table.
  Table() 
  {
    data = new String[10][10];
  }

  // Create a table from the given tab separated values file.
  Table(String filename) 
  {
    String[] rows = loadStrings(filename);
    
    if (rows == null)
    {
      println("Warning: "+filename+" not found. No data loaded into table.");
      return;
    }
    
    data = new String[rows.length][];
    
    for (int i=0; i< rows.length; i++)
    {
      if (trim(rows[i]).length() == 0)
      {
        continue;     // Skip empty rows
      }
      if (rows[i].startsWith("#"))
      {
        continue;    // Skip comment lines
      }
      
      // Split the row on the tabs
      String[] pieces = split(rows[i], TAB);

      // Copy to the table array
      data[rowCount] = pieces;
      rowCount++;
    }
    
    // Resize the 'data' array as necessary
    data = (String[][]) subset(data, 0, rowCount);
  }

  // Reports the number of rows in the table.
  public int getRowCount() 
  {
    return rowCount;
  }
  
  // Find a row by its name, returns -1 if no row found
  public int getRowIndex(String name)
  {
    for (int i=0; i<rowCount; i++)
    {
      if (data[i][0].equals(name)) 
      {
        return i;
      }
    }
    println("No row named '" + name + "' was found");
    return -1;
  }
  
  // Reports the name of the given row (item in first column)
  public String getRowName(int row)
  {
    return getStringAt(row, 0);
  }

  // Reports the item at the given row and column location as a String.
  public String getStringAt(int rowIndex, int column)
  {
    if (rowIndex < 0)
    {
      println("Unknown row index: "+rowIndex+" when querying table.");
      return "";
    }
    return data[rowIndex][column];
  }

  // Reports the item in the given column and row with the given name as a String.
  public String getString(String rowName, int column)
  {
    return getStringAt(getRowIndex(rowName), column);
  }

  // Reports the item in the given column and row with the given name as a whole number.
  public int getInt(String rowName, int column) 
  {
    return parseInt(getString(rowName, column));
  }

   // Reports the item in the given row and column as a whole number.
  public int getIntAt(int rowIndex, int column) 
  {
    return parseInt(getStringAt(rowIndex, column));
  }

  // Reports the item in the given column and row with the given name as a decimal number.
  public float getFloat(String rowName, int column)
  {
    return parseFloat(getString(rowName, column));
  }

  // Reports the item in the given row and column as a decimal number.
  public float getFloatAt(int rowIndex, int column)
  {
    return parseFloat(getStringAt(rowIndex, column));
  }
  
  // Sets the name of the given row. This item will be stored in column 0 of the row.
  public void setRowName(int row, String what)
  {
    data[row][0] = what;
  }

  // Sets the value at the given row and column as the given String.
  public void setStringAt(int rowIndex, int column, String what) 
  {
    data[rowIndex][column] = what;
  }

  // Sets the value at the given column in the row with the given name as the given String.
  public void setString(String rowName, int column, String what)
  {
    int rowIndex = getRowIndex(rowName);
    data[rowIndex][column] = what;
  }

  // Sets the value at the given row and column as the given whole number.
  public void setIntAt(int rowIndex, int column, int what)
  {
    data[rowIndex][column] = str(what);
  }

  // Sets the value at the given column in the row with the given name as the given whole number.
  public void setInt(String rowName, int column, int what) 
  {
    int rowIndex = getRowIndex(rowName);
    data[rowIndex][column] = str(what);
  }

  // Sets the value at the given row and column as the given decimal number.
  public void setFloatAt(int rowIndex, int column, float what)
  {
    data[rowIndex][column] = str(what);
  }

  // Sets the value at the given column in the row with the given name as the given decimal number.
  public void setFloat(String rowName, int column, float what)
  {
    int rowIndex = getRowIndex(rowName);
    data[rowIndex][column] = str(what);
  }
  
  // Writes this table as a TSV file
  public void write(PrintWriter writer) 
  {
    for (int i=0; i<rowCount; i++)
    {
      for (int j=0; j<data[i].length; j++)
      {
        if (j != 0)
        {
          writer.print(TAB);
        }
        if (data[i][j] != null)
        {
          writer.print(data[i][j]);
        }
      }
      writer.println();
    }
    writer.flush();
  }
}
//read in nodes and edges of network

public void readData()
{
  // Read nodes
  String[] textLines = loadStrings("nodes.txt");
  bands = new Band[textLines.length];
  
  for(int i=0; i<textLines.length;i++)
  {
    String[] tokens = split(textLines[i],TAB);
    String id = tokens[0];
    String name = tokens[1];
    float x = PApplet.parseFloat(tokens[2]);
    float y = PApplet.parseFloat(tokens[3]);
    bands[i] = new Band(id,name,x,y);
    pViewer.addNode(bands[i]);
  }
  
  // Read edges
  String[] edgeText = loadStrings("edges.txt");
  
  for(int i=0;i<edgeText.length;i++)
  {
    String[] tokens = split(edgeText[i],TAB);
    int start = PApplet.parseInt(tokens[0]);
    int end = PApplet.parseInt(tokens[1]);
    int numPlayed = PApplet.parseInt(tokens[2]);
    
    pViewer.addEdge(new PlayedWith(bands[start],bands[end],numPlayed),2000);
    pViewer.addSpring(bands[start],bands[end],1800,1);
  }
  
  // populate table of festivals artist has performed in
  festPlayed = new Table("festplayed.txt");
}
  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#F0F0F0", "festivals2011" });
  }
}
