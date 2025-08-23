# Health Tracker REST API

A comprehensive health and wellness tracking application built with modern DevOps practices, featuring complete CI/CD pipeline implementation with Jenkins and GitHub Actions.

## 🎯 Project Overview

This Health Tracker application is a robust Kotlin-based web application designed to provide essential features for effectively tracking user health and wellness. The project demonstrates modern software engineering practices including automated testing, code quality analysis, containerization, and multi-environment deployment strategies.

## 🏗️ Architecture & Tech Stack

### Backend

- **Language**: Kotlin
- **Framework**: Javalin (Lightweight web framework)
- **Database**:
  - PostgreSQL (Production)
  - H2 (Integration Testing)
- **ORM**: Jetbrains Exposed SQL Framework
- **Serialization**: Jackson (with Kotlin & Joda Time modules)
- **Build Tool**: Maven

### Frontend

- **Framework**: Vue.js 3
- **CSS Framework**: Bootstrap
- **HTTP Client**: Axios
- **UI Components**: Vue Components with Webjars

### DevOps & Infrastructure

- **CI/CD**: Jenkins Pipeline + GitHub Actions
- **Containerization**: Docker
- **Container Registry**: Docker Hub
- **Security Scanning**: Trivy
- **Code Quality**: SonarQube
- **Code Coverage**: JaCoCo
- **Documentation**: KDoc (Dokka)
- **Cloud Platforms**:
  - Railway.com (Primary deployment)
  - AWS EC2 (Secondary deployment)

### Testing

- **Unit Testing**: JUnit 5, Kotlin Test
- **Integration Testing**: Unirest, H2 Database
- **UI Testing**: Selenium WebDriver
- **Mocking**: Built-in Kotlin mocking capabilities
- **Assertions**: AssertJ

## 🚀 Features

- **👥 User Management**: Complete CRUD operations for user profiles
- **🏃 Activity Tracking**: Log and monitor physical activities with duration and calories
- **📊 BMI Calculator**: Calculate and track Body Mass Index over time
- **💧 Water Intake Monitoring**: Track daily water consumption
- **😴 Sleep Tracking**: Monitor sleep patterns and duration
- **💡 Health Tips**: Access curated health and wellness advice

## 📋 API Endpoints

### Users

- `GET /api/users` - Get all users
- `GET /api/users/{user-id}` - Get user by ID
- `GET /api/users/email/{email}` - Get user by email
- `POST /api/users` - Create new user
- `PATCH /api/users/{user-id}` - Update user
- `DELETE /api/users/{user-id}` - Delete user

### Activities

- `GET /api/activities` - Get all activities
- `GET /api/activities/{act-id}` - Get activity by ID
- `GET /api/activities/users/{user-id}` - Get activities by user
- `POST /api/activities` - Create new activity
- `PATCH /api/activities/{act-id}` - Update activity
- `DELETE /api/activities/{act-id}` - Delete activity

### Water Intake

- `GET /api/water` - Get all water intake records
- `GET /api/water/{wat-id}` - Get water intake by ID
- `GET /api/water/users/{user-id}` - Get water intake by user
- `POST /api/water` - Create new water intake record
- `PATCH /api/water/{wat-id}` - Update water intake
- `DELETE /api/water/{wat-id}` - Delete water intake

### BMI

- `GET /api/bmi` - Get all BMI records
- `GET /api/bmi/{bmi-id}` - Get BMI by ID
- `GET /api/bmi/users/{user-id}` - Get BMI records by user
- `POST /api/bmi` - Calculate and save BMI
- `DELETE /api/bmi/{bmi-id}` - Delete BMI record

### Sleep

- `GET /api/sleep` - Get all sleep records
- `GET /api/sleep/{slp-id}` - Get sleep record by ID
- `GET /api/sleep/users/{user-id}` - Get sleep records by user
- `POST /api/sleep` - Create new sleep record
- `PATCH /api/sleep/{slp-id}` - Update sleep record
- `DELETE /api/sleep/{slp-id}` - Delete sleep record

