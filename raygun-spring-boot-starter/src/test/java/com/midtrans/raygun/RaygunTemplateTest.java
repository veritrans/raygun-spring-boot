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

package com.midtrans.raygun;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.midtrans.raygun.test.MockRaygunClientFactory;
import com.mindscapehq.raygun4java.core.RaygunClientFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.task.SyncTaskExecutor;

import java.util.Map;
import java.util.Set;

/**
 * Test for {@link RaygunTemplate}.
 *
 * @author Raydhitya Yoseph
 */
class RaygunTemplateTest {
  RaygunClientFactory raygunClientFactory;
  RaygunTemplate raygunTemplate;

  @BeforeEach
  void beforeEach() {
    raygunClientFactory = Mockito.spy(new MockRaygunClientFactory());

    raygunTemplate = new RaygunTemplate(raygunClientFactory, new SyncTaskExecutor());
  }

  @Nested
  class SendException {

    @Test
    void sendTwoTimesInOneThreadShouldCreateOneRaygunClient() throws InterruptedException {
      Thread raygunThread =
          new Thread(
              () -> {
                raygunTemplate.send(new RuntimeException());
                raygunTemplate.send(new RuntimeException());
              });
      raygunThread.start();
      raygunThread.join();

      verify(raygunClientFactory, times(1)).newClient();
    }

    @Test
    void sendTwoTimesInTwoThreadsShouldCreateTwoRaygunClients() throws InterruptedException {
      Thread raygunThread1 =
          new Thread(
              () -> {
                raygunTemplate.send(new RuntimeException());
              });
      raygunThread1.start();

      Thread raygunThread2 =
          new Thread(
              () -> {
                raygunTemplate.send(new RuntimeException());
              });
      raygunThread2.start();

      raygunThread1.join();
      raygunThread2.join();

      verify(raygunClientFactory, times(2)).newClient();
    }
  }

  @Nested
  class SendExceptionWithTags {

    @Test
    void usingSetParameter() throws InterruptedException {
      Thread raygunThread =
          new Thread(
              () ->
                  raygunTemplate.send(new RuntimeException(), Set.of("poppinparty", "morfonica")));
      raygunThread.start();
      raygunThread.join();

      verify(raygunClientFactory, times(1)).newClient();
    }

    @Test
    void usingVarargsParameter() throws InterruptedException {
      Thread raygunThread =
          new Thread(() -> raygunTemplate.send(new RuntimeException(), "poppinparty", "morfonica"));
      raygunThread.start();
      raygunThread.join();

      verify(raygunClientFactory, times(1)).newClient();
    }
  }

  @Nested
  class SendExceptionWithData {

    @Test
    void andWithTags() throws InterruptedException {
      Thread raygunThread =
          new Thread(
              () ->
                  raygunTemplate.send(
                      new RuntimeException(),
                      Set.of("poppinparty", "morfonica"),
                      Map.of("vocal", "mashiro", "bass", "nanami")));
      raygunThread.start();
      raygunThread.join();

      verify(raygunClientFactory, times(1)).newClient();
    }
  }

  @Nested
  class SendRegisteredException {

    @Test
    void typeRegisteredShouldNotBeSent() throws InterruptedException {
      raygunTemplate.registerException(IndexOutOfBoundsException.class);

      Thread raygunThread =
          new Thread(
              () -> {
                raygunTemplate.send(new IndexOutOfBoundsException());
              });
      raygunThread.start();
      raygunThread.join();

      verify(raygunClientFactory, times(0)).newClient();
    }

    @Test
    void childTypeRegisteredParentInstancesShouldBeSent() throws InterruptedException {
      raygunTemplate.registerException(IndexOutOfBoundsException.class);

      Thread raygunThread =
          new Thread(
              () -> {
                raygunTemplate.send(new RuntimeException());
              });
      raygunThread.start();
      raygunThread.join();

      verify(raygunClientFactory, times(1)).newClient();
    }

    @Test
    void parentTypeRegisteredChildInstancesShouldBeSent() throws InterruptedException {
      raygunTemplate.registerException(RuntimeException.class);

      Thread raygunThread =
          new Thread(
              () -> {
                raygunTemplate.send(new IndexOutOfBoundsException());
              });
      raygunThread.start();
      raygunThread.join();

      verify(raygunClientFactory, times(1)).newClient();
    }
  }

  @Nested
  class SendThrowable {

    @Test
    void shouldBeSent() throws InterruptedException {
      Thread raygunThread =
          new Thread(
              () -> {
                raygunTemplate.send(new Error());
                raygunTemplate.send(new Throwable());
              });
      raygunThread.start();
      raygunThread.join();

      verify(raygunClientFactory, times(1)).newClient();
    }

    @Test
    void assignedWithExceptionShouldBeSent() throws InterruptedException {
      Throwable throwable = new RuntimeException();

      Thread raygunThread =
          new Thread(
              () -> {
                raygunTemplate.send(throwable);
              });
      raygunThread.start();
      raygunThread.join();

      verify(raygunClientFactory, times(1)).newClient();
    }

    @Test
    void exceptionTypeRegisteredShouldNotBeSent() throws InterruptedException {
      raygunTemplate.registerException(RuntimeException.class);

      Throwable throwable = new RuntimeException();

      Thread raygunThread =
          new Thread(
              () -> {
                raygunTemplate.send(throwable);
              });
      raygunThread.start();
      raygunThread.join();

      verify(raygunClientFactory, times(0)).newClient();
    }
  }

  @Nested
  class SendThrowableWithTags {

    @Test
    void usingSetParameter() throws InterruptedException {
      Thread raygunThread =
          new Thread(
              () ->
                  raygunTemplate.send(new OutOfMemoryError(), Set.of("poppinparty", "morfonica")));
      raygunThread.start();
      raygunThread.join();

      verify(raygunClientFactory, times(1)).newClient();
    }

    @Test
    void usingVarargsParameter() throws InterruptedException {
      Thread raygunThread =
          new Thread(() -> raygunTemplate.send(new OutOfMemoryError(), "poppinparty", "morfonica"));
      raygunThread.start();
      raygunThread.join();

      verify(raygunClientFactory, times(1)).newClient();
    }
  }

  @Nested
  class SendThrowableWithData {

    @Test
    void andWithTags() throws InterruptedException {
      Thread raygunThread =
          new Thread(
              () ->
                  raygunTemplate.send(
                      new OutOfMemoryError(),
                      Set.of("poppinparty", "morfonica"),
                      Map.of("vocal", "mashiro", "bass", "nanami")));
      raygunThread.start();
      raygunThread.join();

      verify(raygunClientFactory, times(1)).newClient();
    }
  }

  @Nested
  class RegisterException {

    @Test
    void nullShouldThrowIllegalArgumentException() {
      assertThatCode(() -> raygunTemplate.registerException(null))
          .isExactlyInstanceOf(IllegalArgumentException.class)
          .hasMessage("The exception type must not be null");
    }
  }
}
