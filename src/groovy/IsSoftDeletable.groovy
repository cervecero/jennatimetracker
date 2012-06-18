
/**
 * @author Leandro Larroulet
 * Date: 17/09/2010
 * Time: 15:37:00
 */
public class IsSoftDeletable {

  public boolean deleted = false

   static hibernateFilters = {
      enabledFilter(condition:'deleted=0', default:true)
    }

}