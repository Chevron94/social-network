package socialnetwork.beans.impl;

import com.fasterxml.jackson.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestOperations;
import socialnetwork.beans.CapchaBean;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Roman on 26.08.2017 14:54.
 */
@Component
public class CapchaBeanImpl implements CapchaBean {

    enum ErrorCode {
        MISSING_SECRET, INVALID_SECRET,
        MISSING_RESPONSE, INVALID_RESPONSE;

        private static Map<String, ErrorCode> errorsMap = new HashMap<String, ErrorCode>(4);

        static {
            errorsMap.put("missing-input-secret", MISSING_SECRET);
            errorsMap.put("invalid-input-secret", INVALID_SECRET);
            errorsMap.put("missing-input-response", MISSING_RESPONSE);
            errorsMap.put("invalid-input-response", INVALID_RESPONSE);
        }

        @JsonCreator
        public static ErrorCode forValue(String value) {
            return errorsMap.get(value.toLowerCase());
        }
    }

    @Autowired
    @Qualifier("restTemplate")
    private RestOperations restOperations;

    private static final Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");

    private static final String CAPCHA_SECRET = "6LexPy4UAAAAAF9eJZcc0tN8qeBPSCBpFN7nBold";

    @Override
    public Boolean processResponse(String response) {
        if (!responseSanityCheck(response)) {
            return false;
        }

        URI verifyUri = URI.create(String.format(
                "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s",
                CAPCHA_SECRET, response));

        GoogleResponse googleResponse = restOperations.getForObject(verifyUri, GoogleResponse.class);

        return googleResponse.isSuccess();
    }

    private boolean responseSanityCheck(String response) {
        return StringUtils.hasLength(response) && RESPONSE_PATTERN.matcher(response).matches();
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonPropertyOrder({
            "success",
            "challenge_ts",
            "hostname",
            "error-codes"
    })
    public static class GoogleResponse {

        @JsonProperty("success")
        private boolean success;

        @JsonProperty("challenge_ts")
        private String challengeTs;

        @JsonProperty("hostname")
        private String hostname;

        @JsonProperty("error-codes")
        private ErrorCode[] errorCodes;

        @JsonIgnore
        public boolean hasClientError() {
            ErrorCode[] errors = getErrorCodes();
            if (errors == null) {
                return false;
            }
            for (ErrorCode error : errors) {
                if (ErrorCode.INVALID_RESPONSE.equals(error)
                        || ErrorCode.MISSING_RESPONSE.equals(error)) {
                    return true;
                }
            }
            return false;
        }


        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getChallengeTs() {
            return challengeTs;
        }

        public void setChallengeTs(String challengeTs) {
            this.challengeTs = challengeTs;
        }

        public String getHostname() {
            return hostname;
        }

        public void setHostname(String hostname) {
            this.hostname = hostname;
        }

        public ErrorCode[] getErrorCodes() {
            return errorCodes;
        }

        public void setErrorCodes(ErrorCode[] errorCodes) {
            this.errorCodes = errorCodes;
        }

    }
}
