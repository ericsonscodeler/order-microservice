# Java Spring Boot Microservice with RabbitMQ and MongoDB

This repository contains a microservice built with Java Spring Boot that listens to messages from a RabbitMQ queue and stores the data in a MongoDB database. Additionally, the microservice provides a REST API that returns the stored data with a summary, details, and pagination.

## Features

1. **RabbitMQ Listener**: Monitors the queue for incoming messages and stores the data in MongoDB.
2. **REST API**: Returns a summary of the stored data, detailed information, and pagination.

## Technologies Used

- Java 21
- Spring Boot
- RabbitMQ
- MongoDB
- Maven
- Docker

## API Response Structure

```json
{
	"summary": {
		"totalOnOrders": 475.00
	},
	"data": [
		{
			"orderId": 1001,
			"customerId": 1,
			"total": 120.00
		},
		{
			"orderId": 1002,
			"customerId": 1,
			"total": 355.00
		}
	],
	"pagination": {
		"page": 0,
		"pageSize": 10,
		"totalElements": 2,
		"totalPages": 1
	}
}
```

