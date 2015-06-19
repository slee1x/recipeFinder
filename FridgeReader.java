/*
 * This class defines the method to read fridge csv file
 */
package recipefinder;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author slee
 */
public class FridgeReader {
  
  /*
   * This method reads a fridge csv file and returns an array of FridgeIngredient
   * There ia also a 3rd party library to read csv file, e.g. CSVRead.
   * Since reading CSV is simple, I decided not to use a 3rd party library.
   * 
   * @param fridgeCsvFilePath
   * @return fridgeArray
   */
  public List<FridgeIngredient> getFridgeIngredientFromFile(String fridgeCsvFilePath){
    final String SPLITTER = ",";  //CSV delimiter
    final String DATE_FORMAT = "dd/MM/yyyy";
    List<FridgeIngredient> fridgeArray = new ArrayList<FridgeIngredient>();

    Scanner scanner = null;

    try {
      //Read fridge CSV file
      scanner = new Scanner(new File(fridgeCsvFilePath));
      
      Scanner dataLine = null;
      DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
      
      // For each line in the file
      while(scanner.hasNextLine()){
        dataLine = new Scanner(scanner.nextLine());
        dataLine.useDelimiter(SPLITTER);
        
        // Parse each item into a FridgeIngredient, then biuld the array of FridgeIngredient
        while(dataLine.hasNext()){
          String item = dataLine.next();
          int amount = Integer.parseInt(dataLine.next());
          String unit = dataLine.next();
          Date date = null;
          
          try {
            date = dateFormat.parse(dataLine.next());
          } catch (ParseException ex) {
            Logger.getLogger(RecipeFinder.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error pasring date");
            System.exit(2);
          }
          
          FridgeIngredient fridgeIngredient = null;
          try {
            fridgeIngredient = new FridgeIngredient(item, amount, Unit.valueOf(unit), date);
          } catch (IllegalArgumentException ex){
            Logger.getLogger(RecipeFinder.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error pasring unit " + unit);
            System.exit(2);
          }
          fridgeArray.add(fridgeIngredient);
        }
      }
      scanner.close();
    } catch (FileNotFoundException ex) {
      Logger.getLogger(RecipeFinder.class.getName()).log(Level.SEVERE, null, ex);
      System.out.println("Error reading file " + fridgeCsvFilePath);
      System.exit(3);
    }
    return fridgeArray;
  }
  
}
