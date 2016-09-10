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
import org.sonar.api.server.authentication.UserIdentity;

import javax.annotation.Nullable;

import static java.lang.String.format;

/**
 * Converts GitHub JSON response to {@link UserIdentity}
 */
@ServerSide
public class UserIdentityFactory {

    private final LeanSWOAuth2Settings settings;

    public UserIdentityFactory(LeanSWOAuth2Settings settings) {
        this.settings = settings;
    }

    public UserIdentity create(User user, @Nullable String email) {
        UserIdentity.Builder builder = UserIdentity.builder()
                .setProviderLogin(user.getId())
                .setLogin(generateLogin(user))
                .setName(generateName(user))
                .setEmail(email);
        return builder.build();
    }

    private String generateLogin(User gsonUser) {
        return gsonUser.getId();
    }

    private static String generateName(User gson) {
        return gson.getId();
    }

    private static String generateUniqueLogin(User gsonUser) {
        return format("%s@%s", gsonUser.getId(), LeanSWOAuth2IdentityProvider.KEY);
    }

}
