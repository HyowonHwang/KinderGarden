## 📄 Product Requirements Document (PRD)

### 📌 1. Overview

| Item | Description |
|------|-------------|
| Feature Name | Nearby Place Recommendation + Save My Places |
| Objective | Show recommended places based on the user’s current location and allow users to save and manage their favorite/custom places |
| Target Users | Anyone looking for cafes, restaurants, work/study spots, etc. based on location |
| Platforms | Android, iOS (optional Web for Admin Panel) |
| Map SDK | Google Maps SDK (Android & iOS) |

---

### 📍 2. Core Features

#### 2.1 Nearby Place Recommendation
- Show user’s current location on map
- Display recommended places (within 1 km radius) as markers
- Tap marker to show place details (name, category, rating, etc.)
- Filter by category (e.g., cafe, food, study spot, etc.)

#### 2.2 Place Detail View
- Shows place name, address, rating, photos, category, and opening hours
- Button to “Save to My Places”

#### 2.3 Save My Places
- Users can save places by:
  - Tapping on a place on the map and saving
  - Manually adding a new place with custom name, location, and note
- View saved places in list or map view
- Add tags/categories and personal notes
- Edit or delete saved places

#### 2.4 My Place List View
- Sort by recently added, distance, or name
- Toggle favorite/starred places
- Show nearby saved places at top

#### 2.5 Location Permission Handling
- Prompt location access on app start
- No background location usage by default

---

### 🎨 3. UI Layouts (Android Example)

| Screen | Main Elements |
|--------|----------------|
| Main Map View | Current location marker, recommended places, search bar, filters, access to saved places |
| Place Detail View | Full info, photo, save button, map snippet |
| Saved Places List | List/map toggle, edit/delete, search/sort |
| Add/Edit Place View | Name, coordinates, tags, notes input |

---

### 🛠 4. Tech Stack

| Component | Technology |
|----------|-------------|
| Map SDK | Google Maps SDK |
| Location | Android LocationManager, iOS CoreLocation |
| Backend | Node.js + TypeScript |
| Database | PostgreSQL or MongoDB |
| Authentication | Firebase Auth |
| Admin Panel (Optional) | Web (React + Admin UI) to manage global recommendations |

---

### 📡 5. Sample APIs

```http
GET /api/places/recommendations?lat=37.5&lng=127.1&radius=1000
POST /api/user/places
GET /api/user/places
PUT /api/user/places/:id
DELETE /api/user/places/:id
```

---

### 🧪 6. QA Checklist

- [ ] Proper handling when location permission is denied
- [ ] Empty state UX when no nearby places are found
- [ ] Prevent duplicate saved places
- [ ] Marker clustering for overlapping locations

---

### 🔮 7. Future Enhancements

- AI-driven personalized recommendations (based on time, history)
- Share saved places with friends
- User reviews and photo uploads
- Offline support / local caching

---