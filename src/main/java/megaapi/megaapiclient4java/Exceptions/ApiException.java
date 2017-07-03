package megaapi.megaapiclient4java.Exceptions;

import megaapi.megaapiclient4java.Enumerations.ApiResultCode;

public class ApiException extends Exception {

    private ApiException(ApiResultCode apiResultCode) {
        this.ApiResultCode = apiResultCode;
    }

    private final ApiResultCode ApiResultCode;

    public ApiResultCode getApiResultCode() {
        return ApiResultCode;
    }

    @Override
    public String getMessage() {
        return String.format("API response: {0}", this.ApiResultCode);
    }
}
