//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.company.framework.reportPortalFiles;


import com.epam.reportportal.exception.ReportPortalException;
import com.epam.reportportal.listeners.ListenerParameters;
import com.epam.reportportal.service.ReportPortalClient;
import com.epam.reportportal.utils.LaunchFile;
import com.epam.reportportal.utils.RetryWithDelay;
import com.epam.ta.reportportal.ws.model.EntryCreatedRS;
import com.epam.ta.reportportal.ws.model.ErrorType;
import com.epam.ta.reportportal.ws.model.FinishExecutionRQ;
import com.epam.ta.reportportal.ws.model.FinishTestItemRQ;
import com.epam.ta.reportportal.ws.model.OperationCompletionRS;
import com.epam.ta.reportportal.ws.model.StartTestItemRQ;
import com.epam.ta.reportportal.ws.model.issue.Issue;
import com.epam.ta.reportportal.ws.model.launch.StartLaunchRQ;
import com.epam.ta.reportportal.ws.model.launch.StartLaunchRS;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import rp.com.google.common.base.Preconditions;
import rp.com.google.common.cache.CacheBuilder;
import rp.com.google.common.cache.CacheLoader;
import rp.com.google.common.cache.LoadingCache;
import rp.com.google.common.collect.Lists;

public class LaunchImpl extends Launch {
    private static final Function<EntryCreatedRS, String> TO_ID = new Function<EntryCreatedRS, String>() {
        public String apply(EntryCreatedRS rs) throws Exception {
            return rs.getId();
        }
    };
    private static final int ITEM_FINISH_MAX_RETRIES = 10;
    private static final int ITEM_FINISH_RETRY_TIMEOUT = 10;
    private static final String NOT_ISSUE = "NOT_ISSUE";
    private final ReportPortalClient rpClient;
    private final LoadingCache<Maybe<String>, LaunchImpl.TreeItem> QUEUE = CacheBuilder.newBuilder().build(new CacheLoader<Maybe<String>, LaunchImpl.TreeItem>() {
        public LaunchImpl.TreeItem load(Maybe<String> key) throws Exception {
            return new LaunchImpl.TreeItem();
        }
    });
    private Maybe<String> launch;
    private boolean rerun;

    LaunchImpl(final ReportPortalClient rpClient, ListenerParameters parameters, final StartLaunchRQ rq) {
        super(parameters);
        this.rpClient = (ReportPortalClient)Preconditions.checkNotNull(rpClient, "RestEndpoint shouldn't be NULL");
        Preconditions.checkNotNull(parameters, "Parameters shouldn't be NULL");
        if (!parameters.isRerun()) {
            Maybe<StartLaunchRS> launchPromise = Maybe.defer(new Callable<MaybeSource<? extends StartLaunchRS>>() {
                public MaybeSource<? extends StartLaunchRS> call() throws Exception {
                    return rpClient.startLaunch(rq).doOnSuccess(LoggingCallback.logCreated("launch")).doOnError(LoggingCallback.LOG_ERROR).doOnSuccess(new Consumer<StartLaunchRS>() {
                        public void accept(StartLaunchRS rs) throws Exception {
                            System.setProperty("rp.launch.id", rs.getId());
                        }
                    });
                }
            }).doOnError(LoggingCallback.LOG_ERROR).subscribeOn(Schedulers.io()).cache();
            this.launch = launchPromise.map(new Function<StartLaunchRS, String>() {
                public String apply(StartLaunchRS rs) throws Exception {
                    return rs.getId();
                }
            });
            LaunchFile.create(rq.getName(), launchPromise);
        } else {
            this.launch = LaunchFile.find(rq.getName());
            this.rerun = true;
        }

    }

    LaunchImpl(ReportPortalClient rpClient, ListenerParameters parameters, Maybe<String> launch) {
        super(parameters);
        this.rpClient = (ReportPortalClient)Preconditions.checkNotNull(rpClient, "RestEndpoint shouldn't be NULL");
        Preconditions.checkNotNull(parameters, "Parameters shouldn't be NULL");
        this.launch = launch.subscribeOn(Schedulers.io()).cache();
    }

    public synchronized Maybe<String> start() {
        this.launch.subscribe();
        return this.launch;
    }

    public synchronized void finish(final FinishExecutionRQ rq) {
        Completable finish = Completable.concat(((LaunchImpl.TreeItem)this.QUEUE.getUnchecked(this.launch)).getChildren()).andThen(this.launch.flatMap(new Function<String, Maybe<OperationCompletionRS>>() {
            public Maybe<OperationCompletionRS> apply(String id) throws Exception {
                return LaunchImpl.this.rpClient.finishLaunch(id, rq).doOnSuccess(LoggingCallback.LOG_SUCCESS).doOnError(LoggingCallback.LOG_ERROR);
            }
        })).doOnComplete(new Action() {
            public void run() throws Exception {
                LaunchImpl.this.rpClient.close();
            }
        }).ignoreElement().cache();

        try {
            finish.timeout((long)this.getParameters().getReportingTimeout(), TimeUnit.SECONDS).blockingGet();
        } catch (Exception var4) {
            LOGGER.error("Unable to finish launch in ReportPortal", var4);
        }

    }

    public Maybe<String> startTestItem(final StartTestItemRQ rq) {
        Maybe<String> testItem = this.launch.flatMap(new Function<String, Maybe<String>>() {
            public Maybe<String> apply(String id) throws Exception {
                rq.setLaunchId(id);
                return LaunchImpl.this.rpClient.startTestItem(rq).doOnSuccess(LoggingCallback.logCreated("item")).doOnError(LoggingCallback.LOG_ERROR).map(LaunchImpl.TO_ID);
            }
        }).doOnError(LoggingCallback.LOG_ERROR).cache();
        testItem.subscribeOn(Schedulers.io()).subscribe();
        ((LaunchImpl.TreeItem)this.QUEUE.getUnchecked(testItem)).addToQueue(testItem.ignoreElement());
        return testItem;
    }

