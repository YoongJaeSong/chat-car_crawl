# 목적
- chat-car project에서 사용할 데이터 수집

## 방법
crawl로 데이터를 가져와 Excel에 저장하기

### 저장할 데이터
차량 정보 page에 있는 데이터들은 일단 전부다 저장한다.

- Data
> 출시일(년, 월), 출시 가격, 연비, 최대 토크 등등...

### 책입
- 데이터를 가져온다.
  - Crawl
- 데이터를 전처리한다.
- 전처리된 데이터를 Excel에 저장한다.

### 행동
- 특정 CSS에 해당하는 tag정보를 가져온다.
  - Crawl
- 가져온 tag정보에서 text만 따로 뽑아낸다.
- 뽑은 text data를 객체로 만든다.
- data 객체를 Excel에 저장한다.
- 해당 page와 연결
  - Crawl