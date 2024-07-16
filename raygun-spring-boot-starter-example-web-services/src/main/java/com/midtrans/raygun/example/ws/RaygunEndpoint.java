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

import com.midtrans.raygun.RaygunTemplate;
import com.midtrans.raygun.example.CaughtRaygunException;
import com.midtrans.raygun.example.UncaughtRaygunException;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;

/**
 * An {@link Endpoint} that sends exceptions through a {@link RaygunTemplate} and throws uncaught
 * exceptions.
 *
 * @author Raydhitya Yoseph
 */
@Endpoint
class RaygunEndpoint {
  private final RaygunTemplate raygunTemplate;

  RaygunEndpoint(RaygunTemplate raygunTemplate) {
    this.raygunTemplate = raygunTemplate;
  }

  @PayloadRoot(localPart = "caught")
  void caught() {
    try {
      throw new CaughtRaygunException();
    } catch (CaughtRaygunException ex) {
      raygunTemplate.send(ex);
    }
  }

  @PayloadRoot(localPart = "uncaught")
  void uncaught() {
    throw new UncaughtRaygunException();
  }

  @PayloadRoot(localPart = "soapFault")
  void soapFault() {
    throw new SoapFaultRaygunException();
  }
}
