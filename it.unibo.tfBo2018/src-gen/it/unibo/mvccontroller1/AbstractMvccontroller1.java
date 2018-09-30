/* Generated by AN DISI Unibo */ 
package it.unibo.mvccontroller1;
import it.unibo.qactors.PlanRepeat;
import it.unibo.qactors.QActorContext;
import it.unibo.qactors.StateExecMessage;
import it.unibo.qactors.QActorUtils;
import it.unibo.is.interfaces.IOutputEnvView;
import it.unibo.qactors.action.AsynchActionResult;
import it.unibo.qactors.action.IActorAction;
import it.unibo.qactors.action.IActorAction.ActionExecMode;
import it.unibo.qactors.action.IMsgQueue;
import it.unibo.qactors.akka.QActor;
import it.unibo.qactors.StateFun;
import java.util.Stack;
import java.util.Hashtable;
import java.util.concurrent.Callable;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import it.unibo.qactors.action.ActorTimedAction;
public abstract class AbstractMvccontroller1 extends QActor { 
	protected AsynchActionResult aar = null;
	protected boolean actionResult = true;
	protected alice.tuprolog.SolveInfo sol;
	protected String planFilePath    = null;
	protected String terminationEvId = "default";
	protected String parg="";
	protected boolean bres=false;
	protected IActorAction action;
	 
	
		protected static IOutputEnvView setTheEnv(IOutputEnvView outEnvView ){
			return outEnvView;
		}
		public AbstractMvccontroller1(String actorId, QActorContext myCtx, IOutputEnvView outEnvView )  throws Exception{
			super(actorId, myCtx,  
			"./srcMore/it/unibo/mvccontroller1/WorldTheory.pl",
			setTheEnv( outEnvView )  , "init");
			this.planFilePath = "./srcMore/it/unibo/mvccontroller1/plans.txt";
	  	}
		@Override
		protected void doJob() throws Exception {
			String name  = getName().replace("_ctrl", "");
			mysupport = (IMsgQueue) QActorUtils.getQActor( name ); 
			initStateTable(); 
	 		initSensorSystem();
	 		history.push(stateTab.get( "init" ));
	  	 	autoSendStateExecMsg();
	  		//QActorContext.terminateQActorSystem(this);//todo
		} 	
		/* 
		* ------------------------------------------------------------
		* PLANS
		* ------------------------------------------------------------
		*/    
	    //genAkkaMshHandleStructure
	    protected void initStateTable(){  	
	    	stateTab.put("handleToutBuiltIn",handleToutBuiltIn);
	    	stateTab.put("init",init);
	    	stateTab.put("waitCommand",waitCommand);
	    	stateTab.put("checkConditions",checkConditions);
	    	stateTab.put("sendStart",sendStart);
	    	stateTab.put("sendStop",sendStop);
	    	stateTab.put("end",end);
	    }
	    StateFun handleToutBuiltIn = () -> {	
	    	try{	
	    		PlanRepeat pr = PlanRepeat.setUp("handleTout",-1);
	    		String myselfName = "handleToutBuiltIn";  
	    		println( "mvccontroller1 tout : stops");  
	    		repeatPlanNoTransition(pr,myselfName,"application_"+myselfName,false,false);
	    	}catch(Exception e_handleToutBuiltIn){  
	    		println( getName() + " plan=handleToutBuiltIn WARNING:" + e_handleToutBuiltIn.getMessage() );
	    		QActorContext.terminateQActorSystem(this); 
	    	}
	    };//handleToutBuiltIn
	    
	    StateFun init = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("init",-1);
	    	String myselfName = "init";  
	    	temporaryStr = "\"MVCCONTROLLER starts...\"";
	    	println( temporaryStr );  
	    	//switchTo waitCommand
	        switchToPlanAsNextState(pr, myselfName, "mvccontroller1_"+myselfName, 
	              "waitCommand",false, false, null); 
	    }catch(Exception e_init){  
	    	 println( getName() + " plan=init WARNING:" + e_init.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//init
	    
	    StateFun waitCommand = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("waitCommand",-1);
	    	String myselfName = "waitCommand";  
	    	//bbb
	     msgTransition( pr,myselfName,"mvccontroller1_"+myselfName,false,
	          new StateFun[]{stateTab.get("checkConditions"), stateTab.get("sendStop") }, 
	          new String[]{"true","M","frontendCmdMsg", "true","M","tempStateMsg" },
	          600000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_waitCommand){  
	    	 println( getName() + " plan=waitCommand WARNING:" + e_waitCommand.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//waitCommand
	    
	    StateFun checkConditions = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("checkConditions",-1);
	    	String myselfName = "checkConditions";  
	    	temporaryStr = "\"MVCCONTROLLER conditions valid and start from FRONTEND\"";
	    	println( temporaryStr );  
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"startTemp(X)","startTemp(x)", guardVars ).toString();
	    	sendMsg("startTempMsg","tempdetector1", QActorContext.dispatch, temporaryStr ); 
	    	//switchTo sendStart
	        switchToPlanAsNextState(pr, myselfName, "mvccontroller1_"+myselfName, 
	              "sendStart",false, false, null); 
	    }catch(Exception e_checkConditions){  
	    	 println( getName() + " plan=checkConditions WARNING:" + e_checkConditions.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//checkConditions
	    
	    StateFun sendStart = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("sendStart",-1);
	    	String myselfName = "sendStart";  
	    	temporaryStr = "\"MVCCONTROLLER send start to robot...\"";
	    	println( temporaryStr );  
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"consoleCmd(X)","consoleCmd(start)", guardVars ).toString();
	    	sendExtMsg("consoleCmdMsg","player1", "ctxRobot", QActorContext.dispatch, temporaryStr ); 
	    	//delay  ( no more reactive within a plan)
	    	aar = delayReactive(1000,"" , "");
	    	if( aar.getInterrupted() ) curPlanInExec   = "sendStart";
	    	if( ! aar.getGoon() ) return ;
	    	//switchTo waitCommand
	        switchToPlanAsNextState(pr, myselfName, "mvccontroller1_"+myselfName, 
	              "waitCommand",false, false, null); 
	    }catch(Exception e_sendStart){  
	    	 println( getName() + " plan=sendStart WARNING:" + e_sendStart.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//sendStart
	    
	    StateFun sendStop = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("sendStop",-1);
	    	String myselfName = "sendStop";  
	    	temporaryStr = "\"MVCCONTROLLER send stop to robot...\"";
	    	println( temporaryStr );  
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"consoleCmd(X)","consoleCmd(halt)", guardVars ).toString();
	    	sendExtMsg("consoleCmdMsg","player1", "ctxRobot", QActorContext.dispatch, temporaryStr ); 
	    	//switchTo end
	        switchToPlanAsNextState(pr, myselfName, "mvccontroller1_"+myselfName, 
	              "end",false, false, null); 
	    }catch(Exception e_sendStop){  
	    	 println( getName() + " plan=sendStop WARNING:" + e_sendStop.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//sendStop
	    
	    StateFun end = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("end",-1);
	    	String myselfName = "end";  
	    	temporaryStr = "\"MVCCONTROLLER ends...\"";
	    	println( temporaryStr );  
	    	repeatPlanNoTransition(pr,myselfName,"mvccontroller1_"+myselfName,false,false);
	    }catch(Exception e_end){  
	    	 println( getName() + " plan=end WARNING:" + e_end.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//end
	    
	    protected void initSensorSystem(){
	    	//doing nothing in a QActor
	    }
	
	}
