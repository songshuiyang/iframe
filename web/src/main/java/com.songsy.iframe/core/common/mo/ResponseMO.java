package com.songsy.iframe.core.common.mo;

import com.songsy.iframe.core.common.CommonConstants;

import java.io.Serializable;

public class ResponseMO<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 成功 为0   失败 为 1
	 */
	private int code = CommonConstants.RESPONSE_CODE_SUCCESS;
	private String msg = "";
	private T data;
	private String debugInfo = "";

	public ResponseMO() {
	}
	
	public boolean checkFailure() {
		boolean result = false;
		if (this.code == CommonConstants.RESPONSE_CODE_FAILURE) {
			result = true;
		}
		return result;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public void setResponseCodeFailure() {
		this.code = CommonConstants.RESPONSE_CODE_FAILURE;
	}

	public String getDebugInfo() {
		return debugInfo;
	}

	public void setDebugInfo(String debugInfo) {
		this.debugInfo = debugInfo;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "ResponseMO{" +
				"code=" + code +
				", msg='" + msg + '\'' +
				", data=" + data +
				", debugInfo='" + debugInfo + '\'' +
				'}';
	}
}
