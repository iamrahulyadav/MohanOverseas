package com.nucleustech.mohanoverseas.volley;

import com.android.volley.VolleyError;

public interface ServerResponseStringCallback {

	public void onSuccess(String resultJsonObject);

	/**
	 * If there occurs any error while communicating with server
	 * 
	 * @param error
	 */
	public void ErrorMsg(VolleyError error);
}
