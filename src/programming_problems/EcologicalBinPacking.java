package programming_problems;

import java.io.PrintWriter;
import java.util.Scanner;

class EcologicalBinPacking {

    static String[] binPermutations = {"BCG", "BGC", "CBG", "CGB", "GBC", "GCB"};

    static int[][] costsForBin = { {1, 2}, {0, 1}, {0, 2} };

    static int[][] permutations = { {0, 1, 2}, {0, 2, 1}, {1, 0, 2}, {1, 2, 0}, {2, 0, 1}, {2, 1, 0} };

    static int[] firstBin = new int[3];
    static int[] secondBin = new int[3];
    static int[] thirdBin = new int[3];

    public static Result calculateMinimalCost() {

        int minCost = Integer.MAX_VALUE;
        int foundAtPermutationIndex = 0;
        int counter = 0;

        for (int[] permutation : permutations) {
            int cost = 0;

            cost += firstBin[costsForBin[permutation[0]][0]];
            cost += firstBin[costsForBin[permutation[0]][1]];

            cost += secondBin[costsForBin[permutation[1]][0]];
            cost += secondBin[costsForBin[permutation[1]][1]];

            cost += thirdBin[costsForBin[permutation[2]][0]];
            cost += thirdBin[costsForBin[permutation[2]][1]];

            if (cost < minCost) {
                minCost = cost;
                foundAtPermutationIndex = counter;
            }

            counter++;
        }

        Result result = new Result();
        result.numberOfMovements = minCost;
        result.permutation = binPermutations[foundAtPermutationIndex];

        return result;
    }

}

class Result {
    int numberOfMovements;
    String permutation;
}

class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        PrintWriter out = new PrintWriter(System.out, true);

        while (in.hasNextLine()) {
            String line = in.nextLine();

            String[] inputElements = line.split("\\s+");

            for (int i = 0; i < 3; i++) {
                EcologicalBinPacking.firstBin[i] = Integer.parseInt(inputElements[i]);
                EcologicalBinPacking.secondBin[i] = Integer.parseInt(inputElements[i + 3]);
                EcologicalBinPacking.thirdBin[i] = Integer.parseInt(inputElements[i + 6]);
            }

            Result result = EcologicalBinPacking.calculateMinimalCost();

            out.printf("%s %d\n", result.permutation, result.numberOfMovements);
        }

    }

}