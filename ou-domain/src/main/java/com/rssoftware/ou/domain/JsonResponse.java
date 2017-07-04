package com.rssoftware.ou.domain;


public class JsonResponse {

	private String status = null;
	private Integer code = 0;
    private Object result = null;
    private String message = null;
    
    public String getStatus() {
            return status;
    }
    public void setStatus(String status) {
            this.status = status;
    }
    public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public Object getResult() {
            return result;
    }
    public void setResult(Object result) {
            this.result = result;
    }
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}