package com.io.hhplus.concert.common.utils;

import com.io.hhplus.concert.common.enums.ConcertStatus;
import com.io.hhplus.concert.domain.concert.ConcertRepository;
import com.io.hhplus.concert.domain.concert.model.Concert;
import com.io.hhplus.concert.domain.concert.model.ConcertSchedule;
import com.io.hhplus.concert.domain.concert.model.ConcertSeat;
import com.io.hhplus.concert.domain.customer.CustomerRepository;
import com.io.hhplus.concert.domain.customer.model.Customer;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Component
public class TestDataInitializer {

    private final CustomerRepository customerRepository;
    private final ConcertRepository concertRepository;

    public TestDataInitializer(CustomerRepository customerRepository, ConcertRepository concertRepository) {
        this.customerRepository = customerRepository;
        this.concertRepository = concertRepository;
    }

    private List<Customer> dummyCustomers;
    private List<Concert> dummyConcerts;
    private List<ConcertSchedule> dummyConcertSchedules;
    private List<ConcertSeat> dummyConcertSeats;

    public void initializeTestData() {
        // Customer Dummy 데이터 생성
        dummyCustomers = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            dummyCustomers.add(
                    customerRepository.saveCustomer(Customer.builder()
                            .customerName("고객" + i)
                            .customerUuid(UUID.randomUUID())
                            .pointBalance(BigDecimal.valueOf(((int) (Math.random() * 100)) * 1000))
                            .build())
            );
        }

        // Concert, ConcertSchedules, ConcertSeats Dummy 데이터 생성
        dummyConcerts = new ArrayList<>();
        dummyConcertSchedules = new ArrayList<>();
        dummyConcertSeats = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Concert concert = concertRepository.saveConcert(
                    Concert.builder()
                            .concertName("항해플러스 코치님 팬미팅" + i)
                            .artistName("코치" + i)
                            .concertStatus(i % 2 == 0 ? ConcertStatus.AVAILABLE : ConcertStatus.NOT_AVAILABLE)
                            .bookBeginAt(DateUtils.addMinutes(DateUtils.getSysDate(), i))
                            .bookEndAt(DateUtils.addDays(DateUtils.getSysDate(), i))
                            .build()
            );
            for (int j = 1; j <= 10; j++) {
                ConcertSchedule concertSchedule = concertRepository.saveConcertSchedule(
                        ConcertSchedule.builder()
                                .concertId(concert.concertId())
                                .performedAt(i % 2 == 0 ? DateUtils.getSysDate() : DateUtils.subtractMinutes(DateUtils.getSysDate(), -1))
                                .build()
                );
                ConcertSeat concertSeat = concertRepository.saveConcertSeat(
                        ConcertSeat.builder()
                                .concertId(concert.concertId())
                                .concertScheduleId(concertSchedule.concertScheduleId())
                                .seatCapacity(((long) (Math.random() * 100)) * 1000)
                                .seatPrice(BigDecimal.valueOf(((int) (Math.random() * 100)) * 100))
                                .build()
                );
                dummyConcertSchedules.add(concertSchedule);
                dummyConcertSeats.add(concertSeat);
            }
        }
    }

}
