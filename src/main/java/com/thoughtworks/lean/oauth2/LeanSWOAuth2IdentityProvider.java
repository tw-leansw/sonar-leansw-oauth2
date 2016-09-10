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

import org.sonar.api.server.ServerSide;
import org.sonar.api.server.authentication.Display;
import org.sonar.api.server.authentication.OAuth2IdentityProvider;
import org.sonar.api.server.authentication.UserIdentity;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@ServerSide
public class LeanSWOAuth2IdentityProvider implements OAuth2IdentityProvider {

    public static final String KEY = "leansw-oauth2";
    private static final Logger LOGGER = Loggers.get(LeanSWOAuth2IdentityProvider.class);

    private final LeanSWOAuth2Settings settings;
    private final UserIdentityFactory userIdentityFactory;
    private final OAuthServiceWrapper OAuth2API;

    public LeanSWOAuth2IdentityProvider(LeanSWOAuth2Settings settings, UserIdentityFactory userIdentityFactory, OAuthServiceWrapper scribeApi) {
        this.settings = settings;
        this.userIdentityFactory = userIdentityFactory;
        this.OAuth2API = scribeApi;
    }

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public String getName() {
        return "LeanSW-OAuth2";
    }

    @Override
    public Display getDisplay() {
        return Display.builder()
                // URL of src/main/resources/static/DeliFlow-Mark-Light-64px.png at runtime
                .setIconPath("/static/leanswoauth2/DeliFlow-Mark-Light-64px.png")
                .setBackgroundColor("#444444")
                .build();
    }

    @Override
    public boolean isEnabled() {
        return settings.isEnabled();
    }

    @Override
    public boolean allowsUsersToSignUp() {
        return settings.allowUsersToSignUp();
    }

    @Override
    public void init(InitContext context) {
        OAuthServiceWrapper scribe = new OAuthServiceWrapper(settings);
        String url = scribe.getAuthorizationUrl(context.getCallbackUrl());
        context.redirectTo(url);
    }

    @Override
    public void callback(CallbackContext context) {
        try {
            onCallback(context);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void onCallback(CallbackContext context) throws IOException {
        context.verifyCsrfState();
        HttpServletRequest request = context.getRequest();
        OAuthServiceWrapper scribe = new OAuthServiceWrapper(settings);
        String code = request.getParameter("code");
        String accessToken = scribe.getAccessToken(code, context.getCallbackUrl());
        User user = getUser(accessToken);
        UserIdentity userIdentity = userIdentityFactory.create(user, null);
        context.authenticate(userIdentity);
        context.redirectToRequestedPage();
    }


    private User getUser(String accessToken) throws IOException {
        String responseBody = OAuth2API.getProfile(accessToken);
        LOGGER.trace("User response received : {}", responseBody);
        return User.parse(responseBody);
    }

}
