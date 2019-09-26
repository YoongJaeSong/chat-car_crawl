# 실행방법
1. java makerNo fileName

원하는 제조사의 차량들의 url조합을 저장하기위한 실행

|제조사|No|
|:---:|:---:|
|현대|49|
|기아|3|
|제네시스|1010|
|르노삼성|26|
ex) java 49 hyundai.xls

2. java fileName

위에서 url를 저장한 파일명을 입력하면 저장된 url를 통해 crawling을 한다.

ex) java hyundai.xls

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
  - ExcelDTO
- 전처리된 데이터를 Excel에 저장한다.
  - WriteExcel
- Crawling할 사이트를 조합하기
  - UrlVO

### 행동
- 특정 CSS에 해당하는 tag정보를 가져온다.
  - Crawl
- 해당 page와 연결
  - Crawl
- 가져온 tag정보에서 key/value 형태로 만든다.
  - ExcelDTO
- data 객체를 Excel에 저장한다.
  - WriteExcel
- crawling할 자동차의 model_no를 찾는다.
  - UrlVO
- crawling할 자동차의 class_no를 찾는다.
  - UrlVO
- crawling할 자동차의 year를 찾는다.
  - UrlVO
