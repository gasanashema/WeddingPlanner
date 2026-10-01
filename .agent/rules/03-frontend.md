---
trigger: always_on
---

# FRONTEND DEVELOPMENT STANDARDS

## 1. Existing Frontend

The existing frontend is the source of truth.

Before implementing anything, inspect:

- existing pages
- components
- routing
- state management
- API integration
- form handling
- styles
- responsive layouts

Follow the existing conventions.

Do not introduce a new frontend architecture
without a clear reason and approval.

## 2. Component Structure

Reuse existing components whenever possible.

Keep components focused and maintainable.

Separate reusable UI from feature-specific logic
where appropriate.

Avoid:

- giant components
- duplicated UI
- unnecessary abstractions
- unnecessary state
- deeply coupled components

## 3. API Integration

Use the existing API client and
request-handling patterns.

Do not create a separate API client
for an individual feature.

Handle:

- loading
- success
- validation errors
- server errors
- network errors
- unauthorized responses

Never expose secrets in frontend code.

## 4. Forms

Follow existing form patterns.

Every form should:

- have appropriate labels
- validate input
- show useful errors
- prevent accidental duplicate submission
- show submission progress
- handle server validation errors

Do not duplicate backend business logic
as a substitute for server validation.

## 5. State Management

Follow the existing state-management approach.

Avoid unnecessary global state.

Keep temporary UI state local when appropriate.

Do not introduce a new state library
without a clear requirement.

## 6. Routing and Permissions

Follow the existing routing structure.

Protect private pages from unauthorized
navigation where appropriate.

Do not treat route protection as
a replacement for backend authorization.

Do not expose private data in page state
or API responses when the user lacks access.

## 7. Reusability

Reuse existing:

- buttons
- inputs
- modals
- tables
- cards
- badges
- dropdowns
- navigation
- notifications
- loading indicators

Extend existing components when appropriate.

Create new components only when necessary.

## 8. Responsive Behavior

Every new page must work on:

- desktop
- tablet
- mobile

Follow the existing responsive breakpoints
and layout conventions.

Do not introduce unnecessary horizontal scrolling.

Ensure forms, tables and navigation
remain usable on small screens.

## 9. Accessibility

Use semantic elements.

Provide labels for inputs.

Provide meaningful button names.

Maintain visible focus states.

Ensure keyboard accessibility.

Do not communicate status using color alone.

Use appropriate alt text for meaningful images.

## 10. UI States

Implement the relevant:

- loading state
- empty state
- error state
- success state
- disabled state

Reuse existing state components.

Do not leave a blank page while data loads.

## 11. Frontend Quality

Before completing a feature:

- remove unused code
- remove debugging statements
- check console errors
- verify navigation
- verify form behavior
- verify API error handling
- verify responsive behavior
- check consistency with existing screens