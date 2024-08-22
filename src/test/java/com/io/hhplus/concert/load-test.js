import http from 'k6/http';
import { sleep, check } from 'k6';
import { Trend } from 'k6/metrics';
import { randomIntBetween } from 'https://jslib.k6.io/k6-utils/1.2.0/index.js';

export let options = {
    scenarios: {
        concert_scenario: {
            vus: 100, // 가상 사용자
            exec: 'concert_scenario',
            executor: 'per-vu-iterations', // 각각의 가상 사용자들이 정확한 반복 횟수만큼 실행
            iterations: 1000,
        },
    },
    thresholds: {
        http_req_failed: ['rate<0.01'], // http errors 가 1% 이하이어야 함
        http_req_duration: ['p(50)<100', 'p(70)<300', 'p(90)<500', 'p(95)<1000', 'p(99)<1500'] // 요청시간 제한
    },
};

const waitingTimeTrend = new Trend('waiting_time', true); // true로 설정하여 기본 통계 포함
const tpsTrend = new Trend('tps', true); // true로 설정하여 기본 통계 포함

let startTime = Date.now();
let requestCount = 0;

export function concert_scenario() {
    const response = getConcerts();
    check(response, {
        'status is 200': (res) => res.status === 200,
    });
    
    waitingTimeTrend.add(response.timings.waiting);

    requestCount++;
    const currentTime = Date.now();
    const elapsedSeconds = (currentTime - startTime) / 1000;
    const tps = requestCount / elapsedSeconds;

    tpsTrend.add(tps);

    sleep(1);
}

const getResponse = (url = '') => http.get(url);

const getConcerts = () => {
  const url = "http://localhost:80/concerts/";
    return getResponse(url);
};

const getSchedules = () => {
    const concertId = randomIntBetween(1, 2000);
    const url = `http://localhost:80/concerts/${concertId}/schedules`;
    return getResponse(url);
};

const getSeats = () => {
  const concertId = randomIntBetween(1, 2000);
  const scheduleId = randomIntBetween(1, 9999);
  const url = `http://localhost:80/concerts/${concertId}/schedules/${scheduleId}/seats`;
  return getResponse(url);
};

const issueToken = () => {
  const url = "http://localhost:80/queues/issue";
  const payload = {
    customerId : randomIntBetween(1, 1000)
  };
  const params = {};
  return postResponse(url, payload, params);
};

const postResponse = (url = '', payload = {}, params = {}) =>
    http.post(
        url,
        JSON.stringify(payload),
        {
            ...params,
            headers: { 'Content-Type': 'application/json' },
        }
    );

const putResponse = (url = '', payload = {}, params = {}) =>
    http.put(
        url,
        JSON.stringify(payload),
        {
            ...params,
            headers: { 'Content-Type': 'application/json' },
        }
    );
