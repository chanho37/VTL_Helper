from flask import Flask, render_template
from threading import Thread
import serial
import pyrebase

app = Flask(__name__)

firebaseConfig = {
    "apiKey": "AIzaSyCqVu_yca-gu4Ej_Q1sNJnbqQxSFqTjpyI",
    "authDomain": "fir-8db45.firebaseapp.com",
    "databaseURL": "https://fir-8db45-default-rtdb.firebaseio.com",
    "projectId": "fir-8db45",
    "storageBucket": "fir-8db45.appspot.com",
    "messagingSenderId": "1033394850965",
    "appId": "1:1033394850965:web:1248da9cc7da68c8fbdf9c",
    "measurementId": "G-0QE4PVXQD6"
}

serial_port1 = '/dev/cu.usbserial-110'
serial_port2 = '/dev/cu.usbserial-140'

baud_rate = 115200
ser = None
max_display_count = 4  # 표시할 데이터 개수의 최댓값 설정
received_data = []  # 데이터를 저장할 리스트

def get_realtime_data():
    global ser, received_data
    try:
        ser = serial.Serial(serial_port1, baud_rate)
        while True:
            try:
                data = ser.readline().decode('utf-8', errors='replace').strip()
                print(f'Received data: {data}')
                received_data.append(data)  # 받아온 데이터를 리스트에 추가
                firebase=pyrebase.initialize_app(firebaseConfig) 

                db=firebase.database()
                data2={
                    "고도1 = ": data,
                    "고도2 = ": "3.4"
                }

                db.update(data2)
    
                if len(received_data) > max_display_count:
                    received_data.pop(0)  # 리스트의 처음 항목을 제거하여 개수 제한
            except UnicodeDecodeError as e:
                print(f'Failed to decode data: {e}')
    except serial.SerialException:
        print(f"Failed to open the serial port {serial_port1}")

def get_realtime_data2():
    global ser, received_data
    try:
        ser = serial.Serial(serial_port2, baud_rate)
        while True:
            try:
                data = ser.readline().decode('utf-8', errors='replace').strip()
                print(f'Received data: {data}')
                received_data.append(data)  # 받아온 데이터를 리스트에 추가
                if len(received_data) > max_display_count:
                    received_data.pop(0)  # 리스트의 처음 항목을 제거하여 개수 제한
            except UnicodeDecodeError as e:
                print(f'Failed to decode data: {e}')
    except serial.SerialException:
        print(f"Failed to open the serial port {serial_port2}")


data_thread = Thread(target=get_realtime_data)
#data_thread2 = Thread(target=get_realtime_data2)
data_thread.daemon = True
#data_thread2.daemon = True
data_thread.start()
#data_thread2.start()

@app.route('/')
def index():
    return render_template('index.html', received_data=received_data)

if __name__ == '__main__':
    app.run(debug=True)