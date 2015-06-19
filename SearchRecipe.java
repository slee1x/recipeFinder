/*
 * This class provides search functionalies to return a recipe with the smallest useBy date.
 * 
 * Fridge items can be duplicated, e.g. bread can have different number of slices with different dates.
 * e.g.
 *  bread,2,slices,25/12/2014
 *  bread,3,slices,23/12/2014
 * 
 * The example above indicates 2 bread items with different amount and expiry dates.
 * If a recipe calls for 5 slices of bread, then each individual item will not be
 * enough to cover the ingredients.  But combined amount is sufficient.
 * 
 * There is an assumption that if the fridge item ahs the same name, as in above example,
 * then the unit is the same as well.
 * 
 * Therefore, same items need to be combined with the sum of the amount and record the minimum useBy date
 * which will be used to determine the recipe to be selected when multiples of them are found.
 * 
 * After combining all the same fridge ingredient, we will end up with a unique ingredient list which can be
 * represented by a map instead of an array for faster comparison to the recipe ingredient, using the item
 * as the key and the value being FridgeIngredient.
 */
package recipefinder;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author slee
 */
public class SearchRecipe {
  
  /*
   * This method returns a Recipe with the smallest useBy date given an array of FridgeIngredient and Recipe.
   * If no recipe is found, returns null.
   * 
   * @param fridgeIngredientArray
   * @param recipeArray
   * @param someDate
   * @return recipe
   */
  public Recipe search(List<FridgeIngredient> fridgeIngredientArray, List<Recipe> recipeArray, Date someDate){
    Recipe recipe;
    
    // Get FridgeIngredient where its useBy date is equal to or greater than someDate
    // The result will contain only usable FridgeIngredient that have not expired by someDate
    List<FridgeIngredient> fridgeIngredientFilteredByDate = filterFridgeIngredientByDate(fridgeIngredientArray, someDate);
    
    // Fridge items can be duplicated, e.g. bread can have different number of slices with different dates.
    // Therefore, same items need to be combined with the sum of the amount and record the minimum useBy date
    // which will be used to determine the recipe to be selected when multiples of them are found.
    // After combining all the same fridge ingredient, we will end up with a unique ingredient list which can be
    // represented by a map instead of an array for faster comparison to the recipe ingredient, using the item
    // as the key and the value being FridgeIngredient.
    Map<String, FridgeIngredient> combinedFridgeIngredientMap = this.combineFridgeIngredient(fridgeIngredientFilteredByDate);
    
    // Get the recipe with the smallest useBy date from FridgeIngredient
    recipe = matchRecipe(combinedFridgeIngredientMap, recipeArray);
    return recipe;
  }
  
  /*
   * This method returns an array of FridgeIngredient where its useBy date is equal to or greater
   * than someDate.
   * 
   * @param fridgeIngredientArray
   * @param someDate
   * @return fridgeIngredientFilteredByDate
   */
  private List<FridgeIngredient> filterFridgeIngredientByDate(List<FridgeIngredient> fridgeIngredientArray, Date someDate){
    List<FridgeIngredient> fridgeIngredientFilteredByDate = new ArrayList< FridgeIngredient>();
    
    // Loop through the entire FridgeIngredient array and only build the FridgeIngredient
    // with useBy date greater or equal to someDate
    for (FridgeIngredient fi : fridgeIngredientArray){
      int compairedResult = fi.getUseBy().compareTo(someDate);
      
      // If FridgeIngredient's date is after or equal to someDate, then the ingredient can be used
      // and add the FridgeIngredient to the useable array
      if (compairedResult > 0 || compairedResult == 0){
        fridgeIngredientFilteredByDate.add(fi);
      }
    }
    return fridgeIngredientFilteredByDate;
  }
  
