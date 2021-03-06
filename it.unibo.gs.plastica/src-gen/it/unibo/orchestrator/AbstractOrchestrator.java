/* Generated by AN DISI Unibo */ 
package it.unibo.orchestrator;
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
import it.unibo.baseEnv.basicFrame.EnvFrame;
import alice.tuprolog.SolveInfo;
import it.unibo.is.interfaces.IActivity;
import it.unibo.is.interfaces.IIntent;
public abstract class AbstractOrchestrator extends QActor implements IActivity{ 
	protected AsynchActionResult aar = null;
	protected boolean actionResult = true;
	protected alice.tuprolog.SolveInfo sol;
	protected String planFilePath    = null;
	protected String terminationEvId = "default";
	protected String parg="";
	protected boolean bres=false;
	protected IActorAction action;
	 
	
		protected static IOutputEnvView setTheEnv(IOutputEnvView outEnvView ){
			EnvFrame env = new EnvFrame( "Env_orchestrator", java.awt.Color.cyan  , java.awt.Color.black );
			env.init();
			env.setSize(800,430); 
			IOutputEnvView newOutEnvView = ((EnvFrame) env).getOutputEnvView();
			return newOutEnvView;
		}
		public AbstractOrchestrator(String actorId, QActorContext myCtx, IOutputEnvView outEnvView )  throws Exception{
			super(actorId, myCtx,  
			"./srcMore/it/unibo/orchestrator/WorldTheory.pl",
			setTheEnv( outEnvView )  , "init");
			addInputPanel(80);
			addCmdPanels();
			this.planFilePath = "./srcMore/it/unibo/orchestrator/plans.txt";
	  	}
	protected void addInputPanel(int size){
		((EnvFrame) env).addInputPanel(size);			
	}
	protected void addCmdPanels(){
		((EnvFrame) env).addCmdPanel("input", new String[]{"INPUT"}, this);
		((EnvFrame) env).addCmdPanel("alarm", new String[]{"FIRE"}, this);
		((EnvFrame) env).addCmdPanel("help",  new String[]{"HELP"}, this);				
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
	    	stateTab.put("handleOperatorCmd",handleOperatorCmd);
	    	stateTab.put("handleMachineMsg",handleMachineMsg);
	    }
	    StateFun handleToutBuiltIn = () -> {	
	    	try{	
	    		PlanRepeat pr = PlanRepeat.setUp("handleTout",-1);
	    		String myselfName = "handleToutBuiltIn";  
	    		println( "orchestrator tout : stops");  
	    		repeatPlanNoTransition(pr,myselfName,"application_"+myselfName,false,false);
	    	}catch(Exception e_handleToutBuiltIn){  
	    		println( getName() + " plan=handleToutBuiltIn WARNING:" + e_handleToutBuiltIn.getMessage() );
	    		QActorContext.terminateQActorSystem(this); 
	    	}
	    };//handleToutBuiltIn
	    
