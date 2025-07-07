require('dotenv').config();
const express = require('express');
const cors = require('cors');
const { ethers } = require('ethers');
const abi = require('./abi.json');

const app = express(); // ✅ Only ONE definition

app.use(cors());
app.use(express.json());

// 🔐 Connect to Alchemy RPC
const provider = new ethers.providers.JsonRpcProvider(process.env.RPC_URL);
const wallet = new ethers.Wallet(process.env.PRIVATE_KEY, provider);
const contract = new ethers.Contract(process.env.CONTRACT_ADDRESS, abi, wallet);

// 🧪 Simple test route
app.get("/", (req, res) => {
  res.send("✅ Backend is working!");
});

// 💸 Mint endpoint
app.post('/mint', async (req, res) => {
  const { to, amount } = req.body;

  if (!ethers.utils.isAddress(to)) {
    return res.status(400).send({ error: 'Invalid address' });
  }

  try {
    const tx = await contract.mint(to, ethers.utils.parseUnits(amount.toString(), 18));
    await tx.wait();
    res.send({ success: true, txHash: tx.hash });
  } catch (err) {
    console.error(err);
    res.status(500).send({ error: 'Minting failed', message: err.message });
  }
});

const PORT = process.env.PORT || 5000;
app.listen(PORT, () => console.log(`🚀 Backend running at http://localhost:${PORT}`));
