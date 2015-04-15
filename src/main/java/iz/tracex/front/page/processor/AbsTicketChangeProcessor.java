package iz.tracex.front.page.processor;

import iz.tracex.base.TracExUtils;
import iz.tracex.dao.TicketChangeDao;
import iz.tracex.dto.trac.Ticket_Change;
import iz.tracex.dto.trac.translate.TicketCustom;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

class AbsTicketChangeProcessor <T>{
	
	private  Class<T> type;
	@SuppressWarnings("unchecked")
	protected  AbsTicketChangeProcessor(T... e) {
		Class<T> type = (Class<T>) e.getClass().getComponentType();
	    this.type = type;
	}

	protected  Class<T> getType() {
		return type;
	}
	
	/**
	 * 
	 * @param t
	 * @param tOld
	 * @param tc
	 * @param tcOld
	 * @param comment
	 * @param userId
	 * @param id
	 */
	void insertChanges(T t, T tOld, TicketCustom tc, TicketCustom tcOld, String comment, String userId, long id) {
        final TicketChangeDao changeDao = new TicketChangeDao();

        final long time = TracExUtils.nowAsUnixTime();
		Class<T> clazz = getType();
		
        // T(Ticket or Doc) changes
        for (Field f : clazz.getDeclaredFields()) {
            try {
                f.setAccessible(true);
                final Object oldvalue = f.get(tOld);
                final Object newvalue = f.get(t);
                if (!isEquals(oldvalue, newvalue)) {
                    changeDao.insert(makeChange(id, time, f.getName(), Objects.toString(oldvalue, null),
                            Objects.toString(newvalue, null), userId));
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }

        // TicketCustom changes
        final Map<String, Pair<String, String>> customChanges = new HashMap<>();
        for (Map.Entry<String, String> nameValue : tcOld.nameValues()) {
            // old -> new
            final String oldvalue = nameValue.getValue();
            final String newvalue = tc.getValue(nameValue.getKey());
            if (!isEquals(oldvalue, newvalue)) {
                customChanges.put(nameValue.getKey(), new ImmutablePair<String, String>(oldvalue, newvalue));
            }
        }
        for (Map.Entry<String, String> nameValue : tc.nameValues()) {
            // new -> old
            final String oldvalue = tcOld.getValue(nameValue.getKey());
            final String newvalue = nameValue.getValue();
            if (!isEquals(oldvalue, newvalue)) {
                customChanges.put(nameValue.getKey(), new ImmutablePair<String, String>(oldvalue, newvalue));
            }
        }
        for (Map.Entry<String, Pair<String, String>> customChange : customChanges.entrySet()) {
            changeDao.insert(makeChange(id, time, customChange.getKey(), customChange.getValue().getLeft(),
                    customChange.getValue().getRight(), userId));
        }

        // comment
        int latestCommentSeq = changeDao.selectLatestCommentSeqOf(id);
        changeDao.insert(makeChange(id, time, "comment", String.valueOf(++latestCommentSeq),
        StringUtils.isNotBlank(comment) ? comment : null, userId));	
        
    }

	private static Ticket_Change makeChange(long id, long time, String field, String oldvalue, String newvalue, String userId) {
        final Ticket_Change c = new Ticket_Change();
        c.setTicket(id);
        c.setTime(time);
        c.setAuthor(userId);
        c.setField(field);
        c.setOldvalue(oldvalue);
        c.setNewvalue(newvalue);
        return c;
    }
	
	private static boolean isEquals(Object a, Object b){
    	return ((a == null||a.toString().length()==0) && (b == null||b.toString().length()==0))
    			|| (a != null && b != null && (a == b)) 
    			|| (a != null && b != null && a.equals(b));
    }
	
	/**
	 * コメントだけinsert(コピー用)
	 * @param originId
	 * @param newId
	 * @param comment
	 */
	public static void insertCopyComment(long originId, long newId, String userId){
		final TicketChangeDao changeDao = new TicketChangeDao();
		int latestCommentSeq = changeDao.selectLatestCommentSeqOf(originId);
		final long time = TracExUtils.nowAsUnixTime();
		
		String copiedMsg               = String.format("このチケットを#%dとしてコピーしました", newId);
		String createdByCopyMsg = String.format("このチケットは#%dからコピーで生成されました", originId);
		
		changeDao.insert(makeChange(originId, time, "comment", String.valueOf(++latestCommentSeq), copiedMsg, userId));
		changeDao.insert(makeChange(newId, time, "comment", "1", createdByCopyMsg, userId));		
	}
	
}