### Health Tips

- `GET /api/healthTips` - Get all health tips
- `GET /api/healthTips/{hth-id}` - Get health tip by ID
- `POST /api/healthTips` - Create new health tip
- `PATCH /api/healthTips/{hth-id}` - Update health tip
- `DELETE /api/healthTips/{hth-id}` - Delete health tip

## 🔄 CI/CD Pipeline

### Jenkins Pipeline (Primary)

The Jenkins pipeline implements a comprehensive CI/CD workflow with the following stages:

#### 1. **Checkout Stage**

```groovy
stage('Checkout') {
    steps {
        git url: 'https://github.com/deepanshub9/healthtracker14.git', branch: 'main'
    }
}
```

- Clones the repository from GitHub
- Ensures latest code is available for build

#### 2. **Build JAR Stage**

```groovy
stage('Build JAR') {
    steps {
        sh 'mvn clean install -DskipTests'
    }
}
```

- Compiles Kotlin source code
- Creates executable JAR with dependencies
- Skips tests for faster build (tests run separately)

#### 3. **Run Tests Stage**

```groovy
stage('Run Tests') {
    steps {
        sh 'mvn test -Dmaven.test.failure.ignore=true'
    }
}
```

- Executes unit and integration tests
- Generates test reports in `target/surefire-reports`
- Continues pipeline even if some tests fail for analysis

#### 4. **Code Coverage Report Stage**

```groovy
stage('Code Coverage Report') {
    steps {
        sh 'mvn jacoco:report'
    }
}
```

- Generates JaCoCo code coverage reports
- Creates XML and HTML coverage reports
- Outputs to `target/site/jacoco/`

#### 5. **SonarQube Analysis Stage**

```groovy
stage('SonarQube Analysis') {
    environment {
        scannerHome = tool 'SonarQubeScanner'
    }
    steps {
        withSonarQubeEnv('sonarqube') {
            sh '''
                mvn sonar:sonar \
                -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml \
                -Dsonar.junit.reportPaths=target/surefire-reports
            '''
        }
    }
}
```

- Performs static code analysis
- Analyzes code quality, bugs, and vulnerabilities
- Integrates test coverage and test results
- Publishes results to SonarQube dashboard

#### 6. **Build Docker Image Stage**

```groovy
stage('Build Docker Image') {
    steps {
        script {
            def IMAGE_TAG = "${BUILD_NUMBER}"
            docker.build("${IMAGE_NAME}:${IMAGE_TAG}")
            docker.build("${IMAGE_NAME}:latest")
        }
    }
}
```

- Creates Docker image with application JAR
- Tags with build number and 'latest'
- Uses multi-stage Docker build for optimization

#### 7. **Trivy Security Scan Stage**

```groovy
stage('Trivy Scan') {
    steps {
        script {
            def IMAGE_TAG = "${BUILD_NUMBER}"
            sh "trivy image ${IMAGE_NAME}:${IMAGE_TAG}"
        }
    }
}
```

- Scans Docker image for security vulnerabilities
- Checks for known CVEs in base image and dependencies
- Provides security compliance validation

#### 8. **Push Docker Image Stage**

```groovy
stage('Push Docker Image') {
    steps {
        script {
            def IMAGE_TAG = "${BUILD_NUMBER}"
            docker.withRegistry(DOCKER_REGISTRY, DOCKER_CREDENTIALS) {
                docker.image("${IMAGE_NAME}:${IMAGE_TAG}").push()
                docker.image("${IMAGE_NAME}:latest").push()

                // Clean up old images (keep only 3 latest)
                sh """
                curl -s -X GET "${DOCKERHUB_API_URL}/${IMAGE_NAME}/tags?page_size=100" | \
                jq -r '.results | sort_by(.last_updated) | reverse | .[3:] | .[].name' | \
                xargs -I {} curl -X DELETE "${DOCKERHUB_API_URL}/${IMAGE_NAME}/tags/{}" \
                    --header "Authorization: Bearer ${DOCKERHUB_ACCESS_TOKEN}"
                """
            }
        }
    }
}
```

