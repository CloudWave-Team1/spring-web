# cloudwave-team1-springboot-app
클라우드웨이브 1팀 심화 프로젝트 api서버 + 포스기서버 코드

<br>

## Service Flow
<img src="https://user-images.githubusercontent.com/48792230/267191521-1ef2002e-da9d-45a8-b9c9-d48491904430.png" width="600" />

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
<img src="https://user-images.githubusercontent.com/48792230/267191494-c33c54b2-54e2-47d7-92c9-4f5199f5adc0.png"  />
