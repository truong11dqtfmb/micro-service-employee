package com.dqt.apigateway.filter;


import com.dqt.apigateway.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    private RestTemplate template = new RestTemplate();

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    public static class Config {
    }

//    @Override
//    public GatewayFilter apply(Config config) {
//        return ((exchange, chain) -> {
//            if (validator.isSecured.test(exchange.getRequest())) {
//                //header contains token or not
//                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
//                    throw new RuntimeException("missing authorization header");
//                }
//
//                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
//                if (authHeader != null && authHeader.startsWith("Bearer ")) {
//                    authHeader = authHeader.substring(7);
//                }
//                try {
////                    //REST call to AUTH service
////                    template.getForObject("http://IDENTITY-SERVICE//validate?token" + authHeader, String.class);
//                    Boolean flag =  jwtUtil.validateToken(authHeader);
//
//                    System.out.println(flag);
//
//                } catch (Exception e) {
//                    System.out.println("invalid access...!");
//                    throw new RuntimeException("un authorized access to application");
//                }
//            }
//            return chain.filter(exchange);
//        });
//    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            ServerHttpRequest httpRequest = exchange.getRequest();

            if(!httpRequest.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "No Authorization Header", HttpStatus.UNAUTHORIZED);
            }
//
//            String authorizationHeader = httpRequest.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String authorizationHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

            authorizationHeader = authorizationHeader.substring(7);

            String jwt = authorizationHeader.replace("Bearer", "");

            if(!jwtUtil.validateToken(jwt)) {
                return onError(exchange, "JWT Token is not valid!", HttpStatus.UNAUTHORIZED);
            }

            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus){
        ServerHttpResponse httpResponse = exchange.getResponse();
        httpResponse.setStatusCode(httpStatus);
        return httpResponse.setComplete();
    }


}