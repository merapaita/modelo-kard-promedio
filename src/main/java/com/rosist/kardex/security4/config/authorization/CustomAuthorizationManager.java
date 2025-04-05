package com.rosist.kardex.security4.config.authorization;

import com.rosist.kardex.exception2.ObjectNotFoundException;
import com.rosist.kardex.security4.model.GrantedPermission;
import com.rosist.kardex.security4.model.Operation;
import com.rosist.kardex.security4.model.User;
import com.rosist.kardex.security4.repository.OperationRepository;
import com.rosist.kardex.security4.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private UserService userService;

    private static Logger log = LoggerFactory.getLogger(CustomAuthorizationManager.class);

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext requestContext) {
        HttpServletRequest request = requestContext.getRequest();
        String url = extractUrl(request);
        String httpMethod = request.getMethod();

        boolean isPublic = isPublic(url, httpMethod);
        if (isPublic) {
            return new AuthorizationDecision(true);
        }
        System.out.println("check..." + authentication.get());
        boolean isGranted = isGranted(url, httpMethod, authentication.get());

        return new AuthorizationDecision(isGranted);
    }

    private boolean isGranted(String url, String httpMethod, Authentication authentication) {
        if(authentication==null || !(authentication instanceof UsernamePasswordAuthenticationToken)) {
            System.out.println("no estoy logueado");
            throw new AuthenticationCredentialsNotFoundException("User not logged in");
        }
        List<Operation> operations = obtainedOperations(authentication);
        boolean isGranted = operations.stream().anyMatch(getOperationPredicate(url, httpMethod));

        System.out.printf("IS GRANTED:" + isGranted);
        return isGranted;
    }

    private static Predicate<Operation> getOperationPredicate(String url, String httpMethod) {
        return operation -> {
            String basePath = operation.getModule().getBasePath();
            Pattern pattern = Pattern.compile(basePath.concat(operation.getPath()));
            Matcher matcher = pattern.matcher(url);
            System.out.println("operation.met:" + operation.getHttpMethod() + " - httpmet:" + httpMethod);
            return matcher.matches() && operation.getHttpMethod().equals(httpMethod);
        };
    }

    private List<Operation> obtainedOperations(Authentication authentication) {
        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) authentication;
        String username = (String) authToken.getPrincipal();
        User user = userService.findOneByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("User not found. usename:" + username));
        System.out.println("username:" + username + " user:" + user);
        return user.getRole().getPermissions().stream()
                .map(grantedPermission -> grantedPermission.getOperation())
                .collect(Collectors.toList());
    }

    private boolean isPublic(String url, String httpMethod) {
        List<Operation> publicAccessEndpoint = operationRepository
                .findByPublicAccess();

        boolean isPublic = publicAccessEndpoint.stream().anyMatch(getOperationPredicate(url, httpMethod));
        System.out.println("Is Public :" + isPublic);
        return isPublic;
    }



    private String extractUrl(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        String url = request.getRequestURI();
        url = url.replace(contextPath, "");
        System.out.println(url);
        return url;
    }

}
