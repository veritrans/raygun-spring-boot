/*
 * Copyright 2022-2023 authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.midtrans.raygun.example.ws;

import com.midtrans.raygun.test.MockRaygunClientFactory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.webservices.server.WebServiceServerTest;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.ws.test.server.RequestCreators;
import org.springframework.ws.test.server.ResponseMatchers;
import org.springframework.xml.transform.StringSource;

/**
 * Test for {@link RaygunEndpoint} and Raygun test context customization.
 *
 * @author Raydhitya Yoseph
 */
@WebServiceServerTest
class RaygunEndpointTest {

  @Autowired MockWebServiceClient mockWebServiceClient;

  @Autowired MockRaygunClientFactory raygunClientFactory;

  @Test
  void caught() {
    mockWebServiceClient
        .sendRequest(RequestCreators.withPayload(new StringSource("<caught></caught>")))
        .andExpect(ResponseMatchers.noFault());
  }

  @Test
  void uncaught() {
    mockWebServiceClient
        .sendRequest(RequestCreators.withPayload(new StringSource("<uncaught></uncaught>")))
        .andExpect(ResponseMatchers.serverOrReceiverFault());
  }

  @Test
  void soapFault() {
    mockWebServiceClient
        .sendRequest(RequestCreators.withPayload(new StringSource("<soapFault></soapFault>")))
        .andExpect(ResponseMatchers.serverOrReceiverFault("soapFault"));
  }
}
