
/**
 * Created by IntelliJ IDEA.
 * User: Lea
 * Date: 17/09/2010
 * Time: 15:37:00
 * To change this template use File | Settings | File Templates.
 */
public class IsSoftDeletable {

  public boolean deleted = false

   static hibernateFilters = {
      enabledFilter(condition:'deleted=0', default:true)
    }

}