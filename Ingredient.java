/*
 * This class sotres the ingredient
 */
package recipefinder;

/**
 * This class stores the ingredient item, amount and unit of 
 * @author slee
 */
public class Ingredient {
  // Define constants
  public final static String ITEM = "item";
  public final static String AMOUNT = "amount";
  public final static String UNIT = "unit";
  
  private String item;
  private int amount;
  private Unit unit;
  
  /*
   * @param item
   * @param amount
   * @param unit
   */
  public Ingredient(String item, int amount, Unit unit){
    this.item = item;
    this.amount = amount;
    this.unit = unit;
  }

  /*
   * Below defines setter and getter
   */

  /*
   * @return amount
   */
  public int getAmount() {
    return amount;
  }

  /*
   * @param amount
   */
  public void setAmount(int amount) {
    this.amount = amount;
  }

  /*
   * @return item
   */
  public String getItem() {
    return item;
  }

  /*
   * @param item
   */
  public void setItem(String item) {
    this.item = item;
  }

  /*
   * @return unit
   */
  public Unit getUnit() {
    return unit;
  }

  /*
   * @param unit
   */
  public void setUnit(Unit unit) {
    this.unit = unit;
  }
}
