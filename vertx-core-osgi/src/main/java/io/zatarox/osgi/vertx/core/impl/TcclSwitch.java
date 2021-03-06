/*
 * Copyright 2016 Guillaume Chauvet.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.zatarox.osgi.vertx.core.impl;

import java.util.concurrent.Callable;

public final class TcclSwitch {

    private TcclSwitch() {
    }

  public static <T> T executeWithTCCLSwitch(Callable<T> action) throws Exception {
    final ClassLoader original = Thread.currentThread().getContextClassLoader();
    try {
      Thread.currentThread().setContextClassLoader(TcclSwitch.class.getClassLoader());
      return action.call();
    } finally {
      Thread.currentThread().setContextClassLoader(original);
    }
  }

  public static void executeWithTCCLSwitch(Runnable action) {
    final ClassLoader original = Thread.currentThread().getContextClassLoader();
    try {
      Thread.currentThread().setContextClassLoader(TcclSwitch.class.getClassLoader());
      action.run();
    } finally {
      Thread.currentThread().setContextClassLoader(original);
    }
  }

}
