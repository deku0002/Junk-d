@echo off
REM Update your image path here
set IMAGE=C:\Users\sharm\Desktop\test.jpg

REM Call the upload API
curl -X POST http://localhost:8080/api/waste/analyze/upload -F "image=@%IMAGE%" -F "userId=test_user"

pause
