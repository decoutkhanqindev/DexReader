# Project Progress Tracker

## Completed Phases

- [x] Phase 1 - Refactoring Resource Exceptions: Cleaned up domain-level exception types for precision logic handling.
- [x] Phase 2 - Refactoring Camera State Management: Fixed state consistency bugs in Compose Navigation by migrating visual triggers to ViewModel.
- [x] Phase 3 - Refactoring Camera Components: Segregated view responsibilities into reusable layout blocks.
- [x] Phase 4 - Refactoring Immutable Collections: Optimized Compose `presentation` package component parameter tracking to effectively skip recomposition on stable un-changing input dependencies using `kotlinx.collections.immutable` wrappers.

## Current Phases

- [ ] UI Functionality Testing: Perform QA logic passes checking application execution health relative to state changes affected by Immutable lists adoption.

## Architecture Decisions Log

- Adopted discrete / concrete nested subclasses for mapped Exceptions extending the parent Domain logic errors (`BusinessException.Resource`) instead of strings.
- Adopted shared ViewModels over `NavArgs` exclusively for states carrying persistent lifecycle (avoiding transient dialogue states being locked to a route entry argument map).
- Implemented `ImmutableList` as the strict collection encapsulation type across all `@Composable` parameter inputs, `*UiState` models and their related Flow initializers instead of relying on compiler arguments mapping. This assures compiler inferences won't generate unstable states. All `List`s are transformed at the boundary entry-point (`ViewModels`).
