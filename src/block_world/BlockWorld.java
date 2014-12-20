package block_world;

import java.util.*;

public class BlockWorld {

    private List<Integer>[] blockPositions;
    private int[] blockToPosition;
    private enum Command {MOVE_ONTO, MOVE_OVER, PILE_ONTO, PILE_OVER}

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
        this.blockToPosition = new int[numberOfBlockPositions];
        for (int i = 0; i < numberOfBlockPositions; i++) {
            blockPositions[i] = new ArrayList<>();
            blockPositions[i].add(i);
            blockToPosition[i] = i;
        }

    }

    private void executeCommand(int topBlockNumber, int bottomBlockNumber, Command command) {
        CommandInformation cmdInfo = new CommandInformation();
        cmdInfo.topBlockNumber = topBlockNumber;
        cmdInfo.bottomBlockNumber = bottomBlockNumber;
        cmdInfo.topBlockPosition = this.blockToPosition[topBlockNumber];
        cmdInfo.bottomBlockPosition = this.blockToPosition[bottomBlockNumber];

        if (commandIsValid(cmdInfo)) {
            switch (command) {
                case MOVE_ONTO:
                    this.moveOnto(cmdInfo);
                    break;
                case MOVE_OVER:
                    this.moveOver(cmdInfo);
                    break;
                case PILE_ONTO:
                    this.pileOnto(cmdInfo);
                    break;
                case PILE_OVER:
                    this.pileOver(cmdInfo);
                    break;
                default:
                    break;
            }
        }
    }

    private void moveOnto(CommandInformation cmdInfo) {
        returnBlocksOnTopToOriginalPosition(cmdInfo.topBlockNumber);
        returnBlocksOnTopToOriginalPosition(cmdInfo.bottomBlockNumber);
        placeSingleBlock(cmdInfo);
    }

    private void moveOver(CommandInformation cmdInfo) {
        returnBlocksOnTopToOriginalPosition(cmdInfo.topBlockNumber);
        placeSingleBlock(cmdInfo);
    }

    private void pileOnto(CommandInformation cmdInfo) {
        returnBlocksOnTopToOriginalPosition(cmdInfo.bottomBlockNumber);
        placeStackOfBlocks(cmdInfo);
    }

    private void pileOver(CommandInformation cmdInfo) {
        placeStackOfBlocks(cmdInfo);
    }

    private void placeSingleBlock(CommandInformation cmdInfo) {
        this.blockPositions[cmdInfo.bottomBlockPosition].add(cmdInfo.topBlockNumber);
        this.blockToPosition[cmdInfo.topBlockNumber] = cmdInfo.bottomBlockPosition;

        //We need an Integer here instead of an int because otherwise the remove(int index) method
        //is called instead of the remove(Object o) method.
        this.blockPositions[cmdInfo.topBlockPosition].remove(new Integer(cmdInfo.topBlockNumber));
    }

    private void placeStackOfBlocks(CommandInformation cmdInfo) {
        List<Integer> blocksInPositionOfTopBlock = this.blockPositions[cmdInfo.topBlockPosition];
        int indexOfTopBlock = blocksInPositionOfTopBlock.indexOf(cmdInfo.topBlockNumber);
        List<Integer> stackOfBlocksToBePiled = this.getPileOfBlocks(blocksInPositionOfTopBlock, indexOfTopBlock);

        for (Integer blockToBePiled : stackOfBlocksToBePiled) {
            this.blockToPosition[blockToBePiled] = cmdInfo.bottomBlockPosition;
        }

        blocksInPositionOfTopBlock.removeAll(stackOfBlocksToBePiled);
        this.blockPositions[cmdInfo.bottomBlockPosition].addAll(stackOfBlocksToBePiled);
    }

    private boolean commandIsValid(CommandInformation cmdInfo) {
        return cmdInfo.topBlockNumber != cmdInfo.bottomBlockNumber
                && cmdInfo.topBlockPosition != cmdInfo.bottomBlockPosition;
    }

    private void returnBlocksOnTopToOriginalPosition(int blockNumber) {
        int blockPosition = this.blockToPosition[blockNumber];
        List<Integer> blocksInPosition = this.blockPositions[blockPosition];
        int indexOfTopBlock = blocksInPosition.indexOf(blockNumber);

        List<Integer> blocksOnTop = getPileOfBlocks(blocksInPosition, indexOfTopBlock + 1);

        for (Integer blockOnTop : blocksOnTop) {
            blocksInPosition.remove(blockOnTop);
            this.blockPositions[blockOnTop].add(blockOnTop);
        }
    }

    private List<Integer> getPileOfBlocks(List<Integer> blocksInPosition, int indexOfStartingBlock) {
        return new ArrayList<>(blocksInPosition.subList(indexOfStartingBlock, blocksInPosition.size()));
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

    private class CommandInformation {
        int topBlockNumber, bottomBlockNumber;
        int topBlockPosition, bottomBlockPosition;
    }

    public static void main(String[] args) {
        BlockWorld blockWorld = new BlockWorld(10);

        blockWorld.executeCommand(9, 1, Command.MOVE_ONTO);
        blockWorld.executeCommand(8, 1, Command.MOVE_OVER);
        blockWorld.executeCommand(7, 1, Command.MOVE_OVER);
        blockWorld.executeCommand(6, 1, Command.MOVE_OVER);
        blockWorld.executeCommand(8, 6, Command.PILE_OVER);
        blockWorld.executeCommand(8, 5, Command.PILE_OVER);
        blockWorld.executeCommand(2, 1, Command.MOVE_OVER);
        blockWorld.executeCommand(4, 9, Command.MOVE_OVER);

        System.out.println(blockWorld);
    }

}

