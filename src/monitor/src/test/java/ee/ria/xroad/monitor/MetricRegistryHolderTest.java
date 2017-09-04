/**
 * The MIT License
 * Copyright (c) 2015 Estonian Information System Authority (RIA), Population Register Centre (VRK)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ee.ria.xroad.monitor;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static junit.framework.TestCase.fail;

/**
 * MetricsRegistryHolderTest
 */
@Slf4j
public class MetricRegistryHolderTest {

    @Test
    public void testGetOrCreateSimpleSensor() {

        try {
            MetricRegistryHolder holder = MetricRegistryHolder.getInstance();
            holder.getOrCreateSimpleSensor("Testi");
            holder.getOrCreateSimpleSensor("Testi");
        } catch (Exception e) {
            fail("Exception should not have been thrwon!");
        }

    }

    @Test
    public void testGetOrCreateHistogram() {

        try {
            MetricRegistryHolder holder = MetricRegistryHolder.getInstance();
            holder.getOrCreateHistogram("Test");
            holder.getOrCreateHistogram("Test");
        } catch (Exception e) {
            fail("Exception should not have been thrown!");
        }
        
    }
}
