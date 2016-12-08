package com.piggymetrics.account;

import com.jayway.restassured.RestAssured;

import static com.jayway.restassured.filter.log.LogDetail.ALL;
import static com.jayway.restassured.filter.log.LogDetail.BODY;
import static org.hamcrest.core.Is.is;

import com.jayway.restassured.builder.ResponseBuilder;
import com.jayway.restassured.filter.Filter;
import com.jayway.restassured.filter.FilterContext;
import com.jayway.restassured.filter.log.LogDetail;
import com.jayway.restassured.internal.RestAssuredResponseImpl;
import com.jayway.restassured.internal.print.RequestPrinter;
import com.jayway.restassured.internal.print.ResponsePrinter;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ResponseBody;
import com.jayway.restassured.response.ResponseOptions;
import com.jayway.restassured.specification.FilterableRequestSpecification;
import com.jayway.restassured.specification.FilterableResponseSpecification;
import com.piggymetrics.emulators.AuthServiceEmulatorClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Attachment;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.allure.annotations.Title;

import java.io.PrintStream;

import static com.jayway.restassured.RestAssured.given;
import static java.net.HttpURLConnection.*;

/**
 * Created by dmitry on 12/7/16.
 */
public class AccountServiceTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private AuthServiceEmulatorClient authServiceClient = new AuthServiceEmulatorClient();

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = System.getenv("ACCOUNT_SERVICE_HOST"); //"http://account-service";
        RestAssured.port = Integer.parseInt(System.getenv("ACCOUNT_SERVICE_PORT"));
        RestAssured.basePath = System.getenv("ACCOUNT_SERVICE_BASE_PATH");
        RestAssured.filters(new AllureRequestLoggingFilter(), new AllureResponseLoggingFilter());
    }

    @Title("Unauthorized users could't get current user info")
    @Test
    public void unauthorizedUsersShouldNotGetAccountInformation() {

        logInfo("Mock auth service for return unauthorized response");
        authServiceClient.mockUnauthorizedResponse(null);

        logInfo("Get current user account");
        given()
        .when()
            .get("/admin")
        .then()
            .assertThat().statusCode(HTTP_UNAUTHORIZED)
            .assertThat().body("error", is("unauthorized"));

    }

    @Title("Authorized users could get current user info")
    @Test
    public void authorizedUsersShouldGetAccountInformation() {

        logInfo("Mock auth service for return authorized response");
        authServiceClient.mockAuthorizedResponse(null);

        getCurrentUserAccount();

    }

    @Step("{0}")
    private void logInfo(String s) {
        log.info(s);
    }

    @Step("{method}")
    private void getCurrentUserAccount() {
        given()
                .auth().oauth2("SOME_TOKEN")
                .when()
                    .get("/demo")
                .then()
            .statusCode(HTTP_OK);
    }


    class AllureRequestLoggingFilter implements Filter {

        @Override
        public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
            print(requestSpec, responseSpec, ctx);
            return ctx.next(requestSpec, responseSpec);
        }

        @Attachment("Request")
        String print(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
            return  RequestPrinter.print(requestSpec, requestSpec.getMethod().toString(), requestSpec.getURI(), LogDetail.ALL, System.out, true);
        }
    }

    class AllureResponseLoggingFilter implements Filter {

        @Override
        public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
            Response response = ctx.next(requestSpec, responseSpec);
            LogDetail logDetail = LogDetail.ALL;
            print(response, response, System.out, logDetail, true);
            final byte[] responseBody;
                responseBody = response.asByteArray();
            response = cloneResponseIfNeeded(response, responseBody);
            return response;
        }

        private Response cloneResponseIfNeeded(Response response, byte[] responseAsString) {
            if (responseAsString != null && response instanceof RestAssuredResponseImpl && !((RestAssuredResponseImpl) response).getHasExpectations()) {
                final Response build = new ResponseBuilder().clone(response).setBody(responseAsString).build();
                ((RestAssuredResponseImpl) build).setHasExpectations(true);
                return build;
            }
            return response;
        }


        @Attachment("Response")
        String print(ResponseOptions responseOptions, ResponseBody responseBody, PrintStream stream, LogDetail logDetail, boolean shouldPrettyPrint) {
            return  ResponsePrinter.print(responseOptions, responseBody, stream, logDetail, shouldPrettyPrint);
        }
    }
}
