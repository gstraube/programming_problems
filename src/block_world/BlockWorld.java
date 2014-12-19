package block_world;

import java.util.*;

public class BlockWorld {

    private List<Integer>[] blockPositions;
    private Map<Integer, Integer> blockToPosition;

    private enum Command {MOVE_ONTO, MOVE_OVER, PILE_ONTO, PILE_OVER};

    private static final Map<String, Map<String, Command>> rawInputToCommand = new HashMap<>();

    static {
        Map<String, Command> mapForMove = new HashMap<>();
        mapForMove.put("onto", Command.MOVE_ONTO);
        mapForMove.put("over", Command.MOVE_OVER);
        rawInputToCommand.put("move", mapForMove);

        Map<String, Command> mapForPile = new HashMap<>();
        mapForPile.put("onto", Command.PILE_ONTO);
        mapForPile.put("over", Command.PILE_OVER);
        rawInputToCommand.put("pile", mapForPile);
    }

    public BlockWorld(int numberOfBlockPositions) {
        this.blockPositions = new ArrayList[numberOfBlockPositions];
        this.blockToPosition = new HashMap<Integer, Integer>();
        for (int i = 0; i < numberOfBlockPositions; i++) {
            blockPositions[i] = new ArrayList<Integer>();;
            blockPositions[i].add(i);
            blockToPosition.put(i, i);
        }

    }

    private void executeCommand(int topBlockNumber, int bottomBlockNumber, Command command) {
        int topBlockPosition = this.blockToPosition.get(topBlockNumber);
        int bottomBlockPosition = this.blockToPosition.get(bottomBlockNumber);

        if (commandIsValid(topBlockNumber, bottomBlockNumber, topBlockPosition, bottomBlockPosition)) {
            switch (command) {
                case MOVE_ONTO:
                    this.moveOnto(topBlockNumber, topBlockPosition, bottomBlockNumber, bottomBlockPosition);
                    break;
                default:
                    break;
            }
        }

    }

    private void moveOnto(int topBlockNumber, int topBlockPosition, int bottomBlockNumber, int bottomBlockPosition) {
        returnBlocksOnTopToOriginalPosition(topBlockNumber);
        returnBlocksOnTopToOriginalPosition(bottomBlockNumber);

        this.blockPositions[bottomBlockPosition].add(topBlockNumber);
        this.blockToPosition.put(topBlockNumber, bottomBlockPosition);

        //We need an Integer here instead of an int because otherwise the remove(int index) method
        //is called instead of the remove(Object o) method.
        this.blockPositions[topBlockPosition].remove(new Integer(topBlockNumber));
    }

    private boolean commandIsValid(int topBlockNumber, int bottomBlockNumber,
                                   int topBlockPosition, int bottomBlockPosition) {
        return topBlockNumber != bottomBlockNumber && topBlockPosition != bottomBlockPosition;
    }

    private void returnBlocksOnTopToOriginalPosition(int blockNumber) {
        int blockPosition = this.blockToPosition.get(blockNumber);
        List<Integer> blocksInPosition = this.blockPositions[blockPosition];
        int indexOfTopBlock = this.blockPositions[blockPosition].indexOf(blockNumber);

        List<Integer> blocksOnTop = getBlocksOnTop(blocksInPosition, indexOfTopBlock);

        for (Integer blockOnTop : blocksOnTop) {
            blocksInPosition.remove(blockOnTop);
            this.blockPositions[blockOnTop].add(blockOnTop);
        }
    }

    private List<Integer> getBlocksOnTop(List<Integer> blocksInPosition, int indexOfTopBlock) {
        return blocksInPosition.subList(indexOfTopBlock + 1, blocksInPosition.size());
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < this.blockPositions.length; i++) {
            output.append(i).append(":");
            for (Integer blockNumber : this.blockPositions[i]) {
                output.append(" ").append(blockNumber);
            }
            output.append(" | ");
        }
        return output.toString();
    }

    public static void main(String[] args) {
        BlockWorld blockWorld = new BlockWorld(10);

        blockWorld.executeCommand(0, 1, Command.MOVE_ONTO);
        blockWorld.executeCommand(3, 1, Command.MOVE_ONTO);

        System.out.println(blockWorld);
    }
}

