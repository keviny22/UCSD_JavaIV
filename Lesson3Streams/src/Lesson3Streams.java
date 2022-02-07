package src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Lesson3Streams {
    public static void main(String[] args) throws IOException {
        String inputFile = args[0];
        String fileContents = readFile(inputFile);
        long streamMilliSeconds = 0;
        long parallelStreamMilliSeconds = 0;

        do {
            System.out.printf("%nString Size: %d%n",stringSize(fileContents));
            streamMilliSeconds = hexCountStream(fileContents);
            parallelStreamMilliSeconds = hexCountParallelStream(fileContents);
            System.out.printf("Millisecs using stream: %d%n", streamMilliSeconds);
            System.out.printf("Millisecs using parallelStream: %d%n", parallelStreamMilliSeconds);
            if ( parallelStreamMilliSeconds < streamMilliSeconds){
                System.out.printf("%nResults: paralleStream was %d faster than stream.%n", streamMilliSeconds - parallelStreamMilliSeconds);
            }
            if (streamMilliSeconds <= parallelStreamMilliSeconds){
                System.out.printf("Results: stream was %d faster than parallelStream%n", parallelStreamMilliSeconds - streamMilliSeconds);
            }
            fileContents = fileContents + fileContents;

        } while ( streamMilliSeconds < parallelStreamMilliSeconds);

    }

    private static String readFile(String fileName) throws IOException {
        Path path = Paths.get(fileName);
        return new String(Files.readAllBytes(path), "UTF-8");
    }

    private static  long stringSize(String characters) {
        List<String> characterList = Arrays.asList(characters.split(""));
        long count = 0;
        count = characterList.size();
        return count;
    }

    private static  long hexCountStream(String characters) {
        List<String> words = Arrays.asList(characters.split(","));
        long start = System.currentTimeMillis();
        words.stream().filter(w -> w.matches(".*0{8}[0-9A-Fa-f]{8}.*")).count();
        long stop = System.currentTimeMillis();
        return stop - start;
    }

    private static  long hexCountParallelStream(String characters) {
        List<String> words = Arrays.asList(characters.split(","));
        long start = System.currentTimeMillis();
        words.parallelStream().filter(w -> w.matches(".*0{8}[0-9A-Fa-f]{8}.*")).count();
        long stop = System.currentTimeMillis();
        return stop - start;
    }
}
