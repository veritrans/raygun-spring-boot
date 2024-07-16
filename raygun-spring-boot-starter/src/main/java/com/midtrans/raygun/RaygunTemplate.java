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

package com.midtrans.raygun;

import com.mindscapehq.raygun4java.core.RaygunClient;
import com.mindscapehq.raygun4java.core.RaygunClientFactory;

import org.springframework.core.task.TaskExecutor;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A template for sending exceptions to Raygun.
 *
 * <p>Some methods allow to send custom data and tags.
 *
 * @apiNote This class defines both methods to send an {@code Exception} and a {@code Throwable}s
 *     even if it can have only the {@code Exception} methods because this class is intended to send
 *     application exceptions defined by users not {@code Error}s or {@code Throwable}s which should
 *     not be caught by users. Raygun client allows users to send {@code Throwable}s which makes
 *     code written on top of it is also using {@code Throwable}s as parameters even if users only
 *     send {@code Exception}s. The methods using a {@code Throwable}s as its parameter are defined
 *     to allow easier migration to this class.
 * @implNote This implementation instantiates a new {@code RaygunClient} per request thread as
 *     suggested in <a
 *     href="https://raygun.com/documentation/language-guides/java/crash-reporting/installation/#going-further">
 *     Raygun Java documentation</a>.
 * @author Raydhitya Yoseph
 * @see <a
 *     href="https://raygun.com/documentation/product-guides/crash-reporting/custom-data-tags/">Custom
 *     data and tags</a>
 */
public class RaygunTemplate extends RaygunExceptionExcludeRegistry {
  private static ThreadLocal<RaygunClient> client = new ThreadLocal<RaygunClient>();

  private final RaygunClientFactory raygunClientFactory;
  private final TaskExecutor taskExecutor;

  public RaygunTemplate(RaygunClientFactory raygunClientFactory, TaskExecutor taskExecutor) {
    this.raygunClientFactory = raygunClientFactory;
    this.taskExecutor = taskExecutor;
  }

  /**
   * Send an {@code Exception} to Raygun.
   *
   * @param exception the exception
   */
  public void send(Exception exception) {
    send(exception, Set.of(), Map.of());
  }

  /**
   * Send an {@code Exception} to Raygun.
   *
   * @param exception the exception
   * @param tags custom tags
   */
  public void send(Exception exception, String... tags) {
    send(exception, toSet(tags), Map.of());
  }

  /**
   * Send an {@code Exception} to Raygun.
   *
   * @param exception the exception
   * @param tags custom tags
   */
  public void send(Exception exception, Set<String> tags) {
    send(exception, tags, Map.of());
  }

  /**
   * Send an {@code Exception} to Raygun.
   *
   * @param exception the exception
   * @param tags custom tags
   * @param data custom data
   */
  public void send(Exception exception, Set<String> tags, Map<String, String> data) {
    if (shouldSend(exception)) {
      RaygunClient raygunClient = getRaygunClientPerRequestThread();

      taskExecutor.execute(() -> raygunClient.send(exception, tags, data));
    }
  }

  private boolean shouldSend(Exception exception) {
    return !contains(exception.getClass());
  }

  /**
   * Send a {@code Throwable} to Raygun.
   *
   * @param throwable the throwable
   */
  public void send(Throwable throwable) {
    send(throwable, Set.of(), Map.of());
  }

  /**
   * Send a {@code Throwable} to Raygun.
   *
   * @param throwable the throwable
   * @param tags custom tags
   */
  public void send(Throwable throwable, String... tags) {
    send(throwable, toSet(tags), Map.of());
  }

  /**
   * Send a {@code Throwable} to Raygun.
   *
   * @param throwable the throwable
   * @param tags custom tags
   */
  public void send(Throwable throwable, Set<String> tags) {
    send(throwable, tags, Map.of());
  }

  /**
   * Send a {@code Throwable} to Raygun.
   *
   * @param throwable the throwable
   * @param tags custom tags
   * @param data custom data
   */
  public void send(Throwable throwable, Set<String> tags, Map<String, String> data) {
    if (shouldSend(throwable)) {
      RaygunClient raygunClient = getRaygunClientPerRequestThread();

      taskExecutor.execute(() -> raygunClient.send(throwable, tags, data));
    }
  }

  private boolean shouldSend(Throwable throwable) {
    return !(throwable instanceof Exception)
        || throwable instanceof Exception && !contains(((Exception) throwable).getClass());
  }

  private Set<String> toSet(String... tags) {
    return Arrays.stream(tags).filter(Objects::nonNull).collect(Collectors.toSet());
  }

  private RaygunClient getRaygunClientPerRequestThread() {
    RaygunClient raygunClient = client.get();
    if (Objects.isNull(raygunClient)) {
      raygunClient = raygunClientFactory.newClient();
      client.set(raygunClient);
    }
    return raygunClient;
  }
}
