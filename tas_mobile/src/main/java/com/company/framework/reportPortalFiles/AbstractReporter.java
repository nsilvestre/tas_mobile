//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.company.framework.reportPortalFiles;


import com.epam.reportportal.listeners.ListenerParameters;
import com.epam.ta.reportportal.ws.model.FinishExecutionRQ;
import com.epam.ta.reportportal.ws.model.StartTestItemRQ;
import com.epam.ta.reportportal.ws.model.launch.StartLaunchRQ;
import com.epam.ta.reportportal.ws.model.log.SaveLogRQ.File;
import gherkin.formatter.Formatter;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.*;
import io.reactivex.Maybe;
import java.util.ArrayDeque;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rp.com.google.common.base.Supplier;
import rp.com.google.common.base.Suppliers;

public abstract class AbstractReporter implements Formatter, Reporter {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractReporter.class);
    protected static final String COLON_INFIX = ": ";
    private static Scenario scenarioAux;
    protected String currentFeatureUri;
    protected Maybe<String> currentFeatureId;
    protected AbstractReporter.ScenarioContext currentScenario;
    protected String stepPrefix = "";
    private Queue<String> outlineIterations = new ArrayDeque();
    private Boolean inBackground = false;
    private AtomicBoolean finished = new AtomicBoolean(false);
    protected Supplier<Launch> RP = Suppliers.memoize(new Supplier<Launch>() {
        private final Date startTime = Calendar.getInstance().getTime();

        public Launch get() {
            ReportPortal reportPortal = AbstractReporter.this.buildReportPortal();
            ListenerParameters parameters = reportPortal.getParameters();
            StartLaunchRQ rq = new StartLaunchRQ();
            rq.setName(parameters.getLaunchName());
            rq.setStartTime(this.startTime);
            rq.setMode(parameters.getLaunchRunningMode());
            rq.setTags(parameters.getTags());
            rq.setDescription(parameters.getDescription());
            Launch launch = (Launch) reportPortal.newLaunch(rq);
            AbstractReporter.this.finished = new AtomicBoolean(false);
            return launch;
        }
    });

    protected AbstractReporter() {
    }

    protected ReportPortal buildReportPortal() {
        return ReportPortal.builder().build();
    }

    protected static Scenario getScenarioAux() {
        return scenarioAux;
    }

    protected void afterLaunch() {
        FinishExecutionRQ finishLaunchRq = new FinishExecutionRQ();
        finishLaunchRq.setEndTime(Calendar.getInstance().getTime());
        ((Launch)this.RP.get()).finish(finishLaunchRq);
    }

    protected void beforeFeature(Feature feature) {
        StartTestItemRQ rq = new StartTestItemRQ();
        Maybe<String> root = this.getRootItemId();
        rq.setDescription(Utils.buildStatementName(feature, (String)null, ": ", (String)null));
        rq.setName(this.currentFeatureUri);
        rq.setTags(Utils.extractTags(feature.getTags()));
        rq.setStartTime(Calendar.getInstance().getTime());
        rq.setType(this.getFeatureTestItemType());
        if (null == root) {
            this.currentFeatureId = ((Launch)this.RP.get()).startTestItem(rq);
        } else {
            this.currentFeatureId = ((Launch)this.RP.get()).startTestItem(root, rq);
        }

    }

    protected void afterFeature() {
        Utils.finishTestItem((Launch)this.RP.get(), this.currentFeatureId);
        this.currentFeatureId = null;
    }

    protected void beforeScenario(Scenario scenario, String outlineIteration) {
        Maybe<String> id = Utils.startNonLeafNode((Launch)this.RP.get(), this.currentFeatureId, Utils.buildStatementName(scenario, (String)null, ": ", outlineIteration), this.currentFeatureUri + ":" + scenario.getLine(), scenario.getTags(), this.getScenarioTestItemType());
        this.currentScenario = new AbstractReporter.ScenarioContext(id);
        scenarioAux= scenario;
    }

    protected void afterScenario() {
        Utils.finishTestItem((Launch)this.RP.get(), this.currentScenario.getId(), this.currentScenario.getStatus());
        this.currentScenario = null;
    }

    protected abstract void beforeStep(Step var1);

    protected abstract void afterStep(Result var1);

    protected abstract void beforeHooks(Boolean var1);

    protected abstract void afterHooks(Boolean var1);

    protected abstract void hookFinished(Match var1, Result var2, Boolean var3);

    protected void reportResult(Result result, String message) {
        String cukesStatus = result.getStatus();
        String level = Utils.mapLevel(cukesStatus);
        String errorMessage = result.getErrorMessage();
        if (errorMessage != null) {
            Utils.sendLog(errorMessage, level, (File)null);
        }

        if (message != null) {
            Utils.sendLog(message, level, (File)null);
        }

        if (this.currentScenario != null) {
            this.currentScenario.updateStatus(Utils.mapStatus(result.getStatus()));
        }

    }

    protected abstract String getFeatureTestItemType();

    protected abstract String getScenarioTestItemType();

    public void before(Match match, Result result) {
        this.hookFinished(match, result, true);
    }

    public void result(Result result) {
        this.afterStep(result);
        if (!this.inBackground && this.currentScenario.noMoreSteps()) {
            this.beforeHooks(false);
        }

    }

    public void after(Match match, Result result) {
        this.hookFinished(match, result, false);
    }

    public void match(Match match) {
        this.beforeStep(this.currentScenario.getNextStep());
    }

    public void embedding(String mimeType, byte[] data) {
        File file = new File();
        file.setName(UUID.randomUUID().toString());
        file.setContent(data);
        file.setContentType(mimeType);
        Utils.sendLog("embedding", "UNKNOWN", file);
    }

    public void write(String text) {
        Utils.sendLog(text, "INFO", (File)null);
    }

    public void syntaxError(String state, String event, List<String> legalEvents, String uri, Integer line) {
    }

    public void uri(String uri) {
        this.currentFeatureUri = uri;
    }

    public void feature(Feature feature) {
        this.beforeFeature(feature);
    }

    public void scenarioOutline(ScenarioOutline scenarioOutline) {
    }

    public void examples(Examples examples) {
        int num = examples.getRows().size();

        for(int i = 1; i < num; ++i) {
            this.outlineIterations.add(" [" + i + "]");
        }

    }

    public void startOfScenarioLifeCycle(Scenario scenario) {
        this.inBackground = false;
        this.beforeScenario(scenario, (String)this.outlineIterations.poll());
        this.beforeHooks(true);
    }

    public void background(Background background) {
        this.afterHooks(true);
        this.inBackground = true;
        this.stepPrefix = background.getKeyword().toUpperCase() + ": ";
    }

    public void scenario(Scenario scenario) {
        if (!this.inBackground) {
            this.afterHooks(true);
        } else {
            this.inBackground = false;
        }

        this.stepPrefix = "";
    }

    public void step(Step step) {
        if (this.currentScenario != null) {
            this.currentScenario.addStep(step);
        }

    }

    public void endOfScenarioLifeCycle(Scenario scenario) {
        this.afterHooks(false);
        this.afterScenario();
    }

    public void done() {
    }

    public void close() {
        if (this.finished.compareAndSet(false, true)) {
            this.afterLaunch();
        }

    }

    public void eof() {
        this.afterFeature();
    }

    protected abstract Maybe<String> getRootItemId();

    public static class ScenarioContext {
        private Maybe<String> id;
        private Queue<Step> steps;
        private String status;

        public ScenarioContext(Maybe<String> newId) {
            this.id = newId;
            this.steps = new ArrayDeque();
            this.status = "PASSED";
        }

        public Maybe<String> getId() {
            return this.id;
        }

        public void addStep(Step step) {
            this.steps.add(step);
        }

        public Step getNextStep() {
            return (Step)this.steps.poll();
        }

        public boolean noMoreSteps() {
            return this.steps.isEmpty();
        }

        public void updateStatus(String newStatus) {
            if (!newStatus.equals(this.status)) {
                if (!"FAILED".equals(this.status) && !"FAILED".equals(newStatus)) {
                    this.status = "SKIPPED";
                } else {
                    this.status = "FAILED";
                }
            }

        }

        public String getStatus() {
            return this.status;
        }
    }
}

