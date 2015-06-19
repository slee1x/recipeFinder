/*
 * Test RecipeFinder
 */
package recipefinder;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.Date;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author slee
 */
public class RecipeFinderTest {
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

  public RecipeFinderTest() {
  }

  @Before
  public void setUp() {
    System.setOut(new PrintStream(outContent, true));
  }

  @After
  public void tearDown() {
    System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
  }


  /**
   * Test of main method, of class RecipeFinder.
   */
  @Test
  public void testMainError() {
    String[] args = new String[]{"fridge.csv","recipes.json"};

    RecipeFinder.main(args);
    String actual = outContent.toString().trim();
    String expected = "Order Takeout";

    Assert.assertEquals(expected, actual);
  }

  /**
   * Test of recipeFinder.findRecipe by changing the date
   * so a valid recipe is found
   */
  @Test
  public void testChangedDate() {
    String fridgeCsvFilePath = "fridge.csv";
    String recipeJsonFilePath = "recipes.json";

    // Find the recipe
    RecipeFinder recipeFinder = new RecipeFinder();
    Calendar cal = Calendar.getInstance();
    cal.set(2013, Calendar.JANUARY, 9);
    Date today = cal.getTime();

    recipeFinder.findRecipe(fridgeCsvFilePath, recipeJsonFilePath, today);

    String actual = outContent.toString().trim();
    String expected = "salad sandwich";

    Assert.assertEquals(expected, actual);
  }


}
