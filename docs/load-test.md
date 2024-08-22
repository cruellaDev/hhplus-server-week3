# 부하테스트를 위한 K6 도입기

## 서버 성능 테스트란

### 테스트 목적

### 테스트 기대효과

## 서버 성능 테스트 도구

### K6를 선택한 이유
- 경제적, 효율적

### K6 테스트 방법 및 테스트 코드
[테스트코드](../src/test/java/com/io/hhplus/concert/load-test.js)

## 테스트 API
모든 API 에는 전제 조건이 있다. 표본도 있다.
전제 조건이 있을 시 모든 문제를 커버할 수는 없지만 현 상황에서 테스트할 수 있는 최적의 환경이라 생각한다.

### 콘서트 조회
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

```
2024-08-22T23:11:38.365+09:00 ERROR 43082 --- [concert] [nio-80-exec-804] o.h.engine.jdbc.spi.SqlExceptionHelper   : HikariPool-1 - Connection is not available, request timed out after 124005ms (total=10, active=10, idle=0, waiting=1)
2024-08-22T23:13:04.720+09:00 ERROR 43082 --- [concert] [nio-80-exec-654] c.i.h.c.s.e.GlobalExceptionHandler       : Exception : Unable to acquire JDBC Connection [HikariPool-1 - Connection is not available, request timed out after 54955ms (total=10, active=10, idle=0, waiting=0)] [n/a]
2024-08-22T23:13:04.720+09:00 ERROR 43082 --- [concert] [nio-80-exec-804] c.i.h.c.s.e.GlobalExceptionHandler       : Exception : Unable to acquire JDBC Connection [HikariPool-1 - Connection is not available, request timed out after 124005ms (total=10, active=10, idle=0, waiting=1)] [n/a]
2024-08-22T23:13:04.722+09:00 ERROR 43082 --- [concert] [nio-80-exec-676] c.i.h.c.s.e.GlobalExceptionHandler       : Exception : Handler dispatch failed: java.lang.OutOfMemoryError: Java heap space
2024-08-22T23:13:04.723+09:00 ERROR 43082 --- [concert] [nio-80-exec-835] c.i.h.c.s.e.GlobalExceptionHandler       : Exception : Unable to acquire JDBC Connection [HikariPool-1 - Connection is not available, request timed out after 71286ms (total=10, active=10, idle=0, waiting=0)] [n/a]
2024-08-22T23:13:04.728+09:00  WARN 43082 --- [concert] [l-1 housekeeper] com.zaxxer.hikari.pool.HikariPool        : HikariPool-1 - Thread starvation or clock leap detected (housekeeper delta=1m56s526ms).
```

