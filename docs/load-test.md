# 부하테스트를 위한 K6 도입

## 서버 성능 테스트란

서버 성능 테스트는 시스템이 실제 환경에서 얼마나 잘 동작할 수 있는지를 미리 파악하는 중요한 과정이다. 서버의 안정성, 속도, 확장성, 자원 사용률 등을 평가하여 시스템이 요구되는 성능 수준을 충족하는지 확인하는 데 중점을 둔다. 테스트를 통해 시스템의 안정성과 처리 능력을 파악할 수 있으며, 애플리케이션의 신뢰성, 사용자 경험, 그리고 비즈니스 목표 달성을 보장할 수 있다.

### 테스트 목적

1. 성능 기준 검증
    - 서버가 주어진 요구 사항이나 성능 목표(예: 응답 시간, 처리량, 동시 사용자 수)를 충족하는지 확인한다.
2. 최대 처리 용량 파악
    - 서버가 정상적으로 처리할 수 있는 최대 부하를 파악하여 병목 현상을 찾고, 시스템의 한계를 파악하여 서버 확장이나 인프라 조정을 용이하게 한다.
3. 시스템 안정성 평가
    - 다양한 시나리오에서 서버의 안정성을 평가한다.
    - 서버가 지속적인 부하를 받았을 때 메모리 누수나 리소스 고갈로 인해 장애가 발생하지 않는지 등을 확인한다.
4. 병목 현상 식별
    - CPU, 메모리, 네트워크, 데이터베이스 등의 자원에서 발생하는 병목 현상을 식별하여 성능 저하의 원인을 파악하고 개선할 수 있는 정보를 제공한다.
5. 애플리케이션 튜닝
    - 성능 테스트 결과를 기반으로 애플리케이션이나 서버 설정을 최적화하여 성능 개선한다.
    - 데이터베이스 쿼리 최적화, 캐싱 전략 개선, 서버 설정 조정 등이 해당한다.
6. 확장성 테스트
    - 시스템이 수직적, 수평적으로 확장될 때 성능이 어떻게 변화하는지 평가한다.
    - 미래에 사용자 수가 증가할 경우에 대비한 계획을 수립하는 데 도움을 준다.

### 테스트 기대효과

1. 문제 조기 발견 및 해결
   - 성능 테스트를 통해 서버나 애플리케이션이 실사용 환경에서 발생할 수 있는 문제를 미리 발견할 수 있다. 성능 저하, 병목 현상, 또는 시스템 장애를 사전에 파악하고 해결할 수 있다.
2. 시스템 안정성 향상
   - 지속적인 부하 테스트를 통해 서버가 다양한 부하 조건에서도 안정적으로 동작할 수 있도록 한다. 시스템의 신뢰성을 높이고, 예기치 않은 장애 발생을 줄일 수 있다.
3. 최적의 성능 확보
   - 성능 테스트 결과를 바탕으로 서버 및 애플리케이션의 성능을 최적화할 수 있고. 서버 자원의 효율적인 사용을 가능하게 한다.
4. 비용 절감
   - 성능 테스트를 통해 자원을 효율적으로 사용함으로써 불필요한 인프라 확장 비용(서버 증설 등)을 줄일 수 있고 시스템 장애로 인한 비즈니스 중단 비용을 예방할 수 있다.
5. 사용자 경험 향상
   - 최종 사용자의 경험을 향상시켜 사용자 만족도와 유지율을 높인다.
6. 비즈니스 목표 달성 지원
   - e커머스 플랫폼의 경우 대규모 프로모션 기간 동안 급증하는 트래픽을 처리할 수 있는지 사전에 검증함으로써 매출 손실을 예방할 수 있다.
7. 정확한 용량 계획 수립
   - 성능 테스트 결과를 통해 서버의 처리 용량을 정확하게 파악하고, 미래의 트래픽 증가에 대비할 수 있다.

## K6 테스트 및 테스트 코드
[K6 테스트 스크립트](../src/test/java/com/io/hhplus/concert/load-test.js)

