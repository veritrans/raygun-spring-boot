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

package com.midtrans.raygun.ws;

import com.midtrans.raygun.RaygunTemplate;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ws.server.EndpointExceptionResolver;

/**
 * {@link Configuration} for Raygun Web Services integration.
 *
 * @author Raydhitya Yoseph
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(EndpointExceptionResolver.class)
public class RaygunWebServicesConfiguration {

  @Bean
  @ConditionalOnMissingBean(RaygunEndpointExceptionResolver.class)
  EndpointExceptionResolver raygunEndpointExceptionResolver(RaygunTemplate raygunTemplate) {
    return new RaygunEndpointExceptionResolver(raygunTemplate);
  }
}
