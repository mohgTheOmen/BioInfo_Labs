import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class GeneTranslator {
    private static final Map<String, String> code = new HashMap<>();

    static {
        String[] codons = {
            "UUU", "UUC", "UUA", "UUG", "UCU", "UCC", "UCA", "UCG",
            "UAU", "UAC", "UAA", "UAG", "UGU", "UGC", "UGA", "UGG",
            "CUU", "CUC", "CUA", "CUG", "CCU", "CCC", "CCA", "CCG",
            "CAU", "CAC", "CAA", "CAG", "CGU", "CGC", "CGA", "CGG",
            "AUU", "AUC", "AUA", "AUG", "ACU", "ACC", "ACA", "ACG",
            "AAU", "AAC", "AAA", "AAG", "AGU", "AGC", "AGA", "AGG",
            "GUU", "GUC", "GUA", "GUG", "GCU", "GCC", "GCA", "GCG",
            "GAU", "GAC", "GAA", "GAG", "GGU", "GGC", "GGA", "GGG"
    };

        String[] aminoacids = {
            "Phe", "Phe", "Leu", "Leu", "Ser", "Ser", "Ser", "Ser",
            "Tyr", "Tyr", "Stop", "Stop", "Cys", "Cys", "Stop", "Trp",
            "Leu", "Leu", "Leu", "Leu", "Pro", "Pro", "Pro", "Pro",
            "His", "His", "Gln", "Gln", "Arg", "Arg", "Arg", "Arg",
            "Ile", "Ile", "Ile", "Met", "Thr", "Thr", "Thr", "Thr",
            "Asn", "Asn", "Lys", "Lys", "Ser", "Ser", "Arg", "Arg",
            "Val", "Val", "Val", "Val", "Ala", "Ala", "Ala", "Ala",
            "Asp", "Asp", "Glu", "Glu", "Gly", "Gly", "Gly", "Gly"
    };

        for (int i = 0; i < codons.length; i++) {
            code.put(codons[i], aminoacids[i]);
        }
    } 

    public static String dna2mrna(String dna) {
        dna = dna.toUpperCase().replaceAll("\\s+", "");
        
        if (!dna.matches("[ATGC]+")) {
            throw new IllegalArgumentException("Invalid DNA sequence.");
        }

        return dna.replace('T', 'U');
    }

    public static String translate(String dna) {
        String mrna = dna2mrna(dna);

        StringBuilder protein = new StringBuilder();

        for (int i = 0; i < mrna.length() - 2; i += 3) {
            String codon = mrna.substring(i, i + 3);
            String aminoacid = code.get(codon);

            if (aminoacid.equals("Stop")) {
                break;
            }

            if (protein.length() > 0) {
                protein.append("-");
            }

            protein.append(aminoacid);
        }  

        return protein.toString();
    }

    public static String translate2(String dna) {
        String mrna = dna2mrna(dna);

        int start = mrna.indexOf("AUG");

        if (start == -1 || start == 0) {
            return "No start codon (AUG) found.";
        }

        StringBuilder protein = new StringBuilder();

        for (int i = start; i + 2 < mrna.length(); i += 3) {
            String codon = mrna.substring(i, i + 3);
            String aminoacid = code.get(codon);

            if (aminoacid.equals("Stop")) {
                break;
            }

            if (protein.length() > 0) {
                protein.append("-");
            }

            protein.append(aminoacid);
        }

        return protein.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter DNA coding sequence:");
        String dna = scanner.nextLine();

        try {
            System.out.println("mRNA: " + dna2mrna(dna));
            System.out.println("Amino acid: " + translate(dna));
            System.out.println("Amino acid 2: " + translate2(dna));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        scanner.close();
    }
}