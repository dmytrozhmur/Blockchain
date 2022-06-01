package blockchain.management;

import java.io.Serializable;
import java.util.Date;

import static blockchain.Encryption.applySHA256;
import static blockchain.Encryption.getRandomNumber;

public class Block implements Serializable {
    private int seconds;
    private long id;
    private long timeStamp;
    private long magicNumber;
    private String previousHash;
    private String zeros;
    private byte zerosDiff;
    private long miner;

    Block next;

    Block(long miner) {
        this.miner = miner;
    }

//    Block(long id, String previousHash) {
//        this.id = id;
//        this.previousHash = previousHash;
//
//        this.timeStamp = new Date().getTime();
//        this.magicNumber = getRandomNumber();
//    }

    public void init(int zerosQuantity, Block prev) {
        if(prev == null) {
            this.id = 1;
            this.previousHash = String.valueOf(0);
        } else {
            this.id = prev.id + 1;
            this.previousHash = prev.hash();
        }

        this.timeStamp = new Date().getTime();
        this.magicNumber = getRandomNumber();

        this.zeros = "0".repeat(Math.max(0, zerosQuantity));
    }

    public String hash() {
        return applySHA256(id + timeStamp + magicNumber + previousHash);
    }

    public boolean isProved(int zerosQuantityRequired) {
        return hash().startsWith("0".repeat(zerosQuantityRequired))
                && !hash().startsWith("0".repeat(zerosQuantityRequired + 1));
    }

    public String getPreviousHash() {
        return previousHash;
    }

    void print() {
        System.out.println("Block:\nCreated by miner # " + miner);
        System.out.println("Id: " + id);
        System.out.println("Timestamp: " + timeStamp);
        System.out.println("Magic number: " + magicNumber);
        System.out.println("Hash of the previous block:\n" + previousHash);
        System.out.println("Hash of the block:\n" + hash());
        System.out.printf("Block was generated for %d seconds\n", seconds);

        if(zerosDiff == 1) System.out.println("N was increased to " + (zeros.length() + 1));
        else if(zerosDiff == -1) System.out.println("N was decreased by 1");
        else System.out.println("N stays the same");
        System.out.println();
    }

    void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setZerosDiff(byte zerosDiff) {
        this.zerosDiff = zerosDiff;
    }
}
