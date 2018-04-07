/* Generated by AN DISI Unibo */ 
package it.unibo.applr0agent;
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
public abstract class AbstractApplr0agent extends QActor { 
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
		public AbstractApplr0agent(String actorId, QActorContext myCtx, IOutputEnvView outEnvView )  throws Exception{
			super(actorId, myCtx,  
			"./srcMore/it/unibo/applr0agent/WorldTheory.pl",
			setTheEnv( outEnvView )  , "init");
			this.planFilePath = "./srcMore/it/unibo/applr0agent/plans.txt";
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
	    	stateTab.put("doWork",doWork);
	    	stateTab.put("handleSonarEvents",handleSonarEvents);
	    	stateTab.put("avoidMobileObstacle",avoidMobileObstacle);
	    	stateTab.put("robotAtSonar1",robotAtSonar1);
	    	stateTab.put("robotAtSonar2",robotAtSonar2);
	    	stateTab.put("adjustRobotPosition",adjustRobotPosition);
	    	stateTab.put("doWaitAnswer",doWaitAnswer);
	    	stateTab.put("checkTheAnswer",checkTheAnswer);
	    	stateTab.put("alarmHandlePolicy",alarmHandlePolicy);
	    }
	    StateFun handleToutBuiltIn = () -> {	
	    	try{	
	    		PlanRepeat pr = PlanRepeat.setUp("handleTout",-1);
	    		String myselfName = "handleToutBuiltIn";  
	    		println( "applr0agent tout : stops");  
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
	     connectToMqttServer("tcp://localhost:1883");
	    	//switchTo doWork
	        switchToPlanAsNextState(pr, myselfName, "applr0agent_"+myselfName, 
	              "doWork",false, false, null); 
	    }catch(Exception e_init){  
	    	 println( getName() + " plan=init WARNING:" + e_init.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//init
	    
	    StateFun doWork = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp(getName()+"_doWork",0);
	     pr.incNumIter(); 	
	    	String myselfName = "doWork";  
	    	temporaryStr = "\"applr0agent doWork\"";
	    	println( temporaryStr );  
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine, "usercmd(CMD)","usercmd(robotgui(w(low)))", guardVars ).toString();
	    	emit( "usercmd", temporaryStr );
	    	//bbb
	     msgTransition( pr,myselfName,"applr0agent_"+myselfName,false,
	          new StateFun[]{stateTab.get("alarmHandlePolicy"), stateTab.get("handleSonarEvents") }, 
	          new String[]{"true","M","alarmmsg", "true","E","sonarSensor" },
	          6000000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_doWork){  
	    	 println( getName() + " plan=doWork WARNING:" + e_doWork.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//doWork
	    
	    StateFun handleSonarEvents = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("handleSonarEvents",-1);
	    	String myselfName = "handleSonarEvents";  
	    	printCurrentEvent(false);
	    	//onEvent 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("sonar(sonar1,DISTANCE)");
	    	if( currentEvent != null && currentEvent.getEventId().equals("sonarSensor") && 
	    		pengine.unify(curT, Term.createTerm("sonar(NAME,DISTANCE)")) && 
	    		pengine.unify(curT, Term.createTerm( currentEvent.getMsg() ) )){ 
	    			String parg="atsonar1(DISTANCE)";
	    			/* SendDispatch */
	    			parg = updateVars(Term.createTerm("sonar(NAME,DISTANCE)"),  Term.createTerm("sonar(sonar1,DISTANCE)"), 
	    				    		  					Term.createTerm(currentEvent.getMsg()), parg);
	    			if( parg != null ) sendMsg("atsonar1","applr0agent", QActorContext.dispatch, parg ); 
	    	}
	    	//onEvent 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("sonar(sonar2,DISTANCE)");
	    	if( currentEvent != null && currentEvent.getEventId().equals("sonarSensor") && 
	    		pengine.unify(curT, Term.createTerm("sonar(NAME,DISTANCE)")) && 
	    		pengine.unify(curT, Term.createTerm( currentEvent.getMsg() ) )){ 
	    			String parg="atsonar2(DISTANCE)";
	    			/* SendDispatch */
	    			parg = updateVars(Term.createTerm("sonar(NAME,DISTANCE)"),  Term.createTerm("sonar(sonar2,DISTANCE)"), 
	    				    		  					Term.createTerm(currentEvent.getMsg()), parg);
	    			if( parg != null ) sendMsg("atsonar2","applr0agent", QActorContext.dispatch, parg ); 
	    	}
	    	//onEvent 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("sonar(sonar2,DISTANCE)");
	    	if( currentEvent != null && currentEvent.getEventId().equals("sonarSensor") && 
	    		pengine.unify(curT, Term.createTerm("sonar(NAME,DISTANCE)")) && 
	    		pengine.unify(curT, Term.createTerm( currentEvent.getMsg() ) )){ 
	    			//println("WARNING: variable substitution not yet fully implemented " ); 
	    			{//actionseq
	    			//delay  ( no more reactive within a plan)
	    			aar = delayReactive(200,"" , "");
	    			if( aar.getInterrupted() ) curPlanInExec   = "handleSonarEvents";
	    			if( ! aar.getGoon() ) return ;
	    			temporaryStr = QActorUtils.unifyMsgContent(pengine, "usercmd(CMD)","usercmd(robotgui(h(low)))", guardVars ).toString();
	    			emit( "usercmd", temporaryStr );
	    			};//actionseq
	    	}
	    	//onEvent 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("sonar(roversonar,DISTANCE)");
	    	if( currentEvent != null && currentEvent.getEventId().equals("sonarSensor") && 
	    		pengine.unify(curT, Term.createTerm("sonar(NAME,DISTANCE)")) && 
	    		pengine.unify(curT, Term.createTerm( currentEvent.getMsg() ) )){ 
	    			//println("WARNING: variable substitution not yet fully implemented " ); 
	    			{//actionseq
	    			temporaryStr = QActorUtils.unifyMsgContent(pengine, "usercmd(CMD)","usercmd(robotgui(d(low)))", guardVars ).toString();
	    			emit( "usercmd", temporaryStr );
	    			//delay  ( no more reactive within a plan)
	    			aar = delayReactive(750,"" , "");
	    			if( aar.getInterrupted() ) curPlanInExec   = "handleSonarEvents";
	    			if( ! aar.getGoon() ) return ;
	    			temporaryStr = QActorUtils.unifyMsgContent(pengine, "usercmd(CMD)","usercmd(robotgui(h(low)))", guardVars ).toString();
	    			emit( "usercmd", temporaryStr );
	    			if( (guardVars = QActorUtils.evalTheGuard(this, " !?task(tasktodo(obstacleAvoidance,on))" )) != null ){
	    			{//actionseq
	    			temporaryStr = "\"emitting  tasktodo \"";
	    			println( temporaryStr );  
	    			temporaryStr = QActorUtils.unifyMsgContent(pengine, "tasktodo(TASK,ARGS)","tasktodo(obstacleAvoidance,\"do\")", guardVars ).toString();
	    			emit( "tasktodo", temporaryStr );
	    			temporaryStr = QActorUtils.unifyMsgContent(pengine,"waitAnswer(ARG)","waitAnswer(\"obstacleAvoidance\")", guardVars ).toString();
	    			sendMsg("waitAnswer","applr0agent", QActorContext.dispatch, temporaryStr ); 
	    			};//actionseq
	    			}
	    			else{ {//actionseq
	    			temporaryStr = QActorUtils.unifyMsgContent(pengine,"hopemobile(ARG)","hopemobile(\"\")", guardVars ).toString();
	    			sendMsg("hopemobile","applr0agent", QActorContext.dispatch, temporaryStr ); 
	    			};//actionseq
	    			}};//actionseq
	    	}
	    	//bbb
	     msgTransition( pr,myselfName,"applr0agent_"+myselfName,true,
	          new StateFun[]{stateTab.get("avoidMobileObstacle"), stateTab.get("robotAtSonar1"), stateTab.get("robotAtSonar2"), stateTab.get("doWaitAnswer") }, 
	          new String[]{"true","M","hopemobile", "true","M","atsonar1", "true","M","atsonar2", "true","M","waitAnswer" },
	          600000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_handleSonarEvents){  
	    	 println( getName() + " plan=handleSonarEvents WARNING:" + e_handleSonarEvents.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//handleSonarEvents
	    
	    StateFun avoidMobileObstacle = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("avoidMobileObstacle",-1);
	    	String myselfName = "avoidMobileObstacle";  
	    	temporaryStr = "\"hopemobileobstacleeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee\"";
	    	println( temporaryStr );  
	    	//delay  ( no more reactive within a plan)
	    	aar = delayReactive(1000,"" , "");
	    	if( aar.getInterrupted() ) curPlanInExec   = "avoidMobileObstacle";
	    	if( ! aar.getGoon() ) return ;
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine, "usercmd(CMD)","usercmd(robotgui(a(low)))", guardVars ).toString();
	    	emit( "usercmd", temporaryStr );
	    	//switchTo doWork
	        switchToPlanAsNextState(pr, myselfName, "applr0agent_"+myselfName, 
	              "doWork",false, false, null); 
	    }catch(Exception e_avoidMobileObstacle){  
	    	 println( getName() + " plan=avoidMobileObstacle WARNING:" + e_avoidMobileObstacle.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//avoidMobileObstacle
	    
	    StateFun robotAtSonar1 = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("robotAtSonar1",-1);
	    	String myselfName = "robotAtSonar1";  
	    	printCurrentMessage(false);
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("atsonar1(D)");
	    	if( currentMessage != null && currentMessage.msgId().equals("atsonar1") && 
	    		pengine.unify(curT, Term.createTerm("atsonar1(DISTANCE)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		String parg="sonar1ok(D)";
	    		/* AddRule */
	    		parg = updateVars(Term.createTerm("atsonar1(DISTANCE)"),  Term.createTerm("atsonar1(D)"), 
	    			    		  					Term.createTerm(currentMessage.msgContent()), parg);
	    		if( parg != null ) addRule(parg);	    		  					
	    	}
	    	//switchTo doWork
	        switchToPlanAsNextState(pr, myselfName, "applr0agent_"+myselfName, 
	              "doWork",false, false, null); 
	    }catch(Exception e_robotAtSonar1){  
	    	 println( getName() + " plan=robotAtSonar1 WARNING:" + e_robotAtSonar1.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//robotAtSonar1
	    
	    StateFun robotAtSonar2 = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("robotAtSonar2",-1);
	    	String myselfName = "robotAtSonar2";  
	    	printCurrentMessage(false);
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("atsonar2(D)");
	    	if( currentMessage != null && currentMessage.msgId().equals("atsonar2") && 
	    		pengine.unify(curT, Term.createTerm("atsonar2(DISTANCE)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		String parg="sonar2ok(D)";
	    		/* AddRule */
	    		parg = updateVars(Term.createTerm("atsonar2(DISTANCE)"),  Term.createTerm("atsonar2(D)"), 
	    			    		  					Term.createTerm(currentMessage.msgContent()), parg);
	    		if( parg != null ) addRule(parg);	    		  					
	    	}
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?sonar1ok(D)" )) != null ){
	    	temporaryStr = "atsonar1(D)";
	    	temporaryStr = QActorUtils.substituteVars(guardVars,temporaryStr);
	    	println( temporaryStr );  
	    	}
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?sonar2ok(D)" )) != null ){
	    	temporaryStr = "atsonar2(D)";
	    	temporaryStr = QActorUtils.substituteVars(guardVars,temporaryStr);
	    	println( temporaryStr );  
	    	}
	    	//switchTo adjustRobotPosition
	        switchToPlanAsNextState(pr, myselfName, "applr0agent_"+myselfName, 
	              "adjustRobotPosition",false, false, null); 
	    }catch(Exception e_robotAtSonar2){  
	    	 println( getName() + " plan=robotAtSonar2 WARNING:" + e_robotAtSonar2.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//robotAtSonar2
	    
	    StateFun adjustRobotPosition = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("adjustRobotPosition",-1);
	    	String myselfName = "adjustRobotPosition";  
	    	parg = "robotFinalPosition(DELTA)";
	    	//QActorUtils.solveGoal(myself,parg,pengine );  //sets currentActionResult		
	    	solveGoal( parg ); //sept2017
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " ??goalResult(robotFinalPosition(DELTA))" )) != null ){
	    	temporaryStr = "DELTA";
	    	temporaryStr = QActorUtils.substituteVars(guardVars,temporaryStr);
	    	println( temporaryStr );  
	    	}
	    	repeatPlanNoTransition(pr,myselfName,"applr0agent_"+myselfName,false,false);
	    }catch(Exception e_adjustRobotPosition){  
	    	 println( getName() + " plan=adjustRobotPosition WARNING:" + e_adjustRobotPosition.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//adjustRobotPosition
	    
	    StateFun doWaitAnswer = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp(getName()+"_doWaitAnswer",0);
	     pr.incNumIter(); 	
	    	String myselfName = "doWaitAnswer";  
	    	temporaryStr = "\"applr0agent WAITS that a passage is found\"";
	    	println( temporaryStr );  
	    	//bbb
	     msgTransition( pr,myselfName,"applr0agent_"+myselfName,false,
	          new StateFun[]{stateTab.get("checkTheAnswer") }, 
	          new String[]{"true","E","taskdone" },
	          30000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_doWaitAnswer){  
	    	 println( getName() + " plan=doWaitAnswer WARNING:" + e_doWaitAnswer.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//doWaitAnswer
	    
	    StateFun checkTheAnswer = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("checkTheAnswer",-1);
	    	String myselfName = "checkTheAnswer";  
	    	printCurrentEvent(false);
	    	//onEvent 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("taskdone(obstacleAvoidance,\"ok\")");
	    	if( currentEvent != null && currentEvent.getEventId().equals("taskdone") && 
	    		pengine.unify(curT, Term.createTerm("taskdone(TASK,ARGS)")) && 
	    		pengine.unify(curT, Term.createTerm( currentEvent.getMsg() ) )){ 
	    			String parg="goon";
	    			/* AddRule */
	    			parg = updateVars(Term.createTerm("taskdone(TASK,ARGS)"),  Term.createTerm("taskdone(obstacleAvoidance,\"ok\")"), 
	    				    		  					Term.createTerm(currentEvent.getMsg()), parg);
	    			if( parg != null ) addRule(parg);	    		  					
	    	}
	    	//delay  ( no more reactive within a plan)
	    	aar = delayReactive(2000,"" , "");
	    	if( aar.getInterrupted() ) curPlanInExec   = "checkTheAnswer";
	    	if( ! aar.getGoon() ) return ;
	    	//switchTo doWork
	        switchToPlanAsNextState(pr, myselfName, "applr0agent_"+myselfName, 
	              "doWork",false, true, " ??goon"); 
	    }catch(Exception e_checkTheAnswer){  
	    	 println( getName() + " plan=checkTheAnswer WARNING:" + e_checkTheAnswer.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//checkTheAnswer
	    
	    StateFun alarmHandlePolicy = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("alarmHandlePolicy",-1);
	    	String myselfName = "alarmHandlePolicy";  
	    	printCurrentMessage(false);
	    	temporaryStr = "\"applr0agent ALARM HANDLING POLICY (stop)... \"";
	    	println( temporaryStr );  
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine, "usercmd(CMD)","usercmd(robotgui(h(low)))", guardVars ).toString();
	    	emit( "usercmd", temporaryStr );
	    	temporaryStr = "\"applr0agent ALARM HANDLING POLICY DONE \"";
	    	println( temporaryStr );  
	    	repeatPlanNoTransition(pr,myselfName,"applr0agent_"+myselfName,false,true);
	    }catch(Exception e_alarmHandlePolicy){  
	    	 println( getName() + " plan=alarmHandlePolicy WARNING:" + e_alarmHandlePolicy.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//alarmHandlePolicy
	    
	    protected void initSensorSystem(){
	    	//doing nothing in a QActor
	    }
	
	}
