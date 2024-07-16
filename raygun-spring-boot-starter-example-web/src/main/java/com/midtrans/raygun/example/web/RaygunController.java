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

import com.midtrans.raygun.RaygunTemplate;
import com.midtrans.raygun.example.CaughtRaygunException;
import com.midtrans.raygun.example.UncaughtRaygunException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A {@link RestController} that sends exceptions through a {@link RaygunTemplate}.
 *
 * @author Raydhitya Yoseph
 */
@RestController
class RaygunController {
  private final RaygunTemplate raygunTemplate;

  RaygunController(RaygunTemplate raygunTemplate) {
    this.raygunTemplate = raygunTemplate;
  }

  @GetMapping("/caught")
  ResponseEntity<?> caught() {
    try {
      throw new CaughtRaygunException();
    } catch (CaughtRaygunException ex) {
      raygunTemplate.send(ex);
    }
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }

  @GetMapping("/uncaught")
  void uncaught() {
    throw new UncaughtRaygunException();
  }
}
