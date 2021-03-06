/* Copyright 2018 Urban Airship and Contributors */

package com.urbanairship.push;

import android.support.annotation.NonNull;

import com.google.common.collect.Lists;
import com.urbanairship.AirshipConfigOptions;
import com.urbanairship.BaseTestCase;
import com.urbanairship.TestRequest;
import com.urbanairship.UAirship;
import com.urbanairship.http.Request;
import com.urbanairship.http.RequestFactory;
import com.urbanairship.http.Response;
import com.urbanairship.job.JobInfo;
import com.urbanairship.json.JsonException;
import com.urbanairship.json.JsonMap;
import com.urbanairship.json.JsonSerializable;
import com.urbanairship.json.JsonValue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class TagGroupApiClientTest extends BaseTestCase {


    private TestRequest testRequest;
    private AirshipConfigOptions configOptions;
    private RequestFactory requestFactory;
    private TagGroupsMutation mutation;

    @Before
    public void setUp() {
        configOptions = new AirshipConfigOptions.Builder()
                .setDevelopmentAppKey("appKey")
                .setDevelopmentAppSecret("appSecret")
                .setInProduction(false)
                .setHostURL("https://test.urbanairship.com/")
                .build();

        testRequest = new TestRequest();
        testRequest.response = new Response.Builder(HttpURLConnection.HTTP_OK)
                .setResponseMessage("OK")
                .setResponseBody("{ \"ok\": true}")
                .create();

        requestFactory = new RequestFactory() {
            @NonNull
            @Override
            public Request createRequest(String requestMethod, URL url) {
                testRequest.setURL(url);
                testRequest.setRequestMethod(requestMethod);

                return testRequest;
            }
        };

        mutation = TagGroupsMutation.newAddTagsMutation("test", new HashSet<>(Lists.newArrayList("tag1", "tag2")));
    }

    /**
     * Test android channel update.
     */
    @Test
    public void testAndroidChannelTagUpdate() throws JsonException {
        TagGroupApiClient client = new TagGroupApiClient(UAirship.ANDROID_PLATFORM, configOptions, requestFactory);

        Response response = client.updateTagGroups(TagGroupRegistrar.CHANNEL,"identifier", mutation);
        assertEquals(testRequest.response, response);
        assertEquals("https://test.urbanairship.com/api/channels/tags/", testRequest.getURL().toString());
        assertEquals("POST", testRequest.getRequestMethod());

        JsonMap expectedBody = JsonMap.newBuilder()
                                      .put("audience", JsonMap.newBuilder()
                                                              .put("android_channel", "identifier")
                                                              .build())
                                      .putAll(mutation.toJsonValue().getMap())
                                      .build();


        JsonValue requestBody = JsonValue.parseString(testRequest.getRequestBody());
        assertEquals(expectedBody.toJsonValue(), requestBody);
    }

    /**
     * Test amazon channel update.
     */
    @Test
    public void testAmazonChannelTagUpdate() throws JsonException {
        TagGroupApiClient client = new TagGroupApiClient(UAirship.AMAZON_PLATFORM, configOptions, requestFactory);

        Response response = client.updateTagGroups(TagGroupRegistrar.CHANNEL, "identifier", mutation);
        assertEquals(testRequest.response, response);
        assertEquals("https://test.urbanairship.com/api/channels/tags/", testRequest.getURL().toString());
        assertEquals("POST", testRequest.getRequestMethod());

        JsonMap expectedBody = JsonMap.newBuilder()
                                      .put("audience", JsonMap.newBuilder()
                                                              .put("amazon_channel", "identifier")
                                                              .build())
                                      .putAll(mutation.toJsonValue().getMap())
                                      .build();



        JsonValue requestBody = JsonValue.parseString(testRequest.getRequestBody());
        assertEquals(expectedBody.toJsonValue(), requestBody);
    }

    /**
     * Test named user update.
     */
    @Test
    public void testNamedUserTagUpdate() throws JsonException {
        TagGroupApiClient client = new TagGroupApiClient(UAirship.ANDROID_PLATFORM, configOptions, requestFactory);

        Response response = client.updateTagGroups(TagGroupRegistrar.NAMED_USER, "identifier", mutation);
        assertEquals(testRequest.response, response);
        assertEquals("https://test.urbanairship.com/api/named_users/tags/", testRequest.getURL().toString());
        assertEquals("POST", testRequest.getRequestMethod());


        JsonMap expectedBody = JsonMap.newBuilder()
                                      .put("audience", JsonMap.newBuilder()
                                                              .put("named_user_id", "identifier")
                                                              .build())
                                      .putAll(mutation.toJsonValue().getMap())
                                      .build();


        JsonValue requestBody = JsonValue.parseString(testRequest.getRequestBody());
        assertEquals(expectedBody.toJsonValue(), requestBody);
    }
}
