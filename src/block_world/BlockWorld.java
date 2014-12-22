package block_world;

import java.io.PrintWriter;
import java.util.*;

/*
    http://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=37
 */

class BlockWorld {

    private List<Block>[] blockPositions;
    private int[] blockToPosition;
    private Block[] numberToBlock;

    public enum Command {MOVE_ONTO, MOVE_OVER, PILE_ONTO, PILE_OVER}

    public static final Map<String, Map<String, Command>> rawInputToCommand = new HashMap<>();

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
        this.blockPositions = new List[numberOfBlockPositions];
        this.blockToPosition = new int[numberOfBlockPositions];
        this.numberToBlock = new Block[numberOfBlockPositions];
        for (int i = 0; i < numberOfBlockPositions; i++) {
            Block newBlock = new Block(i);

            numberToBlock[i] = newBlock;

            blockPositions[i] = new ArrayList<>();
            blockPositions[i].add(newBlock);

            blockToPosition[i] = i;
        }

    }

    public void executeCommand(int topBlockNumber, int bottomBlockNumber, Command command) {
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
        Block block = this.numberToBlock[cmdInfo.topBlockNumber];

        this.blockPositions[cmdInfo.bottomBlockPosition].add(block);
        this.blockToPosition[cmdInfo.topBlockNumber] = cmdInfo.bottomBlockPosition;

        this.blockPositions[cmdInfo.topBlockPosition].remove(block);
    }

    private void placeStackOfBlocks(CommandInformation cmdInfo) {
        Block topBlock = this.numberToBlock[cmdInfo.topBlockNumber];

        List<Block> blocksInPositionOfTopBlock = this.blockPositions[cmdInfo.topBlockPosition];
        int indexOfTopBlock = blocksInPositionOfTopBlock.indexOf(topBlock);

        List<Block> stackOfBlocksToBePiled = this.getPileOfBlocks(blocksInPositionOfTopBlock, indexOfTopBlock);

        for (Block blockToBePiled : stackOfBlocksToBePiled) {
            this.blockToPosition[blockToBePiled.blockNumber] = cmdInfo.bottomBlockPosition;
        }

        blocksInPositionOfTopBlock.removeAll(stackOfBlocksToBePiled);
        this.blockPositions[cmdInfo.bottomBlockPosition].addAll(stackOfBlocksToBePiled);
    }

    private boolean commandIsValid(CommandInformation cmdInfo) {
        return cmdInfo.topBlockNumber != cmdInfo.bottomBlockNumber
                && cmdInfo.topBlockPosition != cmdInfo.bottomBlockPosition;
    }

    private void returnBlocksOnTopToOriginalPosition(int blockNumber) {
        Block topBlock = numberToBlock[blockNumber];

        int blockPosition = this.blockToPosition[blockNumber];
        List<Block> blocksInPosition = this.blockPositions[blockPosition];
        int indexOfTopBlock = blocksInPosition.indexOf(topBlock);

        List<Block> blocksOnTop = getPileOfBlocks(blocksInPosition, indexOfTopBlock + 1);

        blocksInPosition.removeAll(blocksOnTop);

        for (Block blockOnTop : blocksOnTop) {
            this.blockPositions[blockOnTop.blockNumber].add(blockOnTop);
            this.blockToPosition[blockOnTop.blockNumber] = blockOnTop.blockNumber;
        }
    }

    private List<Block> getPileOfBlocks(List<Block> blocksInPosition, int indexOfStartingBlock) {
        return new ArrayList<>(blocksInPosition.subList(indexOfStartingBlock, blocksInPosition.size()));
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < this.blockPositions.length; i++) {
            output.append(i).append(":");
            for (Block block : this.blockPositions[i]) {
                output.append(" ").append(block.blockNumber);
            }
            output.append("\n");
        }
        return output.toString();
    }

    private class CommandInformation {
        int topBlockNumber, bottomBlockNumber;
        int topBlockPosition, bottomBlockPosition;
    }

    private class Block {

        int blockNumber;

        public Block(int blockNumber) {
            this.blockNumber = blockNumber;
        }
    }

}

class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        PrintWriter out = new PrintWriter(System.out, true);

        int numberOfBlocks = in.nextInt();
        in.nextLine();

        BlockWorld blockWorld = new BlockWorld(numberOfBlocks);

        String line = in.nextLine();

        while (!line.equalsIgnoreCase("quit")) {
            String[] inputElements = line.split("\\s+");

            BlockWorld.Command cmd = BlockWorld.rawInputToCommand.get(inputElements[0]).get(inputElements[2]);
            int topBlockNumber = Integer.parseInt(inputElements[1]);
            int bottomBlockNumber = Integer.parseInt(inputElements[3]);

            blockWorld.executeCommand(topBlockNumber, bottomBlockNumber, cmd);
            line = in.nextLine();
        }
        out.printf(blockWorld.toString());
    }

}

