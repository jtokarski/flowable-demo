package org.defendev.selenium.demo;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v144.network.Network;
import org.openqa.selenium.devtools.v144.network.model.Request;
import org.openqa.selenium.devtools.v144.network.model.RequestWillBeSent;
import org.openqa.selenium.devtools.v144.network.model.RequestWillBeSentExtraInfo;
import org.springframework.http.HttpHeaders;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import static java.lang.String.format;
import static java.util.Objects.isNull;
import static java.util.Optional.empty;
import static java.util.function.Predicate.not;
import static org.apache.commons.lang3.StringUtils.isBlank;



public class DevToolsRequestHeaderExtractor {

    private final Map<String, RequestInfo> requests;

    private final RequestWillBeSentHandler requestWillBeSentHandler;

    private final RequestWillBeSentExtraInfoHandler requestWillBeSentExtraInfoHandler;

    public DevToolsRequestHeaderExtractor() {
        requests = new ConcurrentHashMap<>();
        requestWillBeSentHandler = new RequestWillBeSentHandler(requests);
        requestWillBeSentExtraInfoHandler = new RequestWillBeSentExtraInfoHandler(requests);
    }

    public RequestWillBeSentHandler getRequestWillBeSentHandler() {
        return requestWillBeSentHandler;
    }

    public RequestWillBeSentExtraInfoHandler getRequestWillBeSentExtraInfoHandler() {
        return requestWillBeSentExtraInfoHandler;
    }

    public Optional<String> extractedCookieHeader() {
        return requests.values().stream()
            .filter(not(i -> isBlank(i.cookieHeader())))
            .map(RequestInfo::cookieHeader)
            .findAny();
    }

    public record RequestInfo(String url, String cookieHeader) {

        public RequestInfo withUrl(String url) {
            return new RequestInfo(url, cookieHeader);
        }

        public RequestInfo withCookieHeader(String cookieHeader) {
            return new RequestInfo(url, cookieHeader);
        }

        public static RequestInfo ofUrl(String url) {
            return new RequestInfo(url, null);
        }

        public static RequestInfo ofCookieHeader(String cookieHeader) {
            return new RequestInfo(null, cookieHeader);
        }

    }

    public static class RequestWillBeSentHandler implements Consumer<RequestWillBeSent> {

        private final Map<String, RequestInfo> requests;

        public RequestWillBeSentHandler(Map<String, RequestInfo> requests) {
            this.requests = requests;
        }

        @Override
        public void accept(RequestWillBeSent requestWillBeSent) {
            final Request request = requestWillBeSent.getRequest();
            /*
             * If I ever needed HttpHeaders.AUTHORIZATION header, I wouldn't need RequestWillBeSentExtraInfo,
             * it's available in RequestWillBeSent.
             *
             */
            final String url = request.getUrl();
            final String requestId = requestWillBeSent.getRequestId().toString();
            requests.compute(requestId, (_, requestInfo) -> isNull(requestInfo) ?
                RequestInfo.ofUrl(url) :
                requestInfo.withUrl(url));
        }
    }

    public static class RequestWillBeSentExtraInfoHandler implements Consumer<RequestWillBeSentExtraInfo> {

        private final Map<String, RequestInfo> requests;

        public RequestWillBeSentExtraInfoHandler(Map<String, RequestInfo> requests) {
            this.requests = requests;
        }

        @Override
        public void accept(RequestWillBeSentExtraInfo requestWillBeSentExtraInfo) {
            if (requestWillBeSentExtraInfo.getHeaders().containsKey(HttpHeaders.COOKIE)) {
                final Object cookieHeaderObject = requestWillBeSentExtraInfo.getHeaders().get(HttpHeaders.COOKIE);
                if (cookieHeaderObject instanceof String cookieHeader) {
                    final String requestId = requestWillBeSentExtraInfo.getRequestId().toString();
                    requests.compute(requestId, (_, requestInfo) -> isNull(requestInfo) ?
                        RequestInfo.ofCookieHeader(cookieHeader) :
                        requestInfo.withCookieHeader(cookieHeader));
                } else {
                    throw new IllegalStateException(format("Expected cookie header to be of type String %s",
                        cookieHeaderObject.getClass()));
                }
            }
        }
    }

    public static DevToolsRequestHeaderExtractor enableExtraction(WebDriver driver) {
        if (driver instanceof HasDevTools driverHasDevTools) {
            final DevTools devTools = driverHasDevTools.getDevTools();
            devTools.createSession();
            devTools.send(Network.enable(empty(), empty(), empty(), empty(), empty()));
            final DevToolsRequestHeaderExtractor instance = new DevToolsRequestHeaderExtractor();
            devTools.addListener(Network.requestWillBeSent(), instance.getRequestWillBeSentHandler());
            devTools.addListener(Network.requestWillBeSentExtraInfo(), instance.getRequestWillBeSentExtraInfoHandler());
            return instance;
        } else {
            throw new IllegalStateException("WebDriver is not a HasDevTools instance");
        }
    }

}
