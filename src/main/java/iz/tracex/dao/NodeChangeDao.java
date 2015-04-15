package iz.tracex.dao;

import iz.tracex.dto.trac.Node_Change;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * NodeChangeのDao
 * @author izumikawa_t
 */
public final class NodeChangeDao extends AbstractDao<Node_Change> {
	private static final String sql = "SELECT * FROM node_change WHERE rev = ?"; 
	
	/**
	 * リビジョン指定で変更ファイルを取得
	 * @param rev
	 * @return nodeChangeのリスト
	 */
	public List<Node_Change> selectChangesBy(final long rev) {
			return query(sql, rev);
	}
	
	@Override
	protected Class<Node_Change> entityClass() {
		return Node_Change.class;
	}

	protected class NodeChangeProcessor implements DtoProcessor<Node_Change>{
		@Override
		public Node_Change toDto(ResultSet rs) throws SQLException {
			DtoMapper<Node_Change>  dtoMapper = new DtoMapper<Node_Change>();
			return dtoMapper.toDto(rs);
		}
	}
	
}