```
1. status is 200 실패
설명: 모든 요청이 실패했습니다. status is 200 체크가 0%로 나타나고, 모든 요청이 성공적으로 완료되지 않았음을 의미합니다.
원인: 서버가 모든 요청에 대해 HTTP 200 상태 코드를 반환하지 못하고 있으며, 아마도 서버 오류(예: 500 에러)나 다른 상태 코드가 발생했을 가능성이 큽니다.
2. http_req_failed 100%
설명: 모든 HTTP 요청이 실패했습니다. 이는 서버가 요청을 제대로 처리하지 못하고 있다는 것을 나타냅니다.
원인: 서버에서 장애가 발생했거나 요청을 처리할 수 없는 상태에 있습니다. 서버가 다운되었거나, 네트워크 문제로 인해 요청이 전달되지 않았을 수 있습니다.
3. http_req_duration 및 http_req_waiting 시간이 매우 길다
설명: 요청당 평균 응답 시간이 42.51초로 나타나며, 중간값(median)은 60초입니다. 이는 비정상적으로 긴 응답 시간을 나타냅니다.
원인: 서버가 요청을 처리하지 못하고 타임아웃이 발생했을 가능성이 있습니다. 서버가 요청을 처리하기까지 지나치게 오래 걸리거나, 처리 중에 문제가 발생한 것으로 보입니다.
4. dropped_iterations 및 iteration_duration
설명: 많은 반복(iterations)이 드롭되었고, 반복 시간(iteration_duration)이 매우 길게 나타났습니다.
원인: 서버가 응답을 지연시키거나, 부하를 처리하지 못해 많은 요청이 타임아웃되거나 실패하여 드롭되었습니다.
5. vus 및 vus_max
설명: 가상 사용자(VUs)가 초기 설정한 100명에서 2명으로 줄어들었고, 중간에 인터럽트가 발생했습니다.
원인: 서버가 부하를 처리하지 못해 가상 사용자들이 요청을 제대로 수행하지 못하고 중단된 것 같습니다.
종합적 분석
이 결과는 서버가 부하를 전혀 처리하지 못하고 있음을 나타냅니다. 모든 HTTP 요청이 실패했고, 응답 시간이 지나치게 길며, 가상 사용자들이 목표한 반복을 완료하지 못하고 도중에 중단되었습니다.

가능한 해결 방법
서버 상태 확인: 서버의 상태를 점검하여 장애가 발생하지 않았는지 확인하세요. 로그를 통해 서버에서 발생한 오류를 파악하는 것이 중요합니다.
부하 감소: 초기 테스트에서는 부하를 줄여서 서버가 어떤 수준에서 안정적으로 동작하는지 확인한 후 부하를 점진적으로 증가시키는 것이 좋습니다.
타임아웃 설정 검토: 너무 긴 타임아웃 설정이 되어 있지 않은지 확인하세요. 필요하다면 타임아웃 시간을 조정하여 요청이 적절한 시간 안에 실패하도록 설정하세요.
네트워크 문제: 네트워크 연결 문제로 인해 요청이 실패하고 있는지 확인하세요.
이 문제를 해결하기 위해서는 서버의 상태와 설정을 면밀히 분석하고, 필요한 경우 부하를 줄여가며 테스트를 다시 수행해야 합니다.
```
```
1. 데이터베이스 연결 부족
로그 내용: HikariPool-1 - Connection is not available, request timed out after Xms
원인: HikariCP(Connection Pooling Library)에서 데이터베이스 연결을 얻지 못하고 타임아웃이 발생한 것입니다. 이 로그는 데이터베이스 연결 풀이 모두 사용 중이며, 더 이상 추가 연결을 제공할 수 없다는 것을 의미합니다. 연결 풀의 크기는 total=10으로 설정되어 있으며, active=10이므로 모든 연결이 사용 중입니다. idle=0은 유휴 연결이 없음을 의미하고, 여러 요청이 대기 중인 상황입니다.
해결 방안:
Connection Pool 크기 조정: HikariCP의 최대 연결 수(maximumPoolSize)를 늘려보세요. 하지만, 무작정 늘리는 것은 권장되지 않으며, 데이터베이스 서버가 처리할 수 있는 적절한 수준으로 조정해야 합니다.
쿼리 최적화: 데이터베이스 쿼리를 최적화하여 연결이 더 빨리 반환되도록 하는 것도 중요한 해결책입니다.
연결 시간 제한 검토: 타임아웃 설정(connectionTimeout)을 확인하고, 필요에 따라 조정하세요.
2. OutOfMemoryError
로그 내용: Exception : Handler dispatch failed: java.lang.OutOfMemoryError: Java heap space
원인: 이 오류는 JVM(Java Virtual Machine)에서 메모리를 할당할 수 없을 때 발생합니다. 특히 Java heap space는 애플리케이션이 사용 가능한 힙 메모리 공간을 모두 사용했다는 것을 의미합니다.
해결 방안:
JVM 힙 메모리 크기 증가: 애플리케이션이 실행되는 JVM의 힙 메모리 크기를 늘려보세요. -Xmx 옵션을 사용하여 최대 힙 메모리 크기를 설정할 수 있습니다.
메모리 사용 최적화: 애플리케이션 코드에서 메모리를 많이 사용하는 부분을 분석하고, 불필요한 객체 생성 또는 메모리 누수를 방지하기 위해 코드 최적화가 필요합니다.
GC 튜닝: 가비지 컬렉터(GC) 설정을 조정하여 메모리 관리 효율을 높일 수 있습니다.
3. Thread Starvation
로그 내용: HikariPool-1 - Thread starvation or clock leap detected
원인: 이 메시지는 스레드 기아 상태(thread starvation)가 발생했음을 경고합니다. 스레드가 오랜 시간 동안 자원을 얻지 못해 작업을 수행하지 못하는 상태를 의미합니다. 이것은 일반적으로 시스템에 과부하가 걸리거나, GC가 너무 자주 실행되면서 애플리케이션 스레드가 충분히 실행되지 못하는 경우에 발생할 수 있습니다.
해결 방안:
스레드 풀 크기 조정: 서버의 스레드 풀 크기를 조정하여 요청을 처리할 수 있는 스레드 수를 늘리세요.
작업 분배 최적화: 스레드가 작업을 효율적으로 분배받을 수 있도록 작업의 병렬성을 조정하세요.
GC 튜닝: 메모리 관리를 보다 효율적으로 하여 스레드가 충분히 실행될 수 있도록 GC 튜닝을 고려하세요.
종합 분석
이 로그는 현재 애플리케이션이 데이터베이스와 메모리 자원에 대한 과도한 요청을 처리하지 못하고 있음을 나타냅니다. 이는 부하 테스트에서 발생한 문제와 일치하며, 서버가 과도한 부하를 견디지 못하고 다운되거나 응답하지 않는 상태에 빠졌음을 시사합니다. 위에서 제안한 조치들을 통해 서버 자원 관리 및 성능 최적화를 시도해보세요.
```

