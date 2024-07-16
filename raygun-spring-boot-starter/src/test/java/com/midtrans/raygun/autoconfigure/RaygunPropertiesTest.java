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

package com.midtrans.raygun.autoconfigure;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Test for {@link RaygunProperties}.
 *
 * @author Raydhitya Yoseph
 */
class RaygunPropertiesTest {
  private RaygunProperties raygunProperties = new RaygunProperties();

  @Nested
  class IsProxyConfiguredWhenProxyNull {

    @Test
    void shouldReturnFalse() {
      assertThat(raygunProperties.isProxyConfigured()).isFalse();
    }
  }

  @Nested
  class IsProxyConfiguredWhenProxyNotNull {

    @Test
    void andHostNullShouldReturnFalse() {
      raygunProperties.setProxy(new RaygunProperties.Proxy());

      assertThat(raygunProperties.isProxyConfigured()).isFalse();
    }

    @Test
    void andHostNotNullAndPortNullShouldReturnFalse() {
      RaygunProperties.Proxy proxy = new RaygunProperties.Proxy();
      proxy.setHost("host");

      raygunProperties.setProxy(proxy);

      assertThat(raygunProperties.isProxyConfigured()).isFalse();
    }

    @Test
    void andHostNotNullAndPortNotNullShouldReturnTrue() {
      RaygunProperties.Proxy proxy = new RaygunProperties.Proxy();
      proxy.setHost("host");
      proxy.setPort(Integer.valueOf(3128));

      raygunProperties.setProxy(proxy);

      assertThat(raygunProperties.isProxyConfigured()).isTrue();
    }
  }
}
