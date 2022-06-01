package blockchain;

import blockchain.management.BlockChain;
import blockchain.management.BlockChainManager;
import blockchain.management.Miner;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static blockchain.ConsoleHandler.getInputNumber;

public class Main {
    public static void main(String[] args) throws InterruptedException {
//        BlockChain blockChain = new BlockChain();
//        for (int i = 0; i < 5; i++) {
//            blockChain.generateBlock();
//        }

        BlockChain blockChain = BlockChainManager.getBlockChain();
        ExecutorService executor = Executors.newFixedThreadPool(10);

        executor.submit(BlockChainManager.getMiner(blockChain));

        executor.shutdown();
        while (!executor.isTerminated()) {
            Thread.sleep(100);
        }
        blockChain.printBlocks(5);
    }
}
