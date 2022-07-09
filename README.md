![로고_수정](https://user-images.githubusercontent.com/67117391/169956300-8825858a-6a99-4aed-986c-03b1d65c1c97.png)

# 리뷰의 신뢰도를 평가하는 검색어플 Truri
### 숙명여자대학교 2022년도 상반기 졸업프로젝트 발표작

국주현(AI), 박재은(Fullstack), 이지수(Fullstack)





# 사용 기술
1. Frontend   <img src="https://img.shields.io/badge/Android Java-3DDC84?style=for-the-badge&logo=Android&logoColor=white">
2. Backend   <img src="https://img.shields.io/badge/Flask-000000?style=for-the-badge&logo=Flask&logoColor=white"> <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=white">
3. Database   <img src="https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=MariaDB&logoColor=white">
4. Crawling   <img src="https://img.shields.io/badge/Scrapy-E5422B?style=for-the-badge&logo=Scrapy&logoColor=white">
5. AI   <img src="https://img.shields.io/badge/Tensorflow-FF6F00?style=for-the-badge&logo=Tensorflow&logoColor=white"> <img src="https://img.shields.io/badge/Keras-D00000?style=for-the-badge&logo=Keras&logoColor=white"> <img src="https://img.shields.io/badge/Konlpy-D40000?style=for-the-badge&logo=Konlpy&logoColor=white">
6. Deployment <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=Docker&logoColor=white"> <img src="https://img.shields.io/badge/AWS EC2-232F3E?style=for-the-badge&logo=Amazon AWS&logoColor=white"> 





# 소개
맛집, 상품 등을 검색 시 홍보성 글을 자동으로 필터링하여 사용자들에게 신뢰성 있는 검색 결과를 제공하는 어플


![검색창](https://user-images.githubusercontent.com/67117391/169959688-d5a55fc7-b69b-469a-beef-acb4b9fd1011.png)

###### 소개영상 : https://youtu.be/CR8MJsR97HU

블로그 리뷰를 검색하는 소비자들이 홍보성 리뷰를 쉽게 구분할 수 있도록 신뢰도가 표시된 검색 결과를 보여주는 어플리케이션을 개발하였습니다.
안드로이드 스튜디오 개발 환경에서 SpringBoot와 MariaDB, Flask로 서버를 구현하고, 머신러닝 모델로 신뢰도를 판단합니다.



#데이터 수집
네이버 블로그의 공감 수, 댓글 수, 블로거의 이웃 수, 전체 게시글 수, 게시글, 본문 마지막 이미지 2장



# 데이터 라밸링 근거
‘광고법 제정 이후의 게시글’은 업체로부터 지원금, 광고 부탁을 받은 경우에는 무조건 광고임을 표시하는 문구를 본문에 표시해야 합니다.
위와 같은 근거로 광고법 제정 이후의 데이터를 수집하였는데, 홍보성 문구(EX: 제공 받았지만, 제공받았지만, 제공 받을 수, 제공 받았습니다, 서포터즈, 파트너스 등등)와 블로그에 홍보성 문구가 표기된 이미지들을 참고하여 광고성을 띌 경우에는 ‘1’, 광고성을 안 띌 경우 ‘0’ 으로 라밸링하여 진행하였습니다.




# 핵심 기능

- 검색어를 입력 시 네이버 블로그에서 **실시간으로 리뷰 크롤링**
- 크롤링된 리뷰는 **텍스트, 이미지 검증, 감성분석 딥러닝**을 통해 신뢰도 평가 후 🟠🟡🔵**3가지 색상**으로 사용자에게 공개
- 특정 리뷰를 사용자가 직접 평가 후 의견 전달
- 사용자가 원하는 리뷰 북마크




# APP
1. App 이름은 'Truri'입니다
2. Android Java를 사용하였습니다.
3. 안드로이드 OS version 11에서 테스트 되었습니다.
4. 검색을 위한 인터넷 연결이 필수적입니다.
5. 로그인 기능이 존재하나, 로그인 없이도 사용이 가능합니다.




# Server
1. 서버는 Flask와 Spring Boot를 사용하여 2가지 서버를 구축하였습니다.
2. Docker 및 AWS EC2를 이용하여 배포됩니다.
3. Python 3.9와 Java 15 버전을 기준으로 합니다.
4. Flask 서버는 검색 기능을, Spring boot 서버는 사용자 편의 기능을 제공합니다.
5. MariaDB(AWS RDS)와 연결됩니다.




# Ml & Dl
1. 어플의 홍보성 판단의 정확도를 높이기 위해 다양한 알고리즘을 시도하였습니다.(의사결정 나무, 랜덤 포레스트, SVM, 트랜스포머, KoBERT)
2. padding 길이, 토큰화 기법 등등 적절한 통계치를 활용해 적용시키고, 여러 심층망 레이어들을 쌓은 다양한 구조로 실험한 결과, 트랜스포머 인코더 부분을 사용하여 학습시킨 모델이 가장 높은 한국어 이해도(Accuracy 0.8)를 보였습니다.
3. 트랜스포머 인코더 부분을 기반으로 광고 게시글 필터링 어플을 개발하였습니다.
4. 반응 속도를 고려하여 KoNLPy를 이용한 멀티쓰레딩을 사용하였습니다.

