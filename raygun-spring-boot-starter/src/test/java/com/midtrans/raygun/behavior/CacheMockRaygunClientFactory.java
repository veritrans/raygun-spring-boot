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

package com.midtrans.raygun.behavior;

import com.midtrans.raygun.test.MockRaygunClientFactory;
import com.mindscapehq.raygun4java.core.RaygunClient;
import com.mindscapehq.raygun4java.core.RaygunClientFactory;

/**
 * {@link RaygunClientFactory} which caches and returns the supplied {@link RaygunClient}.
 *
 * @author Raydhitya Yoseph
 */
class CacheMockRaygunClientFactory extends MockRaygunClientFactory {
  private final RaygunClient raygunClient;

  public CacheMockRaygunClientFactory(RaygunClient raygunClient) {
    this.raygunClient = raygunClient;
  }

  @Override
  public RaygunClient newClient() {
    return raygunClient;
  }
}
