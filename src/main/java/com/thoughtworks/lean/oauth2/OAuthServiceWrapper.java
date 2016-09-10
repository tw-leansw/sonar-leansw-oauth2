/*
 * LeanSW OAuth 2.0 Authentication for SonarQube
 * Copyright (C) 2016-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.thoughtworks.lean.oauth2;


import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.sonar.api.internal.apachecommons.io.IOUtils;
import org.sonar.api.server.ServerSide;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@ServerSide
public class OAuthServiceWrapper {
    private final LeanSWOAuth2Settings settings;
    private OkHttpClient client = new OkHttpClient();

    private static final Logger LOGGER = Loggers.get(OAuthServiceWrapper.class);
    private static final String ACCESS_TOKEN_PARAMETER_NAME = "access_token";


    public OAuthServiceWrapper(LeanSWOAuth2Settings settings) {
        this.settings = settings;
    }

    public String getAccessTokenEndpoint() {
        return settings.apiURL() + "cas/oauth2.0/accessToken";
    }

    public String getProfileEndPoint() {
        return settings.apiURL() + "cas/oauth2.0/profile";
    }

    protected String getAuthorizationBaseUrl() {
        return settings.webURL() + "cas/oauth2.0/authorize";
    }

    public String getAuthorizationUrl(String callbackUrl) {
        return String.format("%s?client_id=%s&response_type=code&redirect_uri=%s", getAuthorizationBaseUrl(), settings.clientId(), callbackUrl);
    }



    public String getAccessToken(String code, String callbackUrl) throws IOException {

        Request request = new Request.Builder()
                .url(String.format(
                        "%s?redirect_uri=%s&client_id=%s&client_secret=%s&code=%s",
                        getAccessTokenEndpoint(),
                        URLEncoder.encode(callbackUrl, "UTF-8"),
                        settings.clientId(),
                        settings.clientSecret(),
                        URLEncoder.encode(code, "UTF-8")

                ))
                .build();
        LOGGER.info("request uri: {}", request.url().toString());

        Response response = client.newCall(request).execute();


        return responseToAccessToken(response.body().string());
    }

    private String responseToAccessToken(String responseText) {
        LOGGER.info("responseToAccessToken: {}", responseText);
        String[] params = responseText.split("&");
        String token = "";
        for (String param : params) {
            if (param.startsWith(ACCESS_TOKEN_PARAMETER_NAME)) {
                token = param.substring(ACCESS_TOKEN_PARAMETER_NAME.length() + 1, param.length());
            }
        }
        return token;
    }


    public String getProfile(String accessToken) throws IOException {
        Request request = new Request.Builder()
                .url(String.format(
                        "%s?access_token=%s",
                        getProfileEndPoint(),
                        accessToken
                ))
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
