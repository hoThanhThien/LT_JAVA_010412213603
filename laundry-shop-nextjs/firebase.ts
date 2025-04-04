// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { getAuth } from "firebase/auth";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
  apiKey: "AIzaSyAiUzwRnrvP-TC6ukjI6WUnz8l7E-sqcG8",
  authDomain: "heramo-b5a4c.firebaseapp.com",
  projectId: "heramo-b5a4c",
  storageBucket: "heramo-b5a4c.firebasestorage.app",
  messagingSenderId: "179734903401",
  appId: "1:179734903401:web:ad494138b37f8a96ac40f8",
  measurementId: "G-1Q1WR71QLW",
};

// Initialize Firebase
// Sửa lỗi cách khởi tạo ứng dụng Firebase
const app = initializeApp(firebaseConfig);
const auth = getAuth(app);
auth.useDeviceLanguage(); // Set the default language to the user's device language

export { auth };
