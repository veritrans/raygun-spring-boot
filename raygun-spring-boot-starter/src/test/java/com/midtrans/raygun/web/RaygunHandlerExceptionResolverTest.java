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

package com.midtrans.raygun.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.verify;

import com.midtrans.raygun.RaygunTemplate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Test for {@link RaygunHandlerExceptionResolver}.
 *
 * @author Raydhitya Yoseph
 */
@ExtendWith(MockitoExtension.class)
class RaygunHandlerExceptionResolverTest {

  @Mock RaygunTemplate raygunTemplate;

  @InjectMocks RaygunHandlerExceptionResolver raygunExceptionResolver;

  @Test
  void doResolveExceptionShouldCallRaygunTemplateSend() {
    RuntimeException exception = new RuntimeException();

    assertThat(raygunExceptionResolver.doResolveException(null, null, null, exception)).isNull();

    verify(raygunTemplate, atMostOnce()).send(eq(exception));
  }
}
