/* Generated by AN DISI Unibo */ 
package it.unibo.ctxQaBlsThings;
import it.unibo.qactors.QActorContext;
import it.unibo.is.interfaces.IOutputEnvView;
import it.unibo.system.SituatedSysKb;
public class MainCtxQaBlsThings  {
  
//MAIN
public static QActorContext initTheContext() throws Exception{
	IOutputEnvView outEnvView = SituatedSysKb.standardOutEnvView;
	String webDir = null;
	return QActorContext.initQActorSystem(
		"ctxqablsthings", "./srcMore/it/unibo/ctxQaBlsThings/qablsthings.pl", 
		"./srcMore/it/unibo/ctxQaBlsThings/sysRules.pl", outEnvView,webDir,false);
}
public static void main(String[] args) throws Exception{
	QActorContext ctx = initTheContext();
} 	
}
