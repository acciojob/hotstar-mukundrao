package com.driver.repository;

import com.driver.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface SubscriptionRepository extends JpaRepository<Subscription,Integer> {

}
