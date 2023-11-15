package com.serverless.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.domain.Message;
import com.serverless.domain.Thread;
import com.serverless.domain.User;
import com.serverless.http.ApiGatewayResponse;
import com.serverless.http.Response;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CommentThread  implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private final Logger logger = Logger.getLogger(this.getClass());

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Powered-By", "AWS Lambda & Serverless");
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Credentials", "true");
        try {

            Map<String,String> pathParameters =  (Map<String,String>)input.get("pathParameters");
            String id = pathParameters.get("id");
            JsonNode body = new ObjectMapper().readTree((String) input.get("body"));

            Thread thread = new Thread().get(id);

            if (thread != null) {
                Message message = new Message();
                message.setComment(body.get("comment").asText());
                message.setSender(body.get("sender").asText());

                thread.addMessage(message);
                thread.save(thread);
            }


            // send the response back
            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setObjectBody(thread)
                    .setHeaders(headers)
                    .build();

        } catch (Exception ex) {
            logger.error("Error in saving thread: " + ex);

            // send the error response back
            Response responseBody = new Response("Error in saving comment in thread: ", input);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    .setHeaders(headers)
                    .build();
        }
    }
}