import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.InvalidName;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import idl.Node;
import idl.NodeHelper;

import java.util.Properties;

import org.omg.CORBA.*;
public class NodeClient implements Runnable{ 

	private String[] args;
	ORB orb;
	org.omg.CORBA.Object objRef;
	NamingContext namingContext;
	Node[] node = {null, null, null, null};
	private int nodeId;
	public Boolean ready = false;
	String host = "";
	
	
	public NodeClient(String[] args, int nodeId, String host){
		this.args = args;
		this.nodeId = nodeId;
		this.host = host;
	}
	
	private Boolean checkIfRemotesExist()
	{
		System.out.println("client: trying to access remotes");
		Node node;
		int i=1;
		try {
			for(i=1; i<4; i++)
			{
				NameComponent[] path= {new NameComponent("node"+i, "Object")};
				node = NodeHelper.narrow(namingContext.resolve(path));
				node.alive();
				System.out.println("client: node"+i + ": success");
			}
			
			return true;
			
		} catch (NotFound | org.omg.CORBA.COMM_FAILURE e) {
			System.out.println("client: accessing node"+i+", assuming the node is not online.");
			return false;
		} catch (CannotProceed e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidName e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void run() {
		try {
			System.out.println(args);
			Properties props = new Properties();
			props.put("org.omg.CORBA.OrbinitialHost",host);
			
			orb = ORB.init(args	, props);
			
			objRef = orb.resolve_initial_references("NameService");
			namingContext = NamingContextHelper.narrow(objRef);
			
			while( !checkIfRemotesExist() )
			{
				System.out.println("Looks like some nodes are still offline");
				System.out.println("retrying in 1s");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			
			
			for(int i=1; i<4; i++)
			{
				NameComponent[] path= {new NameComponent("node"+i, "Object")};
				node[i] = NodeHelper.narrow(namingContext.resolve(path));
				System.out.println("client: node"+i + ": success");
				System.out.println(node[i]);
				}
			
			Thread.sleep(1000);
			ready = true;
		
		} catch (org.omg.CORBA.ORBPackage.InvalidName | NotFound | CannotProceed | InvalidName | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void request(int voter, int timestamp) {
		System.out.println(voter);
		node[voter].ask(nodeId, timestamp);	
		
	}

	public void release(int sender, int receiver, int timestamp) {
		node[receiver].release(sender, timestamp);
		
	}
	
	public void reply(int sender, int receiver, int timestamp) {
		node[receiver].reply(sender, timestamp);
	}

}