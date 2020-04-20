//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.company.framework.reportPortalFiles;

import com.epam.reportportal.listeners.ListenerParameters;
import com.epam.ta.reportportal.ws.model.FinishExecutionRQ;
import com.epam.ta.reportportal.ws.model.FinishTestItemRQ;
import com.epam.ta.reportportal.ws.model.StartTestItemRQ;
import io.reactivex.Maybe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Launch {
    static final Logger LOGGER = LoggerFactory.getLogger(Launch.class);
    private final ListenerParameters parameters;
    public static final Launch NOOP_LAUNCH = new Launch(new ListenerParameters()) {
        public Maybe<String> start() {
            return Maybe.empty();
        }

        public void finish(FinishExecutionRQ rq) {
        }

        public Maybe<String> startTestItem(StartTestItemRQ rq) {
            return Maybe.empty();
        }

        public Maybe<String> startTestItem(Maybe<String> parentId, StartTestItemRQ rq) {
            return Maybe.empty();
        }

        public Maybe<String> startTestItem(Maybe<String> parentId, Maybe<String> retryOf, StartTestItemRQ rq) {
            return Maybe.empty();
        }

        @Override
        public void finishTestItem(Maybe<String> var1, FinishTestItemRQ var2, String message) {

        }

        public void finishTestItem(Maybe<String> itemId, FinishTestItemRQ rq) {
        }
    };

    Launch(ListenerParameters parameters) {
        super();
        this.parameters = parameters;
    }

    public abstract Maybe<String> start();

    public abstract void finish(FinishExecutionRQ var1);

    public abstract Maybe<String> startTestItem(StartTestItemRQ var1);

    public abstract Maybe<String> startTestItem(Maybe<String> var1, StartTestItemRQ var2);

    public abstract Maybe<String> startTestItem(Maybe<String> var1, Maybe<String> var2, StartTestItemRQ var3);

    /// Modified for defect type /////////////////////////////////////////////////////////////////
    //public abstract void finishTestItem(Maybe<String> var1, FinishTestItemRQ var2);
    public abstract void finishTestItem(Maybe<String> var1, FinishTestItemRQ var2, String tag); //added
    /// Modified for defect type /////////////////////////////////////////////////////////////////

    public ListenerParameters getParameters() {
        return this.parameters;
    }
}

