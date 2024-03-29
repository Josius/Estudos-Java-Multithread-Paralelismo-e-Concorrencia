import java.util.concurrent.CompletableFuture;

public class Aula12_1_CompletableFuture {

	public static void main(String[] args) {
		
		CompletableFuture<String> processe = processe();

		CompletableFuture<String> thenApply = processe.thenApply(s -> s + " Curta o vídeo!");

		CompletableFuture<Void> thenAccept = thenApply.thenAccept(s -> System.out.println(s));

		System.out.println("Hello there!!");

		sleep();
		sleep();
		sleep();
	}	

	private static CompletableFuture<String> processe(){
		return CompletableFuture.supplyAsync(() -> {
			sleep();
			return "General Kenobi!";
		});
	}

	private static final void sleep() {

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			e.printStackTrace();
		}
	}
}