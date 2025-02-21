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











