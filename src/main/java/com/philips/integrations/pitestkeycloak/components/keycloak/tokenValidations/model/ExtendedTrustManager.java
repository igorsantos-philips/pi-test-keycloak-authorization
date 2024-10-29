package com.philips.integrations.pitestkeycloak.components.keycloak.tokenValidations.model;

import java.net.Socket;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.X509ExtendedTrustManager;

public class ExtendedTrustManager extends X509ExtendedTrustManager{

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[]{};
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) {
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType, Socket socket) {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType, Socket socket) {
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType, SSLEngine engine) {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType, SSLEngine engine) {
    }

}