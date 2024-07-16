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

package com.midtrans.raygun.web;

import com.midtrans.raygun.RaygunTemplate;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A {@link HandlerExceptionResolver} implementation that sends exceptions to Raygun.
 *
 * @author Raydhitya Yoseph
 */
public class RaygunHandlerExceptionResolver extends AbstractHandlerExceptionResolver {
  private final RaygunTemplate raygunTemplate;

  public RaygunHandlerExceptionResolver(RaygunTemplate raygunTemplate) {
    this.raygunTemplate = raygunTemplate;
    this.setOrder(HIGHEST_PRECEDENCE);
  }

  @Override
  protected ModelAndView doResolveException(
      HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    logger.error("Exception thrown during handler execution", ex);

    raygunTemplate.send(ex);

    return null;
  }
}
