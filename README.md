♻️ Junk’d – AI-Driven Waste Sorting & Reward App (Offline-Ready)
Team Name: vibe_coders
Category: Social Impact / Sustainability / Smart Cities / AI
Hackathon: CodeStorm Hackathon 2025
Project Name: Junk’d

📱 Overview
Junk’d is a gamified Android app that makes waste disposal rewarding. Using offline-capable AI, users just take a photo of their waste. The app classifies it in real-time, scores it, and rewards the user with virtual Junk’d Coins. All powered by an integrated Python-based AI model, accessible via a local Flask API.

🌍 Problem Statement
Despite increasing awareness, people often don’t sort their waste due to lack of motivation or tools. There’s little feedback or reward for doing the right thing, making proper segregation a challenge.

🎯 Our Solution
With Junk’d, users click a picture of their waste before disposing. Our Python + TensorFlow Lite model runs via a Flask server on the same device/network and analyzes the image to determine:

The type of waste

Whether it’s sorted properly

Whether it’s recyclable

Users get a score and virtual coins based on the result. Offline usage is possible by running the AI on the device without any cloud server dependency.

🔑 Key Features
📸 AI Waste Classification (Offline Flask)
TFLite model (TensorFlow Lite) detects:

Plastic, Organic, Paper, Glass, Metal, e-Waste, Others

Built-in Flask API running locally on 0.0.0.0:5000

Score calculation based on sorting & recyclability

🪙 Reward System (Junk’d Coins)
Coins awarded based on AI-determined score

Coins shown in wallet UI

Redeem flow under development (no backend required)

🧠 Fully Local Flask AI Inference
No Spring Boot or external server needed

Ideal for hackathon demo and offline use

🧾 Results UI
Waste type, score, recyclability, sorted status

User feedback on how to improve sorting

💡 Why "Junk’d"?
Because your junk just earned you rewards. Sustainability doesn’t have to be boring — we turn every disposal into a fun mission for the planet.

🧩 Tech Stack
Layer	Tech
Frontend (App)	Android (Java + XML)
AI Inference	Python + TensorFlow Lite + Flask (Local)
Model	Trained with Keras → Converted to TFLite
Reward System	Local wallet coins (no backend)

🧠 Architecture (Updated MVP)
plaintext
Copy
Edit
[Android App] ──(HTTP POST)──> [Flask API on localhost or LAN]
     ↓                                  ↓
  Image Upload                     TFLite Model
     ↓                                  ↓
View Result UI <─────── JSON: Waste Type, Score, Coins
✅ Current Progress (MVP Complete)
✅ Android app UI with camera/gallery input

✅ Local Flask API with working AI model

✅ JSON response mapped to UI

✅ Scoring, coin logic, and waste detection fully working

✅ Offline-ready (Flask + model run locally)

🔜 Post-Hackathon Roadmap
🌐 Replace Flask with on-device TensorFlow Lite for true offline AI

☁️ Reintegrate Spring Boot backend for:

User profiles

Leaderboards

Coin redemption

📊 Admin dashboard for waste insights

🏆 Impact Goals
Encourage responsible waste sorting at source

Reward citizens for making eco-conscious choices

Support gamified sustainability for students and urban users

💰 Monetization (Future Scope)
Sponsored coupons for eco-products

Marketplace for verified recyclers

SaaS dashboard for hostels/schools/societies

👨‍💻 Team Members
Devansh Sharma – Led complete development of the Android app, integrated the Flask AI API, handled UI/UX, backend, and API logic.

Ayush – Built and trained the AI waste classification model using TensorFlow and optimized it for mobile using TFLite.

Divesh – Contributed by designing the project presentation (PPT) and helped during initial planning.

🧪 Sample AI Output (Live Demo)
json
Copy
Edit
{
  "prediction": "Plastic",
  "confidence": 99.32
}
→ Internally mapped to:
✅ wasteType: Plastic
✅ sorted: true
✅ recyclableDetected: true
✅ score: 87

📦 Sample Endpoint (Current Live Flask API)
Endpoint	Method	Description
/predict	POST	Upload image, get classification result

“Sort it. Snap it. Score it. Get Junk’d.” 🌱
Ready for demo without external servers. Offline. Fast. Rewarding.
