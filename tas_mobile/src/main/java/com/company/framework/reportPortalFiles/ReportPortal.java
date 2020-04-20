//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.company.framework.reportPortalFiles;


import com.epam.reportportal.exception.InternalReportPortalClientException;
import com.epam.reportportal.listeners.ListenerParameters;
import com.epam.reportportal.message.ReportPortalMessage;
import com.epam.reportportal.message.TypeAwareByteSource;
import com.epam.reportportal.restendpoint.http.HttpClientRestEndpoint;
import com.epam.reportportal.restendpoint.http.RestEndpoint;
import com.epam.reportportal.restendpoint.http.RestEndpoints;
import com.epam.reportportal.restendpoint.serializer.ByteArraySerializer;
import com.epam.reportportal.restendpoint.serializer.Serializer;
import com.epam.reportportal.restendpoint.serializer.json.JacksonSerializer;
import com.epam.reportportal.service.BearerAuthInterceptor;
import com.epam.reportportal.service.ReportPortalClient;
import com.epam.reportportal.service.ReportPortalErrorHandler;
import com.epam.reportportal.utils.MimeTypeDetector;
import com.epam.reportportal.utils.SslUtils;
import com.epam.reportportal.utils.properties.ListenerProperty;
import com.epam.reportportal.utils.properties.PropertiesLoader;
import com.epam.ta.reportportal.ws.model.launch.StartLaunchRQ;
import com.epam.ta.reportportal.ws.model.log.SaveLogRQ;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.Maybe;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rp.com.google.common.base.Function;
import rp.com.google.common.collect.Lists;
import rp.com.google.common.io.Files;
import rp.org.apache.http.client.HttpClient;
import rp.org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import rp.org.apache.http.impl.client.HttpClientBuilder;
import rp.org.apache.http.impl.client.HttpClients;
import rp.org.apache.http.ssl.SSLContextBuilder;

