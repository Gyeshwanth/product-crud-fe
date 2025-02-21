🛒 **Product Management CRUD Application**

## Overview

This is a **Spring Boot 3** application deployed as an **AWS Lambda** function using **API Gateway** as an entry point. It performs **CRUD (Create, Read, Update, Delete)** operations for product management. The application uses **H2 database** as an in-memory database.

## 🧭 Technology Stack

- **Backend**: Spring Boot 3, Java 17
- **Database**: H2 (In-memory)
- **AWS Services**: Lambda, API Gateway, S3 (for large JAR files)
- **Build Tool**: Maven

## 1️⃣ AWS Lambda and StreamLambdaHandler Class

### What is AWS Lambda?
AWS Lambda is a **serverless computing service** that lets you run code without provisioning or managing servers. It automatically scales based on the number of requests and charges only for the compute time consumed.

### StreamLambdaHandler Class
This is the main handler class that integrates **AWS Lambda** with **Spring Boot** using `SpringBootLambdaContainerHandler`.

```java
package com.yeshwanth.pmca;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamLambdaHandler implements RequestStreamHandler {
    private static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

    static {
        try {
            handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(ProductManagementCrudApplication.class);
        } catch (ContainerInitializationException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not initialize Spring Boot application", e);
        }
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context)
            throws IOException {
        handler.proxyStream(inputStream, outputStream, context);
    }
}
```

## 2️⃣ CRUD Operations in Product Management Application

The application provides REST APIs to perform CRUD operations on products.

### Endpoints

| HTTP Method | Endpoint       | Description          |
| ----------- | -------------- | -------------------- |
| GET         | /products      | Fetch all products   |
| GET         | /products/{id} | Get a product by ID  |
| POST        | /products      | Create a new product |
| PUT         | /products/{id} | Update a product     |
| DELETE      | /products/{id} | Delete a product     |

## 3️⃣ H2 Database Configuration

H2 is used as an **in-memory database** for development.

### H2 Dependency in `pom.xml`
```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

### H2 Configuration in `application.yml`
```properties
spring:
  :application:
      name: Product_Management_CRUD_Application


  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: root
    password: root
  h2:
    console:
      enabled: true
      settings:
        web-allow-others: true
      path: /h2-console
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate:
      ddl-auto: update
    show-sql: true



server:
  port: 2002

```

## 4️⃣ Maven Configuration and GitHub Repository

### Maven Dependencies for AWS Lambda
```xml
<dependencies>
    <dependency>
        <groupId>com.amazonaws.serverless</groupId>
        <artifactId>aws-serverless-java-container-springboot3</artifactId>
        <version>2.1.1</version>
    </dependency>
</dependencies>
```

### Maven Shade Plugin for JAR Creation
```xml
 <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>3.6.0</version>
                <configuration>
                    <createDependencyReducedPom>false</createDependencyReducedPom>
                </configuration>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>
                        <configuration>
                            <artifactSet>
                                <excludes>
                                    <exclude>org.apache.tomcat.embed:*</exclude>
                                </excludes>
                            </artifactSet>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
