package com.tasc.hongquan.paymentservice.util;

import com.tasc.hongquan.paymentservice.models.momo.HttpRequest;
import com.tasc.hongquan.paymentservice.models.momo.HttpResponse;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import okio.Buffer;

import java.io.IOException;

@Slf4j
public class Execute {
    OkHttpClient client = new OkHttpClient();
    Logger logger = LoggerFactory.getLogger(Execute.class);
    public HttpResponse sendToMoMo(String endpoint, String payload, String existingSignature) {

        try {
            logger.info("[ExecuteSendToMoMo] Payload: " + payload);
            payload = payload.replaceFirst("\"signature\":\"[^\"]*\"", "\"signature\":\"" + existingSignature + "\"");
            logger.info("[ExecuteSendToMoMo] Payload after: " + payload);
            HttpRequest httpRequest = new HttpRequest("POST", endpoint, payload, "application/json");

            Request request = createRequest(httpRequest);

            logger.info("[HttpPostToMoMo] Endpoint:: " + httpRequest.getEndpoint() + ", RequestBody:: " + httpRequest.getPayload());

            Response result = client.newCall(request).execute();
            HttpResponse response = new HttpResponse(result.code(), result.body().string(), result.headers());

            logger.info("[HttpResponseFromMoMo] " + response.toString());

            return response;
        } catch (Exception e) {
            logger.error("[ExecuteSendToMoMo] "+ e);
        }

        return null;
    }

    public static Request createRequest(HttpRequest request) {
        RequestBody body = RequestBody.create(MediaType.get(request.getContentType()), request.getPayload());
        return new Request.Builder()
                .method(request.getMethod(), body)
                .url(request.getEndpoint())
                .build();
    }

    public String getBodyAsString(Request request) throws IOException {
        Buffer buffer = new Buffer();
        RequestBody body = request.body();
        body.writeTo(buffer);
        return buffer.readUtf8();
    }

}
