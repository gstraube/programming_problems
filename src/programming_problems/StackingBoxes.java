package programming_problems;

import java.io.PrintWriter;
import java.util.*;

import static java.util.Collections.sort;

class StackingBoxes {

    static final Comparator<Box> BOX_COMPARATOR = new Comparator<Box>() {

        @Override
        public int compare(Box box1, Box box2) {
            long product1 = 1;
            long product2 = 1;

            //Both boxes share the same number of dimensions
            for (int i = 0; i < box1.measurements.length; i++) {
                product1 *= box1.measurements[i];
                product2 *= box2.measurements[i];
            }

            return Long.compare(product1, product2);
        }
    };

    private List<Box> longestStringOfBoxes = new ArrayList<>();

    private List<Box> boxes = new ArrayList<>();

    public void addBox(Box box) {
        this.boxes.add(box);
    }

    public void findLongestNestingString() {
        int maxLength = 0;

        this.sortBoxes();

        for (int i = 0; i < this.boxes.size(); i++) {
            List<Box> stringOfBoxes = this.getNestingStringStartingFromBoxIndex(i);
            int stringOfBoxesLength = stringOfBoxes.size();
            if (stringOfBoxesLength > maxLength) {
                this.longestStringOfBoxes = stringOfBoxes;
                maxLength = stringOfBoxesLength;
            }
        }
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

    private List<Box> getNestingStringStartingFromBoxIndex(int startBoxIndex) {
        int currentBoxIndex = startBoxIndex;
        int nextBoxIndex = startBoxIndex + 1;

        List<Box> stringOfBoxes = new ArrayList<>();

        stringOfBoxes.add(this.boxes.get(currentBoxIndex));

        while (nextBoxIndex < this.boxes.size()) {
            Box currentBox = this.boxes.get(currentBoxIndex);
            Box nextBox = this.boxes.get(nextBoxIndex);
            Arrays.sort(currentBox.measurements);
            Arrays.sort(nextBox.measurements);

            if (this.canBoxBeNested(currentBox, nextBox)) {
                stringOfBoxes.add(nextBox);
                currentBoxIndex = nextBoxIndex;
            }
            nextBoxIndex++;
        }

        return stringOfBoxes;
    }

}

class Box {

    int index;
    int[] measurements;

    public Box(int numberOfDimensions) {
        this.measurements = new int[numberOfDimensions];
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        for (Integer measurement : this.measurements) {
            out.append(measurement);
            out.append(" ");
        }
        return out.toString();
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

                stackingBoxes.addBox(box);
            }

            stackingBoxes.findLongestNestingString();

            out.printf("%d\n", stackingBoxes.numberOfBoxesInLongestString());
            out.printf("%s\n", stackingBoxes.boxesInLongestString());
        }

    }

}