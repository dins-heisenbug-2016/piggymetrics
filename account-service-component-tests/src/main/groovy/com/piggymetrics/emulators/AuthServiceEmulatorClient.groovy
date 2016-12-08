package com.piggymetrics.emulators

import com.github.tomakehurst.wiremock.client.WireMock

/**
 * Created by dmitry on 02.06.16.
 */
class AuthServiceEmulatorClient {
    private WireMock wireMock;

    AuthServiceEmulatorClient() {
        // Configure wiremock
        wireMock = new WireMock();
    }

    void mockUnauthorizedResponse(Map context) {

    }

    void mockAuthorizedResponse(Map context) {

    }
}
