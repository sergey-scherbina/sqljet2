package org.tmatesoft.sqljet2.test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;

public class TestUtils {

    public static int WARM = 3000;

    public static String asString(final String s) {
        return (s == null) ? "null" : s;
    }

    public static String asString(final Object x) {
        return (x == null) ? "null" : x.toString();
    }

    public static String asString(final long x) {
        return Long.toString(x);
    }

    public static class Streams {

        private final static Charset CHARSET = Charset.forName("US-ASCII");

        private final static int BUFFERSIZE = 1024 * 512;

        public final static char SPACE = ' ';
        public final static char NL = '\n';

        public static final Streams NULL = new Streams();
        public static final Streams OUT = new Streams(FileDescriptor.out);
        public static final Streams ERR = new Streams(FileDescriptor.err);

        private final OutputStream stream;
        private final WritableByteChannel channel;

        private Streams(final FileDescriptor fd) {
            final FileOutputStream fs = new FileOutputStream(fd);
            stream = new BufferedOutputStream(fs, BUFFERSIZE);
            channel = fs.getChannel();
        }

        private Streams() {
            stream = new OutputStream() {
                public void write(int b) throws IOException {
                }
            };
            channel = new WritableByteChannel() {
                public int write(final ByteBuffer src) throws IOException {
                    return 0;
                }

                @Override
                public boolean isOpen() {
                    return true;
                }

                @Override
                public void close() throws IOException {
                }
            };
        }

        public final Streams print(final char c) throws IOException {
            stream.write(c);
            return this;
        }

        public final Streams nl() throws IOException {
            return print(NL);
        }

        public final Streams space() throws IOException {
            return print(SPACE);
        }

        private final byte[] bytes(final String s) {
            return asString(s).getBytes(CHARSET);
        }

        private final byte[] bytes(final Object x) {
            return asString(x).getBytes(CHARSET);
        }

        private final byte[] bytes(final long x) {
            return asString(x).getBytes(CHARSET);
        }

        public final Streams print(final String s) throws IOException {
            stream.write(bytes(s));
            return this;
        }

        public final Streams print(final Object x) throws IOException {
            stream.write(bytes(x));
            return this;
        }

        public final Streams print(final long x) throws IOException {
            stream.write(bytes(x));
            return this;
        }

        public final Streams println(final String s) throws IOException {
            print(s);
            return nl();
        }

        public final Streams println() throws IOException {
            return nl();
        }

        public final Streams print(final ByteBuffer b) throws IOException {
            channel.write(b);
            return this;
        }

        public final Streams flush() throws IOException {
            stream.flush();
            return this;
        }

    }

    public static final String ns(final long ns) {
        return String.format("%f s", ((double) ns) / 1E9);
    }

    public static final long now() {
        return System.nanoTime();
    }

    public static final long time(final long start) {
        return now() - start;
    }

    public static final long start() {
        return now();
    }

    public static final void printNs(final Streams stream,
                                     final String name, final String ns) throws IOException {
        stream.print(name).space().println(ns).nl().flush();
    }

    public static final long end(final String name, final long start) throws IOException {
        final long time = time(start);
        final String ns = ns(time);
        printNs(Streams.ERR, name, ns);
        printNs(Streams.OUT, name, ns);
        return time;
    }

    interface Testable {
        void test() throws Exception;
    }

    public static final void warm(final Testable t) throws Exception {
        for (int i = 0; i < WARM; i++) t.test();
    }

    public static final void benchmark(final String name, final Testable t) throws Exception {
        final long start = start();
        t.test();
        end(name, start);
    }

}