package ca.jrvs.apps.practice;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public interface LambdaStreamExc {
    /**
     * Creates a String stream from array and stores the arbitrary number of values
     * @param strings
     * @return
     */
    Stream<String> createStrStream(String ... strings);

    /**
     * Converts the strings to uppercase
     * @param strings
     * @return
     */
    Stream<String> toUpperCase (String ... strings);

    /**
     * Filter strings that contains the pattern
     * @param stringStream
     * @param pattern
     * @return
     */
    Stream<String> filter(Stream<String> stringStream, String pattern);

    /**
     * Creates an intStream for array
     * @param arr
     * @return
     */
    IntStream createIntStream(int[] arr);

    /**
     * Converts a stream to list
     * @param stream
     * @param <E>
     * @return
     */
    <E> List<E> toList(Stream<E> stream);

    /**
     * Converts a intStream to list
     * @param intStream
     * @return
     */
    List<Integer> toList(IntStream intStream);

    /**
     * Creates IntStream range from start to end inclusive
     * @param start
     * @param end
     * @return
     */
    IntStream createIntStream(int start, int end);

    /**
     * Converts intStream to a doubleStream and computes square root of each element
     * @param intStream
     * @return
     */
    Double squareRootIntStream(IntStream intStream);

    /**
     * filters all even numbers and return odd numbers from a intStream
     * @param intStream
     * @return
     */
    IntStream getOdd(IntStream intStream);

    /**
     *
     * @param prefix
     * @param suffix
     * @return
     */
    Consumer<String> getLambdaPrinter(String prefix, String suffix);

    /**
     *
     * @param messages
     * @param printer
     */
    void printMessages(String[] messages, Consumer<String> printer);

    /**
     *
     * @param intStream
     * @param printer
     */
    void printOdd(IntStream intStream, Consumer<String> printer);

    /**
     *
     * @param ints
     * @return
     */
    Stream<Integer> flatNestedInt(Stream<List<Integer>> ints);










}
