package com.io.hhplus.concert.domain.queue;

import com.io.hhplus.concert.common.GlobalConstants;
import com.io.hhplus.concert.common.enums.ResponseMessage;
import com.io.hhplus.concert.common.exceptions.CustomException;
import com.io.hhplus.concert.common.utils.DateUtils;
import com.io.hhplus.concert.domain.queue.dto.BankCounterQueueTokenInfo;
import com.io.hhplus.concert.domain.queue.model.QueueToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final QueueTokenRepository queueTokenRepository;

    /**
     * 대기열 토큰 발급 및 조회
     * @param command 대기열 토큰 발급 및 조회 command
     * @return 응답 정보
     */
    public BankCounterQueueTokenInfo issueToken(TokenCommand.IssueTokenBankCounterCommand command) {
        // 토큰 조회
        Optional<QueueToken> optionalQueueToken = queueTokenRepository.findLatestQueueToken(command.getCustomerId())
                .filter(queueToken -> queueToken.isWaiting() || queueToken.isActive());

        QueueToken queueToken;
        if (optionalQueueToken.isPresent()) {
            queueToken = optionalQueueToken.get();
        } else {
            Long activeQueueTokenCount = queueTokenRepository.countActiveQueueToken();
            queueToken = queueTokenRepository.saveQueueToken(QueueToken.enter(command).updateStatusBasedOnCount(activeQueueTokenCount));
        }

        // 대기인원 (앞으로 남은 사람)
        long firstQueue = queueTokenRepository.findFirstWaitingQueueToken().map(QueueToken::queueTokenId).orElse(0L);
        long waitingCountAhead = queueToken.calculateWaitingAhead(firstQueue);
        // 대기인원 (뒤로 남은 사람)
        long lastQueue = queueTokenRepository.findLastWaitingQueueToken().map(QueueToken::queueTokenId).orElse(0L);
        long waitingCountBehind =  queueToken.calculateWaitingBehind(lastQueue);

        return BankCounterQueueTokenInfo.of(queueToken, waitingCountAhead, waitingCountBehind);
    }

    /**
     * 대기열 토큰 활성화 (Scheduler)
     */
    public void activateToken() {
        // 활성화된 대기열 토큰 수
        long activeQueueTokenCount = queueTokenRepository.countActiveQueueToken();
        long upcomingActiveCount = QueueToken.calculateUpcomingActiveTokenCount(activeQueueTokenCount);
        if (upcomingActiveCount == 0) {
            log.warn("현재 대기 중인 토큰이 없습니다.");
            return;
        }

        Pageable pageable = PageRequest.of(0, (int) upcomingActiveCount);
        List<QueueToken> upcomingActiveQueueTokens = queueTokenRepository.findUpcomingActiveQueueTokens(pageable);
        if (upcomingActiveQueueTokens.isEmpty()) {
            log.warn("현재 대기 중인 토큰이 없습니다.");
            return;
        }

        List<QueueToken> activeQueues = queueTokenRepository.saveAllQueueToken(
                upcomingActiveQueueTokens.stream()
                        .map(QueueToken::activate)
                        .collect(Collectors.toList())
        );
        log.info("총 {}개의 토큰이 활성화되었습니다.", activeQueues.size());
    }

    /**
     * 대기열 토큰 만료 (Scheduler)
     */
    public void expireToken() {
        Date expirationDate = DateUtils.subtractSeconds(DateUtils.getSysDate(), GlobalConstants.MAX_DURATION_OF_ACTIVE_QUEUE_IN_SECONDS);
        long upcomingExpiredTokenCount = queueTokenRepository.countUpcomingExpiredToken(expirationDate);
        if (upcomingExpiredTokenCount == 0) {
            log.warn("현재 활성화 상태인 토큰이 없습니다.");
            return;
        }

        int pageUnit = 100;
        Pageable pageable = PageRequest.of(0, (int) pageUnit);
        while (upcomingExpiredTokenCount > 0) {
            List<QueueToken> upcomingActiveQueueTokens = queueTokenRepository.saveAllQueueToken(
                    queueTokenRepository.findUpcomingExpiredQueueTokens(pageable)
                            .stream().map(QueueToken::expire)
                            .collect(Collectors.toList())
            );
            upcomingExpiredTokenCount -= upcomingActiveQueueTokens.size();
        }

        log.info("총 {}개의 토큰이 만료되었습니다.", upcomingExpiredTokenCount);
    }

    /**
     * 대기열 개별 토큰 검증
     * @param token 대기열 개별 토큰
     */
    public void validateIndividualToken(UUID token) {
        QueueToken queueToken = queueTokenRepository.findQueueToken(token).orElseThrow(() -> new CustomException(ResponseMessage.TOKEN_NOT_FOUNT));
        if (!queueToken.isActive()) throw new CustomException(ResponseMessage.QUEUE_STATUS_INVALID);
    }

    /**
     * 대기열 개별 토큰 만료
     * @param token 대기열 개별 토큰
     * @return 응답 정보
     */
    public BankCounterQueueTokenInfo expireIndividualToken(UUID token) {
        QueueToken queueToken = queueTokenRepository.findQueueToken(token).orElseThrow(() -> new CustomException(ResponseMessage.TOKEN_NOT_FOUNT));
        return BankCounterQueueTokenInfo.of(
                queueTokenRepository.saveQueueToken(queueToken.expire()), 0L, 0L
        );
    }
}
