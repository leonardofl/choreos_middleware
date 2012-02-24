package eu.choreos.storagefactory.rest;

import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;

/**
 * Stand alone server that makes the REST API available to clients
 * 
 * @author leonardo, alfonso
 * 
 */
public class StandaloneServer implements Runnable {

	private static final String HOST = "http://localhost:8081/";
	private static boolean running = false;

	public static void start() throws InterruptedException {
		new Thread(new StandaloneServer()).start();
		while (!running) {
			Thread.sleep(1);
		}

	}

	public static void stop() {
		running = false;
	}

	public void run() {
		JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
		sf.setResourceClasses(StoragesResource.class);
		sf.setAddress(HOST);
		sf.create();
		System.out.println("Starting Storage Factory...");
		running = true;

		while (running) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Stoping CHOReOS Middleware...");

	}

	public static void main(String[] args) throws InterruptedException {
		StandaloneServer.start();
	}
}
