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

import static org.assertj.core.api.Assertions.assertThat;

import com.midtrans.raygun.RaygunTemplate;
import com.mindscapehq.raygun4java.core.RaygunClient;
import com.mindscapehq.raygun4java.core.RaygunClientFactory;

import org.junit.jupiter.api.Test;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.TimeUnit;

/**
 * This test compares async and sync {@link RaygunClient}s sending count.
 *
 * @author Raydhitya Yoseph
 */
class RaygunTemplateMessagesSendingTest {

  @Test
  void asyncCountShouldBeGreaterThanSyncCount() throws InterruptedException {
    DelayedMockRaygunClient syncRaygunClient = new DelayedMockRaygunClient(100);
    Thread syncRaygunThread = raygunThread(syncRaygunClient, new SyncTaskExecutor());
    syncRaygunThread.start();

    DelayedMockRaygunClient asyncRaygunClient = new DelayedMockRaygunClient(100);
    Thread asyncRaygunThread = raygunThread(asyncRaygunClient, threadPoolTaskExecutor());
    asyncRaygunThread.start();

    TimeUnit.MILLISECONDS.sleep(1000);

    assertThat(asyncRaygunClient.count()).isGreaterThan(syncRaygunClient.count());
  }

  private Thread raygunThread(RaygunClient raygunClient, TaskExecutor taskExecutor) {
    return new Thread(raygunRunnable(raygunClient, taskExecutor));
  }

  private Runnable raygunRunnable(RaygunClient raygunClient, TaskExecutor taskExecutor) {
    RaygunClientFactory raygunClientFactory = new CacheMockRaygunClientFactory(raygunClient);
    RaygunTemplate raygunTemplate = new RaygunTemplate(raygunClientFactory, taskExecutor);
    return new RaygunRunnable(raygunTemplate, 200);
  }

  private ThreadPoolTaskExecutor threadPoolTaskExecutor() {
    ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
    threadPoolTaskExecutor.setCorePoolSize(8);
    threadPoolTaskExecutor.afterPropertiesSet();
    return threadPoolTaskExecutor;
  }
}
