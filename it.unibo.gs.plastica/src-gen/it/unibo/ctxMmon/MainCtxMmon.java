/* Generated by AN DISI Unibo */ 
package it.unibo.ctxMmon;
import it.unibo.qactors.QActorContext;
import it.unibo.is.interfaces.IOutputEnvView;
import it.unibo.system.SituatedSysKb;
public class MainCtxMmon  {
  
//MAIN
public static QActorContext initTheContext() throws Exception{
	IOutputEnvView outEnvView = SituatedSysKb.standardOutEnvView;
	String webDir = "./srcMore/it/unibo/ctxMmon";
	return QActorContext.initQActorSystem(
		"ctxmmon", "./srcMore/it/unibo/ctxMmon/mmon.pl", 
		"./srcMore/it/unibo/ctxMmon/sysRules.pl", outEnvView,webDir,false);
}
public static void main(String[] args) throws Exception{
	QActorContext ctx = initTheContext();
} 	
}
