package org.ow2.choreos.enact;

import java.util.ArrayList;
import java.util.List;

import org.ow2.choreos.npm.NodePoolManager;
import org.ow2.choreos.npm.client.NPMClient;
import org.ow2.choreos.npm.datamodel.Node;

public class Bootstrapper {

	private static final String NPM_HOST = "http://localhost:9100/nodepoolmanager";

	private int vmsQuantity; // how many VMs we will use
	
	public Bootstrapper(int vmsQuantity) {
		this.vmsQuantity = vmsQuantity;
	}
	
	private List<Node> vms;

	/**
	 * Creates VMs,
	 */
	public void boot() {
		
		System.out.println(Utils.getTimeStamp() + "Creating VMs...");
		long t0 = System.currentTimeMillis();
		List<Node> vms = createVMs();
		System.out.println(Utils.getTimeStamp() + "Created machines: ");
		for (Node vm: vms) {
			String timeStamp = Utils.getTimeStamp();
			if (timeStamp != null & vm != null) {
				System.out.println(timeStamp + vm.getIp() + "  ");
			}
		}
		long tf = System.currentTimeMillis();
		long duration = tf - t0;
		System.out.println(Utils.getTimeStamp() + "### Bootstrap completed in " + duration + " milliseconds ###");
	}

	private List<Node> createVMs() {
	
		final List<Node> vms = new ArrayList<Node>();
		Thread[] trds = new Thread[vmsQuantity];
		
		for (int i=0; i<vmsQuantity; i++) {
			final int idx = i;
			trds[i] = new Thread(new Runnable() {
				@Override public void run() {

					long t0 = System.currentTimeMillis();
					NodePoolManager npm = new NPMClient(NPM_HOST);
					Node req = new Node();
					Node vm = npm.createNode(req);
					long tf = System.currentTimeMillis();
					long duration = tf - t0;
					vms.add(vm);
					System.out.println(Utils.getTimeStamp() + "VM #" + idx
							+ " created in " + duration + " milliseconds");
				}
			});
			trds[i].start();
		}
		
		waitThreads(trds);
		
		return vms;
	}

	private void waitThreads(Thread[] trds) {
		for (Thread t: trds) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public List<Node> getNodes() {
		
		return vms;
	}
}