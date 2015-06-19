/*
 * This class finds a recipe with the smallest useBy date, given a list of items 
 * in the fridge (presented as a csv list), and a collection of recipes 
 * (a collection of JSON formatted recipes).
 * 
 * A date (today) is pased in the search recipe method to elminate fridge items
 * that has expired.  This date is used to compare to the use-by date of the item.
 * Passing in this date makes the search more flexible.
 * 
 * Assumptions:
 *
 * 1)
 * Both the fridge items and recipes are stored in files.
 * 
 * However, care is taken to separate the recipe search from reading the file 
 * content so that the search method can be called directly, say by a batch job,
 * that passes an existing csv array and recipe array.
 * 
 *   findRecipe - read the files and call doSearch() to find the recipe
 *   doSearch - seach the recipe; this method can be called directly if the
 *              csv arary and recipe array are already avaliable
 * 
 * 2)
 * Fridge items can be duplicated, e.g. bread can have different number of slices with different dates.
 * e.g.
 *  bread,2,slices,25/12/2014
 *  bread,3,slices,23/12/2014
 * 
 * The example above indicates 2 bread items with different amount and expiry dates.
 * If a recipe calls for 5 slices of bread, then each individual item will not be
 * enough to cover the ingredients.  But combined amount is sufficient.
 * 
 * Therefore, before finding the recipe, same fridge items need to be combined with 
 * the sum of the amount first and record the minimum useBy date which will 
 * be used to determine the recipe to be selected when multiples of them are found.
 * 
 * After combining all the same fridge items, we will end up with a unique ingredient list which can be
 * represented by a map instead of an array for faster comparison to the recipe ingredient, using the item
 * as the key and the value being FridgeIngredient.

 * To combine similar fridge items, there is an assumption that if the fridge 
 * items have the same name, as in above example, then the unit is the same as well.
 * 
 */
package recipefinder;

import java.util.Date;
import java.util.List;

/**
 *
 * @author slee
 */
public class RecipeFinder {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    if (args.length != 2){
      System.out.println("Usage: recipeFinder <fridge.csv> <recipes.json>");
      System.exit(1);
    }
    
    // Get file paths to the fridge csv and recipe json
    String fridgeCsvFilePath = args[0];
    String recipeJsonFilePath = args[1];
    
    // Find the recipe
    RecipeFinder recipeFinder = new RecipeFinder();
    Date today = new Date();  // This date is used to determine if a fridge item "has expired"
                              // by comparing this date to the useBy date in fridge item
                              // Passing in this date field makes the program more flexible
    recipeFinder.findRecipe(fridgeCsvFilePath, recipeJsonFilePath, today);
  }
  
  /*
   * This method finds the receipe for a given fridge csv file and a recipe json file
   * by 
   * 1) first reading the files into the appropriate data structures and 
   * 2) searches the recipe with the fridge items.
   * 
   * The 2 steps above are kept separate to allow the data structures being
   * used directly to find the recipe.  This will provide flexibility in case the
   * source data is not provided by reading files, e.g. getting the values
   * from the DB or a web page.
   * 
   * @param fridgeCsvFilePath
   * @param recipesJsonFilePath
   * @param someDate
   */
  public void findRecipe(String fridgeCsvFilePath, String recipeJsonFilePath, Date someDate){
    FridgeReader fridgeReader = new FridgeReader();
    List<FridgeIngredient> fridgeIngredientArray = fridgeReader.getFridgeIngredientFromFile(fridgeCsvFilePath);
    
    RecipeReader recipeReader = new RecipeReader();
    List<Recipe> recipeArray = recipeReader.getRecipe(recipeJsonFilePath);
    
    this.doSearch(fridgeIngredientArray, recipeArray, someDate);
  }
  
  /*
   * Given an array of fridge items and recipes find the recipe with all the ingredients
   * avaliable in the fridge items that have the smallest useBy date
   * 
   * @param fridgeIngredientArray
   * @param recipeArray
   * @param someDate
   */
  public void doSearch(List<FridgeIngredient> fridgeIngredientArray, List<Recipe> recipeArray, Date someDate){
    final String ORDER_TAKEOUT = "Order Takeout";
    
    SearchRecipe searchRecipe = new SearchRecipe();
    Recipe recipe = searchRecipe.search(fridgeIngredientArray, recipeArray, someDate);
    
    if (null == recipe){
      System.out.println(ORDER_TAKEOUT);
    }else{
      System.out.println(recipe.getName());
    }
  }
  
}
