/* Generated by AN DISI Unibo */ 
package it.unibo.ctxMbotExecutor;
import it.unibo.qactors.QActorContext;
import it.unibo.is.interfaces.IOutputEnvView;
import it.unibo.system.SituatedSysKb;
public class MainCtxMbotExecutor  {
  
//MAIN
public static QActorContext initTheContext() throws Exception{
	IOutputEnvView outEnvView = SituatedSysKb.standardOutEnvView;
	String webDir = "./srcMore/it/unibo/ctxMbotExecutor";
	return QActorContext.initQActorSystem(
		"ctxmbotexecutor", "./srcMore/it/unibo/ctxMbotExecutor/mbotexecutor.pl", 
		"./srcMore/it/unibo/ctxMbotExecutor/sysRules.pl", outEnvView,webDir,false);
}
public static void main(String[] args) throws Exception{
	QActorContext ctx = initTheContext();
} 	
}
