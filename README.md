# ♻️ Junk’d – AI-Driven Waste Sorting & Reward App (Offline-Ready)

**🚀 Team Name:** `vibe_coders`  
**🏁 Hackathon:** CodeStorm Hackathon 2025  
**🏷️ Category:** Social Impact · Sustainability · Smart Cities · AI  
**📱 Project Name:** `Junk’d`

---

## 📱 Overview

**Junk’d** is a gamified Android app that makes waste disposal **rewarding**.  
Users take a photo of their waste, and the app:

- Detects the **type of waste**
- Calculates a **score**
- Rewards users with **virtual Junk’d Coins**

All powered by a **Python Flask API** using a **TensorFlow Lite** model — fully runnable **offline** on the same device or network.

---

## 🌍 Problem

Even with increasing awareness, **waste segregation remains low**.  
Why? Because there’s no incentive or feedback.

Most people throw trash without knowing if it’s sorted or recyclable.  
**Junk’d** changes that by making waste disposal **fun**, **rewarding**, and **educational**.

---

## 🎯 Solution

With **Junk’d**, users simply:

1. 📸 Click a photo of their waste  
2. 🧠 The AI model predicts waste type, recyclability, and sorting accuracy  
3. 🪙 Users get a score and **Junk’d Coins** — instantly on the app

✅ Works offline (runs Flask & model locally)  
🚫 No need for cloud or external backend

---

## 🔑 Features

### 🤖 AI Waste Classification (Offline)
- Detects: **Plastic, Organic, Paper, Glass, Metal, e-Waste, Others**
- Powered by **TFLite model** (TensorFlow Lite)
- Served via local **Flask API** (`http://0.0.0.0:5000`)

### 🪙 Reward System – Junk’d Coins
- Coins awarded based on AI score
- Wallet shows balance (UI)
- Coin redemption flow under development (no backend yet)

### 🧠 Fully Offline AI Inference
- No Spring Boot needed
- Ideal for hackathons, schools, and disconnected environments

### 🧾 Result UI
- Waste type
- Properly sorted: ✅ / ❌  
- Recyclable: ✅ / ❌  
- Score bar + coins

---

## 💡 Why “Junk’d”?

Because your *junk* just earned you **eco-rewards**.  
We turn every disposal into a **mission for the planet**.  
> *Sort it. Snap it. Score it. Get Junk’d.* 🌱

---

## 🧩 Tech Stack

| Layer           | Technology                                  |
|-----------------|----------------------------------------------|
| Frontend (App)  | Android (Java + XML)                         |
| AI Inference    | Python + Flask + TensorFlow Lite (TFLite)    |
| Model           | Trained in Keras → Converted to `.tflite`    |
| Backend         | None (Fully Offline MVP)                     |
| Token System    | Local Junk’d Coins (Simulated UI wallet)     |

---

## 🧠 Architecture (MVP)

```plaintext
[Android App] ──(HTTP POST)──> [Flask API on localhost or LAN]
     ↓                                  ↓
  Image Upload                     TFLite Model
     ↓                                  ↓
View Result UI <─────── JSON: Waste Type, Score, Coins
✅ What’s Done
✅ Camera/Gallery image selection

✅ Local Flask AI API with image classification

✅ Android UI to display results and coin wallet

✅ Offline-ready for demo and deployment

🔜 What’s Next (Post-Hackathon)
📦 Move TFLite inference to on-device (Android)

🌐 Re-enable Spring Boot backend for:

User authentication

Leaderboards

Coin redemption history

📊 Admin dashboard

🔗 Polygon / off-chain token integration

💰 Monetization Potential
🛍 Sponsored rewards & eco-friendly brands

🏫 SaaS for hostels, societies, schools

📈 Waste analytics for municipalities/NGOs

👨‍💻 Team Contributions
Member	Contribution

Devansh Sharma – Led complete development of the Android app, integrated the Flask AI API, handled UI/UX, backend, and API logic.

Ayush – Built and trained the AI waste classification model using TensorFlow and optimized it for mobile using TFLite.

Divesh – Contributed by designing the project presentation (PPT) and helped during initial planning.


🧪 Sample AI Output
json
Copy
Edit
{
  "prediction": "Plastic",
  "confidence": 99.32
}
→ Mapped internally to:

json
Copy
Edit
{
  "wasteType": "Plastic",
  "sorted": true,
  "recyclableDetected": true,
  "score": 87
}
📦 API Endpoint
Endpoint	Method	Description
/predict	POST	Upload image and get waste classification

🌱 “Sort it. Snap it. Score it. Get Junk’d.”
Ready for offline demo.
No servers. No internet. Just clean waste, clean code, and cool coins.