```

🔗 **GitHub Repository for If Want Create AWS Lambda Using Maven Archetype:**  
[https://github.com/Gyeshwanth/sb-aws-lambda](https://github.com/Gyeshwanth/sb-aws-lambda)

## 5️⃣ Steps to Deploy the Application to AWS Lambda

### Step 1: Build the Application
```sh
mvn clean package
```

### Step 2: Deploy JAR to AWS Lambda

#### Option 1: Direct Upload (JAR Size < 50MB)
1. Go to AWS Lambda Console → Click Create Function.
2. Select "Author from Scratch".
3. Set Function Name (e.g., `SpringBootLambda`).
4. Select Runtime: Java 17.
5. Click Create Function.
6. Scroll down to Code → Click Upload.
7. Select `target/Product_Management_CRUD_Application-0.0.1-SNAPSHOT.jar`.
8. Click Deploy.

#### Option 2: Upload to S3 (JAR Size > 50MB) 
1. Upload the JAR to an **S3 bucket**.
2. Go to AWS Lambda Console → Select Function.
3. Click Code → Select Upload from S3.
4. Enter the **S3 bucket URL** and click Deploy.

## 6️⃣ Deploy API Gateway for AWS Lambda

1. **Create API Gateway** → Select "REST API" → Click Build.
2. **Configure Resource and Methods** → Create `{proxy+}` resource and enable CORS.
3. **Deploy API Gateway** → Deploy to stage `dev` and copy API URL.

https://9tlib4ddd6.execute-api.ap-south-1.amazonaws.com/DEV

![Screenshot (97)](https://github.com/user-attachments/assets/44055625-2f78-4a6d-96ba-e8a4dcd602f1)


## 7️⃣ Configure Angular for API Gateway

Modify `src/environments/environment.ts` to use API Gateway URL:

```ts
export const environment = {
  production: true,
  apiBaseUrl: 'https://9tlib4ddd6.execute-api.ap-south-1.amazonaws.com/DEV'
};
```




# Deploying an Angular Project to Amazon S3

## 1. Build the Angular Project
To create a production-ready build of your Angular project, run the following command:
```sh
ng build --prod
```
This will generate a `dist/` directory containing your optimized production files.

## 2. Configure AWS S3
### Create an S3 Bucket:
1. Log in to the **AWS Management Console**.
2. Navigate to **S3** and click on **Create bucket**.
3. Enter a unique bucket name and select a region.
4. **Make the bucket publicly accessible** by adjusting the permissions.

### Configure Bucket Policy:
1. Go to the **Permissions** tab in your S3 bucket.
2. Click on **Bucket Policy** and add the following policy:
```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "PublicReadGetObject",
      "Effect": "Allow",
      "Principal": "*",
      "Action": "s3:GetObject",
      "Resource": "arn:aws:s3:::your-bucket-name/*"
    }
  ]
}
```
3. Save the changes.

## 3. Upload Files to S3
Use the AWS CLI to upload your files:
```sh
aws s3 sync dist/your-project-name s3://your-bucket-name
```

## 4. Configure S3 for Static Website Hosting
1. Go to the **Properties** tab of your S3 bucket.
2. Scroll down to **Static website hosting** and enable it.
3. Set the **index document** to `index.html`.
4. Set the **error document** to `index.html` (to handle Angular routing).

## 5. Access Your Deployed Application
Once deployment is complete, you can access your application at:
```
http://yeshwanth-product-fe.s3-website.ap-south-1.amazonaws.com/products
```

---

## Detailed Documentation

### Project Overview
- **Description:** [PRODUCT-CRUD]  
- **Technologies Used:** Angular, TypeScript, AWS S3  

### File Structure
- `src/app/service/product.service.ts` - Service for product-related operations.
- `src/environments/environment.ts` - Development environment configuration.
- `src/environments/environment.prod.ts` - Production environment configuration.
- `angular.json` - Angular project configuration file.

### Environment Configuration
- **Development/local:** Uses `http://localhost:2002` as the API base URL.
- **Production:** Uses `https://9tlib4ddd6.execute-api.ap-south-1.amazonaws.com/DEV` as the API base URL.

### Deployment Steps
1. **Build the Project:**
   ```sh
   ng build --prod
   ```
2. **Create and Configure S3 Bucket:**
   - Create a new S3 bucket.
   - Set the bucket to be publicly accessible.
   - Add a bucket policy for public read access.
     
3. **Upload Files to S3:**
   ```sh
   aws s3 sync dist/your-project-name s3://your-bucket-name
   ```
4. **Enable Static Website Hosting:**
   - Set the index document to `index.html`.
   - Set the error document to `index.html`.

### Accessing the Application
- **URL:** http://yeshwanth-product-fe.s3-website.ap-south-1.amazonaws.com   



![Screenshot (95)](https://github.com/user-attachments/assets/78de2e16-df07-4556-9b9f-5130b762f58a)
![Screenshot (96)](https://github.com/user-attachments/assets/425b285b-5db6-48ad-9a53-4a8c02c407d0)












