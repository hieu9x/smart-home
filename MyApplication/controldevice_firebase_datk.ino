#include <FirebaseESP8266.h>
#include  <ESP8266WiFi.h>
#include "DHT.h"
#define ssid "Dat"  //WiFi SSID
#define password "123456789"  //WiFi Password
#define FIREBASE_HOST "smarthome1-47b53-default-rtdb.firebaseio.com"    
#define FIREBASE_AUTH "rVpgUUfM3AhSV6GEnh9HnIExeymZ8k92HKQQz2qn"
FirebaseData firebaseData;
FirebaseJson json;
unsigned long lastMsg = 0;
unsigned long now = millis();
int led2 = D3;
int led1 = D2;
int led3 = D4;
int led4 = D5;
int DHTPin = D1;

DHT dht(DHTPin, DHT11);
float temperature;
float humidity;
void setup() {
 Serial.begin(9600);
   WiFi.begin (ssid, password);
   while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.println(".");
  }
  Serial.println ("");
  Serial.println ("WiFi Connected!");
  Serial.println();
  Serial.print("IP Address is : ");
  Serial.println(WiFi.localIP());
  Firebase.begin(FIREBASE_HOST,FIREBASE_AUTH); 
  pinMode(led1, OUTPUT);
  pinMode(led2, OUTPUT);
  pinMode(led3, OUTPUT);
  pinMode(led4, OUTPUT);
  pinMode(DHTPin, INPUT);
  dht.begin();
  String mac= WiFi.macAddress();
  json.set("macadr",mac);
  String macadr = String("/module")+String("/")+String("macadr");
  Firebase.pushJSON(firebaseData,macadr,json);
}

void loop() {
  tv();
  light();
  fan();
  stove();
  if (now - lastMsg > 1000) {
      lastMsg = now;
      sendTempHumToFireBase();
  }
 }

void stove()
{
  String mac= WiFi.macAddress();
  String path = String("/module/module2")+String("/")+mac+String("/")+String("Stove");
  if (Firebase.get(firebaseData,path)){
    int led4Status = firebaseData.intData();
    if (led4Status == 1) {
      Serial.println("ON");
      digitalWrite(led4, HIGH);
    }
    else {
      Serial.println("OFF");
      digitalWrite(led4, LOW);
    }
  }
}
void tv()
{
  String mac= WiFi.macAddress();
  String path = String("/module/module2")+String("/")+mac+String("/")+String("TV");
  if (Firebase.get(firebaseData,path)){
    int led3Status = firebaseData.intData();
    if (led3Status == 1) {
      Serial.println("ON");
      digitalWrite(led3, HIGH);
    }
    else {
      Serial.println("OFF");
      digitalWrite(led3, LOW);
    }
  }
}
void fan()
{
  String mac= WiFi.macAddress();
  String path = String("/module/module2")+String("/")+mac+String("/")+String("Fan");
  if (Firebase.get(firebaseData,path)){
    int led2Status = firebaseData.intData();
    if (led2Status == 1) {
      Serial.println("ON");
      digitalWrite(led2, HIGH);
    }
    else {
      Serial.println("OFF");
      digitalWrite(led2, LOW);
    }
  }
}
void light()
{
  String mac= WiFi.macAddress();
  String path = String("/module/module2")+String("/")+mac+String("/")+String("Light");
  if (Firebase.get(firebaseData,path)){
    int led1Status = firebaseData.intData();
    if (led1Status == 1) {
      Serial.println("ON");
      digitalWrite(led1, HIGH);
    }
    else {
      Serial.println("OFF");
      digitalWrite(led1, LOW);
    }
  }
}
void sendTempHumToFireBase(){
  temperature = dht.readTemperature();
  humidity = dht.readHumidity(); 
  String mac= WiFi.macAddress();
  String pathHum = String("/module/module1")+String("/")+mac+String("/")+String("humidity");
  String pathTemp = String("/module/module1")+String("/")+mac+String("/")+String("temperature");
  
  Firebase.setFloat(firebaseData,pathHum, humidity);
  Firebase.setFloat(firebaseData,pathTemp, temperature);
  Serial.println(temperature);
  Serial.println(humidity);
}
