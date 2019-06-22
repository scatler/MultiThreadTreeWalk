import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

public class MultiThreadTreeWalker2 {

    private Executor ex = Executors.newFixedThreadPool(2);
    private File f;

    public static void main(String[] args) {

        new MultiThreadTreeWalker2().reviewFileSystem(new File("D:\\From int"));
    }

    private void reviewFileSystem(File f) {
        if (f == null) {
            return;
        }
        if (f.isFile()) {
            System.out.println("File is found" + f + "by " + Thread.currentThread());
            return;
        }
        File[] files = f.listFiles();

        if (files.length != 0) {
            for (File f2: files) {
                ex.execute(() -> reviewFileSystem(f2));
            }
        }

    }

}
