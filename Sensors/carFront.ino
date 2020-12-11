 /*
  * LED & Button controller
  */
class Flasher{
  int ledPin;
  int ledState;

  public:
  Flasher(int led){
    ledPin = led;
    ledState = LOW;
  }

  void Update(int value){
    ledState = value;
    digitalWrite(ledPin, ledState);
  }
  
  void Control(int value){
    // int button = digitalRead(btnPin);
    if(value == HIGH){
      if(ledState == LOW){
        ledState = HIGH;
      }else if(ledState == HIGH){
        ledState = LOW;
      }
      String strLed = "";
      if(ledPin == 4){
        strLed = "00310000000";
      }else if(ledPin == 5){
        strLed = "00320000000";
      }
      strLed += ledState;
      Serial.println(strLed);
    }
      digitalWrite(ledPin, ledState);
  }
};
 /*
  * End LED & Button controller
  */

const int crushPin = A0; // 충돌센서
const int vibrAOPin = A11; // 진동센서 아날로그

const int vibrDOPin = 13; // 진동센서 디지털
const int startledPin = 4; // 시동 LED
const int driveledPin = 5; // 주행 LED
const int doorledPin = 6; // 문 LED
const int startbtnPin = 7; // 시동 버튼
const int drivebtnPin = 8; // 주행 버튼

Flasher start(startledPin);
Flasher drive(driveledPin);
Flasher door(doorledPin);

int crushState = 0; // 충돌 상태
int leituraAant = 0; // 진동 최고 세기
int leituraA = 0; // 진동 세기(아날로그)
int leituraD = 0; // 진동 상태(디지털)

void setup() {
  Serial.begin(9600);
  
  // OUTPUT setting
  pinMode(startledPin, OUTPUT);
  pinMode(driveledPin, OUTPUT);
  pinMode(doorledPin, OUTPUT);
  
  // INPUT setting
  pinMode(startbtnPin, INPUT);
  pinMode(drivebtnPin, INPUT);
  pinMode(crushPin, INPUT);
  pinMode(vibrAOPin, INPUT);
  pinMode(vibrDOPin, INPUT);
}

void loop() {
  int crush = analogRead(crushPin);
  int leituraA = analogRead(vibrAOPin);
  int leituraD = digitalRead(vibrDOPin);
  String strcrush = "";

  // 교통사고 발생
  // 충돌 시작
  if(crushState == 0 && crush < 1000){
    crushState = 1;
    String startcrush = "0002";
    startcrush += crush;
    Serial.println(startcrush);
  }

  // 충돌 중
  if(crushState == 1){
    // 가장 강한 진동 세기 저장
    if(leituraD != 1){
      if(0 != leituraA && leituraAant < leituraA){
        leituraAant = leituraA;
      }
    }

    // 충돌 끝
    if (crush > 1000) {
      strcrush = "0003";
      strcrush += leituraAant;
      Serial.println(strcrush);
      crushState = 0;
      leituraAant = 0;
    }
  }
  
  // 버튼 제어
  start.Control(digitalRead(startbtnPin));
  drive.Control(digitalRead(drivebtnPin));

  // serial 통신을 통한 제어
  String cmd = "";
  cmd = Serial.readString();
  if(cmd == "003100000001"){
   start.Update(HIGH); // 시동
  }else if(cmd == "003100000000"){
   start.Update(LOW); // 시동 종료
  }else if(cmd == "003200000001"){
   drive.Update(HIGH); // 주행
  }else if(cmd == "003200000000"){
   drive.Update(LOW); // 주행 종료
  }else if(cmd == "003300000001"){
   door.Update(HIGH); // 문 열림
  }else if(cmd == "003300000000"){
   door.Update(LOW); // 문 잠금
  }
  
  //Serial.println(cmd);
}


