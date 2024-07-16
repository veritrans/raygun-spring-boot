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

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver;

/**
 * A {@link RestController} that throws uncaught exceptions annotated with the {@link
 * ResponseStatus} which will be handled by the {@link ResponseStatusExceptionResolver}.
 *
 * @author Raydhitya Yoseph
 */
@RestController
class ResponseStatusRaygunController {

  @GetMapping("/responseStatus")
  void responseStatus() {
    throw new ResponseStatusRaygunException();
  }
}
