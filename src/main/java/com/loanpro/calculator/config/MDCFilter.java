package com.loanpro.calculator.config;

import com.loanpro.calculator.security.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Optional;
import java.util.UUID;

@Component
public class MDCFilter extends OncePerRequestFilter {

    private static final String UUID_PATTERN = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";

    private final String responseIdHeader;
    private final String mdcLogKey;

    private final String mdcUserKey;
    private final String requestIdHeader;

    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public MDCFilter(@Value("${mdcfilter.request-id-header}") String requestHeader,
                     @Value("${mdcfilter.response-id-header}") String responseHeader,
                     @Value("${mdcfilter.mdc-log-key}") String mdcLogKey,
                     @Value("${mdcfilter.mdc-log-user}") String mdcLogUser,
                     UserDetailsServiceImpl userDetailsService) {
        this.responseIdHeader = responseHeader;
        this.mdcLogKey = mdcLogKey;
        this.requestIdHeader = requestHeader;
        this.userDetailsService = userDetailsService;
        this.mdcUserKey = mdcLogUser;
    }

    @Override
    protected void doFilterInternal(@NonNull final HttpServletRequest request, @NonNull final HttpServletResponse response,
                                    @NonNull final FilterChain chain) throws java.io.IOException, ServletException {
        String currentUser = "";

        try {
            currentUser = userDetailsService.getCurrentUser().getId().toString();
        } catch (Exception ex) {
            currentUser = "unauthenticated user";
        }

        try {
            final String requestIdFromRequest = this.findRequestIdFromRequest(request);
            MDC.put(mdcLogKey, requestIdFromRequest);
            MDC.put(mdcUserKey, currentUser);

            Optional.ofNullable(responseIdHeader)
                    .filter(resHead -> !StringUtils.isEmpty(resHead))
                    .ifPresent(resHead -> response.addHeader(resHead, requestIdFromRequest));

            chain.doFilter(request, response);
        } finally {
            MDC.remove(mdcLogKey);
        }
    }

    private String findRequestIdFromRequest(final HttpServletRequest request) {
        final String[] token = new String[1];

        Optional.ofNullable(requestIdHeader)
                .filter(reqHd -> !StringUtils.isEmpty(reqHd))
                .filter(reqHd -> !StringUtils.isEmpty(request.getHeader(reqHd)))
                .filter(reqHd -> request.getHeader(reqHd)
                        .matches(UUID_PATTERN))
                .ifPresentOrElse(reqHeader -> token[0] = request.getHeader(reqHeader), () -> token[0] = UUID.randomUUID()
                        .toString());
        return token[0];
    }

    @Override
    protected boolean isAsyncDispatch(@NonNull final HttpServletRequest request) {
        return false;
    }

    @Override
    protected boolean shouldNotFilterErrorDispatch() {
        return false;
    }
}