package blockchain.management;

import blockchain.management.Block;

import java.io.*;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class BlockChain implements Iterable<Block>, Serializable {
    private static final File storageFile;
    private Block head;
    private Block tail;
    private long size;
    private AtomicInteger N = new AtomicInteger(0);

    static {
        storageFile = new File("C:\\Users\\Tamada\\IdeaProjects\\Blockchain\\Blockchain\\task\\src\\blockchain\\BlockChain Storage.txt");
    }

    BlockChain() {}

//    public BlockChain(long zerosQuantity) {
//        this(zerosQuantity, 5);
//    }
//
//    public BlockChain(long zerosQuantity, long blocksQuantity) {
//        this.zerosQuantity = zerosQuantity;
//
//        for (int i = 0; i < blocksQuantity; i++) {
//            generateBlock();
//        }
//
//        for (Block b: this) {
//            System.out.println(b.id);
//        }
//    }

//    public static BlockChain create(long zerosQuantity) {
//        return create(zerosQuantity, 5);
//    }
//
//    public static BlockChain create(long zerosQuantity, long blocksQuantity) throws IllegalArgumentException {
//        BlockChain newChain = load(zerosQuantity);
//
//        if(newChain == null)
//            newChain = generate(zerosQuantity, blocksQuantity);
//
//        return newChain;
//    }

    public void checkValidityOf(Block beingChecked) throws InvalidObjectException {
        if(beingChecked.isProved(N.get()) && (tail == null
                || Objects.equals(tail.hash(), beingChecked.getPreviousHash()))) return;

        throw new InvalidObjectException("Block isn't valid");
    }

    public byte checkN(int generationTime) {
        if(generationTime > 15) {
            N.decrementAndGet();
            return -1;
        }

        if(generationTime <= 5) {
            N.incrementAndGet();
            return 1;
        }

        return 0;
    }

    public void printBlocks(int bound) {
        if(0 > bound || bound > size)
            throw new IllegalArgumentException();

        int counter = 0;
        for (Block block: this) {
            block.print();
            if(counter++ == bound) return;
        }
    }

//    private void readObject(ObjectInputStream ois)
//            throws ClassNotFoundException, IOException {
//        ois.defaultReadObject();
//        if(!checkValidity())
//            throw new IOException();
//    }

//    public static BlockChain load() {
//        BlockChain loaded;
//
//        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storageFile))) {
//            loaded = (BlockChain) ois.readObject();
//            for (Block block: loaded) {
//                if(!loaded.checkValidityOf(block))
//                    throw new ClassNotFoundException();
//                return loaded;
//            }
//        } catch (IOException | ClassNotFoundException e) {
//            System.err.println("Storage not found");
//        }
//
//        return null;
//    }

//    public static BlockChain generate(long blocksQuantity) {
//        BlockChain generated = new BlockChain();
//
//        for (int i = 0; i < blocksQuantity; i++) {
//            LocalTime before = LocalTime.now();
//            generated.add();
//            LocalTime after = LocalTime.now();
//
//            int generationTime = after.toSecondOfDay() - before.toSecondOfDay();
//        }
//        return generated;
//    }

    public boolean add(Block newBlock) {
        try {
            checkValidityOf(newBlock);
        } catch (InvalidObjectException e) {
            return false;
        }

        if(head == null) head = newBlock;
        else tail.next = newBlock;

        tail = newBlock;
        size++;

        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storageFile))) {
            oos.writeObject(this);
        } catch (IOException e) {
            System.err.println("Block data wasn't saved");
        }

        return true;
    }

    Block getHead() {
        return head;
    }

    Block getTail() {
        return tail;
    }

    public int getN() {
        return N.get();
    }

    //    private void readObject(ObjectInputStream ois)
//            throws ClassNotFoundException, IOException {
//        head = (Block) ois.readObject();
//
//        Block temp = head;
//        while (temp.next != null) {
//            temp = (Block) ois.readObject();
//        }
//
//        tail = temp;
//    }


    public long getSize() {
        return size;
    }

    @Override
    public Iterator<Block> iterator() {
        return new BlockItr(head);
    }

    private class BlockItr implements Iterator<Block> {
        private Block curr;
        private Block lastReturned;

        public BlockItr(Block curr) {
            this.curr = curr;
        }

        @Override
        public boolean hasNext() {
            return curr != tail;
        }

        @Override
        public Block next() {
            lastReturned = curr;
            curr = curr.next;
            return lastReturned;
        }
    }


}
