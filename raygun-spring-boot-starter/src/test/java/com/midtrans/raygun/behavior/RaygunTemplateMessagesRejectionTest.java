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

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.midtrans.raygun.RaygunTemplate;
import com.midtrans.raygun.test.MockRaygunClientFactory;
import com.mindscapehq.raygun4java.core.RaygunClientFactory;

import org.junit.jupiter.api.Test;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;

/**
 * This test logs a {@link TaskRejectedException} if test logging is on.
 *
 * <p>By default the auto-configured {@link ThreadPoolTaskExecutor} is configured with {@code
 * maxPoolSize} of {@code Integer.MAX_VALUE}, {@code queueCapacity} of {@code Integer.MAX_VALUE},
 * and {@link RejectedExecutionHandler} of {@link AbortPolicy}.
 *
 * <p>In most cases, the messages are not rejected. For a message to be rejected, it must come when
 * {@link ThreadPoolTaskExecutor}'s pool is at the maximum number of threads and queue is at the
 * maximum number of capacity. This event has a tiny probability to happen.
 *
 * <p>Using {@link ThreadPoolTaskExecutor} will prevent the request processing thread to not be
 * blocked by Raygun in the cost of more memory to hold the threads. Most of the time, user
 * applications have less memory than {@code Integer.MAX_VALUE}. The {@link ThreadPoolTaskExecutor}
 * can kill user application when the application cannot allocate more threads. When using the
 * default configuration, realistically, user applications will run out of memory before the {@link
 * ThreadPoolTaskExecutor} rejects any message.
 *
 * @author Raydhitya Yoseph
 */
class RaygunTemplateMessagesRejectionTest {

  @Test
  void asyncCouldRejectMessagesWhenQueueIsFull() throws InterruptedException {
    Runnable raygunRunnable = raygunRunnable(threadPoolTaskExecutor());
    assertThatThrownBy(raygunRunnable::run).isInstanceOf(TaskRejectedException.class);
  }

  private Runnable raygunRunnable(TaskExecutor taskExecutor) {
    RaygunClientFactory raygunClientFactory = new MockRaygunClientFactory();
    RaygunTemplate raygunTemplate = new RaygunTemplate(raygunClientFactory, taskExecutor);
    return new RaygunRunnable(raygunTemplate, 16);
  }

  private ThreadPoolTaskExecutor threadPoolTaskExecutor() {
    ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
    threadPoolTaskExecutor.setCorePoolSize(8);
    threadPoolTaskExecutor.setMaxPoolSize(8);
    threadPoolTaskExecutor.setQueueCapacity(0);
    threadPoolTaskExecutor.afterPropertiesSet();
    return threadPoolTaskExecutor;
  }
}
