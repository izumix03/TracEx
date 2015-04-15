package iz.tracex.dao;

import iz.tracex.base.jdbc.ConnectionManager;
import iz.tracex.base.jdbc.GeneratedKeysReturnQueryRunner;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author izumi_j
 *
 */
abstract public class AbstractDao<T> {
    private static final Logger logger = LoggerFactory.getLogger(AbstractDao.class);

    /**
     *  @param <T>
     */
    protected interface DtoProcessor<T> {
        T toDto(ResultSet rs) throws SQLException;
    }
    
    protected class DtoMapper<E> {
        private Class<E> type;

        @SuppressWarnings("unchecked")
        protected DtoMapper(E... e) {
            Class<E> type = (Class<E>) e.getClass().getComponentType();
            this.type = type;
        }

        protected Class<E> getType() {
            return type;
        }
        
        @SuppressWarnings("unchecked")
		protected T toDto(ResultSet rs) {
			Class<E> clazz = getType();
			try {
				T c = (T) clazz.newInstance();
				Field[] fields = ((Class<E>) c).getDeclaredFields();
				for (Field f : fields){
					f.setAccessible(true);
					f.set(c, rs.getObject(f.getName()));
				}
				return c;
			} catch (IllegalArgumentException | SQLException | InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
        	return null;
        }
    }
    

    /**
     * @see RowProcessor
     */
    private static final class InternalRowProcessor<T> extends BasicRowProcessor {
        private final DtoProcessor<T> dtoProcessor;

        private InternalRowProcessor(DtoProcessor<T> dtoProcessor) {
            super();
            this.dtoProcessor = dtoProcessor;
        }

        @SuppressWarnings({ "unchecked", "hiding" })
        @Override
        public <T> T toBean(ResultSet rs, Class<T> type) throws SQLException {
            return (T) dtoProcessor.toDto(rs);
        }

        @SuppressWarnings({ "unchecked", "hiding" })
        @Override
        public <T> List<T> toBeanList(ResultSet rs, Class<T> type) throws SQLException {
            List<T> list = new ArrayList<>();
            while (rs.next()) {
                list.add((T) dtoProcessor.toDto(rs));
            }
            return list;
        }
    }

    /**
     * @return class
     */
    abstract protected Class<T> entityClass();

    /**
     * @return custom processor
     */
    protected DtoProcessor<T> getProcessor() {
        return null;
    }

