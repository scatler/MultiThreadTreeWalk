import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

public class MultiThreadTreeWalker extends Thread {

    private static BlockingQueue<File> nodesToReview = new LinkedBlockingDeque<>();
    private File f;

    public MultiThreadTreeWalker(File f) {
        this.f = f;
    }

    public MultiThreadTreeWalker() {
    }

    public static void main(String[] args) {
        Executor ex = Executors.newFixedThreadPool(2);
        MultiThreadTreeWalker mw =  new MultiThreadTreeWalker(new File("D:\\From int\\pwned-passwords-ntlm-ordered-by-hash-v4"));
        mw.run();
        for (int i = 0;i<2;i++) {
            ex.execute(new MultiThreadTreeWalker());
        }
    }

    @Override
    public void run() {
        if (f != null) { //только для первого потока
            reviewFileSystem(f);
        } else {
            try {
                while (true) {


                    f = nodesToReview.take();
                    reviewFileSystem(f);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    void reviewFileSystem(File f) {
        if (f == null) {
            return;
        }
        if (f.isFile()) {

            //завершение обхода
            System.out.println("Файл " + f.getName() + " найден потоком " + Thread.currentThread());
            return;
        }
        File[] files = f.listFiles();

        if (files.length != 0) {

            for (int i = 0; i < files.length - 1; i++) {

                nodesToReview.add(files[i]); //добавление файлов всех кроме последнего

            }
            //последний дочерний узел используется для перехода дальше

            File last = files[files.length - 1];

            reviewFileSystem(last);

        }

    }

}