  /*
   * This mehod returns a FridgeIngredient map in this format <item, FridgeIngredient>.  The FridgeIngredient
   * in the map will contain the combined amount for the item and the minimum useBy date.
   * More details:
   * Fridge items can be duplicated, e.g. bread can have different number of slices with different dates.
   * Therefore, same items need to be combined with the sum of the amount and record the minimum useBy date
   * which will be used to determine the recipe to be selected when multiples of them are found.
   * After combining all the same fridge ingredient, we will end up with a unique ingredient list which can be
   * represented by a map instead of an array for faster comparison to the recipe ingredient, using the item
   * as the key and the value being FridgeIngredient.
   * 
   * @param fridgeIngredientArray
   * @return combinedFridgeIngredientMap
   */
  private Map<String, FridgeIngredient> combineFridgeIngredient(List<FridgeIngredient> fridgeIngredientArray){
    Map<String, FridgeIngredient> combinedFridgeIngredientMap = new HashMap<String, FridgeIngredient>();
    
    //Loop through all the fridge ingredients and stores the maximum item amount
    //and the minimum useBy date in a map
    for (FridgeIngredient fi : fridgeIngredientArray){
      // If fridgeIngredientMap already has the key (fi's item) then need to 
      // 1) calculate the new amount by adding the existing amount from the 
      // FridgeIngredient in the map with the amount from the FridgeIngredient in the array.
      // 2) calculate the minimum date between that in FridgeIngredient in the map
      // and that in FridgeIngredient array
      // 3) put the new FridgeIngredient in the map
      if (combinedFridgeIngredientMap.containsKey(fi.getItem())){
        // Compute the new amount
        int existingAmount = combinedFridgeIngredientMap.get(fi.getItem()).getAmount();
        int newAmount = existingAmount + fi.getAmount();
        
        // Determine the minimum useBy date
        Date existingDate = combinedFridgeIngredientMap.get(fi.getItem()).getUseBy();
        Date minDate;
        if (fi.getUseBy().before(existingDate)){
          // If FridgeIngredient's date in the array is after that in FridgeIngredient's map
          minDate = fi.getUseBy();
        }else{
          minDate = existingDate;
        }
        
        FridgeIngredient newFridgeIngredient = new FridgeIngredient(fi.getItem(), newAmount, fi.getUnit(), minDate);
        combinedFridgeIngredientMap.put(fi.getItem(), newFridgeIngredient);
      }else{
        // fridgeIngredientMap does not have the key, add fi to the map
        FridgeIngredient newFridgeIngredient = new FridgeIngredient(fi.getItem(), fi.getAmount(), fi.getUnit(), fi.getUseBy());
        combinedFridgeIngredientMap.put(newFridgeIngredient.getItem(), newFridgeIngredient);
      }
    }
    return combinedFridgeIngredientMap;
  }

  /*
   * This method returns a recipe with the smallest useBy date from FridgeIngredient
   * 
   * Implementation notes:
   * I loop through each recipe from the recipe array and compare the ingredient amount
   * with that in the fridge.  If all the ingredients are found in the fridge and if
   * the ingredients amount are enough, and if the useBy date is the smallest, then
   * "saves" the recipe to be returned.
   * 
   * Alternatively, we can save *all* the matched recipes and useBy in an array and sort the
   * date to get the recipe with the smallest date.
   * 
   * Another alternative is that we can save *all* the recipes in a TreeMap 
   * with a date Comparator, then get the 1st node that contains the recipe.
   * 
   * @param combinedFridgeIngredientMap
   * @param recipeArray
   * @return recipe
   */
  private Recipe matchRecipe(Map<String, FridgeIngredient> combinedFridgeIngredientMap, List<Recipe> recipeArray) {
    Recipe recipe = null;
    Date minDate = new Date(Long.MAX_VALUE),  // Initialize: Set minDate to the largest possible date
         recipeDate = null;
    
    // Loop through all the recipes
    for(Recipe rp : recipeArray){
      // Check if this recipe has all the ingredients from FridgeIngredent
      List<Ingredient> ingredientArray = rp.getIngredientArray();
      recipeDate = getMinDateFromRecipe(combinedFridgeIngredientMap, ingredientArray);
      
      // If recipeDate has a value, this recipe has all the ingredients in the fridge
      // save the recipe
      if (null != recipeDate){
        if (recipeDate.before(minDate)){
          minDate = recipeDate;
          recipe = rp;
        }
      }
    }
    return recipe;
  }

  /*
   * This method returns the minimum date of the FridgeIngredient if combinedFridgeIngredientMap 
   * contains all the ingredients in ingredientArray, otherwise null.
   * 
   * @param combinedFridgeIngredientMap
   * @param ingredientArray
   * @return minDate
   */
  private Date getMinDateFromRecipe(Map<String, FridgeIngredient> combinedFridgeIngredientMap, List<Ingredient> ingredientArray) {
    Date minDate = new Date(Long.MAX_VALUE);  // Initialize: Set minDate to the largest possible date
    
    // Instaed of using an iterator and a while loop to go through
    // ingredientArray, I use a for loop and break
    // because the logic is simple to understand.
    for(Ingredient ig : ingredientArray){
      String item = ig.getItem();
      if (combinedFridgeIngredientMap.containsKey(item)){
        // combinedFridgeIngredientMap has the item in ingredient (Array)
        // Now compare the amount
        if (ig.getAmount() > combinedFridgeIngredientMap.get(item).getAmount()){
          minDate = null; // The amount in combinedFridgeIngredientMap is not enough for the ingredient (Array)
          break;
        }
        
        // Stores the minimum date of FridgeIngredient
        if (combinedFridgeIngredientMap.get(item).getUseBy().before(minDate)){
          minDate = combinedFridgeIngredientMap.get(item).getUseBy();
        }
      }else{
        // combinedFridgeIngredientMap does not have the item in ingredientArray
        minDate = null;
        break;
      }
    }
    return minDate;
  }
  
}
