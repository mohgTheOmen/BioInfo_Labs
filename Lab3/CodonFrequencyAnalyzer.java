
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class CodonFrequencyAnalyzer {

    private static final Map<String, String> geneticCode =
            new HashMap<>();

    static {
        String[] codons = {
            "UUU","UUC","UUA","UUG","UCU","UCC","UCA","UCG",
            "UAU","UAC","UAA","UAG","UGU","UGC","UGA","UGG",
            "CUU","CUC","CUA","CUG","CCU","CCC","CCA","CCG",
            "CAU","CAC","CAA","CAG","CGU","CGC","CGA","CGG",
            "AUU","AUC","AUA","AUG","ACU","ACC","ACA","ACG",
            "AAU","AAC","AAA","AAG","AGU","AGC","AGA","AGG",
            "GUU","GUC","GUA","GUG","GCU","GCC","GCA","GCG",
            "GAU","GAC","GAA","GAG","GGU","GGC","GGA","GGG"
        };

        String[] aminoAcids = {
            "Phe","Phe","Leu","Leu","Ser","Ser","Ser","Ser",
            "Tyr","Tyr","Stop","Stop","Cys","Cys","Stop","Trp",
            "Leu","Leu","Leu","Leu","Pro","Pro","Pro","Pro",
            "His","His","Gln","Gln","Arg","Arg","Arg","Arg",
            "Ile","Ile","Ile","Met","Thr","Thr","Thr","Thr",
            "Asn","Asn","Lys","Lys","Ser","Ser","Arg","Arg",
            "Val","Val","Val","Val","Ala","Ala","Ala","Ala",
            "Asp","Asp","Glu","Glu","Gly","Gly","Gly","Gly"
        };

        for (int i = 0; i < codons.length; i++) {
            geneticCode.put(codons[i], aminoAcids[i]);
        }
    }

    public static String readFasta(String filePath)
            throws IOException {

        StringBuilder sequence = new StringBuilder();

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(filePath))) {

            String line;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith(">")) {
                    continue;
                }

                sequence.append(line.trim().toUpperCase());
            }
        }

        return sequence.toString().replace('T', 'U');
    }

    public static Map<String, Integer> countCodons(
            String sequence) {

        Map<String, Integer> frequencies = new HashMap<>();

        for (int i = 0; i <= sequence.length() - 3; i++) {

            String codon = sequence.substring(i, i + 3);

            if (!codon.matches("[AUCG]{3}")) {
                continue;
            }

            frequencies.put(
                codon,
                frequencies.getOrDefault(codon, 0) + 1
            );
        }

        return frequencies;
    }

    public static void displayTop10(
            Map<String, Integer> frequencies) {

        int total = frequencies.values()
                .stream()
                .mapToInt(Integer::intValue)
                .sum();

        List<Map.Entry<String, Integer>> top10 =
                frequencies.entrySet()
                .stream()
                .sorted(
                    Map.Entry.<String, Integer>
                        comparingByValue()
                        .reversed()
                        .thenComparing(Map.Entry.comparingByKey())
                )
                .limit(10)
                .collect(Collectors.toList());

        System.out.printf(
            "%-8s %-10s %-12s %-12s%n",
            "Codon", "Amino Acid", "Count", "Frequency"
        );

        System.out.println("----------------------------------------------");

        for (Map.Entry<String, Integer> entry : top10) {

            String codon = entry.getKey();
            int count = entry.getValue();

            double percentage = total == 0 ? 0 :
                    (count * 100.0) / total;

            System.out.printf(
                "%-8s %-10s %-12d %.2f%%%n",
                codon,
                geneticCode.get(codon),
                count,
                percentage
            );
        }
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // System.out.print("Enter FASTA file path: ");
        // String filePath = scanner.nextLine();
        String filePath = "GCF_002194405.1_ViralProj390412_genomic.fna";

        try {
            String genome = readFasta(filePath);

            System.out.println("Genome length: "
                    + genome.length() + " nucleotides");

            Map<String, Integer> frequencies =
                    countCodons(genome);

            displayTop10(frequencies);

        } catch (IOException e) {
            System.out.println("Error reading FASTA file: "
                    + e.getMessage());
        }

        scanner.close();
    }
}
