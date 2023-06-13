import cv2

TRAIN_IMG_FOLDER = './raw_img/'


def get_train_img():
    """
    通过视频获得图片训练集
    :return:
    """
    face_detector = cv2.CascadeClassifier("./haarcascade_frontalface_alt.xml")
    video = cv2.VideoCapture("./video.mp4")

    success, frame = video.read()
    idx = 0
    while success:
        # 将每帧图片灰度化
        size = frame.shape[:2]
        image = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
        # 直方图均衡
        image = cv2.equalizeHist(image)
        im_h, im_w = size
        minSize_1 = (im_w // 10, im_h // 10)
        # 获得人脸识别结果
        # 导入分类器
        face = face_detector.detectMultiScale(image, 1.05, 2, cv2.CASCADE_SCALE_IMAGE, minSize_1)
        # 框入人脸，for循环用来实时刷新
        # for x, y, w, h in face:
        #     # x,y指横纵坐标，左上角开始，h为高，w为宽
        #     cv2.rectangle(frame, (x, y), (x + w, y + h), color=(0, 0, 225), thickness=2)

        cv2.imwrite(TRAIN_IMG_FOLDER + str(idx) + '.jpg', frame)
        idx = idx + 1
        success, frame = video.read()

    video.release()


if __name__ == '__main__':
    get_train_img()
