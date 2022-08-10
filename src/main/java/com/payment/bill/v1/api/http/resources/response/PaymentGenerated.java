package com.payment.bill.v1.api.http.resources.response;

import org.json.JSONException;
import org.json.JSONObject;

import lombok.Data;

@Data
public class PaymentGenerated {

    private String referenceId;
    private String expiresAt;
    private String qrCode;
    private String statusPayment;
    private String paymentUrl;

    public PaymentGenerated(String jsonStr) throws JSONException {
        JSONObject json = new JSONObject(jsonStr);
        this.referenceId = json.getString("referenceId");
        this.expiresAt = json.getString("expiresAt");
        this.qrCode = json.getJSONObject("qrcode").getString("base64");
        this.paymentUrl = json.getString("paymentUrl");
        this.statusPayment = "Pagamento Pendente";
    }
}
