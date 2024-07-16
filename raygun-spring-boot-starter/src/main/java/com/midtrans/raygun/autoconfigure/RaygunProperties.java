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

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * {@link ConfigurationProperties} for Raygun.
 *
 * @author Raydhitya Yoseph
 */
@ConfigurationProperties("raygun")
public class RaygunProperties {

  /** API key. */
  private String apiKey;

  /** Proxy for outgoing requests. */
  private Proxy proxy;

  /** Timeout for the connection. */
  private Integer connectTimeout = Integer.valueOf(10000);

  /** Version of the user application. */
  private String version;

  /** Common tags that are applied to every request. */
  private Set<String> tags = new HashSet<>();

  public String getApiKey() {
    return this.apiKey;
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  public Proxy getProxy() {
    return this.proxy;
  }

  public void setProxy(Proxy proxy) {
    this.proxy = proxy;
  }

  public boolean isProxyConfigured() {
    return Objects.nonNull(proxy)
        && Objects.nonNull(proxy.getHost())
        && Objects.nonNull(proxy.getPort());
  }

  public Integer getConnectTimeout() {
    return connectTimeout;
  }

  public void setConnectTimeout(Integer connectTimeout) {
    this.connectTimeout = connectTimeout;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public Set<String> getTags() {
    return tags;
  }

  public void setTags(Set<String> tags) {
    this.tags = tags;
  }

  /** Proxy for Raygun. */
  public static class Proxy {

    /** Proxy host. */
    private String host;

    /** Proxy port. */
    private Integer port;

    public String getHost() {
      return this.host;
    }

    public void setHost(String host) {
      this.host = host;
    }

    public Integer getPort() {
      return this.port;
    }

    public void setPort(Integer port) {
      this.port = port;
    }
  }
}
