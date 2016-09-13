//read in nodes and edges of network

void readData()
{
  // Read nodes
  String[] textLines = loadStrings("nodes.txt");
  bands = new Band[textLines.length];
  
  for(int i=0; i<textLines.length;i++)
  {
    String[] tokens = split(textLines[i],TAB);
    String id = tokens[0];
    String name = tokens[1];
    float x = float(tokens[2]);
    float y = float(tokens[3]);
    bands[i] = new Band(id,name,x,y);
    pViewer.addNode(bands[i]);
  }
  
  // Read edges
  String[] edgeText = loadStrings("edges.txt");
  
  for(int i=0;i<edgeText.length;i++)
  {
    String[] tokens = split(edgeText[i],TAB);
    int start = int(tokens[0]);
    int end = int(tokens[1]);
    int numPlayed = int(tokens[2]);
    
    pViewer.addEdge(new PlayedWith(bands[start],bands[end],numPlayed),2000);
    pViewer.addSpring(bands[start],bands[end],1800,1);
  }
  
  // populate table of festivals artist has performed in
  festPlayed = new Table("festplayed.txt");
}
