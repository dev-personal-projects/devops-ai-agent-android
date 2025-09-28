# devops-ai-agent-android

## 🛠️ Tech Stack

- **Language**: Kotlin
- **Architecture**: MVVM Architecture
- **UI**: Jetpack Compose
- **Dependency Injection**: Hilt
- **Networking**: Retrofit, OkHttp

# **Configure `keys.properties`**

Create a file named `keys.properties` in the root directory with the following content:

```properties
# Backend API URL
BACKEND_URL=https://your-backend-url.com

```

### Explanation of Properties

- `BACKEND_URL`: The base URL for the backend API

### Ensure the File is Ignored in Version Control

To prevent accidental commits of sensitive information, make sure `keys.properties` is included in
the `.gitignore` file:

```gitignore
keys.properties
```

### How the File is Used in `build.gradle.kts`

The properties are loaded in the build script and used to configure the backend URL and signing
configuration:

```kotlin
import java.util.Properties

val keysFile = rootProject.file("keys.properties")
val keysProps = Properties()
if (keysFile.exists()) {
    keysFile.inputStream().use { keysProps.load(it) }
}

// Provide a fallback if the property is missing
val backendUrlFromKeys: String = keysProps.getProperty("BACKEND_URL")
    ?: throw GradleException("BACKEND_URL not found in keys.properties")


android {
    // others code

        // Expose BACKEND_URL to BuildConfig
        buildConfigField("String", "BACKEND_URL", "\"$backendUrlFromKeys\"")
    }

 buildFeatures {
        compose = true
        buildConfig = true
    }

```
