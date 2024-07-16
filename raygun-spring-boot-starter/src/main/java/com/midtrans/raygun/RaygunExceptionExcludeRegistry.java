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

import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Set;

/**
 * A registry of excluded exception types to be sent to Raygun.
 *
 * @author Raydhitya Yoseph
 */
public class RaygunExceptionExcludeRegistry {
  private final Set<Class<? extends Exception>> exceptionTypes = new HashSet<>();

  /**
   * Register an exception type to be excluded from Raygun sending.
   *
   * <p>Only instances with type retrieved by {@link Object#getClass()} equals with the registered
   * type will be excluded.
   *
   * <p>If the child type is registered, parent instances will not be excluded.
   *
   * <p>If the parent type is registered, child instances will not be excluded.
   *
   * @param exceptionType the exception type to be excluded
   * @throws IllegalArgumentException if exceptionType is null
   */
  public void registerException(Class<? extends Exception> exceptionType) {
    Assert.notNull(exceptionType, "The exception type must not be null");
    exceptionTypes.add(exceptionType);
  }

  protected boolean contains(Class<? extends Exception> exceptionType) {
    return exceptionTypes.contains(exceptionType);
  }
}
