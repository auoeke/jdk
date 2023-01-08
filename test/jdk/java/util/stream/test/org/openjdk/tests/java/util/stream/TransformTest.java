package org.openjdk.tests.java.util.stream;

import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.BaseStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.testng.Assert.*;

@Test
public class TransformTest {
    public void testIntTransform() {
        Supplier<IntStream> stream = () -> IntStream.of(0, 1, 2, 3);
        assertBase(stream.get());
        assertTrue(Arrays.equals(stream.get().transform(s -> multiply(s, 2)).toArray(), new int[]{0, 2, 4, 6}));
        assertTrue(Arrays.equals(stream.get().transform(TransformTest::triple).toArray(), new int[]{0, 3, 6, 9}));
    }

    public void testRefTransform() {
        Supplier<Stream<String>> stream = () -> Stream.of("a", "b", "c", "d");
        assertBase(stream.get());
        assertTrue(stream.get().transform(s -> add(s, "e", "f")).toList().equals(List.of("a", "b", "c", "d", "e", "f")));
        assertTrue(stream.get().transform(TransformTest::addE).toList().equals(List.of("a", "b", "c", "d", "e")));
    }

    private void assertBase(BaseStream<?, ?> stream) {
        assertTrue(stream.transform(Function.identity()) == stream);
        assertTrue(stream.transform(s -> null) == null);
    }

    private static IntStream multiply(IntStream stream, int multiplier) {
        return stream.map(n -> n * multiplier);
    }

    private static IntStream triple(IntStream stream) {
        return multiply(stream, 3);
    }

    private static <T> Stream<T> add(Stream<T> stream, T... appendage) {
        return Stream.concat(stream, Stream.of(appendage));
    }

    private static Stream<String> addE(Stream<String> stream) {
        return add(stream, "e");
    }
}