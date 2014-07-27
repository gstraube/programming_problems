package programming_problems;

import java.io.PrintWriter;
import java.util.Scanner;

public class CollatzConjecture {

	static final int ARRAY_SIZE = 1000000;
	static int[] cycleLenghts = new int[ARRAY_SIZE];

	static int calculateCycleLength(int n) {
		int cycleCounter = 1;
		long currentNumber = n;
		while (currentNumber > 1) {

			boolean hasBeenComputed = currentNumber < ARRAY_SIZE
					&& cycleLenghts[(int) currentNumber] != 0;
			if (hasBeenComputed) {
				cycleCounter += cycleLenghts[(int) currentNumber];
				break;
			}

			if ((currentNumber % 2) == 0) {
				currentNumber = currentNumber / 2;
				cycleCounter++;
			} else {
				/*
				 * If currentNumber is odd, then currentNumber = 2 * m + 1 for
				 * some m. The next number is 3 * (2 * m + 1) + 1 = 6 * m + 4 =
				 * 2 * (3 * m + 2), i.e. the next number is even. We can make
				 * use of this fact and skip the next number by dividing by 2.
				 */
				currentNumber = (3 * currentNumber + 1) / 2;
				cycleCounter = cycleCounter + 2;
			}

		}
		if (n < ARRAY_SIZE) {
			cycleLenghts[(int) currentNumber] = cycleCounter;
		}

		return cycleCounter;
	}

	static int getMaxCycleLength(int i, int j) {

		int maxCycleLength = 0;
		for (int k = i; k <= j; k++) {
			maxCycleLength = Math.max(maxCycleLength, calculateCycleLength(k));
		}

		return maxCycleLength;
	}

	public static void main(String[] args) {
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		PrintWriter out = new PrintWriter(System.out, true);

		while (in.hasNextInt()) {
			int i = in.nextInt();
			int j = in.nextInt();
			int lower = Math.min(i, j);
			int upper = Math.max(i, j);

			int maxCycleLength = getMaxCycleLength(lower, upper);

			out.printf("%d %d %d\n", i, j, maxCycleLength);
		}

	}

}
