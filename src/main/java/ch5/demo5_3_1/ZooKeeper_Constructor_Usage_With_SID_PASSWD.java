package ch5.demo5_3_1;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

public class ZooKeeper_Constructor_Usage_With_SID_PASSWD implements Watcher {
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws Exception{
        ZooKeeper zooKeeper = new ZooKeeper("192.168.43.200:2181",
                5000,
                new ZooKeeper_Constructor_Usage_With_SID_PASSWD());
        connectedSemaphore.await();
        long sessionId = zooKeeper.getSessionId();
        byte[] passwd = zooKeeper.getSessionPasswd();

        // Use illegal sessionId and sessionPassWd
        zooKeeper = new ZooKeeper("192.168.43.200:2181",
                5000,
                new ZooKeeper_Constructor_Usage_With_SID_PASSWD(),
                1L,
                "test".getBytes());

        // Use correct sessionId and sessionPassWd
        zooKeeper = new ZooKeeper("192.168.43.200:2181",
                5000,
                new ZooKeeper_Constructor_Usage_With_SID_PASSWD(),
                sessionId,
                passwd);
        Thread.sleep(Integer.MAX_VALUE);
    }

    public void process(WatchedEvent watchedEvent) {
        System.out.println("Receive watched event:" + watchedEvent);
        if(Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            connectedSemaphore.countDown();
        }
    }
}
