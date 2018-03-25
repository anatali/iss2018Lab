/* Generated by AN DISI Unibo */ 
package it.unibo.ctxMbotAgent;
import it.unibo.qactors.QActorContext;
import it.unibo.is.interfaces.IOutputEnvView;
import it.unibo.system.SituatedSysKb;
public class MainCtxMbotAgent  {
  
//MAIN
public static QActorContext initTheContext() throws Exception{
	IOutputEnvView outEnvView = SituatedSysKb.standardOutEnvView;
	it.unibo.is.interfaces.IBasicEnvAwt env=new it.unibo.baseEnv.basicFrame.EnvFrame( 
		"Env_ctxMbotAgent",java.awt.Color.yellow , java.awt.Color.black );
	env.init();
	outEnvView = env.getOutputEnvView();
	String webDir = null;
	return QActorContext.initQActorSystem(
		"ctxmbotagent", "./srcMore/it/unibo/ctxMbotAgent/mbotagent.pl", 
		"./srcMore/it/unibo/ctxMbotAgent/sysRules.pl", outEnvView,webDir,false);
}
public static void main(String[] args) throws Exception{
	QActorContext ctx = initTheContext();
} 	
}
