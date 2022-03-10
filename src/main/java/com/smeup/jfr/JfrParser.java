package com.smeup.jfr;


import jdk.jfr.consumer.RecordedClass;
import jdk.jfr.consumer.RecordedEvent;
import jdk.jfr.consumer.RecordedThread;
import jdk.jfr.consumer.RecordingFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

/***
 * https://docs.oracle.com/en/java/javase/17/jfapi/parsing-recording-file.html
 */
public class JfrParser {

    public static void parse(Path path, Consumer<Path> createdPath) throws IOException, FileNotFoundException {
        if (!Files.exists(path)) {
            throw new FileNotFoundException(path.toString());
        }
        final Path outPath = Paths.get(path.getParent().toString(), path.getFileName().toString() + ".tsv");
        try (var out = new FileOutputStream(outPath.toFile())) {
            parse(path, out);
        }
        if (createdPath != null) {
            createdPath.accept(outPath);
        }
    }

    public static void parse(Path path, OutputStream out) throws IOException {
        try (var outputStream = new PrintStream(out)) {
            outputStream.println("thread" + "\t" + "package" + "\t" + "class" + "\t" + "method");
            try (var recordingFile = new RecordingFile(path)) {
                while (recordingFile.hasMoreEvents()) {
                    RecordedEvent recordedEvent = recordingFile.readEvent();
                    if (recordedEvent.getEventType().getName().equals("jdk.ExecutionSample")) {
                        recordedEvent.getStackTrace().getFrames().forEach(recordedFrame -> {
                            final RecordedClass recordedClass = recordedFrame.getMethod().getType();
                            if (recordedClass.getName().matches("com\\.smeup\\..*|it\\.smea\\..*")) {
                                final RecordedThread recordedThread = recordedEvent.getValue("sampledThread");
                                final String _package = recordedClass.getName().substring(0, recordedClass.getName().lastIndexOf("."));
                                final String className =  recordedClass.getName();
                                final String method =   className + "." + recordedFrame.getMethod().getName();
                                outputStream.println(recordedThread.getOSName() + "\t" + _package + "\t" + className + "\t" + method);
                            }
                        });
                    }
                }
            }
        }
    }
}
