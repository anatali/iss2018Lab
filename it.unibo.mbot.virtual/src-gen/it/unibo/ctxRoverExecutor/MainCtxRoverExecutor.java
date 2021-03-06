/* Generated by AN DISI Unibo */ 
package it.unibo.ctxRoverExecutor;
import it.unibo.qactors.QActorContext;
import it.unibo.is.interfaces.IOutputEnvView;
import it.unibo.system.SituatedSysKb;
public class MainCtxRoverExecutor  {
  
//MAIN
public static QActorContext initTheContext() throws Exception{
	IOutputEnvView outEnvView = SituatedSysKb.standardOutEnvView;
	it.unibo.is.interfaces.IBasicEnvAwt env=new it.unibo.baseEnv.basicFrame.EnvFrame( 
		"Env_ctxRoverExecutor",java.awt.Color.white , java.awt.Color.black );
	env.init();
	outEnvView = env.getOutputEnvView();
	String webDir = "./srcMore/it/unibo/ctxRoverExecutor";
	return QActorContext.initQActorSystem(
		"ctxroverexecutor", "./srcMore/it/unibo/ctxRoverExecutor/roverexecutor.pl", 
		"./srcMore/it/unibo/ctxRoverExecutor/sysRules.pl", outEnvView,webDir,false);
}
public static void main(String[] args) throws Exception{
	QActorContext ctx = initTheContext();
} 	
}
