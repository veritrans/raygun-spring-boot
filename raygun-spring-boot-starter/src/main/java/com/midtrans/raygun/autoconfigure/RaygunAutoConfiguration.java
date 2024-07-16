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

package com.midtrans.raygun.autoconfigure;

import com.midtrans.raygun.DefaultRaygunExceptionExcludeRegistrar;
import com.midtrans.raygun.RaygunExceptionExcludeRegistrar;
import com.midtrans.raygun.RaygunTemplate;
import com.midtrans.raygun.web.RaygunWebMvcConfiguration;
import com.midtrans.raygun.ws.RaygunWebServicesConfiguration;
import com.mindscapehq.raygun4java.core.RaygunClient;
import com.mindscapehq.raygun4java.core.RaygunClientFactory;
import com.mindscapehq.raygun4java.core.RaygunSettings;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import java.util.Map;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Raygun.
 *
 * @author Raydhitya Yoseph
 */
@AutoConfiguration
@ConditionalOnClass(RaygunClient.class)
@EnableConfigurationProperties(RaygunProperties.class)
@Import({RaygunWebMvcConfiguration.class, RaygunWebServicesConfiguration.class})
public class RaygunAutoConfiguration {
  private static final String RAYGUN_TASK_EXECUTOR_BEAN_NAME = "raygunTaskExecutor";

  @Bean
  @ConditionalOnMissingBean(RaygunTemplate.class)
  public RaygunTemplate raygunTemplate(
      RaygunClientFactory raygunClientFactory,
      Map<String, TaskExecutor> taskExecutors,
      RaygunExceptionExcludeRegistrar raygunExceptionExcludeRegistrar) {
    RaygunTemplate raygunTemplate =
        new RaygunTemplate(raygunClientFactory, taskExecutor(taskExecutors));

    raygunExceptionExcludeRegistrar.registerExceptions(raygunTemplate);

    return raygunTemplate;
  }

  @Bean
  @ConditionalOnMissingBean(RaygunClientFactory.class)
  public RaygunClientFactory raygunClientFactory(RaygunProperties raygunProperties) {
    configure(RaygunSettings.getSettings(), raygunProperties);

    RaygunClientFactory raygunClientFactory =
        new RaygunClientFactory(raygunProperties.getApiKey())
            .withVersion(raygunProperties.getVersion());

    raygunClientFactory.setTags(raygunProperties.getTags());

    return raygunClientFactory;
  }

  private void configure(RaygunSettings raygunSettings, RaygunProperties raygunProperties) {
    if (raygunProperties.isProxyConfigured()) {
      raygunSettings.setHttpProxy(
          raygunProperties.getProxy().getHost(), raygunProperties.getProxy().getPort());
    }

    raygunSettings.setConnectTimeout(raygunProperties.getConnectTimeout());
  }

  private TaskExecutor taskExecutor(Map<String, TaskExecutor> taskExecutors) {
    if (taskExecutors.isEmpty()) {
      return new SyncTaskExecutor();
    }
    if (taskExecutors.size() == 1) {
      return taskExecutors.values().iterator().next();
    }
    if (!taskExecutors.containsKey(RAYGUN_TASK_EXECUTOR_BEAN_NAME)) {
      return new SyncTaskExecutor();
    }
    return taskExecutors.get(RAYGUN_TASK_EXECUTOR_BEAN_NAME);
  }

  @Bean
  @ConditionalOnMissingBean(RaygunExceptionExcludeRegistrar.class)
  public RaygunExceptionExcludeRegistrar defaultRaygunExceptionExcludeRegistrar() {
    return new DefaultRaygunExceptionExcludeRegistrar();
  }
}