	    StateFun init = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp(getName()+"_init",0);
	     pr.incNumIter(); 	
	    	String myselfName = "init";  
	    	temporaryStr = "\"orchestrator starts\"";
	    	println( temporaryStr );  
	     connectToMqttServer("tcp://localhost:1883");
	    	//bbb
	     msgTransition( pr,myselfName,"orchestrator_"+myselfName,false,
	          new StateFun[]{stateTab.get("handleOperatorCmd"), stateTab.get("handleMachineMsg") }, 
	          new String[]{"true","M","operatorCmd", "true","M","machineMsg" },
	          600000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_init){  
	    	 println( getName() + " plan=init WARNING:" + e_init.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//init
	    
	    StateFun handleOperatorCmd = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("handleOperatorCmd",-1);
	    	String myselfName = "handleOperatorCmd";  
	    	temporaryStr = "\"handleOperatorCmd\"";
	    	println( temporaryStr );  
	    	printCurrentMessage(false);
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("operatorCmd(CMD)");
	    	if( currentMessage != null && currentMessage.msgId().equals("operatorCmd") && 
	    		pengine.unify(curT, Term.createTerm("operatorCmd(PARAMS)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		String parg="machineCmd(CMD)";
	    		/* SendDispatch */
	    		parg = updateVars(Term.createTerm("operatorCmd(PARAMS)"),  Term.createTerm("operatorCmd(CMD)"), 
	    			    		  					Term.createTerm(currentMessage.msgContent()), parg);
	    		if( parg != null ) sendMsg("machineCmd","presslogical", QActorContext.dispatch, parg ); 
	    	}
	    	repeatPlanNoTransition(pr,myselfName,"orchestrator_"+myselfName,false,true);
	    }catch(Exception e_handleOperatorCmd){  
	    	 println( getName() + " plan=handleOperatorCmd WARNING:" + e_handleOperatorCmd.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//handleOperatorCmd
	    
	    StateFun handleMachineMsg = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("handleMachineMsg",-1);
	    	String myselfName = "handleMachineMsg";  
	    	temporaryStr = "\"handleMachineMsg\"";
	    	println( temporaryStr );  
	    	printCurrentMessage(false);
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("machineMsg(P)");
	    	if( currentMessage != null && currentMessage.msgId().equals("machineMsg") && 
	    		pengine.unify(curT, Term.createTerm("machineMsg(PARAMS)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		String parg="updatedInfo(P)";
	    		/* RaiseEvent */
	    		parg = updateVars(Term.createTerm("machineMsg(PARAMS)"),  Term.createTerm("machineMsg(P)"), 
	    			    		  					Term.createTerm(currentMessage.msgContent()), parg);
	    		if( parg != null ) emit( "updatedInfo", parg );
	    	}
	    	repeatPlanNoTransition(pr,myselfName,"orchestrator_"+myselfName,false,true);
	    }catch(Exception e_handleMachineMsg){  
	    	 println( getName() + " plan=handleMachineMsg WARNING:" + e_handleMachineMsg.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//handleMachineMsg
	    
	    protected void initSensorSystem(){
	    	//doing nothing in a QActor
	    }
	
		/* 
		* ------------------------------------------------------------
		* IACTIVITY (aactor with GUI)
		* ------------------------------------------------------------
		*/
		private String[] actions = new String[]{
		    	"println( STRING | TERM )", 
		    	"play('./audio/music_interlude20.wav'),20000,'alarm,obstacle', 'handleAlarm,handleObstacle'",
		"emit(EVID,EVCONTENT)  ",
		"move(MOVE,DURATION,ANGLE)  with MOVE=mf|mb|ml|mr|ms",
		"forward( DEST, MSGID, MSGCONTENTTERM)"
		    };
		    protected void doHelp(){
				println("  GOAL ");
				println("[ GUARD ], ACTION  ");
				println("[ GUARD ], ACTION, DURATION ");
				println("[ GUARD ], ACTION, DURATION, ENDEVENT");
				println("[ GUARD ], ACTION, DURATION, EVENTS, PLANS");
				println("Actions:");
				for( int i=0; i<actions.length; i++){
					println(" " + actions[i] );
				}
		    }
		@Override
		public void execAction(String cmd) {
			if( cmd.equals("HELP") ){
				doHelp();
				return;
			}
			if( cmd.equals("FIRE") ){
				emit("alarm", "alarm(fire)");
				return;
			}
			String input = env.readln();
			//input = "\""+input+"\"";
			input = it.unibo.qactors.web.GuiUiKb.buildCorrectPrologString(input);
			//println("input=" + input);
			try {
				Term.createTerm(input);
	 			String eventMsg=it.unibo.qactors.web.QActorHttpServer.inputToEventMsg(input);
				//println("QActor eventMsg " + eventMsg);
				emit("local_"+it.unibo.qactors.web.GuiUiKb.inputCmd, eventMsg);
	  		} catch (Exception e) {
		 		println("QActor input error " + e.getMessage());
			}
		}
	 	
		@Override
		public void execAction() {}
		@Override
		public void execAction(IIntent input) {}
		@Override
		public String execActionWithAnswer(String cmd) {return null;}
	}
