package com.tasc.hongquan.paymentservice.services;

import com.tasc.hongquan.paymentservice.config.Environment;
import com.tasc.hongquan.paymentservice.config.PartnerInfo;
import com.tasc.hongquan.paymentservice.exception.MoMoException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tasc.hongquan.paymentservice.util.Execute;


public abstract class AbstractProcess<T, V> {


    protected PartnerInfo partnerInfo;
    protected Environment environment;
    protected Execute execute = new Execute();

    public AbstractProcess(Environment environment) {
        this.environment = environment;
        this.partnerInfo = environment.getPartnerInfo();
    }

    public static Gson getGson() {
        return new GsonBuilder()
                .disableHtmlEscaping()
                .create();
    }

    public abstract V execute(T request) throws MoMoException;
}