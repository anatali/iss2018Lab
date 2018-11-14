/* Generated by AN DISI Unibo */ 
/*
This code is generated only ONCE
*/
package it.unibo.qahttpclient;
import it.unibo.is.interfaces.IOutputEnvView;
import it.unibo.qactors.QActorContext;
import it.unibo.rest.clienthttp.RestClientHttp;

public class Qahttpclient extends AbstractQahttpclient { 
	public Qahttpclient(String actorId, QActorContext myCtx, IOutputEnvView outEnvView )  throws Exception{
		super(actorId, myCtx, outEnvView);
	}
/*
 * ADDED BY THE APPLICATION DESIGNER	
 */
	public void sendPut(String msg, int port) {
		println("sendPut:" + msg);
		RestClientHttp.setCtx( this.getQActorContext() );
 		RestClientHttp.sendPut(msg, "http://localhost:"+port);
		RestClientHttp.setCtx( null );
		
	}
	public void sendGet( int port) {
		println("sendGet:" );
		RestClientHttp.setCtx( this.getQActorContext() );
 		RestClientHttp.sendGet( port );
		RestClientHttp.setCtx( null );		
	}
}
 