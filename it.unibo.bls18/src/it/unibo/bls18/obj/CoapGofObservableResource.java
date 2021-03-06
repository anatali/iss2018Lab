package it.unibo.bls18.obj;

import org.eclipse.californium.core.CoapResource;
import it.unibo.bls18.interfaces.IResourceIot;
import it.unibo.bls18.interfaces.IResourceLocalObserver;

public abstract class CoapGofObservableResource extends CoapResource implements IResourceIot{
protected IResourceLocalObserver observer  ;
	
	public CoapGofObservableResource(String name) {
		super(name);
 	}
	
	public void setGofObserver( IResourceLocalObserver obs ) {
		observer = obs;
	}
	/*
	 * To be defined by the application designer.
	 * It can be used by physical devices to change the model
	 */
	public abstract void setValue(String v);
	
	protected void update(String v) {
		System.out.println("	CoapGofObservableResource update " + v );
		if( observer != null ) observer.update(v);
	}

}