### 콘서트 조회
1. vus = 100, iteration = 1000 의 조건으로 테스트 실행
    ```
         execution: local
            script: /Users/cruella/Desktop/repos/hhplus-server-week3/src/test/java/com/io/hhplus/concert/load-test.js
            output: -
    
         scenarios: (100.00%) 1 scenario, 100 max VUs, 10m30s max duration (incl. graceful stop):
                  * concert_scenario: 1000 iterations for each of 100 VUs (maxDuration: 10m0s, exec: concert_scenario, gracefulStop: 30s)
    
    
         ✗ status is 200
          ↳  0% — ✓ 0 / ✗ 1221
    
         checks.....................: 0.00%   ✓ 0          ✗ 1221 
         data_received..............: 64 kB   102 B/s
         data_sent..................: 84 kB   133 B/s
         dropped_iterations.........: 98777   156.786949/s
         http_req_blocked...........: avg=574.87ms min=0s      med=298µs   max=19.51s  p(90)=8.08ms  p(95)=14.58ms
         http_req_connecting........: avg=574.16ms min=0s      med=179µs   max=19.51s  p(90)=4.5ms   p(95)=10.41ms
       ✗ http_req_duration..........: avg=42.51s   min=0s      med=1m0s    max=1m0s    p(90)=1m0s    p(95)=1m0s   
       ✗ http_req_failed............: 100.00% ✓ 1221       ✗ 0    
         http_req_receiving.........: avg=434.97µs min=0s      med=0s      max=19.38ms p(90)=77µs    p(95)=470µs  
         http_req_sending...........: avg=612.16µs min=0s      med=33µs    max=126.7ms p(90)=442µs   p(95)=1.14ms 
         http_req_tls_handshaking...: avg=0s       min=0s      med=0s      max=0s      p(90)=0s      p(95)=0s     
         http_req_waiting...........: avg=42.51s   min=0s      med=1m0s    max=1m0s    p(90)=1m0s    p(95)=1m0s   
         http_reqs..................: 1221    1.938071/s
         iteration_duration.........: avg=49.59s   min=1s      med=1m1s    max=1m1s    p(90)=1m1s    p(95)=1m1s   
         iterations.................: 1221    1.938071/s
         tps........................: avg=21.49µs  min=16.42µs med=19.84µs max=33.12µs p(90)=31.86µs p(95)=33.1µs 
         vus........................: 2       min=2        max=100
         vus_max....................: 100     min=100      max=100
         waiting_time...............: avg=42.51s   min=0s      med=1m0s    max=1m0s    p(90)=1m0s    p(95)=1m0s   
    
    
    running (10m30.0s), 000/100 VUs, 1221 complete and 2 interrupted iterations
    concert_scenario ✓ [======================================] 100 VUs  10m0s  001221/100000 iters, 1000 per VU
    ERRO[0631] thresholds on metrics 'http_req_duration, http_req_failed' have been crossed 
    ```

   - metrics 분석 : 서버가 부하를 전혀 처리하지 못하고 있다.
     1. 모든 요청 실패인 것으로 보아 서버 오류가 발생했거나 네트워크 문제로 인해 요청이 전달되지 못했을 가능성이 높다.
     2. 요청 당 평균 응답 시간이 42.51초로 비정상적으로 길은 것으로 보아 타임아웃이 발생했을 가능성이 높다.
     3. dropped_iterations 이 98,777 건으로 건수가 매우 많고 iteration_duration 또한 길게 나타난 것으로 보아 부하를 처리하지 못해 요청이 타임아웃된 것으로 보인다.
     4. 가상 사용자(VUs)가 초기 설정한 100명에서 2명으로 줄어들었고, 중간에 인터럽트가 발생했는데 서버가 부하를 처리하지 못해 가상 사용자들이 요청을 제대로 수행하지 못하고 중단된 것 같다.

   - 어플리케이션 로그 확인
      ```
      2024-08-22T23:11:38.365+09:00 ERROR 43082 --- [concert] [nio-80-exec-804] o.h.engine.jdbc.spi.SqlExceptionHelper   : HikariPool-1 - Connection is not available, request timed out after 124005ms (total=10, active=10, idle=0, waiting=1)
      2024-08-22T23:13:04.720+09:00 ERROR 43082 --- [concert] [nio-80-exec-654] c.i.h.c.s.e.GlobalExceptionHandler       : Exception : Unable to acquire JDBC Connection [HikariPool-1 - Connection is not available, request timed out after 54955ms (total=10, active=10, idle=0, waiting=0)] [n/a]
      2024-08-22T23:13:04.720+09:00 ERROR 43082 --- [concert] [nio-80-exec-804] c.i.h.c.s.e.GlobalExceptionHandler       : Exception : Unable to acquire JDBC Connection [HikariPool-1 - Connection is not available, request timed out after 124005ms (total=10, active=10, idle=0, waiting=1)] [n/a]
      2024-08-22T23:13:04.722+09:00 ERROR 43082 --- [concert] [nio-80-exec-676] c.i.h.c.s.e.GlobalExceptionHandler       : Exception : Handler dispatch failed: java.lang.OutOfMemoryError: Java heap space
      2024-08-22T23:13:04.723+09:00 ERROR 43082 --- [concert] [nio-80-exec-835] c.i.h.c.s.e.GlobalExceptionHandler       : Exception : Unable to acquire JDBC Connection [HikariPool-1 - Connection is not available, request timed out after 71286ms (total=10, active=10, idle=0, waiting=0)] [n/a]
      2024-08-22T23:13:04.728+09:00  WARN 43082 --- [concert] [l-1 housekeeper] com.zaxxer.hikari.pool.HikariPool        : HikariPool-1 - Thread starvation or clock leap detected (housekeeper delta=1m56s526ms).
      ```
     1. 데이터 베이스 연결 풀이 모두 사용 중이어서 더 이상 추가 연결을 제공할 수 없었기 때문에 타임아웃이 발생하였다. `HikariPool-1 - Connection is not available, request timed out after Xms`
     2. OutOfMemoryError - JVM(Java Virtual Machine)에서 메모리를 할당할 수 없을 때 발생하는 에러인데 Java heap space는 애플리케이션이 사용 가능한 힙 메모리 공간을 모두 사용했다는 것을 의미한다. `Exception : Handler dispatch failed: java.lang.OutOfMemoryError: Java heap space`
     3. Thread Starvation - 스레드가 오랜 시간 동안 자원을 얻지 못해 작업을 수행하지 못하는 상태를 의미하여 일반적으로 시스템에 과부하가 걸리거나, GC가 너무 자주 실행되면서 애플리케이션 스레드가 충분히 실행되지 못하는 경우에 발생한다. `HikariPool-1 - Thread starvation or clock leap detected`

   - vus = 1, iteration = 1 로 변경 후 재시도하였으나 결과는 같았다.
      ```
           ✗ status is 200
            ↳  0% — ✓ 0 / ✗ 1
      
           checks.....................: 0.00%   ✓ 0        ✗ 1  
           data_received..............: 0 B     0 B/s
           data_sent..................: 0 B     0 B/s
           http_req_blocked...........: avg=0s min=0s med=0s max=0s p(90)=0s p(95)=0s
           http_req_connecting........: avg=0s min=0s med=0s max=0s p(90)=0s p(95)=0s
         ✓ http_req_duration..........: avg=0s min=0s med=0s max=0s p(90)=0s p(95)=0s
         ✗ http_req_failed............: 100.00% ✓ 1        ✗ 0  
           http_req_receiving.........: avg=0s min=0s med=0s max=0s p(90)=0s p(95)=0s
           http_req_sending...........: avg=0s min=0s med=0s max=0s p(90)=0s p(95)=0s
           http_req_tls_handshaking...: avg=0s min=0s med=0s max=0s p(90)=0s p(95)=0s
           http_req_waiting...........: avg=0s min=0s med=0s max=0s p(90)=0s p(95)=0s
           http_reqs..................: 1       0.998694/s
           iteration_duration.........: avg=1s min=1s med=1s max=1s p(90)=1s p(95)=1s
           iterations.................: 1       0.998694/s
           tps........................: avg=1s min=1s med=1s max=1s p(90)=1s p(95)=1s
           vus........................: 1       min=1      max=1
           vus_max....................: 1       min=1      max=1
           waiting_time...............: avg=0s min=0s med=0s max=0s p(90)=0s p(95)=0s
      
      
      running (00m01.0s), 0/1 VUs, 1 complete and 0 interrupted iterations
      concert_scenario ✓ [======================================] 1 VUs  00m01.0s/10m0s  1/1 iters, 1 per VU
      ERRO[0001] thresholds on metrics 'http_req_failed' have been crossed 
      ```
     - 쿼리 확인 : `SELECT C FROM ConcertEntity C WHERE C.concertStatus = 'AVAILABLE' AND NOW() BETWEEN C.bookBeginAt AND C.bookEndAt AND C.deletedAt IS NULL`
       1. 위 쿼리에 해당하는 데이터를 조회 했을 때 조회되는 데이터는 모두 `1,000,016` 건이었다.
          ```
          select count(*)
            from hhplusdev.concert c
           where c.concert_status  = 'AVAILABLE'
             and NOW()             between  c.book_begin_at and c.book_end_at
             and c.deleted_at      is null;
          ```
       2. 조회되는 데이터가 너무 많아 페이지네이션을 추가하여 조회되는 데이터 건수를 줄이고 쿼리 조회 시간을 줄여 최종 응답 시간까지 줄이는 개선이 필요해 보인다.

