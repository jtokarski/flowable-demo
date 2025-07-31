package org.defendev.microsoft.graph;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;



public class LoggingOkInterceptor implements Interceptor {

    private static final Logger log = LogManager.getLogger();

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request request = chain.request();
        final HttpUrl url = request.url();

        log.info(url);

        return chain.proceed(request);
    }


}
