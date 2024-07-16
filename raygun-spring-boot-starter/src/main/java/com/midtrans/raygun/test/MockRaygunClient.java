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

package com.midtrans.raygun.test;

import com.mindscapehq.raygun4java.core.RaygunClient;
import com.mindscapehq.raygun4java.core.messages.RaygunMessage;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Mock implementation of the {@link RaygunClient} for testing purposes.
 *
 * @author Raydhitya Yoseph
 */
public class MockRaygunClient extends RaygunClient {
  private final List<RaygunMessage> raygunMessages;

  public MockRaygunClient() {
    super("apiKey");
    this.raygunMessages = new ArrayList<>();
  }

  @Override
  public int send(RaygunMessage raygunMessage) {
    raygunMessages.add(raygunMessage);
    return super.send(raygunMessage);
  }

  @Override
  public int send(String payload) throws IOException {
    return HttpURLConnection.HTTP_OK;
  }

  public int count() {
    return raygunMessages.size();
  }
}