### 일정 조회
1. vus = 100, iteration = 1000
   ```
        execution: local
           script: /Users/cruella/Desktop/repos/hhplus-server-week3/src/test/java/com/io/hhplus/concert/load-test.js
           output: -
   
        scenarios: (100.00%) 1 scenario, 100 max VUs, 10m30s max duration (incl. graceful stop):
                 * concert_scenario: 1000 iterations for each of 100 VUs (maxDuration: 10m0s, exec: concert_scenario, gracefulStop: 30s)
   
   
        ✓ status is 200
   
        checks.........................: 100.00% ✓ 59400     ✗ 0    
        data_received..................: 82 MB   135 kB/s
        data_sent......................: 6.0 MB  9.9 kB/s
        dropped_iterations.............: 40600   67.395086/s
        http_req_blocked...............: avg=11.59µs  min=0s       med=3µs      max=10.56ms p(90)=7µs     p(95)=11µs   
        http_req_connecting............: avg=5.44µs   min=0s       med=0s       max=5.44ms  p(90)=0s      p(95)=0s     
      ✓ http_req_duration..............: avg=9.96ms   min=902µs    med=8.48ms   max=63.63ms p(90)=18.83ms p(95)=22.75ms
          { expected_response:true }...: avg=9.96ms   min=902µs    med=8.48ms   max=63.63ms p(90)=18.83ms p(95)=22.75ms
      ✓ http_req_failed................: 0.00%   ✓ 0         ✗ 59400
        http_req_receiving.............: avg=301.82µs min=6µs      med=35µs     max=50.9ms  p(90)=232µs   p(95)=982µs  
        http_req_sending...............: avg=21.1µs   min=2µs      med=11µs     max=14.98ms p(90)=32µs    p(95)=55µs   
        http_req_tls_handshaking.......: avg=0s       min=0s       med=0s       max=0s      p(90)=0s      p(95)=0s     
        http_req_waiting...............: avg=9.63ms   min=873µs    med=8.15ms   max=63.43ms p(90)=18.31ms p(95)=22.25ms
        http_reqs......................: 59400   98.602663/s
        iteration_duration.............: avg=1.01s    min=1s       med=1s       max=1.06s   p(90)=1.01s   p(95)=1.02s  
        iterations.....................: 59400   98.602663/s
        tps............................: avg=1.02ms   min=985.72µs med=992.79µs max=17.54ms p(90)=1ms     p(95)=1.02ms 
        vus............................: 100     min=100     max=100
        vus_max........................: 100     min=100     max=100
        waiting_time...................: avg=9.63ms   min=873µs    med=8.15ms   max=63.43ms p(90)=18.31ms p(95)=22.25ms
   
   
   running (10m02.4s), 000/100 VUs, 59400 complete and 0 interrupted iterations
   concert_scenario ✓ [======================================] 100 VUs  10m0s  059400/100000 iters, 1000 per VU
   ```
   - metrics
     1. TPS (Transactions Per Second, 초당 처리된 요청 수)
       - metrics 로 제공된 값에 따르면 평균 TPS는 avg=1.02ms 로, min=985.72µs, med=992.79µs, max=17.54ms로 나타나 있는데 이 값은 잘못된 값으로 보인다.
       - 일반적으로 TPS는 "요청/초"로 표현되며 http_reqs 메트릭과 테스트 실행 시간으로 계산하자면
         - 전체 요청 수: 59,400 요청 
         - 테스트 시간: 10분 (600초)
         - 평균 TPS: 59,400 / 600 = 99 요청/초 
       - 시스템은 평균적으로 초당 약 99개의 요청을 처리하고 있다.
     2. 응답 시간
        - http_req_duration 를 보면 
          - 평균 응답 시간: avg=9.96ms 
          - 최소 응답 시간: min=902µs 
          - 최대 응답 시간: max=63.63ms 
          - p(90): 18.83ms 
          - p(95): 22.75ms
        - 평균 응답 시간이 9.96ms로 매우 양호하며, 대부분의 요청(p(95))이 22.75ms 이내에 처리되었음을 나타낸다.
        - 최대 응답 시간이 63.63ms로 나타나, 일부 요청은 비교적 긴 시간이 소요되었지만 이는 드문 경우로 보안다.
     3. 드롭된 반복 횟수 40,600회로 서버 자원 부족으로 인해 일부 요청이 누실됐을 가능성이 있다.
        - 서버가 처리할 수 있는 최대 부하에 도달했거나, 자원(CPU, 메모리) 과부하로 인해 요청이 성공적으로 완료되지 않았을 수 있다.
     4. 더 높은 부하 시나리오(예: 200명의 동시 사용자)를 테스트하여 시스템의 확장성을 평가하고, 필요한 경우 인프라를 개선할 수 있다.

