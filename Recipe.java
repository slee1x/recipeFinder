/*
 * This class stores the recipe
 */
package recipefinder;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author slee
 */
public class Recipe{
  public final static String NAME = "name";
  public final static String INGREDIENTS = "ingredients";

  private String name;
  private List<Ingredient> ingredientArray;

  public Recipe(String name){
    this.name = name;
  }

  /*
   * Below defines setter and getter
   */

  /*
   * @return ingredientArray
   */
  public List<Ingredient> getIngredientArray() {
    return ingredientArray;
  }

  /*
   * @param ingredientArray
   */
  public void setIngredientArray(List<Ingredient> ingredientArray) {
    this.ingredientArray = ingredientArray;
  }

  /*
   * @return name
   */
  public String getName() {
    return name;
  }

  /*
   * @param name
   */
  public void setName(String name) {
    this.name = name;
  }

}
