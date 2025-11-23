# OrderViewerEndSemesterApp

Android ERP-style sample that authenticates with Firebase and redirects users to an in-app image gallery after login. The project reuses the existing database schema and backend services; only the post-login experience was adjusted to surface gallery content.

## Features
- Email/password authentication backed by Firebase Authentication, with inline validation feedback during login.
- Automatic session resume that skips the login screen when a Firebase user is already authenticated.
- Grid-based gallery screen populated from bundled drawable resources with localized titles and accessible content descriptions.
- Tap any gallery tile to view the full image in a modal dialog.

## Getting started
1. **Requirements**: Android Studio Flamingo (or newer), Android SDK 24+, and a connected emulator or device.
2. **Firebase config**: The repo includes `app/google-services.json` for Firebase Authentication. Replace it with your own project configuration if you use a different Firebase project.
3. **Open the project**: Import the repository root into Android Studio and let it sync Gradle dependencies.
4. **Run**: Use **Run ▶ Run 'app'** to install and launch on your target device.

## Usage
- Launching the app opens the **StartLoginActivity**, where you can sign in with email and password stored in your Firebase Authentication project.
- Successful login takes you directly to the **GalleryActivity** grid. Tapping a card opens an enlarged view of that image.
- To test the sign-up flow locally, use the existing **RegistrationActivity** entry point from the login screen (the "Register" button).

## Key code references
- **Login flow**: `app/src/main/java/com/example/domartorders/StartLoginActivity.java`
- **Gallery screen**: `app/src/main/java/com/example/domartorders/GalleryActivity.java`, `app/src/main/java/com/example/domartorders/GalleryAdapter.java`, and layout resources under `app/src/main/res/layout/`.
- **Localization**: Gallery labels and content descriptions live in `app/src/main/res/values/strings.xml`.

## Testing
No automated tests are included. Build and run the app in Android Studio to verify login and gallery behavior.