2. vus = 200, iteration = 1000
   ```
       ✗ status is 200
         ↳  99% — ✓ 118702 / ✗ 98
   
        checks.........................: 99.91% ✓ 118702     ✗ 98    
        data_received..................: 163 MB 272 kB/s
        data_sent......................: 12 MB  20 kB/s
        dropped_iterations.............: 81200  135.212172/s
        http_req_blocked...............: avg=17.8µs   min=0s       med=3µs      max=111.39ms p(90)=6µs     p(95)=8µs    
        http_req_connecting............: avg=12.57µs  min=0s       med=0s       max=111.35ms p(90)=0s      p(95)=0s     
      ✓ http_req_duration..............: avg=10.4ms   min=807µs    med=6.84ms   max=349.01ms p(90)=22.92ms p(95)=30.87ms
          { expected_response:true }...: avg=10.4ms   min=807µs    med=6.85ms   max=349.01ms p(90)=22.93ms p(95)=30.88ms
      ✓ http_req_failed................: 0.08%  ✓ 98         ✗ 118702
        http_req_receiving.............: avg=181.22µs min=0s       med=31µs     max=36.7ms   p(90)=144µs   p(95)=432µs  
        http_req_sending...............: avg=20.69µs  min=2µs      med=9µs      max=28.68ms  p(90)=25µs    p(95)=46µs   
        http_req_tls_handshaking.......: avg=0s       min=0s       med=0s       max=0s       p(90)=0s      p(95)=0s     
        http_req_waiting...............: avg=10.2ms   min=783µs    med=6.59ms   max=348.36ms p(90)=22.63ms p(95)=30.7ms 
        http_reqs......................: 118800 197.822734/s
        iteration_duration.............: avg=1.01s    min=1s       med=1s       max=1.35s    p(90)=1.02s   p(95)=1.03s  
        iterations.....................: 118800 197.822734/s
        tps............................: avg=1.04ms   min=990.73µs med=992.64µs max=100ms    p(90)=1ms     p(95)=1.01ms 
        vus............................: 200    min=200      max=200 
        vus_max........................: 200    min=200      max=200 
        waiting_time...................: avg=10.2ms   min=783µs    med=6.59ms   max=348.36ms p(90)=22.63ms p(95)=30.7ms 
   
   
   running (10m00.5s), 000/200 VUs, 118800 complete and 0 interrupted iterations
   concert_scenario ✓ [======================================] 200 VUs  10m0s  118800/200000 iters, 1000 per VU
   ```
   - metrics
     - 요청의 99.91%가 성공적으로 처리되었고 200명의 동시 사용자로 테스트했을 때, 98건의 요청이 실패했지만, 이는 전체 요청 수에 비해 매우 적은 비율(0.08%)로 서버는 상당히 안정적인 성능을 보인다.

