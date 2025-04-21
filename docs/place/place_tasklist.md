## ✅ Android Client Task List

### 🗺️ 1. Google Maps Integration
- [x] Set up Google Maps SDK with `google-services.json` and API key
- [x] Request runtime permissions (`ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION`)
- [x] Display current user location using `FusedLocationProviderClient`
- [x] Render markers on the map

---

### 📍 2. Nearby Place Recommendations
- [ ] Calculate radius based on current GPS coordinates
- [ ] Call backend API: `GET /api/places/recommendations`
- [ ] Display recommended places as map markers
- [ ] Show place details in a BottomSheet or Dialog on marker click

---

### 💾 3. Save My Places Feature
- [ ] Add "Save to My Places" button in the place detail view
- [ ] Call backend API: `POST /api/user/places`
- [ ] Update local UI or cache on success
- [ ] Visually differentiate user-saved markers from recommended ones

---

### 📚 4. My Places Management
- [ ] Create "My Places" screen with map/list toggle
- [ ] Implement sorting options (by distance, name, or recently added)
- [ ] Implement place edit/delete functionality (`PUT`, `DELETE` APIs)
- [ ] Allow users to add tags, categories, and notes to saved places

---

### 🧭 5. Location & Permission Handling
- [ ] Handle runtime permission flow using `ActivityResultLauncher`
- [ ] Graceful handling for denied permissions (fallback UI/message)
- [ ] Auto-refresh nearby places and saved places on location change

---

### 🧪 6. UX & Error Handling
- [ ] Show empty state if no places are found nearby
- [ ] Handle network errors (e.g., show Snackbar or retry option)
- [ ] Confirm save/delete actions via Snackbar or Toast

---

### 🌐 7. API Integration (Retrofit + Kotlin)
- [ ] Define Retrofit service interfaces
- [ ] Attach Firebase Auth token to requests
- [ ] Create `data class` models for API responses
- [ ] Centralize error handling using `Result` or `sealed class`

---

### 🎨 8. UI & Design
- [ ] Apply Material Design (with Jetpack Compose)
- [ ] Use BottomSheet for place detail and save actions
- [ ] Use custom map marker icons (differentiate recommended vs saved)
- [ ] Design responsive cards for the list view

---

### 🔐 9. User Authentication
- [ ] Integrate Firebase Authentication (email, Google sign-in, etc.)
- [ ] Retrieve user token and attach it to API headers
- [ ] Maintain logged-in session and locally store user info

---

### 📦 10. Extras / Optional
- [ ] Prevent duplicate saves of the same place
- [ ] Implement clustering for overlapping markers
- [ ] Support dark mode theming

---
