from flask import Flask, render_template
from threading import Thread
import serial
import pyrebase

app = Flask(__name__)

firebaseConfig = {
    "apiKey": "AIzaSyDq6CUVifzNrN7Ln6ITV0EEWclzuWPwIMs",
    "authDomain": "uam-user.firebaseapp.com",
    "databaseURL": "https://uam-user-default-rtdb.firebaseio.com",
    "projectId": "uam-user",
    "storageBucket": "uam-user.appspot.com",
    "messagingSenderId": "96701696328",
    "appId": "1:96701696328:web:9e53820e954a4d61d08b7d",
    "measurementId": "G-M9HHVEQ00J"
}

serial_port1 = '/dev/cu.usbserial-110'
serial_port2 = '/dev/cu.usbserial-120'
serial_port3 = '/dev/cu.usbserial-130'

baud_rate = 115200

ser1 = None
ser2 = None
ser3 = None

max_display_count = 1 # 표시할 데이터 개수의 최댓값 설정
max_display_count2 = 1  # 표시할 데이터 개수의 최댓값 설정
max_display_count3 = 1  # 표시할 데이터 개수의 최댓값 설정

received_data1 = []  # 데이터를 저장할 리스트
received_data2 = []  # 데이터를 저장할 리스트
received_data3 = []  # 데이터를 저장할 리스트


def get_realtime_data():
    global ser1, ser2, ser3, received_data1, received_data2, received_data3
    try:
        ser1 = serial.Serial(serial_port1, baud_rate)
        ser2 = serial.Serial(serial_port2, baud_rate)
        ser3 = serial.Serial(serial_port3, baud_rate)
        while True:
            try:
                data1 = ser1.readline().decode('utf-8', errors='replace').strip()
                data2 = ser2.readline().decode('utf-8', errors='replace').strip()
                data3 = ser3.readline().decode('utf-8', errors='replace').strip()
                
                print(f'Received data dis: {data1}')
                print(f'Received data alt1: {data2}')
                print(f'Received data alt2: {data3}')

                received_data1.append(data1)  # 받아온 데이터를 리스트에 추가
                received_data2.append(data2)  # 받아온 데이터를 리스트에 추가
                received_data3.append(data3)  # 받아온 데이터를 리스트에 추가

                input_string = received_data1[0].split()

                num1 = input_string[0]
                num2 = input_string[1]

                dis1 = float(num1)
                dis2 = float(num2)
                
                alt1 = float(data2)
                alt2 = float(data3)

                cal1 = dis1*dis1 + alt1*alt1
                cal2 = dis2*dis2 + alt2*alt2

                cal1 = round(cal1, 4)
                cal2 = round(cal2, 4)

                cal1 = str(cal1)
                cal2 = str(cal2)
                
                firebase=pyrebase.initialize_app(firebaseConfig) 

                db=firebase.database()
                data_push={
                    "dis1": num1,
                    "dis2": num2,
                    "alt1": data2,
                    "alt2": data3,
                    "UAM1": cal1,
                    "UAM2": cal2
                }

                db.update(data_push)
    
                if len(received_data1) > max_display_count:
                    received_data1.pop(0)  # 리스트의 처음 항목을 제거하여 개수 제한
                if len(received_data2) > max_display_count2:
                    received_data2.pop(0)  # 리스트의 처음 항목을 제거하여 개수 제한
                if len(received_data3) > max_display_count3:
                    received_data3.pop(0)  # 리스트의 처음 항목을 제거하여 개수 제한
            except UnicodeDecodeError as e:
                print(f'Failed to decode data: {e}')
    except serial.SerialException:
        print(f"Failed to open the serial port {serial_port1}")





data_thread = Thread(target=get_realtime_data)
data_thread.daemon = True
data_thread.start()

@app.route('/')
def index():
    return render_template('index.html', num1=received_data1, num2=received_data2, num3=received_data3)

if __name__ == '__main__':
    app.run(debug=True)
