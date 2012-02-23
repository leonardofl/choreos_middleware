package br.usp.ime.ccsl.choreos.hadoop;


import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;

public class HadoopWSServer implements Runnable {

	private static boolean running = false;

	public static void start() throws InterruptedException {
		new Thread(new HadoopWSServer()).start();
		while (!running) {
			Thread.sleep(1);
		}

	}

	public static void stop() {
		running = false;
	}

	public void run() {
		JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
		sf.setResourceClasses(HadoopWS.class);
		sf.setAddress("http://localhost:8080/");
		sf.create();
		System.out.println("Starting server...");
		running = true;

		while (running) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Stopping server...");

	}

	public static void main(String[] args) throws InterruptedException {
		HadoopWSServer.start();
	}

}