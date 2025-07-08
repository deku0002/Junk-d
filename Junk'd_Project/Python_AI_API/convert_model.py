import tensorflow as tf

print("📦 Loading model forcibly...")

model = tf.keras.models.load_model(
    "C:/Users/sharm/Desktop/Junk-d/prediction_model/model_compatible.h5",
    custom_objects={},  # Trick to force bypass
    compile=False
)

print("✅ Model loaded. Converting to TFLite...")

converter = tf.lite.TFLiteConverter.from_keras_model(model)
converter.optimizations = [tf.lite.Optimize.DEFAULT]

tflite_model = converter.convert()

output_path = "C:/Users/sharm/Desktop/Junk-d/prediction_model/modelv2.tflite"
with open(output_path, "wb") as f:
    f.write(tflite_model)

print(f"✅ Done! Saved at: {output_path}")
