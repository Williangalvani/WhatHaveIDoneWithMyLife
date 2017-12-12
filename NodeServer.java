import org.omg.CosNaming.*;

import java.util.Properties;

//import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
//import org.omg.PortableServer.POA;
//import java.util.Properties;

public class NodeServer implements Runnable{ 
	private String[] args;
	private Integer processId=2;
	private String host;
	private ORB orb;

	public NodeServer(String[] args, int nodeId, String host){ 
		this.args = args;
		this.processId = nodeId;
		this.host = host;
	}

	public void batata()
	{
		System.out.println("batata");
	}
	
	@Override
	public void run() {
		try{ 
			System.out.println("Server: iniciando ORB no servidor");
			// create and initialize the ORB

			Properties props = new Properties();
			props.put("org.omg.CORBA.OrbinitialHost",host);
			
			orb = ORB.init(args	, props);
			

			// get reference to rootpoa & activate the POAManager
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
      		rootpoa.the_POAManager().activate();	     

			// create servants 
      		
      	    System.out.println("Server: iniciando Objetos de aplicacao");	
			NodeServant impl1 =new NodeServant(this, processId);
			// and register it with the ORB and get object reference to servant
			org.omg.CORBA.Object ref1 = rootpoa.servant_to_reference(impl1);			
			
			
			// get the root naming context
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			NamingContext namingContext = NamingContextHelper.narrow(objRef);
			
			// bind the Object References in Naming
			NameComponent[] path = {new NameComponent("node"+processId, "Object")};  //Objeto 1
			System.out.println("Server: ligando o node"+processId);
			namingContext.rebind(path, ref1);
		
			
			System.out.println("Server: esperando chamadas");
			
			// wait for invocations from clients
      			orb.run();

			
		}
		catch(Exception e) { 
			System.err.println("Error: " + e);
			e.printStackTrace(System.out);
		}
		
	}
}
