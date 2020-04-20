//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

//package com.epam.reportportal.cucumber;
package com.company.framework.reportPortalFiles;


import com.epam.ta.reportportal.ws.model.FinishTestItemRQ;
import com.epam.ta.reportportal.ws.model.StartTestItemRQ;
import com.epam.ta.reportportal.ws.model.log.SaveLogRQ;
import com.epam.ta.reportportal.ws.model.log.SaveLogRQ.File;
import gherkin.formatter.model.*;
import io.reactivex.Maybe;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rp.com.google.common.base.Function;
import rp.com.google.common.base.Strings;
import rp.com.google.common.collect.ImmutableMap;

public class Utils {
    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);
    private static final String TABLE_SEPARATOR = "|";
    private static final String DOCSTRING_DECORATOR = "\n\"\"\"\n";
    private static final ImmutableMap<Object, Object> STATUS_MAPPING = ImmutableMap.builder().put("passed", "PASSED").put("skipped", "SKIPPED").put("pending", "SKIPPED").put("undefined", "SKIPPED").build();

    private Utils() {
    }

    public static void finishTestItem(Launch rp, Maybe<String> itemId) {
        finishTestItem(rp, itemId, (String)null);
    }

    public static void finishTestItem(Launch rp, Maybe<String> itemId, String status) {
        if (itemId == null) {
            LOGGER.error("BUG: Trying to finish unspecified test item.");
        } else {
            FinishTestItemRQ rq = new FinishTestItemRQ();
            rq.setStatus(status);
            rq.setEndTime(Calendar.getInstance().getTime());
    /// Modified for defect type //////////////////////////////////////////////////////////////////
            //rp.finishTestItem(itemId, rq);
            String tagName = getTagName();
            rp.finishTestItem(itemId, rq, tagName);

        }
    }

    public static String getTagName() {
        String tagName;
        tagName = AbstractReporter.getScenarioAux().getTags().get(0).getName();
        return tagName;
    }
    /// Modified for defect type /////////////////////////////////////////////////////////////////

    public static Maybe<String> startNonLeafNode(Launch rp, Maybe<String> rootItemId, String name, String description, List<Tag> tags, String type) {
        StartTestItemRQ rq = new StartTestItemRQ();
        rq.setDescription(description);
        rq.setName(name);
        rq.setTags(extractTags(tags));
        rq.setStartTime(Calendar.getInstance().getTime());
        rq.setType(type);
        return rp.startTestItem(rootItemId, rq);
    }

    public static void sendLog(final String message, final String level, final File file) {
        ReportPortal.emitLog(new Function<String, SaveLogRQ>() {
            public SaveLogRQ apply(String item) {
                SaveLogRQ rq = new SaveLogRQ();
                rq.setMessage(message);
                rq.setTestItemId(item);
                rq.setLevel(level);
                rq.setLogTime(Calendar.getInstance().getTime());
                if (file != null) {
                    rq.setFile(file);
                }

                return rq;
            }
        });
    }

    public static Set<String> extractTags(List<Tag> tags) {
        Set<String> returnTags = new HashSet();
        Iterator var2 = tags.iterator();

        while(var2.hasNext()) {
            Tag tag = (Tag)var2.next();
            returnTags.add(tag.getName());
        }

        return returnTags;
    }

    public static String mapLevel(String cukesStatus) {
        String mapped = null;
        if (cukesStatus.equalsIgnoreCase("passed")) {
            mapped = "INFO";
        } else if (cukesStatus.equalsIgnoreCase("skipped")) {
            mapped = "WARN";
        } else {
            mapped = "ERROR";
        }

        return mapped;
    }

    public static String mapStatus(String cukesStatus) {
        if (Strings.isNullOrEmpty(cukesStatus)) {
            return "FAILED";
        } else {
            String status = (String)STATUS_MAPPING.get(cukesStatus.toLowerCase());
            return null == status ? "FAILED" : status;
        }
    }

    public static String buildStatementName(BasicStatement stmt, String prefix, String infix, String suffix) {
        return (prefix == null ? "" : prefix) + stmt.getKeyword() + infix + stmt.getName() + (suffix == null ? "" : suffix);
    }

    public static String buildMultilineArgument(Step step) {
        List<DataTableRow> table = step.getRows();
        DocString ds = step.getDocString();
        StringBuilder marg = new StringBuilder();
        if (table != null) {
            marg.append("\r\n");
            Iterator var4 = table.iterator();

            while(var4.hasNext()) {
                Row row = (Row)var4.next();
                marg.append("|");
                Iterator var6 = row.getCells().iterator();

                while(var6.hasNext()) {
                    String cell = (String)var6.next();
                    marg.append(" ").append(cell).append(" ").append("|");
                }

                marg.append("\r\n");
            }
        }

        if (ds != null) {
            marg.append("\n\"\"\"\n").append(ds.getValue()).append("\n\"\"\"\n");
        }

        return marg.toString();
    }
}

