package br.com.osvaldsoza.util;

public enum StatusCode {
    UNPROCESSABLE_ENTITY(422);

    private Integer statusCode;

    StatusCode(Integer statusCOde) {
        this.statusCode = statusCOde;
    }

    public Integer getStatusCode() {
        return statusCode;
    }
}
