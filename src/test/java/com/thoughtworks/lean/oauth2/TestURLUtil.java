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

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by yongliuli on 9/10/16.
 */
public class TestURLUtil {
    @Test
    public void should_get_return_url_from_url() {
        String retUrl = URLUtil.getReturnUrlFromReferer("https://sonar.dev.twleansw.com/sessions/new?return_to=%2F");
        Assert.assertEquals("https://sonar.dev.twleansw.com/",retUrl);
    }

    @Test
    public void should_get_return_url_from_url2() {
        String retUrl = URLUtil.getReturnUrlFromReferer("https://sonar.dev.twleansw.com:9443/sessions/new?return_to=%2Fhelp");
        Assert.assertEquals("https://sonar.dev.twleansw.com:9443/help",retUrl);
    }
}
