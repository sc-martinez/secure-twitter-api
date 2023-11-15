package com.serverless.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.http.ApiGatewayResponse;
import com.serverless.http.Response;
import com.serverless.domain.User;
import org.apache.log4j.Logger;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GetUser implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

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

            User user = new User().get(id);

            if (user != null) {
                return ApiGatewayResponse.builder()
                        .setStatusCode(200)
                        .setObjectBody(user)
                        .setHeaders(headers)
                        .build();
            } else {
                return ApiGatewayResponse.builder()
                        .setStatusCode(404)
                        .setObjectBody("Users with id: '" + id + "' not found.")
                        .setHeaders(headers)
                        .build();
            }
        } catch (Exception ex) {
            logger.error("Error in retrieving Users: " + ex);
            Response responseBody = new Response("Users in retrieving Users: ", input);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    .setHeaders(headers)
                    .build();
        }
    }
}