/* Generated by AN DISI Unibo */ 
package it.unibo.robotpfrs;
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
public abstract class AbstractRobotpfrs extends QActor { 
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
		public AbstractRobotpfrs(String actorId, QActorContext myCtx, IOutputEnvView outEnvView )  throws Exception{
			super(actorId, myCtx,  
			"./srcMore/it/unibo/robotpfrs/WorldTheory.pl",
			setTheEnv( outEnvView )  , "init");
			this.planFilePath = "./srcMore/it/unibo/robotpfrs/plans.txt";
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
	    	stateTab.put("applicationLogic",applicationLogic);
	    	stateTab.put("goAhead",goAhead);
	    	stateTab.put("obstacle",obstacle);
	    }
	    StateFun handleToutBuiltIn = () -> {	
	    	try{	
	    		PlanRepeat pr = PlanRepeat.setUp("handleTout",-1);
	    		String myselfName = "handleToutBuiltIn";  
	    		println( "robotpfrs tout : stops");  
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
	    	//delay  ( no more reactive within a plan)
	    	aar = delayReactive(3000,"" , "");
	    	if( aar.getInterrupted() ) curPlanInExec   = "init";
	    	if( ! aar.getGoon() ) return ;
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"usercmd(CMD)","usercmd(robotgui(a(low)))", guardVars ).toString();
	    	sendMsg("moveRobot","player", QActorContext.dispatch, temporaryStr ); 
	    	//delay  ( no more reactive within a plan)
	    	aar = delayReactive(1000,"" , "");
	    	if( aar.getInterrupted() ) curPlanInExec   = "init";
	    	if( ! aar.getGoon() ) return ;
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"usercmd(CMD)","usercmd(robotgui(h(low)))", guardVars ).toString();
	    	sendMsg("moveRobot","player", QActorContext.dispatch, temporaryStr ); 
	    	temporaryStr = "\"player starts\"";
	    	println( temporaryStr );  
	     connectToMqttServer("ws://localhost:1884");
	    	//bbb
	     msgTransition( pr,myselfName,"robotpfrs_"+myselfName,false,
	          new StateFun[]{stateTab.get("applicationLogic") }, 
	          new String[]{"true","M","startAppl" },
	          600000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_init){  
	    	 println( getName() + " plan=init WARNING:" + e_init.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//init
	    
	    StateFun applicationLogic = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("applicationLogic",-1);
	    	String myselfName = "applicationLogic";  
	    	temporaryStr = "\"player starts the applicationLogic\"";
	    	println( temporaryStr );  
	    	//switchTo goAhead
	        switchToPlanAsNextState(pr, myselfName, "robotpfrs_"+myselfName, 
	              "goAhead",false, false, null); 
	    }catch(Exception e_applicationLogic){  
	    	 println( getName() + " plan=applicationLogic WARNING:" + e_applicationLogic.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//applicationLogic
	    
	    StateFun goAhead = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("goAhead",-1);
	    	String myselfName = "goAhead";  
	    	it.unibo.utils.clientTcp.sendMsg( myself ,"{ 'type': 'moveForward', 'arg': -1 }"  );
	    	//bbb
	     msgTransition( pr,myselfName,"robotpfrs_"+myselfName,false,
	          new StateFun[]{() -> {	//AD HOC state to execute an action and resumeLastPlan
	          try{
	            PlanRepeat pr1 = PlanRepeat.setUp("adhocstate",-1);
	            //ActionSwitch for a message or event
	             if( currentMessage.msgContent().startsWith("usercmd") ){
	            	String parg="robotpfrs ENDS";
	            	/* EndPlan */
	            	parg =  updateVars( Term.createTerm("usercmd(CMD)"),  
	            	                    Term.createTerm("usercmd(robotgui(h(X)))"), 
	            		    		  	Term.createTerm(currentMessage.msgContent()), parg);
	            	if( parg != null ){  
	            		println( parg);
	            		return ; //JULY2017
	            		//break
	            	}					
	             }
	            repeatPlanNoTransition(pr1,"adhocstate","adhocstate",false,true);
	          }catch(Exception e ){  
	             println( getName() + " plan=goAhead WARNING:" + e.getMessage() );
	             //QActorContext.terminateQActorSystem(this); 
	          }
	          },
	           stateTab.get("obstacle") }, 
	          new String[]{"true","M","moveRobot", "true","E","sonarDetect" },
	          600000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_goAhead){  
	    	 println( getName() + " plan=goAhead WARNING:" + e_goAhead.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//goAhead
	    
	    StateFun obstacle = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("obstacle",-1);
	    	String myselfName = "obstacle";  
	    	printCurrentEvent(false);
	    	//onEvent 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("sonarDetect(wallUp)");
	    	if( currentEvent != null && currentEvent.getEventId().equals("sonarDetect") && 
	    		pengine.unify(curT, Term.createTerm("sonarDetect(X)")) && 
	    		pengine.unify(curT, Term.createTerm( currentEvent.getMsg() ) )){ 
	    			//println("WARNING: variable substitution not yet fully implemented " ); 
	    			{//actionseq
	    			it.unibo.utils.clientTcp.sendMsg( myself ,"{ 'type': 'turnRight', 'arg': 800 }"  );
	    			it.unibo.utils.clientTcp.sendMsg( myself ,"{ 'type': 'moveForward', 'arg': 200 }"  );
	    			it.unibo.utils.clientTcp.sendMsg( myself ,"{ 'type': 'turnRight', 'arg': 800 }"  );
	    			};//actionseq
	    	}
	    	//onEvent 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("sonarDetect(wallLeft)");
	    	if( currentEvent != null && currentEvent.getEventId().equals("sonarDetect") && 
	    		pengine.unify(curT, Term.createTerm("sonarDetect(X)")) && 
	    		pengine.unify(curT, Term.createTerm( currentEvent.getMsg() ) )){ 
	    			//println("WARNING: variable substitution not yet fully implemented " ); 
	    			{//actionseq
	    			it.unibo.utils.clientTcp.sendMsg( myself ,"{ 'type': 'turnRight', 'arg': 800 }"  );
	    			};//actionseq
	    	}
	    	//onEvent 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("sonarDetect(wallDown)");
	    	if( currentEvent != null && currentEvent.getEventId().equals("sonarDetect") && 
	    		pengine.unify(curT, Term.createTerm("sonarDetect(X)")) && 
	    		pengine.unify(curT, Term.createTerm( currentEvent.getMsg() ) )){ 
	    			//println("WARNING: variable substitution not yet fully implemented " ); 
	    			{//actionseq
	    			it.unibo.utils.clientTcp.sendMsg( myself ,"{ 'type': 'turnLeft', 'arg': 800 }"  );
	    			it.unibo.utils.clientTcp.sendMsg( myself ,"{ 'type': 'moveForward', 'arg': 200 }"  );
	    			it.unibo.utils.clientTcp.sendMsg( myself ,"{ 'type': 'turnLeft', 'arg': 800 }"  );
	    			};//actionseq
	    	}
	    	//switchTo goAhead
	        switchToPlanAsNextState(pr, myselfName, "robotpfrs_"+myselfName, 
	              "goAhead",false, false, null); 
	    }catch(Exception e_obstacle){  
	    	 println( getName() + " plan=obstacle WARNING:" + e_obstacle.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//obstacle
	    
	    protected void initSensorSystem(){
	    	//doing nothing in a QActor
	    }
	
	}
