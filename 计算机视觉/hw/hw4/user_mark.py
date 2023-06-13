import cv2
import face_recognition
import numpy as np

video = cv2.VideoCapture("./video.mp4")
# 获取opencv的分类器——人眼识别和人脸识别
face_Cascade = cv2.CascadeClassifier("./haarcascade_frontalface_alt.xml")

width = int(video.get(cv2.CAP_PROP_FRAME_WIDTH))
height = int(video.get(cv2.CAP_PROP_FRAME_HEIGHT))
fps = video.get(cv2.CAP_PROP_FPS)
print(fps)
font = cv2.FONT_HERSHEY_SIMPLEX
fourcc = cv2.VideoWriter_fourcc('m', 'p', '4', 'v')
outVideo = cv2.VideoWriter('./new_video.mp4', fourcc, fps, (width, height))

success, frame = video.read()
match_faces = {}

while success:
    # 将每帧图片灰度化
    size = frame.shape[:2]
    image = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    # 直方图均衡
    image = cv2.equalizeHist(image)
    im_h, im_w = size
    minSize_1 = (im_w // 10, im_h // 10)
    # 获得人脸识别结果
    face_rect_results = face_Cascade.detectMultiScale(image, 1.05, 2, cv2.CASCADE_SCALE_IMAGE, minSize_1)
    if len(face_rect_results) > 0:
        # 绘制当前帧结果
        for x, y, w, h in face_rect_results:
            rgb_frame = np.ascontiguousarray(frame[y:y + h, x:x + w, ::-1])
            cv2.imwrite("./data/" + str(x) + "," + str(y) + ".jpg", rgb_frame)

            face_locations = face_recognition.face_locations(rgb_frame)
            if len(face_locations) > 0:
                # 这里其实 face_encodings 的 len 为 1
                face_encodings = face_recognition.face_encodings(rgb_frame, face_locations)
                if len(face_encodings) != 1:
                    print("face encodings length=" + str(len(face_encodings)))
                if len(face_encodings) > 0:
                    if len(match_faces) == 0:
                        user_idx = 1
                        for face_encoding in face_encodings:
                            match_faces[user_idx] = face_encoding
                            user_idx = user_idx + 1
                    else:
                        for face_encoding in face_encodings:
                            is_find = False
                            for key, value in match_faces.items():
                                matches = face_recognition.compare_faces(
                                    [value],
                                    face_encoding,  # The single unknown face encoding
                                    tolerance=0.4
                                )
                                if True in matches:
                                    cv2.putText(frame, 'User' + str(key), (x + 10, y + h), font, 5, (255, 255, 255), thickness=4)
                                    is_find = True
                                    break
                            if not is_find:
                                # 加入字典作为新的人
                                user_idx = len(match_faces) + 1
                                match_faces[user_idx] = face_encoding

            # 绘制人脸框
            cv2.rectangle(frame, (x, y), (x + w, y + h), [255, 255, 0], 2)

    # 输出每一帧结果
    outVideo.write(frame)
    last_frame = frame
    success, frame = video.read()

video.release()
outVideo.release()
