package com.example.junk_app;

import android.content.Context;
import android.graphics.Bitmap;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class TFLiteWasteAnalyzer {
    private Interpreter interpreter;
    private static final int IMG_SIZE = 128;
    private static final String[] LABELS = {"e-waste", "glass", "metal", "organic", "others", "paper", "plastic"};

    public TFLiteWasteAnalyzer(Context context) throws IOException {
        interpreter = new Interpreter(loadModelFile(context, "modelv2.tflite"));
    }

    private MappedByteBuffer loadModelFile(Context context, String modelFile) throws IOException {
        FileInputStream inputStream = new FileInputStream(context.getAssets().openFd(modelFile).getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = context.getAssets().openFd(modelFile).getStartOffset();
        long declaredLength = context.getAssets().openFd(modelFile).getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    public PredictionResult analyze(Bitmap bitmap) {
        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, IMG_SIZE, IMG_SIZE, true);

        ByteBuffer inputBuffer = ByteBuffer.allocateDirect(1 * IMG_SIZE * IMG_SIZE * 3 * 4);
        inputBuffer.order(ByteOrder.nativeOrder());

        for (int y = 0; y < IMG_SIZE; y++) {
            for (int x = 0; x < IMG_SIZE; x++) {
                int pixel = scaled.getPixel(x, y);
                inputBuffer.putFloat(((pixel >> 16) & 0xFF) / 255.f); // R
                inputBuffer.putFloat(((pixel >> 8) & 0xFF) / 255.f);  // G
                inputBuffer.putFloat((pixel & 0xFF) / 255.f);         // B
            }
        }

        float[][] output = new float[1][LABELS.length];
        interpreter.run(inputBuffer, output);

        int maxIdx = 0;
        float maxVal = 0;
        for (int i = 0; i < LABELS.length; i++) {
            if (output[0][i] > maxVal) {
                maxVal = output[0][i];
                maxIdx = i;
            }
        }

        return new PredictionResult(LABELS[maxIdx], maxVal);
    }

    public static class PredictionResult {
        public final String label;
        public final float confidence;

        public PredictionResult(String label, float confidence) {
            this.label = label;
            this.confidence = confidence;
        }
    }
}