    public Maybe<String> startTestItem(final Maybe<String> parentId, Maybe<String> retryOf, final StartTestItemRQ rq) {
        return retryOf.flatMap(new Function<String, Maybe<String>>() {
            public Maybe<String> apply(String s) throws Exception {
                return LaunchImpl.this.startTestItem(parentId, rq);
            }
        }).cache();
    }

    public Maybe<String> startTestItem(final Maybe<String> parentId, final StartTestItemRQ rq) {
        if (null == parentId) {
            return this.startTestItem(rq);
        } else {
            Maybe<String> itemId = this.launch.flatMap(new Function<String, Maybe<String>>() {
                public Maybe<String> apply(final String launchId) throws Exception {
                    return parentId.flatMap(new Function<String, MaybeSource<String>>() {
                        public MaybeSource<String> apply(String parentIdx) throws Exception {
                            rq.setLaunchId(launchId);
                            Launch.LOGGER.debug("Starting test item..." + Thread.currentThread().getName());
                            return LaunchImpl.this.rpClient.startTestItem(parentIdx, rq).doOnSuccess(LoggingCallback.logCreated("item")).doOnError(LoggingCallback.LOG_ERROR).map(LaunchImpl.TO_ID);
                        }
                    });
                }
            }).doOnError(LoggingCallback.LOG_ERROR).cache();
            itemId.subscribeOn(Schedulers.io()).subscribe();
            ((LaunchImpl.TreeItem)this.QUEUE.getUnchecked(itemId)).withParent(parentId).addToQueue(itemId.ignoreElement());
            LoggingContext.init(itemId, this.rpClient, this.getParameters().getBatchLogsSize(), this.getParameters().isConvertImage());
            return itemId;
        }
    }

        /// Modified for defect type ////////////////////////////////////////////////////////////////////////////////
//      public void finishTestItem(final Maybe<String> itemId, final FinishTestItemRQ rq) {
        public void finishTestItem(final Maybe<String> itemId, final FinishTestItemRQ rq, String tagName) {
        Preconditions.checkArgument(null != itemId, "ItemID should not be null");
//        if ("SKIPPED".equals(rq.getStatus()) && !this.getParameters().getSkippedAnIssue()) {
//            Issue issue = new Issue();
//            issue.setIssueType("NOT_ISSUE");
//            rq.setIssue(issue);
//        }
        if ("SKIPPED".equals(rq.getStatus())) {
            Issue issue = new Issue();
            switch (tagName) {
                case "@Broken":
                    issue.setIssueType("nd_qe9e1a7qeqt1");
                    break;
                case "@NotImplementedYet":
                    issue.setIssueType("nd_t81afck935tc");
                    break;
                case "@OutOfScope":
                    issue.setIssueType("nd_1jenr7i09qhis");
                    break;
            }
            //issue.setIssueType(message);
            rq.setIssue(issue);
        }
        /// Modified for defect type ////////////////////////////////////////////////////////////////////////////////

        ((LaunchImpl.TreeItem)this.QUEUE.getUnchecked(this.launch)).addToQueue(LoggingContext.complete());
        LaunchImpl.TreeItem treeItem = (LaunchImpl.TreeItem)this.QUEUE.getIfPresent(itemId);
        if (null == treeItem) {
            treeItem = new LaunchImpl.TreeItem();
            LOGGER.error("Item {} not found in the cache", itemId);
        }

        Completable finishCompletion = Completable.concat(treeItem.getChildren()).andThen(itemId.flatMap(new Function<String, Maybe<OperationCompletionRS>>() {
            public Maybe<OperationCompletionRS> apply(String itemId) throws Exception {
                return LaunchImpl.this.rpClient.finishTestItem(itemId, rq).retry(new RetryWithDelay(new Predicate<Throwable>() {
                    public boolean test(Throwable throwable) throws Exception {
                        return throwable instanceof ReportPortalException && ErrorType.FINISH_ITEM_NOT_ALLOWED.equals(((ReportPortalException)throwable).getError().getErrorType());
                    }
                }, 10L, TimeUnit.SECONDS.toMillis(10L))).doOnSuccess(LoggingCallback.LOG_SUCCESS).doOnError(LoggingCallback.LOG_ERROR);
            }
        })).doAfterSuccess(new Consumer<OperationCompletionRS>() {
            public void accept(OperationCompletionRS operationCompletionRS) throws Exception {
                LaunchImpl.this.QUEUE.invalidate(itemId);
            }
        }).ignoreElement().cache();
        finishCompletion.subscribeOn(Schedulers.io()).subscribe();
        Maybe<String> parent = treeItem.getParent();
        if (null != parent) {
            ((LaunchImpl.TreeItem)this.QUEUE.getUnchecked(parent)).addToQueue(finishCompletion);
        } else {
            ((LaunchImpl.TreeItem)this.QUEUE.getUnchecked(this.launch)).addToQueue(finishCompletion);
        }

    }

    public boolean isRerun() {
        return this.rerun;
    }

    static class TreeItem {
        private Maybe<String> parent;
        private List<Completable> children = new CopyOnWriteArrayList();

        TreeItem() {
        }

        synchronized LaunchImpl.TreeItem withParent(Maybe<String> parent) {
            this.parent = parent;
            return this;
        }

        LaunchImpl.TreeItem addToQueue(Completable completable) {
            this.children.add(completable);
            return this;
        }

        List<Completable> getChildren() {
            return Lists.newArrayList(this.children);
        }

        synchronized Maybe<String> getParent() {
            return this.parent;
        }
    }
}

