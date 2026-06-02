# Changelog

## [Unreleased]

## [0.1.0] - 2026-06-01

### Added
- `@Journey` annotation to mark a sealed interface as a navigation journey
- `@Step` annotation to mark each subclass as a navigable step
- `@Exit` annotation (repeatable) to declare typed navigation transitions between steps
- `@Piggyback` annotation (repeatable) to attach named side-effects to steps, firing on `ON_ENTER` or `ON_EXIT`
- KSP processor (`journey-kmp-ksp`) that generates typed `*Controller` interfaces, a sealed `*View` class, and a `*JourneyHost` Compose composable per journey
- `JourneyStep` base interface for all journey sealed interfaces
- `StepController` base interface with a `back()` function on every generated controller
- `navigateTo` utility implementing pop-or-push navigation on a `SnapshotStateList` back-stack
- `PiggybackRegistry` for registering and firing named side-effect handlers
- `LocalPiggybackRegistry` Compose composition local for providing the registry through the tree
- Bazaar ecommerce example app demonstrating all library features: SignIn, SignUp, CreateListing, EditListing, DeleteListing, Checkout, and Logout journeys
- Maven Central publishing configuration for all three library modules
- MIT License
