/* Generated by AN DISI Unibo */ 
package it.unibo.qareceiver;
import it.unibo.qactors.QActorContext;
import it.unibo.is.interfaces.IOutputEnvView;
import it.unibo.qactors.akka.QActorMsgQueue;

public class MsgHandle_Qareceiver extends QActorMsgQueue{
	public MsgHandle_Qareceiver(String actorId, QActorContext myCtx, IOutputEnvView outEnvView )  {
		super(actorId, myCtx, outEnvView);
	}
}
