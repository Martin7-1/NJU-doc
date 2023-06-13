import face_recognition

# Load image and detect faces
image = face_recognition.load_image_file("./140-1.jpg")
face_locations = face_recognition.face_locations(image)
face_location = face_locations[0]
face_encodings = face_recognition.face_encodings(image, [face_location])
known_face_encoding = face_encodings[0]

predict_img = face_recognition.load_image_file("./309.jpg")
unknown_face_encodings = face_recognition.face_encodings(predict_img)

for unknown_face_encoding in unknown_face_encodings:
    matches = face_recognition.compare_faces(
        [known_face_encoding],
        unknown_face_encoding,  # The single unknown face encoding
        tolerance=0.4
    )
    print(matches)
