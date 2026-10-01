---
trigger: always_on
---

# UI DESIGN AND CONSISTENCY RULES

## 1. Source of Truth

The current frontend design is the
only source of truth for visual decisions.

Do not independently decide to redesign
the application.

Do not introduce a new visual identity
when implementing a feature.

## 2. Mandatory Inspection

Before writing frontend UI code:

1. Identify the closest existing screen.
2. Inspect its layout.
3. Inspect the components it uses.
4. Inspect the relevant styles or design tokens.
5. Identify responsive behavior.
6. Reuse the established patterns.

If no suitable example exists,
inspect other screens for shared conventions.

## 3. Visual Consistency

Follow the existing:

- color palette
- typography
- font sizes
- font weights
- spacing
- border radius
- shadows
- borders
- icons
- button styles
- input styles
- card styles
- table styles
- navigation
- modal styles
- animations

Do not introduce a different style
for an individual page.

## 4. Design Tokens

Use existing design tokens and
CSS variables where available.

Do not hard-code new colors or spacing
when an established token exists.

If a required token is missing,
follow the existing naming and scaling
conventions when extending the system.

Do not create competing token systems.

## 5. Component Reuse

Reuse existing components before
creating new ones.

Do not duplicate a component simply
because a different implementation
seems easier.

If a component must be extended,
preserve its existing behavior and
avoid breaking other pages.

## 6. Layout

Follow established:

- page containers
- content widths
- sidebar behavior
- headers
- section spacing
- grid layouts
- mobile layouts

Do not introduce an unrelated
dashboard or navigation pattern.

## 7. Interaction

Follow existing interaction patterns
for:

- hover
- focus
- active
- disabled
- loading
- confirmation
- validation
- notifications

Do not add decorative animations
without a functional reason.

## 8. Responsive Design

Use existing breakpoints.

Match the behavior of similar pages.

Ensure that new layouts do not break
the existing navigation or page structure.

## 9. No Unrequested Redesign

Do not:

- change global colors
- replace fonts
- redesign the sidebar
- change global spacing
- replace the component library
- introduce a new theme

unless explicitly requested.

## 10. Visual Review

Before completing the task:

- compare the new screen with similar screens
- check typography consistency
- check spacing consistency
- check component consistency
- check mobile behavior
- check interaction states

If the new screen looks unrelated
to the rest of the application,
revise it before completion.

The goal is to extend the existing
design system, not replace it.