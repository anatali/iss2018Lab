/* Generated by AN DISI Unibo */ 
package it.unibo.rover;
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
public abstract class AbstractRover extends QActor { 
	protected AsynchActionResult aar = null;
	protected boolean actionResult = true;
	protected alice.tuprolog.SolveInfo sol;
	protected String planFilePath    = null;
	protected String terminationEvId = "default";
	protected String parg="";
	protected boolean bres=false;
	protected IActorAction action;
	//protected String mqttServer = "tcp://localhost:1883";
	
		protected static IOutputEnvView setTheEnv(IOutputEnvView outEnvView ){
			return outEnvView;
		}
		public AbstractRover(String actorId, QActorContext myCtx, IOutputEnvView outEnvView )  throws Exception{
			super(actorId, myCtx,  
			"./srcMore/it/unibo/rover/WorldTheory.pl",
			setTheEnv( outEnvView )  , "init");
			this.planFilePath = "./srcMore/it/unibo/rover/plans.txt";
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
	    	stateTab.put("waitForCmd",waitForCmd);
	    	stateTab.put("execMove",execMove);
	    }
	    StateFun handleToutBuiltIn = () -> {	
	    	try{	
	    		PlanRepeat pr = PlanRepeat.setUp("handleTout",-1);
	    		String myselfName = "handleToutBuiltIn";  
	    		println( "rover tout : stops");  
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
	    	temporaryStr = "\"rover START\"";
	    	println( temporaryStr );  
	     connectToMqttServer("tcp://localhost:1883");
	    	//switchTo waitForCmd
	        switchToPlanAsNextState(pr, myselfName, "rover_"+myselfName, 
	              "waitForCmd",false, false, null); 
	    }catch(Exception e_init){  
	    	 println( getName() + " plan=init WARNING:" + e_init.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//init
	    
	    StateFun waitForCmd = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp(getName()+"_waitForCmd",0);
	     pr.incNumIter(); 	
	    	String myselfName = "waitForCmd";  
	    	//bbb
	     msgTransition( pr,myselfName,"rover_"+myselfName,false,
	          new StateFun[]{() -> {	//AD HOC state to execute an action and resumeLastPlan
	          try{
	            PlanRepeat pr1 = PlanRepeat.setUp("adhocstate",-1);
	            //ActionSwitch for a message or event
	             if( currentEvent.getMsg().startsWith("alarm") ){
	            	//println("WARNING: variable substitution not yet fully implemented " ); 
	            	{//actionseq
	            	temporaryStr = QActorUtils.unifyMsgContent(pengine, "alarm(X)","alarm(X)", guardVars ).toString();
	            	emit( "alarmev", temporaryStr );
	            	};//actionseq
	             }
	            repeatPlanNoTransition(pr1,"adhocstate","adhocstate",false,true);
	          }catch(Exception e ){  
	             println( getName() + " plan=waitForCmd WARNING:" + e.getMessage() );
	             //QActorContext.terminateQActorSystem(this); 
	          }
	          },
	           stateTab.get("execMove") }, 
	          new String[]{"true","E","alarm", "true","M","moveRover" },
	          3600000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_waitForCmd){  
	    	 println( getName() + " plan=waitForCmd WARNING:" + e_waitForCmd.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//waitForCmd
	    
	    StateFun execMove = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("execMove",-1);
	    	String myselfName = "execMove";  
	    	printCurrentMessage(false);
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("usercmd(robotgui(h(X)))");
	    	if( currentMessage != null && currentMessage.msgId().equals("moveRover") && 
	    		pengine.unify(curT, Term.createTerm("usercmd(CMD)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		//println("WARNING: variable substitution not yet fully implemented " ); 
	    		execUnity("rover","stop",0, 40,0); //rover: default namefor virtual robot		
	    	}
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("usercmd(robotgui(w(X)))");
	    	if( currentMessage != null && currentMessage.msgId().equals("moveRover") && 
	    		pengine.unify(curT, Term.createTerm("usercmd(CMD)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		//println("WARNING: variable substitution not yet fully implemented " ); 
	    		execUnity("rover","forward",0, 40,0); //rover: default namefor virtual robot		
	    	}
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("usercmd(robotgui(s(X)))");
	    	if( currentMessage != null && currentMessage.msgId().equals("moveRover") && 
	    		pengine.unify(curT, Term.createTerm("usercmd(CMD)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		//println("WARNING: variable substitution not yet fully implemented " ); 
	    		execUnity("rover","backward",0, 40,0); //rover: default namefor virtual robot		
	    	}
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("usercmd(robotgui(a(X)))");
	    	if( currentMessage != null && currentMessage.msgId().equals("moveRover") && 
	    		pengine.unify(curT, Term.createTerm("usercmd(CMD)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		//println("WARNING: variable substitution not yet fully implemented " ); 
	    		execUnity("rover","left",750, 40,0); //rover: default namefor virtual robot		
	    	}
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("usercmd(robotgui(d(X)))");
	    	if( currentMessage != null && currentMessage.msgId().equals("moveRover") && 
	    		pengine.unify(curT, Term.createTerm("usercmd(CMD)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		//println("WARNING: variable substitution not yet fully implemented " ); 
	    		execUnity("rover","right",750, 40,0); //rover: default namefor virtual robot		
	    	}
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("usercmd(robotgui(x(X)))");
	    	if( currentMessage != null && currentMessage.msgId().equals("moveRover") && 
	    		pengine.unify(curT, Term.createTerm("usercmd(CMD)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		//println("WARNING: variable substitution not yet fully implemented " ); 
	    		{//actionseq
	    		parg = "terminateSystem()"; 
	    		actorOpExecute(parg, false);	//OCT17		 
	    		};//actionseq
	    	}
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("usercmd(robotgui(unityAddr(X)))");
	    	if( currentMessage != null && currentMessage.msgId().equals("moveRover") && 
	    		pengine.unify(curT, Term.createTerm("usercmd(CMD)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		//println("WARNING: variable substitution not yet fully implemented " ); 
	    		{//actionseq
	    		if( (guardVars = QActorUtils.evalTheGuard(this, " not !?unityOn" )) != null )
	    		{
	    		{//actionseq
	    		if( (guardVars = QActorUtils.evalTheGuard(this, " !?unityConfig(BATCH)" )) != null ){
	    		it.unibo.utils.external.connectRoverToUnity( myself ,guardVars.get("BATCH")  );
	    		}
	    		temporaryStr = "unityOn";
	    		addRule( temporaryStr );  
	    		execUnity("rover","backward",1000, 70,0); //rover: default namefor virtual robot		
	    		execUnity("rover","right",1000, 70,0); //rover: default namefor virtual robot		
	    		};//actionseq
	    		}
	    		else{ temporaryStr = "\"UNITY ALREADY ACTIVATED\"";
	    		println( temporaryStr );  
	    		}};//actionseq
	    	}
	    	repeatPlanNoTransition(pr,myselfName,"rover_"+myselfName,false,true);
	    }catch(Exception e_execMove){  
	    	 println( getName() + " plan=execMove WARNING:" + e_execMove.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//execMove
	    
	    protected void initSensorSystem(){
	    	//doing nothing in a QActor
	    }
	
	}
