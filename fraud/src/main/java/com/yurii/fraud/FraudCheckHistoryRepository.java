package com.yurii.fraud;

import org.springframework.data.jpa.repository.JpaRepository;

interface FraudCheckHistoryRepository extends JpaRepository<FraudCheckHistory,Integer> {

}
