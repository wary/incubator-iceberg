/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.iceberg.io;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.NoSuchElementException;
import org.apache.iceberg.AssertHelpers;
import org.junit.Assert;
import org.junit.Test;

public class TestCloseableIterable {

  @Test
  public void testConcateWithEmptyIterables() {
    CloseableIterable<Integer> iter = CloseableIterable.combine(Lists.newArrayList(1, 2, 3), () -> { });
    CloseableIterable<Integer> empty = CloseableIterable.empty();

    CloseableIterable<Integer> concat1 = CloseableIterable.concat(Lists.newArrayList(iter, empty, empty));
    Assert.assertEquals(Iterables.getLast(concat1).intValue(), 3);

    CloseableIterable<Integer> concat2 = CloseableIterable.concat(Lists.newArrayList(empty, empty, iter));
    Assert.assertEquals(Iterables.getLast(concat2).intValue(), 3);

    CloseableIterable<Integer> concat3 = CloseableIterable.concat(Lists.newArrayList(empty, iter, empty));
    Assert.assertEquals(Iterables.getLast(concat3).intValue(), 3);

    CloseableIterable<Integer> concat4 = CloseableIterable.concat(Lists.newArrayList(empty, iter, empty, empty, iter));
    Assert.assertEquals(Iterables.getLast(concat4).intValue(), 3);

    // This will throw a NoSuchElementException
    CloseableIterable<Integer> concat5 = CloseableIterable.concat(Lists.newArrayList(empty, empty, empty));
    AssertHelpers.assertThrows("should throw NoSuchElementException",
        NoSuchElementException.class,
        () -> Iterables.getLast(concat5));
  }
}
