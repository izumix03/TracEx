package iz.tracex.dto.search;

import java.util.List;
import java.util.Map;

/**
 * 検索結果。チケット一覧はグルーピングして表示する。
 *
 * @author izumi_j
 *
 */
public final class SearchResult {
    public static final String NON_GRP = "nonGrp";

    public final String groupedBy;
    public final List<Map<String, String>> tickets;

    public SearchResult(String groupedBy, List<Map<String, String>> tickets) {
        super();
        this.groupedBy = groupedBy;
        this.tickets = tickets;
    }
}
