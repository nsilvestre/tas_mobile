//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

//package com.epam.reportportal.service;
package com.company.framework.reportPortalFiles;

import com.epam.reportportal.message.TypeAwareByteSource;
import com.epam.reportportal.restendpoint.http.MultiPartRequest.Builder;
import com.epam.reportportal.service.ReportPortalClient;
import com.epam.reportportal.utils.files.ImageConverter;
import com.epam.ta.reportportal.ws.model.BatchSaveOperatingRS;
import com.epam.ta.reportportal.ws.model.log.SaveLogRQ;
import com.epam.ta.reportportal.ws.model.log.SaveLogRQ.File;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import java.util.Iterator;
import java.util.List;
import org.reactivestreams.Publisher;
import rp.com.google.common.base.Strings;
import rp.com.google.common.io.ByteSource;
import rp.com.google.common.net.MediaType;

public class LoggingContext {
    public static final int DEFAULT_BUFFER_SIZE = 10;
    static final ThreadLocal<LoggingContext> CONTEXT_THREAD_LOCAL = new ThreadLocal();
    private final PublishSubject<Maybe<SaveLogRQ>> emitter;
    private final Maybe<String> itemId;
    private final boolean convertImages;

    public static LoggingContext init(Maybe<String> itemId, ReportPortalClient client) {
        return init(itemId, client, 10, false);
    }

    public static LoggingContext init(Maybe<String> itemId, ReportPortalClient client, int bufferSize, boolean convertImages) {
        LoggingContext context = new LoggingContext(itemId, client, bufferSize, convertImages);
        CONTEXT_THREAD_LOCAL.set(context);
        return context;
    }

    public static Completable complete() {
        LoggingContext loggingContext = (LoggingContext)CONTEXT_THREAD_LOCAL.get();
        return null != loggingContext ? loggingContext.completed() : Maybe.empty().ignoreElement();
    }

    LoggingContext(Maybe<String> itemId, final ReportPortalClient client, int bufferSize, boolean convertImages) {
        this.itemId = itemId;
        this.emitter = PublishSubject.create();
        this.convertImages = convertImages;
        this.emitter.toFlowable(BackpressureStrategy.BUFFER).flatMap(new Function<Maybe<SaveLogRQ>, Publisher<SaveLogRQ>>() {
            public Publisher<SaveLogRQ> apply(Maybe<SaveLogRQ> rq) throws Exception {
                return rq.toFlowable();
            }
        }).buffer(bufferSize).flatMap(new Function<List<SaveLogRQ>, Flowable<BatchSaveOperatingRS>>() {
            public Flowable<BatchSaveOperatingRS> apply(List<SaveLogRQ> rqs) throws Exception {
                Builder builder = new Builder();
                builder.addSerializedPart("json_request_part", rqs);
                Iterator var3 = rqs.iterator();

                while(var3.hasNext()) {
                    SaveLogRQ rq = (SaveLogRQ)var3.next();
                    File file = rq.getFile();
                    if (null != file) {
                        builder.addBinaryPart("binary_part", file.getName(), Strings.isNullOrEmpty(file.getContentType()) ? MediaType.OCTET_STREAM.toString() : file.getContentType(), ByteSource.wrap(file.getContent()));
                    }
                }

                return client.log(builder.build()).toFlowable();
            }
        }).doOnError(new Consumer<Throwable>() {
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        }).observeOn(Schedulers.computation()).subscribe();
    }

    public void emit(final rp.com.google.common.base.Function<String, SaveLogRQ> logSupplier) {
        this.emitter.onNext(this.itemId.map(new Function<String, SaveLogRQ>() {
            public SaveLogRQ apply(String input) throws Exception {
                SaveLogRQ rq = (SaveLogRQ)logSupplier.apply(input);
                File file = rq.getFile();
                if (LoggingContext.this.convertImages && null != file && ImageConverter.isImage(file.getContentType())) {
                    TypeAwareByteSource source = ImageConverter.convert(ByteSource.wrap(file.getContent()));
                    file.setContent(source.read());
                    file.setContentType(source.getMediaType());
                }

                return rq;
            }
        }));
    }

    public Completable completed() {
        this.emitter.onComplete();
        return this.emitter.ignoreElements();
    }
}
