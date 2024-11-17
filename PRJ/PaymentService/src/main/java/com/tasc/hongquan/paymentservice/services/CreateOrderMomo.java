package com.tasc.hongquan.paymentservice.services;

import com.tasc.hongquan.paymentservice.config.Environment;
import com.tasc.hongquan.paymentservice.constant.Parameter;
import com.tasc.hongquan.paymentservice.exception.MoMoException;
import com.tasc.hongquan.paymentservice.models.momo.*;
import com.tasc.hongquan.paymentservice.util.Encoder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.AbstractProcessor;
@Slf4j
public class CreateOrderMomo extends AbstractProcess<PaymentRequest, PaymentResponse> {
    public CreateOrderMomo(Environment environment) {
        super(environment);
    }

    private static final Logger logger = LoggerFactory.getLogger(CreateOrderMomo.class);

    /**
     * Capture MoMo Process on Payment Gateway
     *
     * @param amount
     * @param extraData
     * @param orderInfo
     * @param env       name of the environment (dev or prod)
     * @param orderId   unique order ID in MoMo system
     * @param requestId request ID
     * @param returnURL URL to redirect customer
     * @param notifyURL URL for MoMo to return transaction status to merchant
     * @return PaymentResponse
     **/

    public static PaymentResponse process(Environment env, String orderId, String requestId, String amount, String orderInfo, String returnURL, String notifyURL, String extraData, RequestType requestType, Boolean autoCapture) throws Exception {
        try {
            CreateOrderMomo m2Processor = new CreateOrderMomo(env);

            PaymentRequest request = m2Processor.createPaymentCreationRequest(orderId, requestId, amount, orderInfo, returnURL, notifyURL, extraData, requestType, autoCapture);
            PaymentResponse captureMoMoResponse = m2Processor.execute(request);

            return captureMoMoResponse;
        } catch (Exception exception) {
            logger.error("[CreateOrderMoMoProcess] " + exception);
        }
        return null;
    }

    @Override
    public PaymentResponse execute(PaymentRequest request) throws MoMoException {
        try {

            String payload = getGson().toJson(request, PaymentRequest.class);

            HttpResponse response = execute.sendToMoMo(environment.getMomoEndpoint().getCreateUrl(), payload);

            if (response.getStatus() != 200) {
                throw new MoMoException("[PaymentResponse] [" + request.getOrderId() + "] -> Error API");
            }

            System.out.println("uweryei7rye8wyreow8: " + response.getData());

            PaymentResponse captureMoMoResponse = getGson().fromJson(response.getData(), PaymentResponse.class);
            String responserawData = Parameter.REQUEST_ID + "=" + captureMoMoResponse.getRequestId() +
                    "&" + Parameter.ORDER_ID + "=" + captureMoMoResponse.getOrderId() +
                    "&" + Parameter.MESSAGE + "=" + captureMoMoResponse.getMessage() +
                    "&" + Parameter.PAY_URL + "=" + captureMoMoResponse.getPayUrl() +
                    "&" + Parameter.RESULT_CODE + "=" + captureMoMoResponse.getResultCode();

            logger.info("[PaymentMoMoResponse] rawData: " + responserawData);

            return captureMoMoResponse;

        } catch (Exception exception) {
            logger.error("[PaymentMoMoResponse] " + exception);
            throw new IllegalArgumentException("Invalid params capture MoMo Request");
        }
    }

    /**
     * @param orderId
     * @param requestId
     * @param amount
     * @param orderInfo
     * @param returnUrl
     * @param notifyUrl
     * @param extraData
     * @return
     */
    public PaymentRequest createPaymentCreationRequest(String orderId, String requestId, String amount, String orderInfo,
                                                       String returnUrl, String notifyUrl, String extraData, RequestType requestType, Boolean autoCapture) {

        try {
            String requestRawData = new StringBuilder()
                    .append(Parameter.ACCESS_KEY).append("=").append(partnerInfo.getAccessKey()).append("&")
                    .append(Parameter.AMOUNT).append("=").append(amount).append("&")
                    .append(Parameter.EXTRA_DATA).append("=").append(extraData).append("&")
                    .append(Parameter.IPN_URL).append("=").append(notifyUrl).append("&")
                    .append(Parameter.ORDER_ID).append("=").append(orderId).append("&")
                    .append(Parameter.ORDER_INFO).append("=").append(orderInfo).append("&")
                    .append(Parameter.PARTNER_CODE).append("=").append(partnerInfo.getPartnerCode()).append("&")
                    .append(Parameter.REDIRECT_URL).append("=").append(returnUrl).append("&")
                    .append(Parameter.REQUEST_ID).append("=").append(requestId).append("&")
                    .append(Parameter.REQUEST_TYPE).append("=").append(requestType.getRequestType())
                    .toString();

            String signRequest = Encoder.signHmacSHA256(requestRawData, partnerInfo.getSecretKey());
            logger.debug("[PaymentRequest] rawData: " + requestRawData + ", [Signature] -> " + signRequest);

            return new PaymentRequest(partnerInfo.getPartnerCode(), orderId, requestId, Language.EN, orderInfo, Long.valueOf(amount), "test MoMo", null, requestType,
                    returnUrl, notifyUrl, "test store ID", extraData, null, autoCapture, null, signRequest);
        } catch (Exception e) {
            logger.error("[PaymentRequest] " + e);
        }

        return null;
    }
}
