//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.company.framework.reportPortalFiles;

import com.epam.ta.reportportal.ws.model.EntryCreatedRS;
import com.epam.ta.reportportal.ws.model.OperationCompletionRS;
import io.reactivex.functions.Consumer;

final class LoggingCallback {
    static final Consumer<OperationCompletionRS> LOG_SUCCESS = new Consumer<OperationCompletionRS>() {
        public void accept(OperationCompletionRS rs) throws Exception {
            Launch.LOGGER.debug(rs.getResultMessage());
        }
    };
    static final Consumer<Throwable> LOG_ERROR = new Consumer<Throwable>() {
        public void accept(Throwable rs) throws Exception {
            Launch.LOGGER.error("[{}] ReportPortal execution error", Thread.currentThread().getId(), rs);
        }
    };

    private LoggingCallback() {
    }

    static Consumer<EntryCreatedRS> logCreated(final String entry) {
        return new Consumer<EntryCreatedRS>() {
            public void accept(EntryCreatedRS rs) throws Exception {
                Launch.LOGGER.debug("ReportPortal {} with ID '{}' has been created", entry, rs.getId());
            }
        };
    }
}
