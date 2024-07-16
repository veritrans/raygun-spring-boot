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

package com.midtrans.raygun.example.web;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.midtrans.raygun.test.MockRaygunClientFactory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

/**
 * Test for {@link RaygunController} and Raygun test context customization.
 *
 * @author Raydhitya Yoseph
 */
@WebMvcTest(RaygunController.class)
class RaygunControllerTest {

  @Autowired MockMvc mockMvc;

  @Autowired MockRaygunClientFactory mockRaygunClientFactory;

  @Test
  void caught() throws Exception {
    mockMvc.perform(get("/caught")).andExpect(status().isNotImplemented());
  }

  /**
   * @see https://github.com/spring-projects/spring-boot/issues/7321
   */
  @Test
  void uncaught() throws Exception {
    assertThatCode(
            () -> mockMvc.perform(get("/uncaught")).andExpect(status().isInternalServerError()))
        .isInstanceOf(NestedServletException.class);
  }
}
