
/**
* NodePOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from node.idl
* Monday, December 11, 2017 9:32:58 AM BRST
*/

public abstract class NodePOA extends org.omg.PortableServer.Servant
 implements NodeOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("ask", new java.lang.Integer (0));
    _methods.put ("release", new java.lang.Integer (1));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {
       case 0:  // Node/ask
       {
         int asker = in.read_long ();
         int timestamp = in.read_long ();
         this.ask (asker, timestamp);
         out = $rh.createReply();
         break;
       }

       case 1:  // Node/release
       {
         int sender = in.read_long ();
         int timestamp = in.read_long ();
         this.release (sender, timestamp);
         out = $rh.createReply();
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:Node:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public Node _this() 
  {
    return NodeHelper.narrow(
    super._this_object());
  }

  public Node _this(org.omg.CORBA.ORB orb) 
  {
    return NodeHelper.narrow(
    super._this_object(orb));
  }


} // class NodePOA