/*
 * This class provides methods to read json recipe file.
 * 
 * Two getRecipe methods are provided: 1 takes in a JSON file path
 * which calls the other one which takes in a JSONArray.
 * 
 * The reason for 2 getRecipe methods is for flexibility, in that if the JSON
 * data is to be passed in, instead of reading from a file, the 2nd getRecipe method
 * can be used directly.
 * 
 * This class uses json-simple-1.1.1.jar which can be downloaded here 
 * https://code.google.com/p/json-simple/
 */
package recipefinder;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author slee
 */
public class RecipeReader {
  
  /*
   * This method reads the json recipe file and returns an array of Recipe
   * I have decided to use JSON.simple 3rd party library.  Google GSON is
   * another to be considered which can convert objects to/from JSON.
   * For a comparison of differnet JSON libraries, go here 
   * http://www.rojotek.com/blog/2009/05/07/a-review-of-5-java-json-libraries/
   * There are also performance review on these libraries
   * 
   * @param recipeJsonFilePath
   * @return recipeArray
   */
  public List<Recipe> getRecipe(String recipeJsonFilePath){
    List<Recipe> recipeArray = null;
    
    JSONParser parser = new JSONParser();

    try {
      //Read json recipe file
      JSONArray jsonRecipeArray = (JSONArray) parser.parse(new FileReader(recipeJsonFilePath));
      
      recipeArray = getRecipe(jsonRecipeArray);
    } catch (IOException ex) {
      Logger.getLogger(RecipeFinder.class.getName()).log(Level.SEVERE, null, ex);
      System.out.println("Error reading file " + recipeJsonFilePath);
      System.exit(4);
    } catch (org.json.simple.parser.ParseException ex) {
      Logger.getLogger(RecipeFinder.class.getName()).log(Level.SEVERE, null, ex);
      System.out.println("Error pasring json at position " + parser.getPosition());
      System.out.println(parser);
      System.exit(5);
    }
    
    return recipeArray;
  }
  

  /*
   * This method returns an array of recipe for the given jsonRecipeArray
   * 
   * @param jsonRecipeArray
   * @return recipeArray
   */
  public List<Recipe> getRecipe(JSONArray jsonRecipeArray){
    List<Recipe> recipeArray = new ArrayList<Recipe>();
    
    //Loop through the whole json array; for each recipe build the recipe array
    for (Object recipeObject : jsonRecipeArray){
      JSONObject jsonRecipeObject = (JSONObject) recipeObject;

      String name = (String) jsonRecipeObject.get(Recipe.NAME); //Recipe name

      JSONArray jsonIngredientArray = (JSONArray) jsonRecipeObject.get(Recipe.INGREDIENTS); //Recipe ingredient
      List<Ingredient> ingredientArray = buildIngredientArray(jsonIngredientArray);

      Recipe recipe = new Recipe(name);
      recipe.setIngredientArray(ingredientArray);
      recipeArray.add(recipe);
    }
    return recipeArray;
  }

  /*
   * This method retuns an Ingredient array
   * 
   * @param jsonIngredientArray
   * @return ingredientArray
   */
  public List<Ingredient> buildIngredientArray(JSONArray jsonIngredientArray){
    List<Ingredient> ingredientArray = new ArrayList<Ingredient>();

    for (Object ingredientObject : jsonIngredientArray){
      JSONObject jsonIngredientObject = (JSONObject) ingredientObject;

      String item = (String) jsonIngredientObject.get(Ingredient.ITEM);
      int amount = Integer.parseInt( (String) jsonIngredientObject.get(Ingredient.AMOUNT));
      String unitStr = (String) jsonIngredientObject.get(Ingredient.UNIT);  //Store unit string from JSON object
                                                                            //for easy debugging in case unit
                                                                            //is not a type in Unit enum
      Unit unit = null;
      try{
        unit = Unit.valueOf(unitStr);
      } catch (IllegalArgumentException ex){
        Logger.getLogger(RecipeFinder.class.getName()).log(Level.SEVERE, null, ex);
        System.out.println("Error pasring unit enum type: " + unitStr);
        System.exit(6);
      } catch (NullPointerException ex){
        Logger.getLogger(RecipeFinder.class.getName()).log(Level.SEVERE, null, ex);
        System.out.println("Error pasring unit, check if unit is empty: " + unitStr);
        System.exit(7);
      }

      ingredientArray.add(new Ingredient(item, amount, unit));
    }
    return ingredientArray;
  }
  
}
