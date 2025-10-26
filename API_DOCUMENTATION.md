# API Documentation

## Base URL
```
http://localhost:8080/api
```

## Authentication
Currently, the API does not require authentication. In production, implement JWT or OAuth2.

## Contest Endpoints

### GET /contests/{contestId}
Retrieve contest details with problems.

**Parameters:**
- `contestId` (path, string): Unique contest identifier

**Response:**
```json
{
  "id": 1,
  "contestId": "CONTEST001",
  "name": "Spring Coding Challenge",
  "description": "A coding contest featuring algorithmic problems",
  "startTime": "2025-01-01T00:00:00",
  "endTime": "2025-12-31T23:59:59",
  "problems": [
    {
      "id": 1,
      "title": "Sum of Two Numbers",
      "description": "Calculate the sum of two integers",
      "sampleInput": "5 3",
      "sampleOutput": "8",
      "timeLimit": 5,
      "memoryLimit": 128
    }
  ]
}
```

**Error Responses:**
- `404 Not Found`: Contest not found
- `500 Internal Server Error`: Server error

## Submission Endpoints

### POST /submissions
Submit code for evaluation.

**Request Body:**
```json
{
  "contestId": "CONTEST001",
  "problemId": 1,
  "username": "john_doe",
  "code": "import java.util.Scanner;\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int a = sc.nextInt();\n        int b = sc.nextInt();\n        System.out.println(a + b);\n    }\n}",
  "language": "java"
}
```

**Response:**
```json
{
  "submissionId": "uuid-string",
  "status": "PENDING",
  "message": "Submission queued for evaluation"
}
```

**Error Responses:**
- `400 Bad Request`: Invalid request data
- `404 Not Found`: Contest or problem not found
- `500 Internal Server Error`: Server error

### GET /submissions/{submissionId}
Get submission status and result.

**Parameters:**
- `submissionId` (path, string): Unique submission identifier

**Response:**
```json
{
  "submissionId": "uuid-string",
  "status": "COMPLETED",
  "result": "ACCEPTED",
  "executionTime": 1500,
  "memoryUsed": 64,
  "errorMessage": null,
  "submittedAt": "2025-01-01T12:00:00"
}
```

**Status Values:**
- `PENDING`: Submission queued for evaluation
- `RUNNING`: Code is being executed
- `COMPLETED`: Evaluation finished

**Result Values:**
- `ACCEPTED`: All test cases passed
- `WRONG_ANSWER`: Output doesn't match expected
- `TIME_LIMIT_EXCEEDED`: Execution exceeded time limit
- `MEMORY_LIMIT_EXCEEDED`: Execution exceeded memory limit
- `RUNTIME_ERROR`: Code crashed during execution
- `COMPILATION_ERROR`: Code failed to compile

**Error Responses:**
- `404 Not Found`: Submission not found
- `500 Internal Server Error`: Server error

## Leaderboard Endpoints

### GET /contests/{contestId}/leaderboard
Get live leaderboard for a contest.

**Parameters:**
- `contestId` (path, string): Unique contest identifier

**Response:**
```json
[
  {
    "username": "john_doe",
    "totalScore": 300,
    "submissionCount": 3,
    "acceptedCount": 3,
    "lastSubmissionTime": "2025-01-01T12:30:00"
  },
  {
    "username": "jane_smith",
    "totalScore": 200,
    "submissionCount": 5,
    "acceptedCount": 2,
    "lastSubmissionTime": "2025-01-01T12:25:00"
  }
]
```

**Scoring System:**
- Each accepted submission: 100 points
- Total score = acceptedCount × 100
- Leaderboard sorted by total score (descending), then by last submission time (ascending)

**Error Responses:**
- `404 Not Found`: Contest not found
- `500 Internal Server Error`: Server error

## Error Response Format

All error responses follow this format:
```json
{
  "error": "Error message",
  "timestamp": "2025-01-01T12:00:00",
  "path": "/api/contests/invalid-id"
}
```

## Rate Limiting

Currently, no rate limiting is implemented. In production, consider implementing:
- Submission rate limiting per user
- API call rate limiting per IP
- Resource usage limits per contest

## CORS

The API supports CORS for the frontend running on `http://localhost:3000`.

## Health Check

### GET /actuator/health
Check application health status.

**Response:**
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP"
    },
    "diskSpace": {
      "status": "UP"
    }
  }
}
```
