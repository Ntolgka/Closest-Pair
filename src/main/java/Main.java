import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main{

    public Main(String fileName, int topN) throws IOException {
        //Complete this constructor

        List<String> allLines = Files.readAllLines(Paths.get(fileName));

        List<String> words = new ArrayList<>();

        for (String line : allLines) {
            for (String word : line.split(" ")) {
                if (word.trim().length() > 0)
                    words.add(convert(word));
            }
        }
        // System.out.println(words);

        computeAvgLengthByFirstChar(words, topN);
        Set<Pair> pairs = calculateMinPairDist(words);
    }

    private String convert (String word) {
        word = word.toLowerCase(new Locale("tr", "TR"));
        String rValue = "";
        for (int i = 0; i < word.length(); i++) {
            if (Character.isLetterOrDigit(word.charAt(i)))
                rValue += word.charAt(i);
        }
        return rValue;
    }

    private void computeAvgLengthByFirstChar(List<String> words, int topN) {
        Map<Character, List<String>> map = new HashMap<>();
        for (String word : words) {
            if (!map.containsKey(word.charAt(0)))
                map.put(word.charAt(0), new ArrayList<>());
            map.get(word.charAt(0)).add(word);
        }

        List<CharWithAvarage> resultList = new ArrayList<>();
        for (Character ch : map.keySet()) {
            List<String> list = map.get(ch);
            double sum = 0;
            for (String word : list) {
                sum += word.length();
            }
            double avg = sum / list.size();
            resultList.add(new CharWithAvarage(ch, avg));

        }

        System.out.println("Initial Character & Average Length");
        for (CharWithAvarage cwa : resultList) {
            System.out.println(cwa.ch + "\t" + cwa.avg);
        }


        System.out.println(words.size());

        Set<Pair> set = calculateMinPairDist(words);

        int printSize = Math.min(topN, set.size());

        Pair[] array = set.toArray(new Pair[0]);
        Arrays.sort(array);
        for (int i = 0; i < printSize; i++) {
            System.out.println(array[i]);
        }
    }

    class CharWithAvarage {
        char ch;
        double avg;

        public CharWithAvarage(char ch, double avg) {
            this.ch = ch;
            this.avg = avg;
        }
    }

    private Set<Pair> calculateMinPairDist(List<String> words) {
        Set<Pair> set = new HashSet<>();

        for (int i = 0; i < words.size(); i++) {
            for (int j = i + 1; j < words.size(); j++) {
                String token1 = words.get(i);
                String token2 = words.get(j);

                if (token1.equals(token2))
                    continue;

                double distance = calculatesTotalDistance(words, token1, token2);
                set.add(new Pair(token1, token2, distance));
            }
        }


        return set;
    }

    private double calculatesTotalDistance(List<String> words, String token1, String token2) {
        int f1 = 0;
        int f2 = 0;
        for (String word : words) {
            if (word.equals(token1))
                f1++;
            if (word.equals(token2))
                f2++;
        }
        int sum = 0;
        for (int i = 0; i < words.size(); i++) {
            if (!words.get(i).equals(token1))
                continue;;
            for (int j = i; j < words.size(); j++) {
                if (!words.get(j).equals(token2))
                    continue;
                sum = sum + j - i;
                break;
            }
        }

        return f1 * f2 / (1 + Math.log(sum));
    }


    public static void main(String[] args) throws IOException {
        new Main(args[0],Integer.parseInt(args[1]));

    }


}