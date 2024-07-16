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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A {@link RestController} that throws uncaught exceptions.
 *
 * @author Raydhitya Yoseph
 * @see <a
 *     href="https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-exceptionhandlers">https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-exceptionhandlers</a>
 */
@RestController
class ExceptionHandlerRaygunController {

  @GetMapping("/exceptionHandler")
  void exceptionHandler() {
    throw new ExceptionHandlerRaygunException();
  }

  @ExceptionHandler(ExceptionHandlerRaygunException.class)
  ResponseEntity<?> handle(ExceptionHandlerRaygunException ex) {
    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
  }

  @GetMapping("/controllerAdvice")
  void controllerAdvice() {
    throw new ControllerAdviceRaygunException();
  }
}
