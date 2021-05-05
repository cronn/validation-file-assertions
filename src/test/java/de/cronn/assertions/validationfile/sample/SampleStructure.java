package de.cronn.assertions.validationfile.sample;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;
import java.util.UUID;

class SampleStructure {

	private String name;
	private String messageId;
	private Long transactionId;
	private LocalDateTime timestamp;

	public static SampleStructure filledWithConstantValues() {
		SampleStructure sampleStructure = new SampleStructure();
		sampleStructure.setName("Lorem ipsum");
		sampleStructure.setMessageId("fb8ca74b-c7a3-4031-8250-bec29280e9ad");
		sampleStructure.setTransactionId(7047158660679727112L);
		sampleStructure.setTimestamp(LocalDateTime.of(2020, 02, 02, 12, 20, 30));
		return sampleStructure;
	}

	public static SampleStructure filledWithRandomValues() {
		SampleStructure sampleStructure = new SampleStructure();
		sampleStructure.setName("Lorem ipsum");
		sampleStructure.setMessageId(UUID.randomUUID().toString());
		sampleStructure.setTransactionId(Math.abs(new Random().nextLong()));
		sampleStructure.setTimestamp(LocalDateTime.of(LocalDate.of(2020, 02, 02), LocalTime.now()));
		return sampleStructure;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "SampleStructure{" +
			"name='" + name + '\'' +
			", messageId='" + messageId + '\'' +
			", transactionId=" + transactionId +
			", timestamp=" + timestamp +
			'}';
	}
}
