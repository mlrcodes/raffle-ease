package com.raffleease.payments_service.Payments.Repositories;

import com.raffleease.payments_service.Payments.Model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPaymentsRepository extends MongoRepository<Payment, String> { }