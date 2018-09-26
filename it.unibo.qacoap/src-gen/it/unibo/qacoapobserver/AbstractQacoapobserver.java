/* Generated by AN DISI Unibo */ 
package it.unibo.qacoapobserver;
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
public abstract class AbstractQacoapobserver extends QActor implements IActivity{ 
	protected AsynchActionResult aar = null;
	protected boolean actionResult = true;
	protected alice.tuprolog.SolveInfo sol;
	protected String planFilePath    = null;
	protected String terminationEvId = "default";
	protected String parg="";
	protected boolean bres=false;
	protected IActorAction action;
	 
	
		protected static IOutputEnvView setTheEnv(IOutputEnvView outEnvView ){
			EnvFrame env = new EnvFrame( "Env_qacoapobserver", java.awt.Color.yellow  , java.awt.Color.black );
			env.init();
			env.setSize(800,430); 
			IOutputEnvView newOutEnvView = ((EnvFrame) env).getOutputEnvView();
			return newOutEnvView;
		}
		public AbstractQacoapobserver(String actorId, QActorContext myCtx, IOutputEnvView outEnvView )  throws Exception{
			super(actorId, myCtx,  
			"./srcMore/it/unibo/qacoapobserver/WorldTheory.pl",
			setTheEnv( outEnvView )  , "init");
			addInputPanel(80);
			addCmdPanels();
			this.planFilePath = "./srcMore/it/unibo/qacoapobserver/plans.txt";
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
	    	stateTab.put("work",work);
	    	stateTab.put("observe",observe);
	    }
	    StateFun handleToutBuiltIn = () -> {	
	    	try{	
	    		PlanRepeat pr = PlanRepeat.setUp("handleTout",-1);
	    		String myselfName = "handleToutBuiltIn";  
	    		println( "qacoapobserver tout : stops");  
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
	    	//switchTo work
	        switchToPlanAsNextState(pr, myselfName, "qacoapobserver_"+myselfName, 
	              "work",false, false, null); 
	    }catch(Exception e_init){  
	    	 println( getName() + " plan=init WARNING:" + e_init.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//init
	    
	    StateFun work = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("work",-1);
	    	String myselfName = "work";  
	    	temporaryStr = "\"qacoapoberver STARTS \"";
	    	println( temporaryStr );  
	    	createObserver();
	    	doCoapQuery( "globalkb" ,  "assign(a,100)" );
	    	doCoapQuery( "globalkb" ,  "getVal(a,X)" );
	    	//switchTo observe
	        switchToPlanAsNextState(pr, myselfName, "qacoapobserver_"+myselfName, 
	              "observe",false, false, null); 
	    }catch(Exception e_work){  
	    	 println( getName() + " plan=work WARNING:" + e_work.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//work
	    
	    StateFun observe = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp(getName()+"_observe",0);
	     pr.incNumIter(); 	
	    	String myselfName = "observe";  
	    	temporaryStr = "\"qacoapoberver OBSERVING ... \"";
	    	println( temporaryStr );  
	    	//bbb
	     msgTransition( pr,myselfName,"qacoapobserver_"+myselfName,false,
	          new StateFun[]{() -> {	//AD HOC state to execute an action and resumeLastPlan
	          try{
	            PlanRepeat pr1 = PlanRepeat.setUp("adhocstate",-1);
	            //ActionSwitch for a message or event
	             if( currentEvent.getMsg().startsWith("info") ){
	            	String parg = "qacoapobserver(observeddddddddddddddd,X)";
	            	/* Print */
	            	parg =  updateVars( Term.createTerm("info(X)"), 
	            	                    Term.createTerm("info(X)"), 
	            		    		  	Term.createTerm(currentEvent.getMsg()), parg);
	            	if( parg != null ) println( parg );
	             }
	            repeatPlanNoTransition(pr1,"adhocstate","adhocstate",false,true);
	          }catch(Exception e ){  
	             println( getName() + " plan=observe WARNING:" + e.getMessage() );
	             //QActorContext.terminateQActorSystem(this); 
	          }
	          }
	          }, 
	          new String[]{"true","E","coapInfo" },
	          600000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_observe){  
	    	 println( getName() + " plan=observe WARNING:" + e_observe.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//observe
	    
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