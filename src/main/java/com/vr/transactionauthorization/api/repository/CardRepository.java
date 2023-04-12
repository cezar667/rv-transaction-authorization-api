package com.vr.transactionauthorization.api.repository;

import com.vr.transactionauthorization.api.model.Card;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends MongoRepository<Card, String> {


}
