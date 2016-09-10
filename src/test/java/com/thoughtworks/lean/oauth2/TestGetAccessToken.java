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

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.mockito.Mockito.when;

/**
 * Created by yongliuli on 9/10/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class TestGetAccessToken {

    @Mock
    LeanSWOAuth2Settings leanSWOAuth2Settings;

    @Before
    public void setup() {
        when(leanSWOAuth2Settings.apiURL()).thenReturn("http://cas.dev.twleansw.com:9080/");
        when(leanSWOAuth2Settings.clientId()).thenReturn("clientid");
        when(leanSWOAuth2Settings.clientSecret()).thenReturn("clientSecret");

    }


    @Test
    @Ignore
    public void test() throws IOException {
        OAuthServiceWrapper oAuthServiceWrapper = new OAuthServiceWrapper(leanSWOAuth2Settings);
        oAuthServiceWrapper.getAccessToken2("ST-26-myePfsllQ3x5dHIzxLgC-cas.dev.twleansw.com:9080","https://sonar.dev.twleansw.com/oauth2/callback/leansw-oauth2");
    }
}
