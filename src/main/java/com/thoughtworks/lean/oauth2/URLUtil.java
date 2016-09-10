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

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

/**
 * Created by yongliuli on 9/10/16.
 */
public class URLUtil {
    public static String getReturnUrlFromReferer(String referer) {
        String retUrl = "/";
        try {
            URL url = new URL(referer);
            String query = url.getQuery();
            retUrl = url.getProtocol() + "://" + url.getHost();
            if (url.getPort() > 0 && url.getPort() != url.getDefaultPort()) {
                retUrl += ":" + url.getPort();
            }


            String[] params = query.split("&");
            for (String param : params) {
                if (param.startsWith("return_to")) {
                    retUrl += URLDecoder.decode(param.substring("return_to".length() + 1), "UTF-8");
                }
            }
        } catch (MalformedURLException e) {

        } catch (UnsupportedEncodingException e) {

        }
        return retUrl;
    }
}
