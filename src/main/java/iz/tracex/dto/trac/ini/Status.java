package iz.tracex.dto.trac.ini;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author izumi_j
 *
 */
public enum Status {
    ASSIGNED, BACKED, CLOSED, EVALUATED, EVALUATING, NEW, SUBMITTED, SUSPENDED;

    public static Status find(String name) {
        for (Status e : Status.values()) {
            if (StringUtils.equalsIgnoreCase(name, e.toString())) {
                return e;
            }
        }
        return null;
    }

    /**
     * 引数のステータスの時に利用可能なステータスを返す。<br>
     * ※Tracのワークフロー制御
     *
     * @param status
     * @return status array
     */
    public static String[] availableValuesAsString(Status status) {
        switch (status) {
        case NEW:
            return new String[] { NEW.toString(), ASSIGNED.toString() };
        case ASSIGNED:
            return new String[] { ASSIGNED.toString(), SUBMITTED.toString(), NEW.toString(), SUSPENDED.toString() };
        case SUBMITTED:
            return new String[] { SUBMITTED.toString(), EVALUATING.toString(), ASSIGNED.toString() };
        case EVALUATING:
            return new String[] { EVALUATING.toString(), EVALUATED.toString(), BACKED.toString(), SUSPENDED.toString() };
        case EVALUATED:
            return new String[] { EVALUATED.toString(), CLOSED.toString(), EVALUATING.toString() };
        case CLOSED:
            return new String[] { CLOSED.toString(), BACKED.toString() };
        case BACKED:
            return new String[] { BACKED.toString(), ASSIGNED.toString() };
        case SUSPENDED:
            return new String[] { SUSPENDED.toString(), ASSIGNED.toString() };
        default:
            throw new IllegalArgumentException("Unknown Status " + status);
        }
    }

    @Override
    public String toString() {
        return StringUtils.lowerCase(name());
    }
}
