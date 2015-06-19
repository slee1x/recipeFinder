/*
 * This class stores the fridge item which is Ingredient + a Use-By date
 */
package recipefinder;

import java.util.Date;

/**
 *
 * @author slee
 */
public class FridgeIngredient extends Ingredient{
  private Date useBy;
  
  public FridgeIngredient(String item, int amount, Unit unit, Date useBy){
    super(item, amount, unit);
    this.useBy = useBy;
  }

  /*
   * Below defines setter and getter
   */

  /*
   * @return useBy
   */
  public Date getUseBy() {
    return useBy;
  }

  /*
   * @param useBy
   */
  public void setUseBy(Date useBy) {
    this.useBy = useBy;
  }

}
