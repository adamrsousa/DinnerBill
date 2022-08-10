package com.payment.bill.v1.api.http.resources.response;

import org.json.JSONException;
import org.json.JSONObject;

import lombok.Data;

@Data
public class NewStatusPayment {

    private String referenceId;
    private String status;

    public NewStatusPayment(String jsonStr) throws JSONException {
        JSONObject json = new JSONObject(jsonStr);
        this.referenceId = json.getString("referenceId");
        this.status = json.getString("status");
    }
}
