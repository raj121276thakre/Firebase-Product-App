Admin App
This is a Kotlin-based Android application designed for administrators to manage products. The app allows admins to upload products to Firebase, display the products in an activity, and delete products when necessary.

Features
Upload Product: Admins can upload product details to Firebase.
Display Products: Products stored in Firebase are displayed in a list within the app.
Delete Product: Admins can delete products from Firebase.
Technologies Used
Kotlin
Firebase (Database, Storage, Auth)
RecyclerView
Glide
ViewBinding
Setup and Installation
Clone the repository:

bash
Copy code
git clone https://github.com/raj121276thakre/Firebase-Product-App.git
cd Firebase-Product-App
Open the project in Android Studio.

Add the following dependencies to your build.gradle file:

groovy
Copy code
implementation("com.google.firebase:firebase-database-ktx")
implementation("com.google.firebase:firebase-storage-ktx")
implementation("com.google.firebase:firebase-auth-ktx")
implementation("androidx.recyclerview:recyclerview:1.2.1")
implementation("com.github.bumptech.glide:glide:4.12.0")
implementation("androidx.viewbinding:viewbinding:4.1.1")
Sync the project with Gradle files.

Set up Firebase:

Go to the Firebase Console.
Create a new project or use an existing one.
Add your Android app to the project and follow the setup instructions.
Download the google-services.json file and place it in the app directory of your project.
Enable Firebase Database, Storage, and Authentication in the Firebase Console.
Usage
Sign In: The admin must sign in to access the product management features.
Upload Product: Navigate to the upload section and fill in the product details. Click on the "Upload" button to store the product in Firebase.
Display Products: The main activity will display a list of products fetched from Firebase.
Delete Product: Long press on a product in the list to delete it from Firebase.
Screenshots
