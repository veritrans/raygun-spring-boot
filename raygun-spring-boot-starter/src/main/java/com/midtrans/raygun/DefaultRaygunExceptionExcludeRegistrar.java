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

/**
 * Default {@link RaygunExceptionExcludeRegistrar} implementation that does not register any {@link
 * Exception} type.
 *
 * @author Raydhitya Yoseph
 */
public class DefaultRaygunExceptionExcludeRegistrar implements RaygunExceptionExcludeRegistrar {

  @Override
  public void registerExceptions(RaygunExceptionExcludeRegistry registry) {}
}
