/* Generated by AN DISI Unibo */ 
package it.unibo.ctxApplR1;
import alice.tuprolog.Term;
import it.unibo.contactEvent.interfaces.IEventItem;
import it.unibo.qactors.platform.EventHandlerComponent;
import it.unibo.is.interfaces.IOutputEnvView;
import it.unibo.qactors.QActorContext;
import it.unibo.qactors.QActorUtils;

public abstract class AbstractEvhtask extends EventHandlerComponent { 
protected IEventItem event;
	public AbstractEvhtask(String name, QActorContext myCtx, IOutputEnvView outEnvView, String[] eventIds ) throws Exception {
		super(name, myCtx, eventIds, outEnvView);
  	}
	@Override
	public void doJob() throws Exception {	}
	
	public void handleCurrentEvent() throws Exception {
		event = this.currentEvent; //AKKA getEventItem();
		if( event == null ) return;
//showMsg( "---------------------------------------------------------------------" );	
showMsg( event.getPrologRep()  );				 
//showMsg( "---------------------------------------------------------------------" );	
		{
		Term msgt       = Term.createTerm(event.getMsg());
		Term msgPattern = Term.createTerm("tasktodo(TASK,ARGS)");
				boolean b = this.pengine.unify(msgt, msgPattern);
				if( b ) {
			  		sendMsg("taskmsg","applr1agent", QActorContext.dispatch, msgt.toString() ); 
				}else{
					println("non unifiable");
				}
		}
	}//handleCurrentEvent
	
	@Override
	protected void handleQActorEvent(IEventItem ev) {
		super.handleQActorEvent(ev);
 		try {
			handleCurrentEvent();
		} catch (Exception e) {
 			e.printStackTrace();
		}
	}//handleQActorEvent
	
}
