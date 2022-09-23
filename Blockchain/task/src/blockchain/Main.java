package blockchain;

import blockchain.management.BlockChain;
import blockchain.management.BlockChainManager;
import blockchain.management.Miner;

import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static blockchain.ConsoleHandler.getMessage;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        LocalTime before = LocalTime.now();

        BlockChain blockChain = BlockChainManager.getBlockChain();
//        List<User> users = List.of(
//                new User("Dmytro", blockChain), new User("Usup", blockChain));
        List<MoneyHandler> moneyHandlers = new ArrayList<>();

        for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
            moneyHandlers.add(new Miner(blockChain));
        }
        blockChain.setMoneyHandlers(moneyHandlers);

        moneyHandlers.forEach(Thread::start);
//        users.get(0).run();

        for (MoneyHandler handler : moneyHandlers) {
            handler.join();
        }

        blockChain.printBlocks(15);
        LocalTime after = LocalTime.now();

        System.out.println("\n" + (after.toSecondOfDay() - before.toSecondOfDay()));
    }
}
