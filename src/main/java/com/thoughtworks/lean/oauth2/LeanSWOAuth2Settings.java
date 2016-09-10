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

import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.config.Settings;
import org.sonar.api.server.ServerSide;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static org.sonar.api.PropertyType.*;

@ServerSide
public class LeanSWOAuth2Settings {

    private static final String CLIENT_ID = "sonar.auth.leansw.ouath2.clientId.secured";
    private static final String CLIENT_SECRET = "sonar.auth.leansw.ouath2.clientSecret.secured";
    private static final String ENABLED = "sonar.auth.leansw.ouath2.enabled";
    private static final String ALLOW_USERS_TO_SIGN_UP = "sonar.auth.leansw.ouath2.allowUsersToSignUp";
    private static final String GROUPS_SYNC = "sonar.auth.leansw.ouath2.groupsSync";
    private static final String API_URL = "sonar.auth.leansw.ouath2.apiUrl";
    private static final String WEB_URL = "sonar.auth.leansw.ouath2.webUrl";
    static final String LOGIN_STRATEGY = "sonar.auth.leansw.ouath2.loginStrategy";
    static final String LOGIN_STRATEGY_PROVIDER_ID = "Same as GitHub login";

    private static final String CATEGORY = "tw-leansw";
    private static final String SUBCATEGORY = "authentication";

    private final Settings settings;

    public LeanSWOAuth2Settings(Settings settings) {
        this.settings = settings;
    }

    public String clientId() {
        return emptyIfNull(settings.getString(CLIENT_ID));
    }

    public String clientSecret() {
        return emptyIfNull(settings.getString(CLIENT_SECRET));
    }

    public boolean isEnabled() {
        return settings.getBoolean(ENABLED) && !clientId().isEmpty() && !clientSecret().isEmpty();
    }

    public boolean allowUsersToSignUp() {
        return settings.getBoolean(ALLOW_USERS_TO_SIGN_UP);
    }

    public String loginStrategy() {
        return emptyIfNull(settings.getString(LOGIN_STRATEGY));
    }

    public boolean syncGroups() {
        return settings.getBoolean(GROUPS_SYNC);
    }

    @CheckForNull
    public String webURL() {
        return urlWithEndingSlash(settings.getString(WEB_URL));
    }

    @CheckForNull
    public String apiURL() {
        return urlWithEndingSlash(settings.getString(API_URL));
    }

    @CheckForNull
    private static String urlWithEndingSlash(@Nullable String url) {
        if (url != null && !url.endsWith("/")) {
            return url + "/";
        }
        return url;
    }

    private static String emptyIfNull(@Nullable String s) {
        return s == null ? "" : s;
    }

    public static List<PropertyDefinition> definitions() {
        int index = 1;
        return Arrays.asList(
                PropertyDefinition.builder(ENABLED)
                        .name("Enabled")
                        .description("Enable LeanSW OAuth2.0 Login plugin. Value is ignored if client ID and secret are not defined.")
                        .category(CATEGORY)
                        .subCategory(SUBCATEGORY)
                        .type(BOOLEAN)
                        .defaultValue(valueOf(false))
                        .index(index++)
                        .build(),
                PropertyDefinition.builder(CLIENT_ID)
                        .name("Client ID")
                        .description("Client ID provided by GitHub when registering the application.")
                        .category(CATEGORY)
                        .subCategory(SUBCATEGORY)
                        .index(index++)
                        .build(),
                PropertyDefinition.builder(CLIENT_SECRET)
                        .name("Client Secret")
                        .description("Client password provided by GitHub when registering the application.")
                        .category(CATEGORY)
                        .subCategory(SUBCATEGORY)
                        .index(index++)
                        .build(),
                PropertyDefinition.builder(ALLOW_USERS_TO_SIGN_UP)
                        .name("Allow users to sign-up")
                        .description("Allow new users to authenticate. When set to 'false', only existing users will be able to authenticate to the server.")
                        .category(CATEGORY)
                        .subCategory(SUBCATEGORY)
                        .type(BOOLEAN)
                        .defaultValue(valueOf(true))
                        .index(index++)
                        .build(),
                PropertyDefinition.builder(API_URL)
                        .name("The API url for a LeanSW OAuth2.0 instance.")
                        .description("The API url for a LeanSW OAuth2.0 instance. http://cas.dev.twleansw.com:9080/")
                        .category(CATEGORY)
                        .subCategory(SUBCATEGORY)
                        .type(STRING)
                        .defaultValue(valueOf("http://cas.dev.twleansw.com:9080/"))
                        .index(index++)
                        .build(),
                PropertyDefinition.builder(WEB_URL)
                        .name("The WEB url for a LeanSW OAuth2.0  instance.")
                        .description("The WEB url for a LeanSW OAuth2.0  instance. " +
                                "https://cas.dev.twleansw.com/ for github.com.")
                        .category(CATEGORY)
                        .subCategory(SUBCATEGORY)
                        .type(STRING)
                        .defaultValue(valueOf("https://cas.dev.twleansw.com/"))
                        .index(index++)
                        .build());
    }
}
