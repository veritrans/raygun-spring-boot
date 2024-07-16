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

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * An {@link ApplicationRunner} that sends exceptions through a {@link RaygunTemplate}.
 *
 * @author Raydhitya Yoseph
 */
@Component
class RaygunApplicationRunner implements ApplicationRunner {
  private final RaygunTemplate raygunTemplate;

  RaygunApplicationRunner(RaygunTemplate raygunTemplate) {
    this.raygunTemplate = raygunTemplate;
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    raygunTemplate.send(new UncaughtRaygunException());
    raygunTemplate.send(new ExcludedRaygunException());
  }
}
