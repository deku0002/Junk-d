from flask import Flask, request, jsonify
from flask_cors import CORS
import base64
import io
import numpy as np
from PIL import Image
import logging
import random
import os

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

app = Flask(__name__)
CORS(app)

# Mock AI Model for demo (replace with actual TensorFlow/PyTorch model)
class WasteClassifier:
    def __init__(self):
        self.waste_types = ['Plastic', 'Paper', 'Organic', 'Metal', 'Glass', 'E-waste', 'Mixed']
        self.model_loaded = True
        logger.info("🤖 Waste Classifier initialized")
    
    def analyze_image(self, image_array):
        """
        Mock analysis function - replace with actual AI model inference
        """
        try:
            # Simulate processing time
            import time
            time.sleep(0.5)
            
            # Mock analysis based on image characteristics
            height, width = image_array.shape[:2]
            
            # Generate realistic predictions
            waste_type = random.choice(self.waste_types)
            
            # Simulate sorting accuracy based on image "quality"
            image_clarity = min(height, width) / 100
            sorted_probability = min(0.9, max(0.3, image_clarity / 10))
            sorted = random.random() < sorted_probability
            
            # Recyclable detection
            recyclable_types = ['Plastic', 'Paper', 'Metal', 'Glass']
            recyclable = waste_type in recyclable_types and random.random() > 0.2
            
            # Calculate score
            base_score = random.randint(60, 95)
            if sorted:
                base_score += 10
            if recyclable:
                base_score += 5
            
            score = min(100, base_score)
            
            return {
                'wasteType': waste_type,
                'sorted': sorted,
                'recyclableDetected': recyclable,
                'score': score,
                'confidence': round(random.uniform(0.75, 0.95), 2),
                'analysis_time': 0.5
            }
            
        except Exception as e:
            logger.error(f"Error in image analysis: {str(e)}")
            raise

# Initialize the classifier
classifier = WasteClassifier()

@app.route('/health', methods=['GET'])
def health_check():
    """Health check endpoint"""
    return jsonify({
        'status': 'healthy',
        'service': 'Junk\'d AI Analysis API',
        'model_loaded': classifier.model_loaded,
        'timestamp': np.datetime64('now').astype(str)
    })

@app.route('/analyze', methods=['POST'])
def analyze_waste():
    """Main waste analysis endpoint"""
    try:
        logger.info("📷 Received waste analysis request")
        
        # Get JSON data
        data = request.get_json()
        
        if not data or 'image_data' not in data:
            return jsonify({
                'success': False,
                'error': 'No image data provided'
            }), 400
        
        # Decode base64 image
        image_data = data['image_data']
        user_id = data.get('user_id', 'anonymous')
        
        # Process image
        try:
            # Remove data URL prefix if present
            if ',' in image_data:
                image_data = image_data.split(',')[1]
            
            # Decode base64
            image_bytes = base64.b64decode(image_data)
            image = Image.open(io.BytesIO(image_bytes))
            
            # Convert to numpy array
            image_array = np.array(image)
            
            logger.info(f"🖼️ Image processed: {image_array.shape}")
            
        except Exception as e:
            logger.error(f"Error processing image: {str(e)}")
            return jsonify({
                'success': False,
                'error': f'Invalid image data: {str(e)}'
            }), 400
        
        # Analyze with AI model
        try:
            analysis_result = classifier.analyze_image(image_array)
            
            # Prepare response
            response = {
                'success': True,
                'wasteType': analysis_result['wasteType'],
                'sorted': analysis_result['sorted'],
                'recyclableDetected': analysis_result['recyclableDetected'],
                'score': analysis_result['score'],
                'confidence': analysis_result['confidence'],
                'user_id': user_id,
                'timestamp': np.datetime64('now').astype(str),
                'analysis_time': analysis_result['analysis_time']
            }
            
            logger.info(f"✅ Analysis completed: {response}")
            return jsonify(response)
            
        except Exception as e:
            logger.error(f"Error in AI analysis: {str(e)}")
            return jsonify({
                'success': False,
                'error': f'Analysis failed: {str(e)}'
            }), 500
            
    except Exception as e:
        logger.error(f"Unexpected error: {str(e)}")
        return jsonify({
            'success': False,
            'error': f'Internal server error: {str(e)}'
        }), 500

@app.route('/demo', methods=['GET'])
def demo_analysis():
    """Demo endpoint for testing without image"""
    logger.info("🎭 Demo analysis requested")
    
    # Generate demo response
    demo_result = {
        'success': True,
        'wasteType': random.choice(['Plastic', 'Paper', 'Organic']),
        'sorted': random.choice([True, False]),
        'recyclableDetected': random.choice([True, False]),
        'score': random.randint(70, 95),
        'confidence': round(random.uniform(0.8, 0.95), 2),
        'user_id': 'demo_user',
        'timestamp': np.datetime64('now').astype(str),
        'analysis_time': 0.1
    }
    
    return jsonify(demo_result)

@app.route('/model/info', methods=['GET'])
def model_info():
    """Get model information"""
    return jsonify({
        'model_name': 'Junk\'d Waste Classifier v1.0',
        'supported_types': classifier.waste_types,
        'model_loaded': classifier.model_loaded,
        'version': '1.0.0',
        'features': [
            'Waste type classification',
            'Sorting quality assessment',
            'Recyclable material detection',
            'Scoring system'
        ]
    })

@app.errorhandler(404)
def not_found(error):
    return jsonify({
        'success': False,
        'error': 'Endpoint not found'
    }), 404

@app.errorhandler(500)
def internal_error(error):
    return jsonify({
        'success': False,
        'error': 'Internal server error'
    }), 500

if __name__ == '__main__':
    logger.info("🚀 Starting Junk'd AI Analysis API...")
    logger.info("🔬 Model ready for waste analysis!")
    
    # Run the app
    app.run(
        host='0.0.0.0',
        port=5000,
        debug=True
    )