    /**
     * @param columns
     * @param values
     * @return new id
     */
    protected long insert(String[] columns, Object[] values) {
        try {
            final GeneratedKeysReturnQueryRunner qr = new GeneratedKeysReturnQueryRunner();
            final String sql = safeJoin("INSERT INTO", tableName(), "(", arrayToCommaText(columns), ") VALUES (",
                    StringUtils.repeat("?", ",", columns.length), ")");
            logger.debug(sql);
            return qr.insert(ConnectionManager.getConnection(), sql, values);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * @param columns
     * @param values
     * @param whereColumns
     * @param params
     * @return number of updated
     */
    protected int update(String[] columns, Object[] values, String[] whereColumns, Object... params) {
        try {
            final QueryRunner qr = new QueryRunner();
            final StringBuilder sql = new StringBuilder(safeJoin("UPDATE", tableName(), "SET"));
            for (String column : columns) {
                if (ArrayUtils.indexOf(columns, column) > 0) {
                    sql.append(",");
                }
                sql.append(" ").append(column).append(" = ?");
            }
            sql.append(" ").append(buildWhere(whereColumns));
            logger.debug(sql.toString());

            return qr.update(ConnectionManager.getConnection(), sql.toString(), ArrayUtils.addAll(values, params));
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * @param whereColumns
     * @param params
     * @return number of deleted
     */
    protected int delete(String[] whereColumns, Object... params) {
        try {
            final QueryRunner qr = new QueryRunner();
            final String sql = safeJoin("DELETE FROM", tableName(), buildWhere(whereColumns));
            logger.debug(sql);
            return qr.update(ConnectionManager.getConnection(), sql, params);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * @param whereColumns
     * @param params
     * @return single result
     */
    protected T queryForObject(String[] whereColumns, Object... params) {
        try {
            final QueryRunner qr = new QueryRunner();
            final String sql = safeJoin(buildSelectBase(), buildWhere(whereColumns));
            logger.debug(sql);
            return qr.query(ConnectionManager.getConnection(), sql, getHandler(), params);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * SELECT * FROM {table} WHERE {whereColumns} ORDER BY {orderColumns}
     * @param whereColumns
     * @param orderColumns
     * @param params
     * @return result list
     */
    protected List<T> query(String[] whereColumns, String[] orderColumns, Object... params) {
        return query(safeJoin(buildSelectBase(), buildWhere(whereColumns), buildOrderBy(orderColumns)), params);
    }

    /**
     * SELECT * FROM {table} ORDER BY 1
     * @return result list
     */
    protected List<T> query() {
        return query(safeJoin(buildSelectBase(), buildOrderBy("1")));
    }

    /**
     * e.g. SELECT a, b FROM some_table WHERE c = ? AND d = ? ORDER BY e
     * @param sql
     * @param params
     * @return result list
     */
    protected List<T> query(String sql, Object... params) {
        try {
            final QueryRunner qr = new QueryRunner();
            logger.debug(sql);
            return qr.query(ConnectionManager.getConnection(), sql, getListHandler(), params);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * @see #query(String[], String[], Object...)
     */
    protected List<Map<String, Object>> queryMap(String[] whereColumns, String[] orderColumns, Object... params) {
        return queryMap(safeJoin(buildSelectBase(), buildWhere(whereColumns), buildOrderBy(orderColumns)), params);
    }

    /**
     * @see #query()
     */
    protected List<Map<String, Object>> queryMap() {
        return queryMap(safeJoin(buildSelectBase(), buildOrderBy("1")));
    }

    /**
     * @see #query(String, Object...)
     */
    protected List<Map<String, Object>> queryMap(String sql, Object... params) {
        try {
            final QueryRunner qr = new QueryRunner();
            logger.debug(sql);
            return qr.query(ConnectionManager.getConnection(), sql, new MapListHandler(), params);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * @return tableName
     */
    protected String tableName() {
        return StringUtils.lowerCase(entityClass().getSimpleName());
    }

    /**
     * @return SELECT * FROM table
     */
    protected String buildSelectBase() {
        return safeJoin("SELECT * FROM", tableName());
    }

    /**
     * @param whereColumns
     * @return WHERE a = ? AND b = ? AND ...
     */
    protected final String buildWhere(String... whereColumns) {
        final StringBuilder result = new StringBuilder();
        if (whereColumns.length == 0) {
            return result.toString();
        }

        for (String whereColumn : whereColumns) {
            if (result.length() == 0) {
                result.append("WHERE");
            } else {
                result.append("AND");
            }
            result.append(" ").append(whereColumn).append(" = ?");
        }
        return result.toString();
    }

    /**
     * @param orderColumns
     * @return ORDER BY a, b, c...
     */
    protected final String buildOrderBy(String... orderColumns) {
        final StringBuilder result = new StringBuilder();
        if (orderColumns.length == 0) {
            return result.toString();
        }

        result.append("ORDER BY ");
        for (String orderColumn : orderColumns) {
            if (ArrayUtils.indexOf(orderColumns, orderColumn) > 0) {
                result.append(", ");
            }
            result.append(orderColumn);
        }
        return result.toString();
    }

    /**
     * @see #arrayToCommaText(Object[], boolean)
     */
    protected final String arrayToCommaText(Object[] array) {
        return arrayToCommaText(array, false);
    }

    /**
     * @param array
     * @param sigleQuote
     * @return joined with comma like 'a, b, c...'
     */
    protected final String arrayToCommaText(Object[] array, boolean singleQuote) {
        final StringBuilder result = new StringBuilder();
        for (Object o : array) {
            if (o == null || StringUtils.isEmpty(o.toString())) {
                continue;
            }
            if (result.length() > 0) {
                result.append(",");
            }
            if (singleQuote) {
                result.append("'").append(o.toString()).append("'");
            } else {
                result.append(o.toString());
            }

        }
        return result.toString();
    }
    
    protected final String listStringToCommaText(List<String> list, boolean singleQuote){
    	final StringBuilder result = new StringBuilder();
        for (Object o : list) {
            if (o == null || StringUtils.isEmpty(o.toString())) {
                continue;
            }
            if (result.length() > 0) {
                result.append(",");
            }
            if (singleQuote) {
                result.append("'").append(o.toString()).append("'");
            } else {
                result.append(o.toString());
            }

        }
        return result.toString();
    }

    /**
     * @param strings
     * @return joined with space
     */
    protected final String safeJoin(String... strings) {
        return StringUtils.join(strings, " ");
    }

    /**
     * @return handler
     */
    private ResultSetHandler<T> getHandler() {
        final DtoProcessor<T> processor = getProcessor();
        if (processor != null) {
            return new BeanHandler<>(entityClass(), new InternalRowProcessor<T>(processor));
        } else {
            return new BeanHandler<>(entityClass());
        }
    }

    /**
     * @return list handler
     */
    private ResultSetHandler<List<T>> getListHandler() {
        final DtoProcessor<T> processor = getProcessor();
        if (processor != null) {
            return new BeanListHandler<>(entityClass(), new InternalRowProcessor<T>(processor));
        } else {
            return new BeanListHandler<>(entityClass());
        }
    }
}
