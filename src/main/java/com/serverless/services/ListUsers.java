package com.serverless.services;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.http.ApiGatewayResponse;
import com.serverless.http.Response;
import com.serverless.domain.User;
import org.apache.log4j.Logger;
import java.util.Collections;
import java.util.Map;
import java.util.List;

public class ListUsers implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private final Logger logger = Logger.getLogger(this.getClass());

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        try {
            List<User> users = new User().list();

            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setObjectBody(users)
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                    .build();
        } catch (Exception ex) {
            Response responseBody = new Response("Error in getting User ......" + ex, input);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                    .build();
        }
    }
}