%====================================================================================
% Context ctxMbotAgent  SYSTEM-configuration: file it.unibo.ctxMbotAgent.mbotAgent.pl 
%====================================================================================
context(ctxmbotagent, "localhost",  "TCP", "8039" ).  		 
%%% -------------------------------------------
qactor( roveragent , ctxmbotagent, "it.unibo.roveragent.MsgHandle_Roveragent"   ). %%store msgs 
qactor( roveragent_ctrl , ctxmbotagent, "it.unibo.roveragent.Roveragent"   ). %%control-driven 
%%% -------------------------------------------
eventhandler(evh,ctxmbotagent,"it.unibo.ctxMbotAgent.Evh","alarmev").  
%%% -------------------------------------------

