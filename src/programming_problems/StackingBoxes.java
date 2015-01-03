package programming_problems;

import java.io.PrintWriter;
import java.util.*;

import static java.util.Collections.sort;

/*
    http://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=3&page=show_problem&problem=39
 */

class StackingBoxes {

    static final Comparator<Box> BOX_COMPARATOR = new Comparator<Box>() {

        @Override
        public int compare(Box box1, Box box2) {
            final int LESS_THAN = -1;
            final int GREATER = 1;
            final int EQUAL = 0;

            //Both boxes share the same number of dimensions
            for (int i = 0; i < box1.measurements.length; i++) {
                if (box1.measurements[i] > box2.measurements[i]) {
                    return GREATER;
                } else if (box1.measurements[i] < box2.measurements[i]) {
                    return LESS_THAN;
                }

            }

            return EQUAL;
        }
    };

    private List<Box> longestStringOfBoxes = new ArrayList<>();

    private List<Box> boxes = new ArrayList<>();

    public void addBox(Box box) {
        this.boxes.add(box);
    }

    public void findLongestNestingString() {
        int numberOfBoxes = this.boxes.size();

        int maxLength = 1;
        int endOfLongestNestingString = 0;
        int[] lengthOfStringAtIndex = new int[numberOfBoxes];
        int[] indicesOfLongestString = new int[numberOfBoxes];

        this.sortBoxes();

        lengthOfStringAtIndex[0] = 1;
        indicesOfLongestString[0] = -1;

        for (int i = 0; i < numberOfBoxes; i++) {

            lengthOfStringAtIndex[i] = 1;
            indicesOfLongestString[i] = -1;

            for (int j = i - 1; j >= 0; j--) {
                boolean canPreviousBoxBeNested = this.canBoxBeNested(this.boxes.get(j), this.boxes.get(i));
                if (canPreviousBoxBeNested && (lengthOfStringAtIndex[j] + 1 > lengthOfStringAtIndex[i])) {
                    lengthOfStringAtIndex[i] = lengthOfStringAtIndex[j] + 1;
                    indicesOfLongestString[i] = j;
                }
            }

            if (lengthOfStringAtIndex[i] > maxLength) {
                endOfLongestNestingString = i;
                maxLength = lengthOfStringAtIndex[i];
            }

        }

        int index = endOfLongestNestingString;
        while (index != -1) {
            this.longestStringOfBoxes.add(this.boxes.get(index));
            index = indicesOfLongestString[index];
        }

        Collections.reverse(this.longestStringOfBoxes);
    }

    public String boxesInLongestString() {
        StringBuilder out = new StringBuilder();

        for (int i = 0; i < this.longestStringOfBoxes.size(); i++) {
            out.append(this.longestStringOfBoxes.get(i).index);
            if (i < this.longestStringOfBoxes.size() - 1) {
                out.append(" ");
            }
        }

        return out.toString();
    }

    public int numberOfBoxesInLongestString() {
        return this.longestStringOfBoxes.size();
    }

    private void sortBoxes() {
        sort(this.boxes, BOX_COMPARATOR);
    }

    private boolean canBoxBeNested(Box boxToBeNested, Box containerBox) {
        for (int i = 0; i < boxToBeNested.measurements.length; i++) {
            if (boxToBeNested.measurements[i] >= containerBox.measurements[i]) {
                return false;
            }
        }
        return true;
    }

}

class Box {

    int index;
    int[] measurements;

    public Box(int numberOfDimensions) {
        this.measurements = new int[numberOfDimensions];
    }
}

class SBMain {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        PrintWriter out = new PrintWriter(System.out, true);

        while (in.hasNextLine()) {

            String firstLine = in.nextLine();
            String[] specification = firstLine.split("\\s+");

            int numberOfBoxes = Integer.parseInt(specification[0]);
            int numberOfDimensions = Integer.parseInt(specification[1]);

            StackingBoxes stackingBoxes = new StackingBoxes();

            for (int i = 0; i < numberOfBoxes; i++) {
                String boxLine = in.nextLine();

                String[] boxMeasurements = boxLine.split("\\s+");

                Box box = new Box(numberOfDimensions);
                box.index = i + 1; //Boxes count starts with 1

                for (int j = 0; j < numberOfDimensions; j++) {
                    box.measurements[j] = Integer.parseInt(boxMeasurements[j]);
                }

                Arrays.sort(box.measurements);

                stackingBoxes.addBox(box);
            }

            stackingBoxes.findLongestNestingString();

            out.printf("%d\n", stackingBoxes.numberOfBoxesInLongestString());
            out.printf("%s\n", stackingBoxes.boxesInLongestString());
        }

    }

}