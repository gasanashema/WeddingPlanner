---
trigger: always_on
---

# DATABASE STANDARDS

## 1. Existing Database

Inspect existing entities, migrations,
relationships and naming conventions first.

Do not create duplicate tables or
relationships without checking the schema.

Preserve existing data and behavior
unless a change is explicitly required.

## 2. Database Technology

Use the database already configured
for the project.

Follow the existing ORM and
persistence conventions.

Do not introduce another persistence
technology without approval.

## 3. Entity Design

Use appropriate:

- primary keys
- foreign keys
- unique constraints
- NOT NULL constraints
- indexes
- data types
- relationship mappings

Avoid unnecessary relationships
and duplicated data.

## 4. Data Integrity

Enforce important business constraints
at the database level where appropriate.

Examples:

- unique membership constraints
- valid foreign keys
- required fields
- valid relationship ownership

Do not rely only on frontend validation
or application-level checks.

## 5. Wedding Relationships

Wedding-specific roles belong to
wedding membership.

Do not assume that a user has
one universal wedding role.

Maintain clear relationships between:

- User
- Wedding
- WeddingMember
- wedding-specific resources

Ensure resources are associated
with the correct wedding.

## 6. Privacy

Database queries must respect
wedding membership and visibility.

Do not fetch private information
and rely on the frontend to hide it.

Use appropriate query restrictions
and service authorization.

## 7. Financial Data

Use appropriate numeric types
for monetary amounts.

Avoid floating-point arithmetic
for financial calculations.

Use consistent currency handling.

Do not silently mix currencies.

Keep planned budgets separate
from actual expenses.

## 8. Migrations

Every schema change must have
a version-controlled migration.

Do not make undocumented schema changes.

Use clear migration names.

Consider existing production data
when changing columns or relationships.

Do not use destructive migrations
without explicit approval.

## 9. Query Performance

Avoid:

- N+1 queries
- unnecessary joins
- unbounded result sets
- repeated loading of the same data

Use indexes based on actual
query and relationship needs.

Paginate large collections.

## 10. Transactions

Use transactions when multiple
database changes must succeed together.

Ensure that failed operations
do not leave inconsistent records.

## 11. Database Testing

Test:

- entity relationships
- constraints
- persistence
- migrations
- transactional behavior
- important queries
- deletion behavior

Verify that migrations work
on a clean database when appropriate.