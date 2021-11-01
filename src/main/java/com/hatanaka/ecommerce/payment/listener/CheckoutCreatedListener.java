package com.hatanaka.ecommerce.payment.listener;

import java.util.UUID;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.hatanaka.ecommerce.checkout.event.CheckoutCreatedEvent;
import com.hatanaka.ecommerce.payment.event.PaymentCreatedEvent;
import com.hatanaka.ecommerce.payment.steaming.CheckoutProcessor;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CheckoutCreatedListener {
	
	private final CheckoutProcessor checkoutProcessor;
	
	@StreamListener(CheckoutProcessor.INPUT)
	public void handler(CheckoutCreatedEvent event) {
		
		//vai no gateway de pagamento e processa o pagamento
		//salva os dados de pagamento no BD
		final PaymentCreatedEvent paymentCreatedEvent = PaymentCreatedEvent.newBuilder()
				.setCheckoutCode(event.getCheckoutCode())
				.setCheckoutStatus(event.getStatus())
				.setPaymentCode(UUID.randomUUID().toString())
				.build();
		checkoutProcessor.output().send(MessageBuilder.withPayload(paymentCreatedEvent).build());
	}

}
