import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Lesson2RegEx {

    public static void main(String[] args) throws IOException{
        String inputFile = args[0];
        String fileContents = readFile(inputFile);
        processPANID(fileContents);
        processMAC(fileContents);
    }

    private static String readFile(String inputFile) throws IOException {
        Path path = Paths.get(inputFile);
        return Files.readString(path);
    }

    private static void processPANID(String data){
        String patternString = "PANID =.*";
        Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);

        List<String> results = pattern.matcher(data).results().map(MatchResult::group).collect(Collectors.toList());

        System.out.println("Results are as follows:");
        System.out.printf("- List of PAN IDs (Total of %s)\n", results.size());

        for (Object result: results) {
            System.out.println(result);
        }
    }

    private static void processMAC(String data){
        String patternString = "([0-9A-Fa-f]{16})(\\s+\\S+){6}";
        Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);

        List<String[]> results = pattern.matcher(data).results().map(MatchResult::group ).map(s -> s.split("\\s+")).collect(Collectors.toList());

        System.out.printf("- List of MAC Addresses (Total of %s)\n", results.size());

        for (String[] result: results) {
            System.out.printf("%s\n", result[0]);
        }

        System.out.println("- List of RF_RSSI_M values for each MAC address");

        for (String[] result: results) {
            System.out.printf("%s %s\n", result[0], result[6]);

        }
    }
}



