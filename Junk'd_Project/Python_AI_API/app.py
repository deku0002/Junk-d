import tensorflow as tf
import numpy as np
from flask import Flask, request, jsonify
from PIL import Image
import io

# Load TFLite model
interpreter = tf.lite.Interpreter(model_path="C:/Users/sharm/Desktop/Junk-d/prediction_model/model_tf213_v3.tflite")
interpreter.allocate_tensors()
input_details = interpreter.get_input_details()
output_details = interpreter.get_output_details()

print("🟢 Model loaded successfully!")

app = Flask(__name__)

# Your actual model classes
LABELS = ['e-waste', 'glass', 'metal', 'organic', 'others', 'paper', 'plastic']

# Simulate a coin system per session (resets on server restart)
user_coins = 0

def preprocess_image(image_bytes):
    img = Image.open(io.BytesIO(image_bytes)).convert('RGB')
    img = img.resize((128, 128))
    img_array = np.array(img, dtype=np.float32) / 255.0
    img_array = np.expand_dims(img_array, axis=0)
    return img_array

@app.route('/predict', methods=['POST'])
def predict():
    if 'file' not in request.files:
        return jsonify({'error': 'No file part'}), 400

    file = request.files['file']
    if file.filename == '':
        return jsonify({'error': 'No selected file'}), 400

    try:
        img_array = preprocess_image(file.read())
        interpreter.set_tensor(input_details[0]['index'], img_array)
        interpreter.invoke()
        output_data = interpreter.get_tensor(output_details[0]['index'])[0]

        predicted_index = int(np.argmax(output_data))
        confidence = float(output_data[predicted_index])
        predicted_label = LABELS[predicted_index]

        # Calculate score and coins
        score = int(confidence * 100)
        coins_earned = 5 if score > 60 else 2
        global user_coins
        user_coins += coins_earned

        # Mock logic for sorted & recyclable
        recyclable = predicted_label in ['paper', 'plastic', 'glass', 'metal']
        sorted_flag = True  # Assume it's always sorted correctly for now

        return jsonify({
            "wasteType": predicted_label,
            "sorted": sorted_flag,
            "recyclableDetected": recyclable,
            "score": score,
            "message": "Waste analyzed successfully",
            "coinsEarned": coins_earned,
            "totalCoins": user_coins,
            "ranking": np.random.randint(1, 100),  # Fake rank for now
            "success": True
        })

    except Exception as e:
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)
