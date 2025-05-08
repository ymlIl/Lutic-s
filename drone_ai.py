import cv2
from ultralytics import YOLO
import os
import socket
import struct
import threading
import time

def reverse_bytes(data):
    return data[::-1]

def udp_client():
    client_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

    # Адрес и порт сервера
    ipRasp = '10.12.34.2'
    ipDec = '192.168.137.2'
    server_address = (ipRasp, 8080)

    try:
        while True:              
            if str(box1) == string:
                user_input = (str(0.5) + ',' + str(0.5))
            else:
                user_input = (str(box1)[9:15] + ',' + str(box1)[17:23])
            time.sleep(0.1)
                                
            if user_input.lower() == 'exit':
                print("Завершение работы клиента.")
                break

            try:
                # Разделяем ввод по запятым и преобразуем в числа
                numbers = [float(x.strip()) for x in user_input.split(',')]
                
                # Упаковываем числа и булево значение isDrone в байты
                packed_data = struct.pack(f'{len(numbers)}f?', *numbers, isDrone)
                
                # Переворачиваем байты
                reversed_packed_data = reverse_bytes(packed_data)
                
                client_socket.sendto(reversed_packed_data, server_address)
                
                print(f"Отправлены числа: {', '.join(map(str, numbers))}, isDrone: {isDrone}")
                print(f"Отправленные байты (в обратном порядке): {reversed_packed_data.hex()}")

            except ValueError:
                print("Ошибка: Введите корректные числа, разделенные запятыми.")
            
            # data, server = client_socket.recvfrom(1024)
            # reversed_data = reverse_bytes(data)
            # received_numbers = struct.unpack(f'{len(data)//4}f', reversed_data)
            # print(f"Ответ от сервера: {', '.join(map(str, received_numbers))}")

    except Exception as e:
        print(f"Произошла ошибка: {e}")

    finally:
        # Закрываем сокет
        client_socket.close()

def reverse_bytes(data):
    return data[::-1]

udp_thread = threading.Thread(target=udp_client)
udp_thread.daemon = True
count = 0

model = YOLO("best558.pt")
model.conf = 0.75
#torch.device = 0
#model.predict(imgsz=640, conf=0.7)

camera_url = 'rtsp://admin:admin@192.168.1.1:554/0'
cap1 = cv2.VideoCapture(1)
# cap2 = cv2.VideoCapture(2)

cv2.namedWindow("YOLO Inference", cv2.WINDOW_NORMAL)
# cv2.namedWindow("YOLO Inference2", cv2.WINDOW_NORMAL)

threshold_confidence = 0.75
string = "tensor([], device='cuda:0', size=(0, 4))"
isDrone = False

while cap1.isOpened():
    success1, frame1 = cap1.read()
    # success2, frame2 = cap2.read()

    if success1:
        results1 = model(frame1, conf=threshold_confidence, verbose = False)
        # results2 = model(frame2, conf=threshold_confidence)

        if len(results1) > 0 and len(results1[0]) > 0:
            classes = results1[0].boxes.cls.cpu().numpy()
            drone_class_id = 0
            isDrone = drone_class_id in classes
        else:
            isDrone = False
        
        box1 = results1[0].boxes.xywhn
        if count == 0:
            udp_thread.start()
            count += 1

        annotated_frame1 = results1[0].plot()
        # annotated_frame2 = results2[0].plot()


        cv2.imshow("YOLO Inference", annotated_frame1)
        # cv2.imshow("YOLO Inference2", annotated_frame2)

        if cv2.waitKey(1) & 0xFF == ord("q"):
            break
    else:
        break

#file.close()

cap1.release()
# cap2.release()
cv2.destroyAllWindows()