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

package com.midtrans.raygun;

import static com.midtrans.raygun.AutoConfigurationImportedCondition.importedAutoConfiguration;
import static org.assertj.core.api.Assertions.assertThat;

import com.midtrans.raygun.autoconfigure.RaygunAutoConfiguration;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.webservices.client.WebServiceClientTest;
import org.springframework.boot.test.autoconfigure.webservices.server.WebServiceServerTest;
import org.springframework.context.ApplicationContext;

/**
 * Test for additional {@link org.springframework.boot.autoconfigure.ImportAutoConfiguration
 * Auto-configuration imports}.
 *
 * @author Raydhitya Yoseph
 * @see <a
 *     href="https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#features.testing.spring-boot-applications.autoconfigured-tests">https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#features.testing.spring-boot-applications.autoconfigured-tests</a>
 * @see <a
 *     href="https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#features.testing.spring-boot-applications.additional-autoconfiguration-and-slicing">https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#features.testing.spring-boot-applications.additional-autoconfiguration-and-slicing</a>
 * @see org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
 * @see org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc
 * @see org.springframework.boot.test.autoconfigure.webservices.server.WebServiceServerTest
 * @see org.springframework.boot.test.autoconfigure.webservices.server.AutoConfigureWebServiceServer
 */
class TestSliceRaygunAutoConfigurationTest {

  @Nested
  @WebMvcTest
  class WhenWebMvcTestConfigured {

    @Autowired ApplicationContext applicationContext;

    @Test
    void shouldImportRaygunAutoConfiguration() {
      assertThat(applicationContext).has(importedAutoConfiguration(RaygunAutoConfiguration.class));
    }
  }

  @Nested
  @RestClientTest
  class WhenWebMvcTestNotConfigured {

    @Autowired ApplicationContext applicationContext;

    @Test
    void shouldNotImportRaygunAutoConfiguration() {
      assertThat(applicationContext)
          .doesNotHave(importedAutoConfiguration(RaygunAutoConfiguration.class));
    }
  }

  @Nested
  @WebServiceServerTest
  class WhenWebServiceServerTestConfigured {

    @Autowired ApplicationContext applicationContext;

    @Test
    void shouldImportRaygunAutoConfiguration() {
      assertThat(applicationContext).has(importedAutoConfiguration(RaygunAutoConfiguration.class));
    }
  }

  @Nested
  @WebServiceClientTest
  class WhenWebServiceClientTestConfigured {

    @Autowired ApplicationContext applicationContext;

    @Test
    void shouldNotImportRaygunAutoConfiguration() {
      assertThat(applicationContext)
          .doesNotHave(importedAutoConfiguration(RaygunAutoConfiguration.class));
    }
  }
}
