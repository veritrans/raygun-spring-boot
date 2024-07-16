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

package com.midtrans.raygun.example;

import com.midtrans.raygun.RaygunTemplate;
import com.midtrans.raygun.test.MockRaygunClientFactory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Test for {@link RaygunTemplate} auto-configuration and Raygun test context customization.
 *
 * @author Raydhitya Yoseph
 */
@SpringBootTest
class RaygunTest {

  @Autowired RaygunTemplate raygunTemplate;

  @Autowired MockRaygunClientFactory mockRaygunClientFactory;

  @Test
  void contextLoads() {
    raygunTemplate.send(new CaughtRaygunException());
  }
}
