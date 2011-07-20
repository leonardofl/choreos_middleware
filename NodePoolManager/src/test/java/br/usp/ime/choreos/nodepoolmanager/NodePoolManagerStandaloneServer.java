package br.usp.ime.choreos.nodepoolmanager;

import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;

public class NodePoolManagerStandaloneServer implements Runnable {

    private static boolean running = false;

    public static void start() throws InterruptedException {
        new Thread(new NodePoolManagerStandaloneServer()).start();
        while (!running) {
            Thread.sleep(1);
        }

    }

    public static void stop() {
        running = false;
    }

    public void run() {
        JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
        sf.setResourceClasses(NodeResource.class, NodesResource.class);
        sf.setAddress("http://localhost:8080/");
        sf.create();
        System.out.println("Iniciou...");
        running = true;

        while (running) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Finalizou...");

    }

    public static void main(String[] args) throws InterruptedException {
        NodePoolManagerStandaloneServer.start();
    }
}
