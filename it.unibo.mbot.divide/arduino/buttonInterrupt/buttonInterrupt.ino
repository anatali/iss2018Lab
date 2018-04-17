/*
=======================================
 buttonInterrupt.ino
 project it.unibo.arduino.intro
 Input from Pin 3 (Button)
 ARDUINO UNO
 	pin 3 maps to interrupt 1, 
 	pin 2 is interrupt 0, 
 =======================================
 Arduino has internal PULLUP resistors to tie the input high.  
 Hook one end of the switch to the input (5V) and the other end to ground: 
 when we PUSH the switch the input goes LOW. 
 */
 
int pinButton = 3;   
int pinLed    = 13;  

void initLed(){
   pinMode(pinLed, OUTPUT);  
}
void initButton(){
  pinMode(pinButton, INPUT); 
  digitalWrite(pinButton, HIGH); //set PULLUP : PUSH=>0
  attachInterrupt(1, buttonInterruptHandler, CHANGE); // RISING CHANGE FOLLING LOW
}
/*
-----------------------------
INTERRUPT HANDLER function
-----------------------------
*/ 
void buttonInterruptHandler(){
  boolean ok = debouncing(); 
  if( ok ){
    int v  = digitalRead(pinButton);
    Serial.println(   "interrupt v=" + String(v));
    digitalWrite( pinLed, v ); 
   }
}
boolean debouncing(){
static unsigned long lastInterruptTime = 0;
static int  lastVal = 0;
unsigned long interruptTime = millis(); 
int v = digitalRead(pinButton);
  if( (interruptTime - lastInterruptTime) > 100 ){
    if( (v != lastVal) ) lastVal = v;
    return true;
  }
  lastInterruptTime = interruptTime;
  return false;
}
void setup(){  
  Serial.begin(9600);
  Serial.println( "--------------------------------------"  );
  Serial.println( "project it.unibo.arduino.intro"  );
  Serial.println( "buttonInterrupt/buttonInterrupt.ino"  );
  Serial.println( "--------------------------------------"  );
  initButton();
  initLed();
}
void loop(){
  //do nothing
}
