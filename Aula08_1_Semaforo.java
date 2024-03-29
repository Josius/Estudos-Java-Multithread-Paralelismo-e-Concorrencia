import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class Aula08_1_Semaforo {
    
    private static final Semaphore SEMAFORO = new Semaphore(3);
    public static void main(String[] args) {
        
        ExecutorService executor = Executors.newCachedThreadPool();

        Runnable r1 = () -> {
            String name = Thread.currentThread().getName();
            int usuario = new Random().nextInt(10000);

            acquired();
            System.out.println("Usuário " + usuario + " usou a thread " + name);
            sleep();

            SEMAFORO.release();
        };

        for (int i = 0; i < 500; i++) {
            executor.execute(r1);
        }
        executor.shutdown();
    }

    private static void sleep() {
        // espera de 1 a 6 segundos
        try {
            int tempoEspera = new Random().nextInt(6);
            tempoEspera++;
            Thread.sleep(1000 * tempoEspera);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    private static void acquired() {
        try {
            SEMAFORO.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
}