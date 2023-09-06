# cloudwave-team1-springboot-app
클라우드웨이브 1팀 심화 프로젝트 api서버 + 포스기서버 코드

<br>

## Service Flow
<img src="https://private-user-images.githubusercontent.com/48792230/265862995-9472cbdf-870d-40e1-accb-01ffaaa13061.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTEiLCJleHAiOjE2OTM5NjM1MDAsIm5iZiI6MTY5Mzk2MzIwMCwicGF0aCI6Ii80ODc5MjIzMC8yNjU4NjI5OTUtOTQ3MmNiZGYtODcwZC00MGUxLWFjY2ItMDFmZmFhYTEzMDYxLnBuZz9YLUFtei1BbGdvcml0aG09QVdTNC1ITUFDLVNIQTI1NiZYLUFtei1DcmVkZW50aWFsPUFLSUFJV05KWUFYNENTVkVINTNBJTJGMjAyMzA5MDYlMkZ1cy1lYXN0LTElMkZzMyUyRmF3czRfcmVxdWVzdCZYLUFtei1EYXRlPTIwMjMwOTA2VDAxMjAwMFomWC1BbXotRXhwaXJlcz0zMDAmWC1BbXotU2lnbmF0dXJlPTUyNDZmMDAyZTVjMDJmODE0MmE4Yzc0MWZhOGZmY2MyZjdlOTdlYjYwNmJlY2NiMmE3NzBhZDEwYzY4NzljNzImWC1BbXotU2lnbmVkSGVhZGVycz1ob3N0JmFjdG9yX2lkPTAma2V5X2lkPTAmcmVwb19pZD0wIn0.xpj2hLLk5yJULZFV034ptwRFFfNWm4y0UBB0oKjlEJA" width="600" />

- 관리자 포스기 프로그램을 웹 서비스로 대체
- 관리자가 주문 요청을 확인하고 처리를 완료할 수 있음
- AWS SQS를 사용하여 주문 큐를 구성
- AWS SNS, Firebase Cloud Messaging를 사용하여 사용자의 모바일 기기에 푸시 알림 전송
- api 서버 + 포스기 서버 분리하지 않고 코드 작성
- 로그인은 AWS Cognito에서 처리

<br>

## API List
| REST | 요청 경로                  | 설명                                                    |
|------|---------------------------|--------------------------------------------------------|
| POST | /register/token           | 사용자가 로그인 요청 시 매개변수로 받은 FCM Token을 통해 SNS Endpoint ARN를 발급 후 사용자 정보 저장 |
| POST | /api/orders               | DB에 주문 정보 저장 및 SQS 큐 대기열에 메시지 추가       |
| GET  | /api/messages             | SQS 큐 대기열에 있는 메시지 조회                         |
| POST | /api/messages/completeAndNotify | 모바일 푸시 알림 전송 후 SQS 큐 대기열에서 삭제     |

<br>

## 포스기 UI
<img src="https://private-user-images.githubusercontent.com/48792230/265865363-3106e445-51b0-4dfb-81ce-a97d17baefa8.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTEiLCJleHAiOjE2OTM5NjQ1NDksIm5iZiI6MTY5Mzk2NDI0OSwicGF0aCI6Ii80ODc5MjIzMC8yNjU4NjUzNjMtMzEwNmU0NDUtNTFiMC00ZGZiLTgxY2UtYTk3ZDE3YmFlZmE4LnBuZz9YLUFtei1BbGdvcml0aG09QVdTNC1ITUFDLVNIQTI1NiZYLUFtei1DcmVkZW50aWFsPUFLSUFJV05KWUFYNENTVkVINTNBJTJGMjAyMzA5MDYlMkZ1cy1lYXN0LTElMkZzMyUyRmF3czRfcmVxdWVzdCZYLUFtei1EYXRlPTIwMjMwOTA2VDAxMzcyOVomWC1BbXotRXhwaXJlcz0zMDAmWC1BbXotU2lnbmF0dXJlPWFhMDk2YzUwMjYyMGNmOTE4NDdlYTkxODJhYmVkYTFhYWZkOTA5YmM3YWViZmRhNTUzMTlhOWU4MTU3NDcyMDYmWC1BbXotU2lnbmVkSGVhZGVycz1ob3N0JmFjdG9yX2lkPTAma2V5X2lkPTAmcmVwb19pZD0wIn0.qUQc77lEhMGB2epLHkNv9nNIL7D6fbKD9nQGuqMn3Ws"  />