- Pushes images to Docker Hub registry
- Implements image retention policy (keeps 3 latest versions)
- Automatically cleans up old images to save storage

#### 9. **Deploy to EC2 Stage**

```groovy
stage('Deploy to EC2') {
    steps {
        sshagent(['ec2-ssh-credentials']) {
            withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                script {
                    sh """
ssh -o StrictHostKeyChecking=no ubuntu@54.82.50.60 <<EOF
docker login -u ${DOCKER_USER} -p ${DOCKER_PASS}
docker pull ${IMAGE_NAME}:latest

docker stop health-tracker-rest || true
docker rm health-tracker-rest || true
docker run -d -p 8001:8001 --name health-tracker-rest ${IMAGE_NAME}:latest
EOF
                    """
                }
            }
        }
    }
}
```

- Deploys to AWS EC2 instance via SSH
- Performs zero-downtime deployment
- Pulls latest image and restarts container
- Exposes application on port 8001

#### 10. **Post-Build Actions**

```groovy
post {
    always {
        sh 'docker image prune -a -f'
        cleanWs()
    }
}
```

- Cleans up Docker images to free space
- Clears Jenkins workspace


The GitHub Actions workflow provides additional CI/CD capabilities:

#### 1. **Package Job**

- Builds JAR artifacts using Maven
- Caches dependencies for faster builds
- Uploads build artifacts for other jobs

#### 2. **Trivy Security Scan Job**

- Downloads and installs Trivy scanner
- Performs vulnerability scanning on filesystem
- Fails pipeline on HIGH/CRITICAL vulnerabilities

#### 3. **Documentation Job**

- Generates KDoc documentation using Dokka
- Archives documentation artifacts
- Provides API documentation for developers

#### 4. **Test Job**

- Runs comprehensive test suite
- Includes unit and integration tests
- Verifies code quality and functionality

#### 5. **Deploy Job**

- Deploys to Railway.com platform
- Uses Railway CLI for deployment
- Triggered only after successful tests

## 🐳 Docker Configuration

### Dockerfile

```dockerfile
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copy the fat JAR into the container
COPY . .

# Expose the port used by your application
EXPOSE 8001

# Run the application
CMD ["java", "-jar", "target/health-tracker-rest-1.0-SNAPSHOT-jar-with-dependencies.jar"]
```

- Uses Eclipse Temurin JDK 21 as base image
- Configures working directory as `/app`
- Exposes port 8001 for web traffic
- Runs the fat JAR with all dependencies included

## 🔧 Local Development Setup

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL (optional, H2 used for testing)
- Docker (optional, for containerized development)

### Getting Started

1. **Clone the repository**:

   ```bash
   git clone https://github.com/deepanshub9/healthtracker14.git
   cd healthtracker14
   ```

2. **Build the application**:

   ```bash
   mvn clean install
   ```

3. **Run tests**:

   ```bash
   mvn test
   ```

4. **Generate code coverage report**:

   ```bash
   mvn jacoco:report
   ```

5. **Run the application**:

   ```bash
   java -jar target/health-tracker-rest-1.0-SNAPSHOT-jar-with-dependencies.jar
   ```

6. **Access the application**:
   - Web Interface: http://localhost:8001
   - API Base URL: http://localhost:8001/api
   - Health Check: http://localhost:8001/api/users

### Development with Docker

1. **Build Docker image**:

   ```bash
   docker build -t health-tracker-rest .
   ```

2. **Run container**:

   ```bash
   docker run -d -p 8001:8001 --name health-tracker health-tracker-rest
   ```

3. **View logs**:
   ```bash
   docker logs health-tracker
   ```

## 📊 Quality Assurance

### Code Coverage

- **Tool**: JaCoCo
- **Target**: >80% line coverage
- **Reports**: Available in `target/site/jacoco/index.html`

