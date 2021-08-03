package de.marcluque.reversi.util;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
 * Created with <3 by marcluque, March 2021
 */
public class Logger {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");;

    private static void message(PrintStream stream, String message) {
        stream.println(message);
    }

    private static void appendMessage(PrintStream stream, String message) {
        stream.append(message);
    }

    public static void print(String message) {
        message(System.out, String.format("[%s]: %s", dateTimeFormatter.format(LocalDateTime.now()), message));
    }

    public static void print(String formatMessage, Object... args) {
        message(System.out, String.format("[%s]: %s", dateTimeFormatter.format(LocalDateTime.now()), String.format(formatMessage, args)));
    }

    public static void appendPrintMessage(String message) {
        appendMessage(System.out, String.format("[%s]: %s", dateTimeFormatter.format(LocalDateTime.now()), message + "\n"));
    }

    public static void appendPrintMessage(String formatMessage, Object... args) {
        appendMessage(System.out, String.format("[%s]: %s", dateTimeFormatter.format(LocalDateTime.now()), String.format(formatMessage + "\n", args)));
    }

    public static void flushPrintMessage() {
        System.out.flush();
    }

    public static void error(String message) {
        message(System.err, String.format("[%s]: %s", dateTimeFormatter.format(LocalDateTime.now()), message));
    }

    public static void error(String formatMessage, Object... args) {
        message(System.err, String.format("[%s]: %s", dateTimeFormatter.format(LocalDateTime.now()), String.format(formatMessage, args)));
    }

    public static void appendError(String message) {
        appendMessage(System.err, String.format("[%s]: %s", dateTimeFormatter.format(LocalDateTime.now()), message + "\n"));
    }

    public static void appendError(String formatMessage, Object... args) {
        appendMessage(System.err, String.format("[%s]: %s", dateTimeFormatter.format(LocalDateTime.now()), String.format(formatMessage + "\n", args)));
    }

    public static void flushErrorMessage() {
        System.err.flush();
    }
}