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

package com.midtrans.raygun.example.web;

import com.midtrans.raygun.example.RaygunException;

import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

/**
 * A {@link RuntimeException} that will be thrown from a {@code Controller} handler method and
 * handled by the {@link ExceptionHandlerExceptionResolver}.
 *
 * @author Raydhitya Yoseph
 */
class ControllerAdviceRaygunException extends RaygunException {
  private static final long serialVersionUID = -4713057582628385907L;
}
