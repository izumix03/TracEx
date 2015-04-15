package iz.tracex.dto.trac.ini;

import org.apache.commons.lang3.StringUtils;

/**
 * 既存Tracのカスタムフィールドに存在して、TracExで使用していないフィールド群。
 *
 * @author izumi_j
 *
 */
public enum DisabledTicketCustomFields {
    ACTION_PLAN(""),
    ATTACHED_REPORT_URL(""),
    AT_SUPORT_TROUBLE_NO(""),
    CLIENT(""),
    COMPLETE("0"),
    DESIGN_REVIEW_LEVEL("なし"),
    DEVELOPMENT_STATUS("整理中"),
    DOCTEXT_CHECK("---"),
    DOC_CHECK_MEMO(""),
    DOC_TESTER_MEMO(""),
    DUE_ASSIGN(""),
    DUE_CHECK(""),
    FIX_DIFFICULTY_LEVEL("---"),
    FIX_PRIORITY("---"),
    GUARANTEE_OPERATION("---"),
    GUARANTEE_OPERATION_LEVEL("なし"),
    KAIJI_KUBUN("---"),
    MANHOUR_RESULTS(""),
    MANHOUR_SCHEDULE(""),
    MIDTERM_REVIEW_LEVEL("なし"),
    OPERATION_PREP("---"),
    PENALTY_COUNT(""),
    PRE_RELEASED("0"),
    REVIEW_RECORD(""),
    RE_OPERATION_COUNT(""),
    SCENARIO_NO(""),
    SHIPMENT_DAY(""),
    TEST_VERSION("1.0"),
    ZOO_HUGUAI_NO(""), ;

    private final String def;

    private DisabledTicketCustomFields(String def) {
        this.def = def;
    }

    @Override
    public String toString() {
        return StringUtils.lowerCase(name());
    }

    public String def() {
        return def;
    }
}
