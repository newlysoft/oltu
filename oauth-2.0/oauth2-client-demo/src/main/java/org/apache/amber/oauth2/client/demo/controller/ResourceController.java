/**
 *       Copyright 2010 Newcastle University
 *
 *          http://research.ncl.ac.uk/smart/
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.amber.oauth2.client.demo.controller;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.servlet.http.HttpServletRequest;

import org.apache.amber.oauth2.client.demo.Utils;
import org.apache.amber.oauth2.client.demo.model.OAuthParams;
import org.apache.amber.oauth2.common.utils.OAuthUtils;
import org.apache.amber.oauth2.common.OAuth;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Maciej Machulak (m.p.machulak@ncl.ac.uk)
 * @author Lukasz Moren (lukasz.moren@ncl.ac.uk)
 * @author Aad van Moorsel (aad.vanmoorsel@ncl.ac.uk)
 */
@Controller
@RequestMapping("/get_resource")
public class ResourceController {

    @RequestMapping
    public ModelAndView authorize(@ModelAttribute("oauthParams") OAuthParams oauthParams,
                                  HttpServletRequest req) {

        try {
            String tokenName = OAuth.OAUTH_TOKEN_DRAFT_0;
            if (Utils.SMART_GALLERY.equals(oauthParams.getApplication())) {
                tokenName = OAuth.OAUTH_TOKEN;
            }
            URL url = new URL(
                oauthParams.getResourceUrl() + "?" + tokenName + "=" + oauthParams.getAccessToken());
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            if (conn.getResponseCode() == 200) {
                oauthParams.setResource(OAuthUtils.saveStreamAsString(conn.getInputStream()));
            } else {
                oauthParams.setErrorMessage(
                    "Could not access resource: " + conn.getResponseCode() + " " + conn.getResponseMessage());
            }
        } catch (IOException e) {
            oauthParams.setErrorMessage(e.getMessage());
        }

        return new ModelAndView("resource");


    }
}
