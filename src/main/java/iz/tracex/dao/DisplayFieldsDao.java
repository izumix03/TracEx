package iz.tracex.dao;

import iz.tracex.dto.search.Criteria;
import iz.tracex.dto.search.CriteriaCustom;
import iz.tracex.dto.search.SortValue;
import iz.tracex.dto.trac.ini.DisplayFields;
import iz.tracex.dto.trac.ini.Status;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DisplayFieldsDao extends AbstractDao<DisplayFields> {
	// 一度作成したら変更しない
	private String	            selectBase;
	private static final Logger	logger	= LoggerFactory.getLogger(DisplayFieldsDao.class);
	
	@Override
	protected Class<DisplayFields> entityClass() {
		return DisplayFields.class;
	}
	
	public List<DisplayFields> selectBy(final Criteria c) {
		final StopWatch sw = new StopWatch();
		sw.start();
		
		// 検索条件を組み立てる
		final StringBuilder where = new StringBuilder("WHERE id > 0");
		
		// status
		final Status[] status = c.getCheckedStatus();
		if (status.length > 0) {
			final String s = arrayToCommaText(status, true);
			if (StringUtils.isNotEmpty(s)) {
				where.append(" AND status IN (").append(s).append(")");
			}
		}
		// milestone
		if (c.milestones.size() > 0) {
			final String s = listStringToCommaText(c.milestones, true);
			if (StringUtils.isNotEmpty(s)) {
				where.append(" AND milestone IN (").append(s).append(")");
			}
		}
		// owner
		if (c.owners.size() > 0) {
			final String s = listStringToCommaText(c.owners, true);
			if (StringUtils.isNotEmpty(s)) {
				where.append(" AND owner IN (").append(s).append(")");
			}
		}
		// component
		if (c.components.size() > 0) {
			final String s = listStringToCommaText(c.components, true);
			if (StringUtils.isNotEmpty(s)) {
				where.append(" AND component IN (").append(s).append(")");
			}
		}
		// keywords
		if (c.keywords.size() > 0) {
			for (int i = 0; i < c.keywords.size(); i++) {
				if (i == 0) {
					where.append(" AND (keywords like '%").append(c.keywords.get(i)).append("%' ");
				} else {
					where.append(" OR keywords like '%").append(c.keywords.get(i)).append("%' ");
				}
			}
			
			where.append(")");
		}
		
		// summary
		if (c.summary.size() > 0) {
			for (int i = 0; i < c.summary.size(); i++) {
				if (i == 0) {
					where.append(" AND (summary like '%").append(c.summary.get(i)).append("%' ");
				} else {
					where.append(" OR summary like '%").append(c.summary.get(i)).append("%' ");
				}
			}
			where.append(")");
		}
		// description
		if (c.description.size() > 0) {
			for (int i = 0; i < c.description.size(); i++) {
				if (i == 0) {
					where.append(" AND (description like '%").append(c.description.get(i)).append("%' ");
				} else {
					where.append(" OR description like '%").append(c.description.get(i)).append("%' ");
				}
			}
			where.append(")");
		}
		
		// id
		if (c.ticketIds.size() > 0) {
			final String s = listStringToCommaText(c.ticketIds, true);
			if (StringUtils.isNotEmpty(s)) {
				where.append(" AND id IN (").append(s).append(")");
			}
		}
		// ids from elastic
		if (c.idsFromElastic.size() > 0){
			final String s = listStringToCommaText(c.idsFromElastic, true);
			if (StringUtils.isNotEmpty(s)) {
				where.append(" AND id IN (").append(s).append(")");
			}
		}
		
		// product-files
		if (c.productFiles.size() > 0) {
			for (int i = 0; i < c.productFiles.size(); i++) {
				if (i == 0) {
					where.append(" AND id in (SELECT ticket_id FROM ccm.t_product_file where path like '%").append(c.productFiles.get(i)).append("' ");
				} else {
					where.append(" OR path like '%").append(c.description.get(i)).append("' ");
				}
			}
			where.append(")");
		}
		
		// ticket_custom
		Class<?> clazz = CriteriaCustom.class;
		for (Field f : clazz.getDeclaredFields()) {
			try {
				f.setAccessible(true);
				if (f.getType().isArray()) {
					// 配列
					boolean hasVal = false;
					String[] o = (String[]) f.get(c.custom);
					if (o == null) {
						continue;
					}
					for (String s : o) {
						if (s != null && StringUtils.isNotEmpty(s)) {
							hasVal = true;
						}
					}
					if (hasVal && o.length != 0) {
						where.append(" AND id IN (SELECT ticket FROM ticket_custom WHERE name='").append(f.getName()).append("' ");
						final String s = arrayToCommaText(o, true);
						if (StringUtils.isNotEmpty(s)) {
							where.append(" AND value IN (").append(s).append("))");
						}
					}
				} else {
					try {
						// 単一String
						String o = (String) f.get(c.custom);
						if (o != null && StringUtils.isNotEmpty(o)) {
							where.append(" AND id IN (SELECT ticket FROM ticket_custom WHERE name='").append(f.getName()).append("' and value ='")
							        .append(o).append("')");
						}
					} catch (ClassCastException ce) {
						// list
						boolean hasVal = false;
						@SuppressWarnings("unchecked")
						List<String> o = (List<String>) f.get(c.custom);
						if (o == null) {
							continue;
						}
						// patricalMatchOrConcat
						if (c.condition.patricalMatchOrConcat.contains(f.getName().toLowerCase())) {
							int cnt = o.size();
							for (int i = 0; i < cnt; i++) {
								if (i == 0) {
									where.append(" AND id In (SELECT ticket FROM ticket_custom WHERE name='").append(f.getName())
									        .append("' AND (value like '%").append(o.get(i)).append("%'");
								} else {
									where.append(" OR value like '%").append(o.get(i)).append("%' ");
								}
								
								if (i == cnt - 1) {
									where.append("))");
								}
							}
							
							continue;
						}
						
						// in
						for (String s : o) {
							if (s != null && StringUtils.isNotEmpty(s)) {
								hasVal = true;
							}
						}
						if (hasVal && o.size() != 0) {
							where.append(" AND id IN (SELECT ticket FROM ticket_custom WHERE name='").append(f.getName()).append("' ");
							final String s = listStringToCommaText(o, true);
							if (StringUtils.isNotEmpty(s)) {
								where.append(" AND value IN (").append(s).append("))");
							}
						}
						
					}
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new IllegalStateException(e);
			}
		}
		
		// クエリ実行
		final String sql = safeJoin(buildSelectBase(c.displayFields.keySet(), c.sortFields), where.toString(), buildOrderBy(c.sortFields),
		        "LIMIT 500");
		final List<DisplayFields> result = query(sql);
		sw.stop();
		logger.debug("time={}ms", sw.getTime());
		return result;
	}
	
	private String buildOrderBy(Map<SortValue, String> sortValues) {
		if (sortValues.isEmpty()) {
			return "ORDER BY id";
		}
		
		final StringBuilder result = new StringBuilder();
		for (SortValue s : sortValues.keySet()) {
			if (result.length() == 0) {
				result.append("ORDER BY ");
			} else {
				result.append(", ");
			}
			result.append(s.field);
			if (s.desc) {
				result.append(" DESC");
			}
		}
		result.append(", id");
		
		return result.toString();
	}
	
	protected String buildSelectBase(Set<String> set, Map<SortValue, String> sortFields) {
		List<String> sortFieldNames = new ArrayList<String>();
		for (SortValue s : sortFields.keySet()) {
			sortFieldNames.add(s.field);
		}
		List<String> fields = new ArrayList<String>();
		fields.addAll(sortFieldNames);
		fields.addAll(set);
		
		final StringBuilder sb = new StringBuilder();
		sb.append("SELECT ");
		Class<?> clazz = DisplayFields.class;
		Class<?> sClazz = clazz.getSuperclass();
		
		Field[] ticketFields = sClazz.getDeclaredFields();
		for (int i = 0; i < ticketFields.length; i++) {
			Field f = ticketFields[i];
			if (i != 0) {
				sb.append(", ");
			}
			sb.append(f.getName());
		}
		
		Field[] ticketCustomFields = clazz.getDeclaredFields();
		for (Field f : ticketCustomFields) {
			if (fields.contains(f.getName())) {
				sb.append(", ");
				sb.append(" (select value from ticket_custom where ticket = id and name='");
				sb.append(f.getName());
				sb.append("') as ");
				sb.append(f.getName());
			}
		}
		
		sb.append(" FROM TICKET ");
		selectBase = sb.toString();
		return selectBase;
	}
	
}
