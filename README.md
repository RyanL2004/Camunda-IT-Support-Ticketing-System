# 🎟️ IT Support Ticketing System

A modern, enterprise-grade IT support ticketing system built with Camunda Platform 8, Spring Boot, and MongoDB. This system provides a complete business process management solution for handling support tickets through their entire lifecycle.

## 📚 Documentation
- [Technical Report - Detailed system architecture and implementation details](https://github.com/RyanL2004/Camunda-IT-Support-Ticketing-System/blob/main/Development%20Information%20System%20Report.pdf)
- [Powerpoint Presentation](https://github.com/RyanL2004/Camunda-IT-Support-Ticketing-System/blob/main/Software%20Dev%20presentationslidesTT.pptx) </br>

## System

![System](https://github.com/user-attachments/assets/38d2a3bf-f295-4d03-9a65-9e69ceb9b667)



## 🚀 Features

- **Multi-Channel Ticket Creation**
  - Web portal interface for direct ticket submission
  - Email integration for ticket creation and updates
  - Automated ticket routing and assignment

- **Intelligent Workflow Management**
  - BPMN 2.0 process modeling
  - Dynamic task assignment
  - Automated status updates
  - SLA monitoring

- **Communication Features**
  - Automated email notifications
  - Status update notifications
  - Request for additional information workflow
  - Survey distribution system

- **Data Management**
  - MongoDB integration for ticket storage
  - Secure credential handling
  - Audit trail logging
  - Survey response collection

## 🛠️ Technology Stack
- **Language**: Java 11
- **Library**: Spring Boot
- **Process Management**: Camunda Platform 8
- **Backend Framework**: Spring Boot 2.4.3
- **Database**: MongoDB Atlas
- **Email Service**: Spring Mail with Gmail SMTP
- **Build Tool**: Maven
- **Additional Libraries**:
  - Zeebe Client
  - Commons Validator
  - Jackson for JSON processing
  - MongoDB Driver

## 📋 Prerequisites

- Java 11 or higher
- Maven 3.6+
- Mongoose, MongoDB or MongoDB 
- Camunda Platform 8 account
- Gmail account for SMTP services

## 🔧 Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/it-support-ticketing-system.git
   ```

2. Configure MongoDB connection:
   - Update `application.yml` with your MongoDB credentials
   - Set up your database collections

3. Configure email service:
   ```yaml
   spring.mail:
     host: smtp.gmail.com
     port: 587
     username: your-email@gmail.com
     password: your-app-password
   ```

4. Configure Camunda credentials:
   ```yaml
   zeebe:
     client:
       cloud:
         region: your-region
         clusterId: your-cluster-id
         clientId: your-client-id
         clientSecret: your-client-secret
   ```

5. Build the project:
   ```bash
   mvn clean install
   ```

## 🚀 Running the Application

1. Start the Email Service:
   ```bash
   cd CamundaSendEmailservices
   mvn spring-boot:run
   ```

2. Start the Ticketing System Service:
   ```bash
   cd CamundaTicketingSystemservices
   mvn spring-boot:run
   ```

3. Deploy the BPMN workflow through Camunda Platform 8 Console

## 🔐 Security

- Secure password handling
- MongoDB Atlas security features
- Email validation and sanitization
- Protected API endpoints

## 📈 Monitoring and Management

- Built-in logging system
- MongoDB data persistence
- Camunda Operate integration
- Survey feedback collection

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## 👥 Authors

- **[Rayan]** - *Initial work* - [GitHub Profile](https://github.com/RyanL2004)

## 🙏 Acknowledgments

- Camunda Community
- Spring Boot Team
- MongoDB Team

## 📞 Support

For support questions, please open an issue in the GitHub repository.
