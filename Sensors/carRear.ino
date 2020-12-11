// LoadCell Library
#include "HX711.h"

// LCD Library
#include <LiquidCrystal.h> 

#define LOADCELL_DOUT_PIN 2
#define LOADCELL_SCK_PIN 3

// LiquidCrystal lcd(RS, E, D4, D5, D6, D7)
LiquidCrystal lcd(6, 5, 23, 22, 21, 13);
float goaltemp = 0;

const int pirPin = A11; // 적외선 센서
const int tempPin = A1; // 온도 센서
float preTemp = 0.0; // 이전 온도 데이터

// LoadCell scale Function
HX711 scale;
 
//-4486 worked for my 3kg max scale setup
float calibration_factor = -4486;
 
void setup() 
{
  Serial.begin(9600);
  Serial.println("HX711 calibration sketch");
  Serial.println("Remove all weight from scale");
  Serial.println("After readings begin, place known weight on scale");
  Serial.println("Press + or a to increase calibration factor");
  Serial.println("Press - or z to decrease calibration factor");

  // Pin setting
  pinMode(pirPin, INPUT);
  lcd.begin(16, 2);
  
  scale.begin(LOADCELL_DOUT_PIN, LOADCELL_SCK_PIN);
  scale.set_scale();
 
  //Reset the scale to 0
  scale.tare();
 
  //Get a baseline reading
  long zero_factor = scale.read_average(); 
  //This can be used to remove the need to tare the scale. Useful in permanent scale projects.
  Serial.print("Zero factor: ");
  Serial.println(zero_factor);
}
 
void loop() 
{
  int temp = analogRead(tempPin);
  int pir = digitalRead(pirPin);
  
  // 센싱
  // 온도 센서 작동
  float tempF = ((5.0 * temp)/1024.0) * 100; // 현재 온도
  if(preTemp != tempF){
    preTemp = tempF;
    String strtemp = "0001";
    strtemp += tempF;
    Serial.println(strtemp);
    delay(500);
  }

  // 적외선 센서 작동
  if(pir == HIGH){
    Serial.println("000400000001");
  }

  String cmd = Serial.readString();

  if(cmd.substring(0,4) == "0021"){
    // LCD 제어
    goaltemp = cmd.substring(8).toFloat(); // 목표 온도
    lcd.clear();
    lcd.print("temperature set");
    lcd.setCursor(0, 1); // 줄바꿈
    lcd.print(goaltemp/100.0);
  }else if(cmd.substring(0,4) == "0032"){
    //주행 or Stop 명령
    if(cmd.substring(11,12) == "1"){
      Serial.println("00059999");
      delay(500);
    }
  }

    
  //Adjust to this calibration factor
  scale.set_scale(calibration_factor);
  
  double value = scale.get_units()*10;
  if(value < 0){
    value = value *(-1);
  }
  if(value == 0 ){
    value = value + 1;  
  }
  String value_str = String(value, 1);
  Serial.println("0005"+value_str);
  //Change this to kg and re-adjust the calibration factor if you follow SI units like a sane person
  //기존 예제가 파운드(lbs) 기준이지만 우리는 그램(g)을 쓸것이므로 'g'로 바꿉니다.
  //Serial.print(" g"); 
  //Serial.print(" calibration_factor: ");
  //Serial.print(calibration_factor);
  //Serial.println();
  delay(300);
 
  if(Serial.available())
  {
    

//    char temp_2 = char(temp[0]);
//
//    // 무게센서 보정값 변동
//    // 변경 : 보정값 범위 설정 가능하도록 변경
//    switch(temp_2)
//    {
//      case "0":
//        calibration_factor += 1;
//        break;
//      case "1":
//        calibration_factor += 10;
//        break;
//      case "2":
//        calibration_factor += 50;
//        break;
//      case "3":
//        calibration_factor += 100;
//        break;
//      case "4":
//        calibration_factor += 1000;
//        break;
//      case "5":
//        calibration_factor += 10000;
//        break;
//      case "6":
//        calibration_factor += 100000;
//        break;
// 
//      case "a":
//        calibration_factor -= 10;
//        break;
//      case "s":
//        calibration_factor -= 50;
//        break;
//      case "d":
//        calibration_factor -= 100;
//        break;
//      case "f":
//        calibration_factor -= 1000;
//        break;
//      case "g":
//        calibration_factor -= 10000;
//        break;
//      case "h":
//        calibration_factor -= 100000;
//        break;
//      case "l":
//        calibration_factor -= 1;
//        break;
//    }
  }
  
}

