package com.smeup.jfr;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("File to parse is mandatory");
            System.exit(1);
        }
        Path path = Paths.get(args[0]);
        JfrParser.parse(path, created -> System.out.println("File created at path: " + created));
    }
}