### 좌석 조회
1. vus = 100, iteration = 1000
   ```
        execution: local
           script: /Users/cruella/Desktop/repos/hhplus-server-week3/src/test/java/com/io/hhplus/concert/load-test.js
           output: -
   
        scenarios: (100.00%) 1 scenario, 100 max VUs, 10m30s max duration (incl. graceful stop):
                 * concert_scenario: 1000 iterations for each of 100 VUs (maxDuration: 10m0s, exec: concert_scenario, gracefulStop: 30s)
   
   WARN[0112] The test has generated metrics with 100809 unique time series, which is higher than the suggested limit of 100000 and could cause high memory usage. Consider not using high-cardinality values like unique IDs as metric tags or, if you need them in the URL, use the name metric tag or URL grouping. See https://grafana.com/docs/k6/latest/using-k6/tags-and-groups/ for details.  component=metrics-engine-ingester
   WARN[0225] The test has generated metrics with 200034 unique time series, which is higher than the suggested limit of 100000 and could cause high memory usage. Consider not using high-cardinality values like unique IDs as metric tags or, if you need them in the URL, use the name metric tag or URL grouping. See https://grafana.com/docs/k6/latest/using-k6/tags-and-groups/ for details.  component=metrics-engine-ingester
   WARN[0449] The test has generated metrics with 400140 unique time series, which is higher than the suggested limit of 100000 and could cause high memory usage. Consider not using high-cardinality values like unique IDs as metric tags or, if you need them in the URL, use the name metric tag or URL grouping. See https://grafana.com/docs/k6/latest/using-k6/tags-and-groups/ for details.  component=metrics-engine-ingester
   
        ✗ status is 200
         ↳  99% — ✓ 59368 / ✗ 32
   
        checks.........................: 99.94% ✓ 59368     ✗ 32   
        data_received..................: 15 MB  24 kB/s
        data_sent......................: 6.6 MB 11 kB/s
        dropped_iterations.............: 40600  67.606894/s
        http_req_blocked...............: avg=12.16µs  min=0s       med=4µs      max=9.16ms   p(90)=8µs     p(95)=12µs   
        http_req_connecting............: avg=5.54µs   min=0s       med=0s       max=2.87ms   p(90)=0s      p(95)=0s     
      ✓ http_req_duration..............: avg=10.18ms  min=753µs    med=7.89ms   max=341.84ms p(90)=18.37ms p(95)=22.86ms
          { expected_response:true }...: avg=10.18ms  min=753µs    med=7.89ms   max=341.84ms p(90)=18.37ms p(95)=22.86ms
      ✓ http_req_failed................: 0.05%  ✓ 32        ✗ 59368
        http_req_receiving.............: avg=254.82µs min=6µs      med=36µs     max=23.12ms  p(90)=205µs   p(95)=636µs  
        http_req_sending...............: avg=26.15µs  min=2µs      med=11µs     max=23.41ms  p(90)=38µs    p(95)=69µs   
        http_req_tls_handshaking.......: avg=0s       min=0s       med=0s       max=0s       p(90)=0s      p(95)=0s     
        http_req_waiting...............: avg=9.9ms    min=718µs    med=7.64ms   max=341.73ms p(90)=17.9ms  p(95)=22.25ms
        http_reqs......................: 59400  98.912549/s
        iteration_duration.............: avg=1.01s    min=1s       med=1s       max=1.34s    p(90)=1.01s   p(95)=1.02s  
        iterations.....................: 59400  98.912549/s
        tps............................: avg=999.65µs min=990.74µs med=991.36µs max=3.3ms    p(90)=1ms     p(95)=1ms    
        vus............................: 100    min=100     max=100
        vus_max........................: 100    min=100     max=100
        waiting_time...................: avg=9.9ms    min=718µs    med=7.64ms   max=341.73ms p(90)=17.9ms  p(95)=22.25ms
   
   
   running (10m00.5s), 000/100 VUs, 59400 complete and 0 interrupted iterations
   concert_scenario ✓ [======================================] 100 VUs  10m0s  059400/100000 iters, 1000 per VU
   ```
   - metrics
     1. TPS (Transactions Per Second, 초당 처리된 요청 수)
        - tps 값이 avg=999.65µs로 나타나 있지만, 이는 잘못된 표현으로 시간 단위가 아닌 TPS의 처리 속도를 나타내야 한다. 계산하자면
          - 전체 요청 수: 59,400 
          - 테스트 시간: 10분 (600초)
          - 평균 TPS: 59,400 / 600 = 99 요청/초
        - 서버는 초당 평균 약 99개의 요청을 처리하고 있다.
     2. 응답 시간 (http_req_duration)
        - 평균 응답 시간: avg=10.18ms 
        - 최소 응답 시간: min=753µs 
        - 최대 응답 시간: max=341.84ms 
        - p(90): 18.37ms 
        - p(95): 22.86ms
        - 평균 응답 시간은 10.18ms로 양호한 성능을 보이며, 최대 응답 시간이 341.84ms로 일부 요청이 상대적으로 긴 시간이 소요되었음을 알 수 있다.
        - 그러나 p(90) 및 p(95) 값도 각각 18.37ms, 22.86ms로 나타나 전반적으로 서버가 요청을 빠르게 처리하고 있다.
     3. 자원 사용률
        - Dropped Iterations: 40,600회가 드롭되었는데 이는 서버가 모든 요청을 처리할 수 없는 상황에서 발생한다.
        - 경고 다시지 발생 `The test has generated metrics with 100809 unique time series, which is higher than the suggested limit of 100000...`
          - 테스트에서 생성된 메트릭이 너무 많아 메모리 사용량이 크게 증가했을 가능성이 높다. 메모리 부족 또는 시스템 자원 사용률이 높아져 성능이 저하되었을 수도 있다는 것이.
        - 서버의 메모리와 CPU 사용률이 매우 높아져 일부 요청이 처리되지 못한 것으로 추정된다.

