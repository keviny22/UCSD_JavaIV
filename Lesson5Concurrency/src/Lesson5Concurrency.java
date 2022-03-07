import org.apache.commons.cli.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.io.FileUtils;

public class Lesson5Concurrency {

    static class countCharacters implements Callable<Integer> {
        private final Collection<File> fileList;

        public countCharacters(Collection<File> fileList) {
            this.fileList = fileList;
        }

        public Integer call() {
            int totalCharacters = 0;
            String line;
            totalCharacters = getTotalCharacters(totalCharacters, fileList);
            return totalCharacters;

        }

        private static int getTotalCharacters(int totalCharacters, Collection<File> fileList) {
            String line;
            for (File file : fileList) {
                try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
                    while ((line = bufferedReader.readLine()) != null) {
                        String[] myWords = line.replaceAll("\\s+", " ").split(" ");
                        for (String s : myWords) {
                            totalCharacters += s.length();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return totalCharacters;
        }
    }

    static class countCharactersWithRetrantLock implements Callable<Integer> {
        private final Collection<File> fileList;

        countCharactersWithRetrantLock(Collection<File> fileList) {
            this.fileList = fileList;
        }

        public Integer call() {
            Lock lockCount = new ReentrantLock();
            int totalCharacters = 0;
            lockCount.lock();
            String line;
            try {
                totalCharacters = countCharacters.getTotalCharacters(totalCharacters, fileList);
            } finally {
                lockCount.unlock();
            }
            return totalCharacters;
        }
    }

    public static AtomicLong counter = new AtomicLong(0);

    static class countCharactersWithAtomicLong implements Callable<Long> {
        private final Collection<File> fileList;


        countCharactersWithAtomicLong(Collection<File> fileList) {
            this.fileList = fileList;
        }

        public Long call() {
            int totalCharacters = 0;
            String line;
            for (File file : fileList) {

                try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
                    while ((line = bufferedReader.readLine()) != null) {
                        String[] myWords = line.replaceAll("\\s+", " ").split(" ");
                        for (String s : myWords) {
                            totalCharacters += s.length();
                            long i = 0;
                            while (i < totalCharacters) {
                                i = counter.incrementAndGet();
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return counter.get();
        }
    }

    public static void main(String[] args) {
        Options options = new Options();

        Option numThreads = new Option("n", "num-threads", true, "the number of threads");
        numThreads.setRequired(false);
        options.addOption(numThreads);

        Option reentrantLock = new Option("r", "ReentrantLock", false, "ReentrantLock");
        reentrantLock.setRequired(false);
        options.addOption(reentrantLock);

        Option atomicLong = new Option("a", "AtomicLong", false, "AtomicLong");
        atomicLong.setRequired(false);
        options.addOption(atomicLong);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            CommandLine commandLine = parser.parse(options, args);
            int num_processors = Runtime.getRuntime().availableProcessors();

            if (commandLine.hasOption("n")) {
                num_processors = Integer.parseInt(commandLine.getOptionValue("n"));
            }

            File dirToCheck = new File(".");
            String[] extensions = {"class", "java"};
            Collection<File> files = FileUtils.listFiles(dirToCheck, extensions, true);

            ExecutorService executorService = Executors.newFixedThreadPool(num_processors);

            if (commandLine.hasOption("r")) {
                Future<Integer> result = executorService.submit(new countCharactersWithRetrantLock(files));
                System.out.println(result.get());
            } else if (commandLine.hasOption("a")) {
                Future<Long> result = executorService.submit(new countCharactersWithAtomicLong(files));
                System.out.println(result.get());
            } else {
                Future<Integer> result = executorService.submit(new countCharacters(files));
                System.out.println(result.get());
            }

        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
