# HTTP/HTTPS

- HTTP와 HTTPS는 웹 상에서 데이터를 교환하기 위한 프로토콜.
- 두 프로토콜의 주요 차이점은 데이터 전송의 보안 수준에 있다.

## HTTP(HyperText Transfer Protocol)

HTTP는 웹 브라우저와 서버 간의 문서, 이미지 비디오 등 다양한 자원을 주고받을 수 있는 애플리케이션 계층의 프로토콜이다.

- 클라이언트(브라우저)가 서버에 요청을 보내면, 서버는 해당 요청에 따라 적절한 응답을 반환한다.
- HTTP는 기본적으로 평문(암호화되지 않은 텍스트)으로 데이터를 전송하기 때문에, 중간에 공격자가 데이터를 가로채거나 조작할 수 있는 위험이 있다.

## HTTPS(HyperText Transfer Protocol Secure)

HTTPS는 HTTP에 보안 계층을 추가한 프로토콜로, 웹 브라우저와 서버 간의 통신을 암호화하여 중간자 공격을 방지한다.
- HTTPS는 SSL(Secure Sockets Layer) 또는 TLS(Transport Layer Security)와 같은 암호화 프로토콜을 사용하여 데이터를 전송한다.
- 이로 인해 사용자의 정보가 안전하게 보호된다.

## 요약

HTTP는 웹 상에서 데이터를 교환하기 위한 기본 프로토콜이며, HTTPS는 HTTP에 보안 기능을 추가한 버전이다.