### 토큰 발급 및 조회
1. vus = 100, iteration = 1000
   ```
        execution: local
           script: /Users/cruella/Desktop/repos/hhplus-server-week3/src/test/java/com/io/hhplus/concert/load-test.js
           output: -
   
        scenarios: (100.00%) 1 scenario, 100 max VUs, 10m30s max duration (incl. graceful stop):
                 * concert_scenario: 1000 iterations for each of 100 VUs (maxDuration: 10m0s, exec: concert_scenario, gracefulStop: 30s)
   
   
        ✗ status is 200
         ↳  99% — ✓ 58945 / ✗ 255
   
        checks.........................: 99.56% ✓ 58945     ✗ 255  
        data_received..................: 19 MB  31 kB/s
        data_sent......................: 9.5 MB 16 kB/s
        dropped_iterations.............: 40800  67.892178/s
        http_req_blocked...............: avg=12.34µs  min=0s       med=3µs     max=4.29ms   p(90)=6µs      p(95)=11µs    
        http_req_connecting............: avg=6.98µs   min=0s       med=0s      max=2.39ms   p(90)=0s       p(95)=0s      
      ✓ http_req_duration..............: avg=14.61ms  min=1.31ms   med=10.02ms max=932.86ms p(90)=28.2ms   p(95)=38.16ms 
          { expected_response:true }...: avg=14.63ms  min=1.66ms   med=10.03ms max=932.86ms p(90)=28.21ms  p(95)=38.16ms 
      ✓ http_req_failed................: 0.43%  ✓ 255       ✗ 58945
        http_req_receiving.............: avg=92.15µs  min=7µs      med=39µs    max=24.29ms  p(90)=115µs    p(95)=187µs   
        http_req_sending...............: avg=20.93µs  min=3µs      med=13µs    max=5ms      p(90)=36µs     p(95)=56µs    
        http_req_tls_handshaking.......: avg=0s       min=0s       med=0s      max=0s       p(90)=0s       p(95)=0s      
        http_req_waiting...............: avg=14.5ms   min=1.17ms   med=9.86ms  max=932.67ms p(90)=28.1ms   p(95)=38.06ms 
        http_reqs......................: 59200  98.510219/s
        iteration_duration.............: avg=1.01s    min=1s       med=1.01s   max=1.93s    p(90)=1.02s    p(95)=1.03s   
        iterations.....................: 59200  98.510219/s
        tps............................: avg=991.09µs min=986.62µs med=987µs   max=2.56ms   p(90)=991.93µs p(95)=999.08µs
        vus............................: 100    min=100     max=100
        vus_max........................: 100    min=100     max=100
        waiting_time...................: avg=14.5ms   min=1.17ms   med=9.86ms  max=932.67ms p(90)=28.1ms   p(95)=38.06ms 
   
   
   running (10m01.0s), 000/100 VUs, 59200 complete and 0 interrupted iterations
   concert_scenario ✓ [======================================] 100 VUs  10m0s  059200/100000 iters, 1000 per VU
   ```
   - metrics
     1. TPS (Transactions Per Second, 초당 처리된 요청 수)
       - tps 값이 avg=991.09µs로 표시되어 있지만, 이는 잘못된 단위로 보이며 계산을 바로 잡는다면, 
         - 전체 요청 수: 59,200 
         - 테스트 시간: 10분 (600초)
         - 평균 TPS: 59,200 / 600 = 약 98.67 요청/초 
       - 서버는 초당 평균 약 98.67개의 요청을 처리할 수 있으며 양호한 성능을 가지고 있다.
     2. 응답 시간 (http_req_duration)
        - 평균 응답 시간: avg=14.61ms
        - 최소 응답 시간: min=1.31ms
        - 최대 응답 시간: max=932.86ms
        - p(90): 28.2ms
        - p(95): 38.16ms
        - 평균 응답 시간이 14.61ms로 양호한 성능을 보이고 있다.
        - p(90) 및 p(95) 값이 각각 28.2ms, 38.16ms로 나타나 대부분의 요청이 비교적 짧은 시간 안에 처리되었음을 보여준다.
     3. 드롭된 반복 횟수가 40,800회로 상당히 많아, 서버가 부하를 완전히 처리하지 못하고 일부 요청이 실패한 것으로 보입니다.

## 향후 어플리케이션 및 테스트 개선 방향

1. 서버 자원 모니터링
   - 서버의 CPU, 메모리, 네트워크 사용량을 실시간으로 모니터링하여 병목 현상을 파악하고, 필요한 경우 자원을 확장한다.
2. 성능 최적화
   - 애플리케이션의 병목이 되는 부분(예: 데이터베이스 쿼리, 코드 최적화)을 개선하여 응답 시간을 줄이고, 드롭된 반복 횟수를 감소시킨다.
3. 장기적인 테스트 수행
   - 장기적인 부하 테스트를 통해 시스템의 안정성을 평가하고, 메모리 누수나 자원 과다 사용을 방지하기 위한 개선 작업을 수행한다.