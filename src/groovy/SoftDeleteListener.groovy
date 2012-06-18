import org.hibernate.event.DeleteEvent;
import org.hibernate.event.def.DefaultDeleteEventListener
import org.hibernate.event.EventSource
import org.hibernate.persister.entity.EntityPersister
import org.hibernate.engine.PersistenceContext
import java.text.SimpleDateFormat;

/**
 * @author Leandro Larroulet
 * Date: 17/09/2010
 * Time: 10:42:24
 */
public class SoftDeleteListener extends DefaultDeleteEventListener {


	@Override
    public void onDelete(DeleteEvent event, Set transientEntities){
        def objectToDelete= event.getObject();

        objectToDelete.deleted = true

        Date date = new Date()
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_hhmm")

        try {
          objectToDelete.name+="_"+sdf.format(date)
        } catch (Exception ex){
          // This Object doesn't have a name. So... don't update it :)
        }


        try {
          if (objectToDelete.class == User.class)
            objectToDelete.account+="_"+sdf.format(date)
        } catch (Exception ex){
          // This Object doesn't have a name. So... don't update it :)
        }


        final EventSource source = event.getSession();
        final PersistenceContext persistenceContext = source.getPersistenceContext();
        final EntityPersister persister;
        Object entity = persistenceContext.unproxyAndReassociate( event.getObject() );


        persister = source.getEntityPersister( event.getEntityName(), entity );

        cascadeBeforeDelete( source, persister, entity, null, transientEntities );
		cascadeAfterDelete( source, persister, entity, transientEntities );


    }
}

