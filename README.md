# Simple Note Test App

## Overview
A modern Android notes app built with Jetpack Compose, MVVM, Hilt, Retrofit, Moshi, Coil, and Room. Features onboarding, authentication, notes CRUD, settings, and more, matching the provided design tokens and UI specs.

## How to Run
1. Clone the repo.
2. Open in Android Studio.
3. Set your API base URL in the appropriate DI module (see `di/NetworkModule.kt`).
4. Place your assets (illustrations, icons) in `app/src/main/res/drawable/`.
5. Build and run on an emulator or device (minSdk 24).

## Project Structure
- `app/data/remote` – Retrofit interfaces, DTOs
- `app/data/local` – Room DAOs, entities
- `app/data/models` – Shared data models
- `app/data/repos` – Repository implementations
- `app/di` – Hilt DI modules
- `app/ui` – Compose UI (components, screens, theme)
- `app/util` – Utilities

## API Integration
- OpenAPI schema: https://simple.darkube.app/api/schema/redoc/
- If needed, try:
  - https://simple.darkube.app/api/schema/?format=openapi-json
  - https://simple.darkube.app/api/schema/swagger.json
  - https://simple.darkube.app/api/schema/

## Assets
- Place provided images and icons in `res/drawable/`.
- Use Coil for image loading.

## Theming
- All colors, typography, and radii are defined in `ui/theme/NotesTheme.kt`.

## Testing
- Sample unit tests for ViewModels and Repositories are in `app/src/test/java/`.

## Customization
- To swap assets, replace files in `res/drawable/`.
- To change API base URL, update the DI network module.

