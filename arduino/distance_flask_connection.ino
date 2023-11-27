#include <WiFi.h>
#include <HTTPClient.h>



const  char* ssid1 = "ㅇㅈㅇ"; // 첫 번째 Wi-Fi 네트워크 이름
const char* password1 = "qqqqqqqq"; // 첫 번째 Wi-Fi 네트워크 비밀번호
const char* ssid2 = "찬호의 iPhone"; // 두 번째 Wi-Fi 네트워크 이름
const char* password2 = "song000507"; // 두 번째 Wi-Fi 네트워크 비밀번호

const char* serverAddress = "http://127.0.0.1:5000";  // 플라스크 웹 서버 주소

void setup() {
  Serial.begin(115200);
  connectToWiFi(ssid1, password1);
  connectToWiFi(ssid2, password2);
}

void loop() {
//  int rssi1 = WiFi.RSSI(); // 첫 번째 네트워크의 RSSI 값 얻기
  int rssi2;
  
  //RSSI 값을 얻을 때 다른 네트워크로 전환
  connectToWiFi(ssid2, password2);
  rssi2 = WiFi.RSSI(); // 두 번째 네트워크의 RSSI 값 얻기
  connectToWiFi(ssid1, password1); // 첫 번째 네트워크로 다시 연결

  double distance1 = calculateDistance(rssi1);
  double distance2 = calculateDistance(rssi2);  
    
  Serial.print("UAM1 추정 거리 (센): ");
  Serial.println(distance1);
  
  Serial.print("UAM2 추정 거리 (센): ");
  Serial.println(distance2);

if (WiFi.status() == WL_CONNECTED) {
    // 보낼 데이터를 생성

    String dataToSend = "UAM1 추정 거리 (센): " + String(distance1) + " UAM2 추정 거리 (센): " + String(distance2);
    String dataToSend = " UAM2 추정 거리 (센): " + String(distance2);

    // HTTP 클라이언트 설정
    HTTPClient http;
    http.begin("http://127.0.0.1:5000");
    http.addHeader("Content-Type", "text/plain");

    // 데이터를 POST 요청으로 보냄
    int httpCode = http.POST(dataToSend);
    http.end();
  }

  
  delay(500); // 0.5초마다 RSSI 값을 갱신
}

void connectToWiFi(const char* ssid, const char* password) {
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    //Serial.println("Connecting to WiFi…");
  }
}

double calculateDistance(int rssi) {
  // 환경에 맞게 조정할 수 있는 상수들
  double A = 30;
  double n = 2.8;
  
  // RSSI를 거리로 변환하는 Log-distance path loss 모델 사용
  return pow(10, ((A - rssi) / (10 * n)))/10;
}