public class ReportPortal {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportPortal.class);
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private ReportPortalClient rpClient;
    private ListenerParameters parameters;

    ReportPortal(ReportPortalClient rpClient, ListenerParameters parameters) {
        this.rpClient = rpClient;
        this.parameters = parameters;
    }

    public Launch newLaunch(StartLaunchRQ rq) {
        if (!this.parameters.getEnable()) {
            return Launch.NOOP_LAUNCH;
        } else {
            LaunchImpl service = new LaunchImpl(this.rpClient, this.parameters, rq);
            return service;
        }
    }

    public Launch withLaunch(Maybe<String> currentLaunchId) {
        return new LaunchImpl(this.rpClient, this.parameters, currentLaunchId);
    }

    public ListenerParameters getParameters() {
        return this.parameters;
    }

    public ReportPortalClient getClient() {
        return this.rpClient;
    }

    public static ReportPortal.Builder builder() {
        return new ReportPortal.Builder();
    }

    public static ReportPortal create(ReportPortalClient client, ListenerParameters params) {
        return new ReportPortal(client, params);
    }

    public static boolean emitLog(Function<String, SaveLogRQ> logSupplier) {
        LoggingContext loggingContext = (LoggingContext) LoggingContext.CONTEXT_THREAD_LOCAL.get();
        if (null != loggingContext) {
            loggingContext.emit(logSupplier);
            return true;
        } else {
            return false;
        }
    }

    public static boolean emitLog(final String message, final String level, final Date time) {
        return emitLog(new Function<String, SaveLogRQ>() {
            public SaveLogRQ apply(@Nullable String id) {
                SaveLogRQ rq = new SaveLogRQ();
                rq.setLevel(level);
                rq.setLogTime(time);
                rq.setTestItemId(id);
                rq.setMessage(message);
                return rq;
            }
        });
    }

    public static boolean emitLog(final String message, final String level, final Date time, final File file) {
        return emitLog(new Function<String, SaveLogRQ>() {
            public SaveLogRQ apply(@Nullable String id) {
                SaveLogRQ rq = new SaveLogRQ();
                rq.setLevel(level);
                rq.setLogTime(time);
                rq.setTestItemId(id);
                rq.setMessage(message);

                try {
                    com.epam.ta.reportportal.ws.model.log.SaveLogRQ.File f = new com.epam.ta.reportportal.ws.model.log.SaveLogRQ.File();
                    f.setContentType(MimeTypeDetector.detect(file));
                    f.setContent(Files.toByteArray(file));
                    f.setName(UUID.randomUUID().toString());
                    rq.setFile(f);
                } catch (IOException var4) {
                    ReportPortal.LOGGER.error("Cannot send file to ReportPortal", var4);
                }

                return rq;
            }
        });
    }

    public static boolean emitLog(final ReportPortalMessage message, final String level, final Date time) {
        return emitLog(new Function<String, SaveLogRQ>() {
            public SaveLogRQ apply(@Nullable String id) {
                SaveLogRQ rq = new SaveLogRQ();
                rq.setLevel(level);
                rq.setLogTime(time);
                rq.setTestItemId(id);
                rq.setMessage(message.getMessage());

                try {
                    TypeAwareByteSource data = message.getData();
                    com.epam.ta.reportportal.ws.model.log.SaveLogRQ.File file = new com.epam.ta.reportportal.ws.model.log.SaveLogRQ.File();
                    file.setContent(data.read());
                    file.setContentType(data.getMediaType());
                    file.setName(UUID.randomUUID().toString());
                    rq.setFile(file);
                } catch (Exception var5) {
                    ReportPortal.LOGGER.error("Cannot send file to ReportPortal", var5);
                }

                return rq;
            }
        });
    }

    public static class Builder {
        public static final String API_BASE = "/api/v1";
        private static final String HTTPS = "https";
        private HttpClientBuilder httpClient;
        private ListenerParameters parameters;

        public Builder() {
        }

        public ReportPortal.Builder withHttpClient(HttpClientBuilder client) {
            this.httpClient = client;
            return this;
        }

        public ReportPortal.Builder withParameters(ListenerParameters parameters) {
            this.parameters = parameters;
            return this;
        }

        public ReportPortal build() {
            try {
                ListenerParameters params = null == this.parameters ? new ListenerParameters(this.defaultPropertiesLoader()) : this.parameters;
                return new ReportPortal(this.buildClient(ReportPortalClient.class, params), params);
            } catch (Exception var3) {
                String errMsg = "Cannot build ReportPortal client";
                ReportPortal.LOGGER.error(errMsg, var3);
                throw new InternalReportPortalClientException(errMsg, var3);
            }
        }

        public <T extends ReportPortalClient> T buildClient(Class<T> clientType, ListenerParameters params) {
            try {
                HttpClient client = null == this.httpClient ? this.defaultClient(params) : this.httpClient.addInterceptorLast(new BearerAuthInterceptor(params.getUuid())).build();
                return (T) RestEndpoints.forInterface(clientType, this.buildRestEndpoint(params, (HttpClient)client));
            } catch (Exception var5) {
                String errMsg = "Cannot build ReportPortal client";
                ReportPortal.LOGGER.error(errMsg, var5);
                throw new InternalReportPortalClientException(errMsg, var5);
            }
        }

        protected RestEndpoint buildRestEndpoint(ListenerParameters parameters, HttpClient client) {
            ObjectMapper om = new ObjectMapper();
            om.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
            om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            String baseUrl = parameters.getBaseUrl();
            String project = parameters.getProjectName();
            JacksonSerializer jacksonSerializer = new JacksonSerializer(om);
            return new HttpClientRestEndpoint(client, Lists.newArrayList(new Serializer[]{jacksonSerializer, new ByteArraySerializer()}), new ReportPortalErrorHandler(jacksonSerializer), this.buildEndpointUrl(baseUrl, project));
        }

        protected String buildEndpointUrl(String baseUrl, String project) {
            return baseUrl + "/api/v1" + "/" + project;
        }

        protected HttpClient defaultClient(ListenerParameters parameters) throws MalformedURLException {
            String baseUrl = parameters.getBaseUrl();
            String keyStore = parameters.getKeystore();
            String keyStorePassword = parameters.getKeystorePassword();
            String uuid = parameters.getUuid();
            HttpClientBuilder builder = HttpClients.custom();
            if ("https".equals((new URL(baseUrl)).getProtocol()) && keyStore != null) {
                if (null == keyStorePassword) {
                    throw new InternalReportPortalClientException("You should provide keystore password parameter [" + ListenerProperty.KEYSTORE_PASSWORD + "] if you use HTTPS protocol");
                }

                try {
                    builder.setSSLContext(SSLContextBuilder.create().loadTrustMaterial(SslUtils.loadKeyStore(keyStore, keyStorePassword), TrustSelfSignedStrategy.INSTANCE).build());
                } catch (Exception var8) {
                    throw new InternalReportPortalClientException("Unable to load trust store");
                }
            }

            builder.setMaxConnPerRoute(50).setMaxConnTotal(100).evictExpiredConnections();
            return builder.addInterceptorLast(new BearerAuthInterceptor(uuid)).build();
        }

        protected PropertiesLoader defaultPropertiesLoader() {
            return PropertiesLoader.load();
        }
    }
}