### Static Code Analysis

- **Tool**: SonarQube
- **Metrics**: Code smells, bugs, vulnerabilities, duplication
- **Quality Gate**: Must pass before deployment

### Security Scanning

- **Tool**: Trivy
- **Scope**: Container images and filesystem
- **Policy**: No HIGH or CRITICAL vulnerabilities allowed

### Testing Strategy

- **Unit Tests**: Test individual components and functions
- **Integration Tests**: Test API endpoints and database interactions
- **UI Tests**: Test web interface functionality with Selenium
- **Mock Testing**: Isolated testing of service layers

## 🌐 Deployment

### Production Environments

#### Railway.com (Primary)

- **URL**: https://health-tracker-rest-production-6ae2.up.railway.app/
- **Database**: PostgreSQL (managed by Railway)
- **Deployment**: Automatic via GitHub Actions
- **Monitoring**: Railway dashboard

#### AWS EC2 (Secondary)

- **Server**: ubuntu@54.82.50.60
- **Port**: 8001
- **Deployment**: Automatic via Jenkins pipeline
- **Container Runtime**: Docker

### Environment Variables

```bash
PORT=8001                    # Application port
DATABASE_URL=               # PostgreSQL connection string
RAILWAY_TOKEN=              # Railway deployment token
DOCKERHUB_ACCESS_TOKEN=     # Docker Hub API token
```

## 📁 Project Structure

```
healthtracker14/
├── .github/workflows/          # GitHub Actions workflows
│   └── mvn-deploy.yml         # CI/CD pipeline configuration
├── src/
│   ├── main/kotlin/ie/setu/
│   │   ├── controllers/       # REST API controllers
│   │   ├── domain/           # Data models and entities
│   │   │   ├── db/          # Database table definitions
│   │   │   └── repository/   # Data access objects (DAOs)
│   │   ├── config/          # Application configuration
│   │   └── utils/           # Utility functions
│   └── test/kotlin/ie/setu/
│       ├── controllers/      # Controller tests
│       ├── repository/       # DAO tests
│       └── helpers/         # Test fixtures and utilities
├── htmlReport/              # JaCoCo coverage reports
├── target/                  # Maven build artifacts
├── Dockerfile              # Container configuration
├── Jenkinsfile             # Jenkins pipeline configuration
├── pom.xml                 # Maven dependencies and plugins
├── sonar-project.properties # SonarQube configuration
└── health-tracker.yaml     # OpenAPI specification
```

## 🔧 Configuration Files

### Maven (pom.xml)

- Kotlin compilation settings
- Dependency management
- Plugin configuration (JaCoCo, Dokka, Surefire)
- Fat JAR creation

### SonarQube (sonar-project.properties)

```properties
sonar.projectKey=health-tracker-rest
sonar.projectName=Health Tracker Rest API
sonar.sources=src/main
sonar.tests=src/test
sonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
```

### OpenAPI (health-tracker.yaml)

- Complete API specification
- Request/response schemas
- Endpoint documentation
- Example payloads

## 🚀 Performance & Monitoring

### Application Metrics

- Response time monitoring
- Error rate tracking
- Resource utilization
- Database connection pooling

### Logging

- Structured logging with SLF4J
- Request/response logging
- Error tracking and alerting
- Performance metrics

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Code Standards

- Follow Kotlin coding conventions
- Write comprehensive tests
- Update documentation
- Ensure CI pipeline passes

## 📞 Contact & Support

- **Developer**: Deepanshu
- **Email**: deepanshu.b096@gmail.com
- **Repository**: https://github.com/deepanshub9/healthtracker14
- **Issues**: https://github.com/deepanshub9/healthtracker14/issues

## 📄 License

This project is part of a DevOps dissertation demonstrating modern CI/CD practices and cloud-native application development.

---

_This README demonstrates comprehensive documentation practices for a modern DevOps project with complete CI/CD pipeline implementation._
