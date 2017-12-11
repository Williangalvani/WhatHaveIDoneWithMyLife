//import org.omg.CosNaming.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;

import idl.NodePOA;
//import org.omg.PortableServer.POA;
class NodeServant extends NodePOA{
	public NodeServant(NodeServer server, Integer processNumber){
		this.processNumber = processNumber;
		this.server = server;
		System.out.println("Servant: comecando server do processo " + this.processNumber);
	}
	
	private NodeServer server;
	private Integer processNumber;

	@Override
	public void ask(int asker, int timestamp) {
		Node.onRequest(asker, timestamp);
		
	}

	@Override
	public void alive() {
	System.out.println("Servant: am alive");
	
}

	@Override
	public void release(int sender, int timestamp) {
		Node.onRelease(sender, timestamp);
		
	}

	@Override
	public void reply(int sender, int timestamp) {
		Node.onReply(sender, timestamp);
		
	}
}