package blockchain.management;

import blockchain.commands.Command;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalTime;
import java.time.Period;

public class Miner implements Runnable {
    private final BlockChain blockChain;
    private long id;

    Miner(BlockChain blockChain) {
        this.blockChain = blockChain;
    }

    @Override
    public void run() {
        id = Thread.currentThread().getId();

        while (blockChain.getSize() <= 5) {
            generateBlock();
        }
    }

    private Block generateBlock() {
        LocalTime before = LocalTime.now();

        //boolean wasAdded = false;
        long generatedYet = blockChain.getSize();
        Block newBlock = new Block(/*Thread.currentThread().getId()*/id);
        Block prevBlock = blockChain.getTail();

        do {
            newBlock.init(blockChain.getN(), prevBlock);
        } while (!newBlock.isProved(blockChain.getN()));

        LocalTime after = LocalTime.now();
        int generationTime = after.toSecondOfDay() - before.toSecondOfDay();

        synchronized (blockChain) {
            if(generatedYet != blockChain.getSize() || !blockChain.add(newBlock)) return null;
            newBlock.setSeconds(generationTime);
            newBlock.setZerosDiff(blockChain.checkN(generationTime));
            return newBlock;
        }

    }
}
