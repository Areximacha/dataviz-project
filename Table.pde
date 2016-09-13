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
  int getRowCount() 
  {
    return rowCount;
  }
  
  // Find a row by its name, returns -1 if no row found
  int getRowIndex(String name)
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
  String getRowName(int row)
  {
    return getStringAt(row, 0);
  }

  // Reports the item at the given row and column location as a String.
  String getStringAt(int rowIndex, int column)
  {
    if (rowIndex < 0)
    {
      println("Unknown row index: "+rowIndex+" when querying table.");
      return "";
    }
    return data[rowIndex][column];
  }

  // Reports the item in the given column and row with the given name as a String.
  String getString(String rowName, int column)
  {
    return getStringAt(getRowIndex(rowName), column);
  }

  // Reports the item in the given column and row with the given name as a whole number.
  int getInt(String rowName, int column) 
  {
    return parseInt(getString(rowName, column));
  }

   // Reports the item in the given row and column as a whole number.
  int getIntAt(int rowIndex, int column) 
  {
    return parseInt(getStringAt(rowIndex, column));
  }

  // Reports the item in the given column and row with the given name as a decimal number.
  float getFloat(String rowName, int column)
  {
    return parseFloat(getString(rowName, column));
  }

  // Reports the item in the given row and column as a decimal number.
  float getFloatAt(int rowIndex, int column)
  {
    return parseFloat(getStringAt(rowIndex, column));
  }
  
  // Sets the name of the given row. This item will be stored in column 0 of the row.
  void setRowName(int row, String what)
  {
    data[row][0] = what;
  }

  // Sets the value at the given row and column as the given String.
  void setStringAt(int rowIndex, int column, String what) 
  {
    data[rowIndex][column] = what;
  }

  // Sets the value at the given column in the row with the given name as the given String.
  void setString(String rowName, int column, String what)
  {
    int rowIndex = getRowIndex(rowName);
    data[rowIndex][column] = what;
  }

  // Sets the value at the given row and column as the given whole number.
  void setIntAt(int rowIndex, int column, int what)
  {
    data[rowIndex][column] = str(what);
  }

  // Sets the value at the given column in the row with the given name as the given whole number.
  void setInt(String rowName, int column, int what) 
  {
    int rowIndex = getRowIndex(rowName);
    data[rowIndex][column] = str(what);
  }

  // Sets the value at the given row and column as the given decimal number.
  void setFloatAt(int rowIndex, int column, float what)
  {
    data[rowIndex][column] = str(what);
  }

  // Sets the value at the given column in the row with the given name as the given decimal number.
  void setFloat(String rowName, int column, float what)
  {
    int rowIndex = getRowIndex(rowName);
    data[rowIndex][column] = str(what);
  }
  
  // Writes this table as a TSV file
  void write(PrintWriter writer) 
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
