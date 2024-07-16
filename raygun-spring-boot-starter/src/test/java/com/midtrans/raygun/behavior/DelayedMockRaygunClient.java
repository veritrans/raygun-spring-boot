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

import com.midtrans.raygun.test.MockRaygunClient;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * A {@link MockRaygunClient} that delays and counts the number of sending.
 *
 * @author Raydhitya Yoseph
 */
class DelayedMockRaygunClient extends MockRaygunClient {
  private final long delayMilliseconds;

  DelayedMockRaygunClient(long delayMilliseconds) {
    this.delayMilliseconds = delayMilliseconds;
  }

  @Override
  public int send(String payload) throws IOException {
    try {
      TimeUnit.MILLISECONDS.sleep(delayMilliseconds);
    } catch (InterruptedException e) {

    }
    return super.send(payload);
  }
}
