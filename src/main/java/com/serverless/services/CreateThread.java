package com.serverless.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.domain.Message;
import com.serverless.domain.Thread;
import com.serverless.http.ApiGatewayResponse;
import com.serverless.http.Response;
import org.apache.log4j.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class CreateThread implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private final Logger logger = Logger.getLogger(this.getClass());

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        try {
            // get the 'body' from input
            JsonNode body = new ObjectMapper().readTree((String) input.get("body"));
            Message msg = new Message();
            msg.setComment(body.get("comment").asText());
            msg.setComment(body.get("owner").asText());
            // create the Product object for post
            ArrayList<Message> list = new ArrayList<>();
            list.add(msg);
            Thread thread = new Thread();
            thread.setMessages(list);
            thread.setOwner(body.get("owner").asText());
            thread.save(thread);

            // send the response back
            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setObjectBody(thread)
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                    .build();

        } catch (Exception ex) {
            logger.error("Error in saving thread: " + ex);
            // send the error response back
            Response responseBody = new Response("Error in saving thread: ", input);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                    .build();
        }
    }
}