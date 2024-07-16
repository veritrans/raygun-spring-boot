/*
 * Copyright 2022-2024 authors.
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

import com.midtrans.raygun.example.RaygunException;

import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;
import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

/**
 * A {@link RuntimeException} that will be thrown from an {@code @Endpoint} handler method and
 * handled by the {@link SoapFaultMappingExceptionResolver}.
 *
 * @author Raydhitya Yoseph
 */
@SoapFault(faultCode = FaultCode.SERVER, faultStringOrReason = "soapFault")
class SoapFaultRaygunException extends RaygunException {
  private static final long serialVersionUID = -4457284250653408805L;
}