### 일정 조회
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
```
부하 테스트 보고서
테스트 개요
테스트 목적: 서버의 성능과 안정성을 평가하고, 동시 사용자 100명(VUs)이 시스템에 부하를 가했을 때의 응답 시간을 측정하기 위함입니다.
테스트 기간: 2024년 8월 22일
테스트 도구: k6
테스트 대상 시스템: http://localhost:80/concerts
테스트 설정
가상 사용자(VUs): 100명
시나리오: 각 가상 사용자가 1000회의 반복(iterations)을 실행
테스트 총 시간: 10분 30초
성능 목표: HTTP 요청 실패율 1% 미만, 90%의 요청이 500ms 이내에 완료
테스트 결과 요약
총 요청 수: 59,400
성공적인 요청: 100% (59,400건 모두 성공)
드롭된 반복: 40,600회 (67.4회/초)
TPS(Transactions Per Second): 평균 1.02ms (최소 985.72µs, 최대 17.54ms)
응답 시간 (http_req_duration):
평균: 9.96ms
최소: 902µs
최대: 63.63ms
p(90): 18.83ms
p(95): 22.75ms
대기 시간 (http_req_waiting):
평균: 9.63ms
최소: 873µs
최대: 63.43ms
p(90): 18.31ms
p(95): 22.25ms
분석 및 평가
성공적인 요청:

평가: 모든 요청이 성공적으로 처리되었습니다. 이는 서버가 100명의 동시 사용자 부하를 안정적으로 처리할 수 있음을 나타냅니다.
응답 시간:

평균 응답 시간은 9.96ms로 매우 양호한 성능을 보여주고 있습니다. 95%의 요청이 22.75ms 이내에 처리되었으며, 이는 사용자 경험 측면에서 매우 긍정적입니다.
대기 시간 또한 평균 9.63ms로 나타나며, 대부분의 요청이 신속하게 처리되고 있음을 확인할 수 있습니다.
드롭된 반복:

분석: 테스트 도중 40,600회의 반복이 드롭되었습니다. 이는 시스템이 설정된 부하를 감당하지 못하고 일부 요청을 처리하지 못했음을 의미합니다. 드롭된 반복 횟수가 상당히 많기 때문에 서버의 최대 처리 능력에 도달했을 가능성이 있습니다.
평가: 드롭된 반복이 발생한 이유는 서버 리소스 부족이나 네트워크 문제일 수 있습니다. 이는 서버의 최대 부하 한계점을 나타낼 수 있으며, 추가적인 조정이 필요할 수 있습니다.
TPS:

평가: 평균 TPS는 1.02ms로, 서버가 안정적으로 요청을 처리하고 있음을 나타냅니다. 최대 TPS는 17.54ms로 일부 구간에서 요청 처리 속도가 느려졌음을 알 수 있지만, 전반적으로 좋은 성능을 보였습니다.
시스템 자원 상태:

스레드 풀: 모든 가상 사용자가 테스트 기간 동안 유지되었고, 서버는 100명의 동시 사용자를 처리할 수 있었습니다.
평가: 서버의 리소스 관리가 적절히 이루어지고 있음을 보여줍니다.
결론 및 권장 사항
성능: 서버는 100명의 동시 사용자가 10분 동안 지속적인 요청을 보낼 때 안정적으로 동작했습니다. 응답 시간이 매우 짧고, 대부분의 요청이 22.75ms 이내에 완료되었습니다.
드롭된 반복에 대한 개선: 드롭된 반복이 많았기 때문에 서버의 최대 부하를 낮추거나, 서버의 자원(예: CPU, 메모리)을 추가적으로 확장하는 것이 필요할 수 있습니다. 특히 데이터베이스 연결과 메모리 관리 부분에서 추가적인 조정이 필요할 수 있습니다.
향후 계획: 더 높은 부하 시나리오(예: 200명의 동시 사용자)를 테스트하여 시스템의 확장성을 평가하고, 필요한 경우 인프라를 개선할 수 있습니다.
추가 제안
부하 테스트 반복: 서버 리소스를 조정한 후 테스트를 반복하여 개선 사항을 확인합니다.
장기 테스트: 장기적인 부하 테스트를 통해 서버의 안정성을 평가하고, 잠재적인 메모리 누수 또는 리소스 누수를 확인합니다.
```

```
- 200명의 동시 사용자


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
```
성공적인 요청:

평가: 요청의 99.91%가 성공적으로 처리되었습니다. 200명의 동시 사용자로 테스트했을 때, 98건의 요청이 실패했지만, 이는 전체 요청 수에 비해 매우 적은 비율(0.08%)입니다. 따라서 서버는 상당히 안정적인 성능을 보였습니다.
```

### 좌석 조회
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

### 토큰 발급 및 조회
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

## 부하테스트 후 어플리케이션 개선 방향

### 추가 논의 사항

### 참고자료