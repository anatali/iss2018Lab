/* Generated by AN DISI Unibo */ 
package it.unibo.ctxRealRobotWithLedExecutor;
import it.unibo.qactors.QActorContext;
import it.unibo.is.interfaces.IOutputEnvView;
import it.unibo.system.SituatedSysKb;
public class MainCtxRealRobotWithLedExecutor  {
  
//MAIN
public static QActorContext initTheContext() throws Exception{
	IOutputEnvView outEnvView = SituatedSysKb.standardOutEnvView;
	String webDir = "./srcMore/it/unibo/ctxRealRobotWithLedExecutor";
	return QActorContext.initQActorSystem(
		"ctxrealrobotwithledexecutor", "./srcMore/it/unibo/ctxRealRobotWithLedExecutor/realrobotwithledexecutor.pl", 
		"./srcMore/it/unibo/ctxRealRobotWithLedExecutor/sysRules.pl", outEnvView,webDir,false);
}
public static void main(String[] args) throws Exception{
	QActorContext ctx = initTheContext();
} 	
}