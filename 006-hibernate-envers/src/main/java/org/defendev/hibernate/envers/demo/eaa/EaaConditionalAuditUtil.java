package org.defendev.hibernate.envers.demo.eaa;

import org.apache.commons.lang3.ArrayUtils;

import static java.lang.String.format;
import static java.util.Objects.isNull;



public class EaaConditionalAuditUtil {

    private static final String MARK_FOR_AUDIT_PROPERTY_NAME = "lastMarkForAuditZulu";

    public static boolean notMarkedForAudit(Object[] oldState, Object[] newState, String[] propertyNames,
                                            Object entity) {
        final int markPropertyIndex = ArrayUtils.indexOf(propertyNames, MARK_FOR_AUDIT_PROPERTY_NAME);
        if (-1 == markPropertyIndex) {
            throw new IllegalStateException(format("Can not find property %s for entity %s",
                MARK_FOR_AUDIT_PROPERTY_NAME, entity.toString()));
        }

        final Object markOld = oldState[markPropertyIndex];
        final Object markNew = newState[markPropertyIndex];

        if (isNull(markOld)) {
            throw new IllegalStateException(format("markOld is null for entity %s", entity.toString()));
        }

        if (isNull(markNew)) {
            throw new IllegalStateException(format("markNew is null for entity %s", entity.toString()));
        }

        return markNew.equals(markOld);
    }

}
