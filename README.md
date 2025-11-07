# Law Q&A Application

## Overview
A Spring Boot REST API that processes legal questions using Google's Gemini AI API. The application accepts legal text and questions, then returns AI-generated answers.

## Features
- Legal document question answering
- Integration with Gemini 2.5 Flash model
- RESTful API architecture
- CORS enabled for Angular frontend

## Technologies
- Java
- Spring Boot
- Spring Web
- RestTemplate
- Lombok
- Google Gemini API

## Project Structure
```
com.lawQA.lawQA/
├── Controllers/
│   └── LawController.java
└── DTOs/
    ├── LawRequest.java
    └── LawResponse.java
```

## Setup

### Prerequisites
- Java 11 or higher
- Maven
- Google Gemini API key

### Configuration
Add your Gemini API key to `application.properties`:
```properties
gemini.api.key=YOUR_API_KEY_HERE
```

### Installation
```bash
# Clone the repository
git clone <repository-url>

# Navigate to project directory
cd lawQA

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

## API Endpoints

### Ask Legal Question
**Endpoint:** `POST /api/law/ask`

**Request Body:**
```json
{
  "lawText": "The legal document or law text",
  "question": "Your question about the law"
}
```

**Response:**
```json
{
  "answer": "AI-generated answer to your question"
}
```

**Example:**
```bash
curl -X POST http://localhost:8080/api/law/ask \
  -H "Content-Type: application/json" \
  -d '{
    "lawText": "Article 1: All citizens have the right to free speech.",
    "question": "What rights do citizens have?"
  }'
```

## CORS Configuration
Currently configured to accept requests from:
- `http://localhost:4200` (Angular development server)

To modify CORS settings, update the `@CrossOrigin` annotation in `LawController.java`.

## Error Handling
The API handles the following error scenarios:
- Empty response from Gemini API
- No candidates returned by Gemini API
- Network or connection errors
- Invalid request format

All errors return appropriate HTTP status codes and error messages.

## DTOs

### LawRequest
| Field | Type | Description |
|-------|------|-------------|
| lawText | String | The legal document or law text |
| question | String | The question about the law |

### LawResponse
| Field | Type | Description |
|-------|------|-------------|
| answer | String | The AI-generated answer |

## Development

### Adding New Features
1. Create new DTOs in the `DTOs` package
2. Add new endpoints in `LawController`
3. Update this README with new documentation

### Testing
```bash
# Run tests
mvn test
```

## Deployment

### Production Configuration
Update `application.properties` for production:
```properties
# Use environment variable for API key
gemini.api.key=${GEMINI_API_KEY}

# Configure allowed origins
cors.allowed.origins=https://your-production-domain.com
```

## Troubleshooting

### Common Issues

#### API Key Error
**Problem:** "Error: Unauthorized"
**Solution:** Verify your Gemini API key is correct in `application.properties`

#### CORS Error
**Problem:** "Access to XMLHttpRequest has been blocked by CORS policy"
**Solution:** Add your frontend URL to the `@CrossOrigin` annotation

#### Empty Response
**Problem:** "Error: Empty response from Gemini API"
**Solution:** Check your internet connection and API key validity

## License
[Your License Here]

## Contributing
1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## Contact
[Your Contact Information]

## Acknowledgments
- Google Gemini API
- Spring Boot Framework
- Lombok Project
