import requests

url = 'http://127.0.0.1:5000/predict'
image_path = r'C:\Users\sharm\Desktop\test.jpg'  # Replace with an actual image path

with open(image_path, 'rb') as img_file:
    files = {'file': img_file}
    response = requests.post(url, files=files)

print("🔁 Server response:", response.status_code)
print(response.json())
