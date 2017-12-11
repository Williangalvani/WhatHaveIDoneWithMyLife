import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;
public class ShowBindings{ 
	public static void main(String[] args){ 
		try{ 
			ORB orb = ORB.init(args,null);
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			NamingContext namingContext = NamingContextHelper.narrow(objRef);
			BindingListHolder bl = new BindingListHolder();
			BindingIteratorHolder bi = new BindingIteratorHolder();
			namingContext.list(6,  bl,  bi);
			Binding[] bindings = bl.value;
			for (int i = 0; i < bindings.length; i++)
			System.out.println(bindings[i].binding_name[0].id + " Kind:" + bindings[i].binding_name[0].kind);
			for (int i = 0; i < bindings.length; i++)
			System.out.println("IOR de " + bindings[i].binding_name[0].id + ": " + 
				orb.object_to_string(namingContext.resolve(bindings[i].binding_name)));
		}
		catch(Exception e){
			System.out.println("Error " + e);
		}
	}
}	