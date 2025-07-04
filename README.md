# ♻️ Junk’d – Gamified Waste Sorting & Recycling App

**Team Name:** vibe_coders  
**Category:** Sustainability / Smart Cities / AI / Web3  
**Hackathon:** Hackathon 2025  
**Project Name:** Junk’d

---

## 📱 Overview

**Junk’d** is a gamified waste management app that transforms everyday trash disposal into a fun, rewarding, and competitive challenge. With AI-driven waste scoring and real-time rankings, users are rewarded for recycling and proper waste sorting using nothing but a photo of their waste.

---

## 🌍 Problem Statement

Despite growing awareness, waste segregation and recycling remain low due to a lack of incentives. Most people dispose of trash without considering its impact, and traditional methods of enforcement or education are ineffective and unengaging.

---

## 🎯 Our Solution

Junk’d leverages AI and gamification to make recycling exciting. Users click a **photo of their waste** before disposal. A **Python-based AI model** analyzes the image to determine how well the waste is sorted and how much is recyclable. Based on this, users are rewarded with **Junk’d Coins**, which can be redeemed for real-life perks. Leaderboards make it a fun competition.

---

## 🔑 Key Features

### 📸 AI Waste Scoring (Python)
- Built using Python (TensorFlow/Keras), hosted via a Flask API
- Detects waste type (plastic, organic, e-waste, mixed, etc.)
- Assigns a score based on:
  - Waste being correctly sorted
  - Presence of recyclable materials

### 🪙 Junk’d Coin Reward System
- Higher scores earn more **Junk’d Coins**
- Coins can be used to redeem:
  - Coupons & brand discounts
  - Sustainable products
  - Donation credits for NGOs

### 🏆 Leaderboards & Challenges
- Weekly/monthly leaderboards to rank top recyclers
- Compete individually or as part of schools/hostels/societies
- Badges and bonuses for consistent performers

### 📊 Smart Waste Analytics
- Track personal eco-impact: total waste sorted, CO2 saved
- Admins and city officials can view analytics at scale

---

## 💡 Why "Junk’d"?

Because your *junk* just earned you real-world rewards. Every correctly sorted image is a step toward saving the planet — and showing off your **eco-drip**.

---

## 🧩 Tech Stack

| Layer         | Tech                                      |
|---------------|-------------------------------------------|
| Frontend      | React Native (Android/iOS App)            |
| Backend       | Java (Spring Boot)                        |
| Database      | MongoDB / MySQL                           |
| AI Module     | Python (Flask API + TensorFlow/Keras)     |
| Token System  | Junk’d Coins (off-chain or via Polygon)   |
| Deployment    | Docker, Firebase Hosting (optional)       |

---

## 🧠 Architecture Overview

```plaintext
[Mobile App] → [Java Backend API] → [Python Flask AI API]
     ↓                 ↓                    ↓
  Upload Image    Process Score       Classify Waste
     ↓                                   ↓
 View Coins / Rank / Impact        Return Score JSON
🛣 Roadmap
✅ Phase 1: MVP (Hackathon)
 React Native mobile app

 Image upload & AI integration

 Flask-based image classification API (Python)

 Java backend for user profiles, scores, coins

 Leaderboard logic

 Rewards redemption flow

🔜 Phase 2: Post-Hackathon
 Smart Bin integration (IoT cameras + sensors)

 Advanced image classification (contamination detection)

 Full blockchain-based coin system (Polygon)

 City-level deployment partnerships

 Achievements & NFT eco-badges

🏆 Impact Goals
Promote eco-friendly behavior through rewards

Encourage better waste sorting at the source

Support local recycling efforts with clean waste streams

Educate and gamify sustainability for youth and citizens

💰 Monetization Potential
🛍 Brand-sponsored rewards and coupons

🏫 SaaS platform for hostels, schools, societies

📈 Waste data insights for municipalities and NGOs

♻️ Marketplace for local recyclers to connect with users

👨‍💻 Team Members
Devansh – Java Backend & API Integration

Ayush – Python AI Model + Flask API

Divesh – Junk’d Coin Logic & Leaderboard System

🧠 Inspiration
Recycling should feel empowering, not boring. Junk’d inspires users to do the right thing by turning sustainability into a game. Real impact, real rewards.

“Sort it. Snap it. Score it. Get Junk’d.” 🌱

🧪 AI Model Output Example
json
Copy
Edit
{
  "wasteType": "Plastic",
  "sorted": true,
  "recyclableDetected": true,
  "score": 87
}
📦 Sample API Endpoints
Endpoint	Method	Description
/submitWasteImage	POST	Upload image and receive score
/getUserScore	GET	Get total user score & coins
/getLeaderboard	GET	View top recyclers
/redeemReward	POST	Redeem coins for rewards

---
