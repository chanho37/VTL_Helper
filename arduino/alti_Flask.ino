#include <Wire.h>
#include <WiFi.h>
#include <SPI.h>
#include <Adafruit_BMP280.h>
#include <HTTPClient.h>


const  char* ssid1 = "ㅇㅈㅇ"; // 첫 번째 Wi-Fi 네트워크 이름
const char* password1 = "qqqqqqqq"; // 첫 번째 Wi-Fi 네트워크 비밀번호

struct Sensor
{
  float alt;
};

Sensor sensor;

float altitude = 0;
float ALTavg = 0;

Adafruit_BMP280 bmp1; // use I2C interface
Adafruit_Sensor *bmp_temp1 = bmp1.getTemperatureSensor();
Adafruit_Sensor *bmp_pressure1 = bmp1.getPressureSensor();

void setup() {
  Serial.begin(115200);
  connectToWiFi(ssid1, password1);
  Wire.begin();
  delay(2000);
  settingBMP();
  calibrationBMP();
}

void loop() {
  getAltitude();
  Serial.println(sensor.alt - ALTavg);
//  Serial.println(" m");
  delay(500);
  
  // 데이터를 Flask 서버로 전송
  sendDataToFlask(sensor.alt - ALTavg);
  delay(500);  // 5초 간격으로 전송하도록 설정 (원하는 간격으로 조절 가능)
}

void sendDataToFlask(float altitudeValue) {
  if (WiFi.status() == WL_CONNECTED) {if (WiFi.status() == WL_CONNECTED) {
  String dataToSend = String(altitudeValue);

  // HTTP 클라이언트 설정
  HTTPClient http;
  http.begin("http://127.0.0.1:5000");  // Flask 서버의 주소로 변경
  http.addHeader("Content-Type", "text/plain");

  // 데이터를 POST 요청으로 보냄
  int httpCode = http.POST("altitude=" + dataToSend);
  if (httpCode == HTTP_CODE_OK) {
    Serial.println("Data sent to Flask server successfully");
  } else {
    //Serial.println("Error sending data to Flask server");
  }

  http.end();
  }
 delay(500);
}
}
void getAltitude()
{
  sensors_event_t temp_event1, pressure_event1, altitude_event1 ;
  bmp_temp1->getEvent(&temp_event1);
  bmp_pressure1->getEvent(&pressure_event1);
  float temperature = temp_event1.temperature;
  float pressure = pressure_event1.pressure;
  altitude = bmp1.readAltitude(1013.25);
  sensor.alt = altitude;
}

void settingBMP()
{
  while ( !Serial ) delay(100);   // wait for native usb
  Serial.println(F("BMP280 Sensor event test"));
  
  unsigned status;
  status = bmp1.begin(0x76);
  if (!status) {
    Serial.println(F("Could not find a valid BMP280 sensor, check wiring or "
                     "try a different address!"));
    Serial.print("SensorID was: 0x"); Serial.println(bmp1.sensorID(), 16);
    Serial.print("        ID of 0xFF probably means a bad address, a BMP 180 or BMP 085\n");
    Serial.print("   ID of 0x56-0x58 represents a BMP 280,\n");
    Serial.print("        ID of 0x60 represents a BME 280.\n");
    Serial.print("        ID of 0x61 represents a BME 680.\n");
    while (1) delay(10);

    bmp1.setSampling(Adafruit_BMP280::MODE_NORMAL,   // Normal 모드로 설정
                     Adafruit_BMP280::SAMPLING_X2,   // 온도 해상도 17-bit (x2)
                     Adafruit_BMP280::SAMPLING_X16,  // 압력 해상도 20-bit (x16)
                     Adafruit_BMP280::FILTER_X16,    // IIR 필터 샘플링 수 16
                     Adafruit_BMP280::STANDBY_MS_500
                    );

    bmp_temp1->printSensorDetails();
  }
}

void connectToWiFi(const char* ssid, const char* password) {
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    //Serial.println("Connecting to WiFi…");
  }
}


void calibrationBMP()
{
  for (int i = 0; i < 1000; i++){
    sensors_event_t temp_event1, pressure_event1, altitude_event1 ;
    bmp_temp1->getEvent(&temp_event1);
    bmp_pressure1->getEvent(&pressure_event1);
    float temperature = temp_event1.temperature;
    float pressure = pressure_event1.pressure;
    altitude = bmp1.readAltitude(1013.25);
    ALTavg += altitude;
  }
  ALTavg = ALTavg / 1000;
}
