package org.tmatesoft.sqljet2.test;

import org.junit.Test;

import java.io.*;

import static org.tmatesoft.sqljet2.test.TestUtils.*;

public class PrintTest {

    private static final int ALOT = 3000;

    private static final void print(final Streams s) throws IOException {
        for (int i = 0; i < ALOT; i++) {
            for (int x = 0; x < 80; x++)
                s.print('a');
            s.nl();
        }
    }

    @Test
    public void testPrint() throws Exception {
        warm(new Testable() {
            public void test() throws Exception {
                print(Streams.NULL);
            }
        });
        final Testable t = new Testable() {
            public void test() throws Exception {
                print(Streams.OUT);
            }
        };
        t.test();
        benchmark("testPrint", t);
    }

}
