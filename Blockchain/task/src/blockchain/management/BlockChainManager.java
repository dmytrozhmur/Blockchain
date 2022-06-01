package blockchain.management;

public class BlockChainManager {
    public static Miner getMiner(BlockChain blockChain) {
        return new Miner(blockChain);
    }

    public static BlockChain getBlockChain() {
        return new BlockChain();
    }
}
