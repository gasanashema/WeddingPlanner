---
trigger: always_on
---

# SECURITY STANDARDS

## 1. General

Security is mandatory for every feature.

Use:

- Secure by default
- Least privilege
- Defense in depth
- Server-side authorization
- Explicit validation

Never treat frontend checks as security controls.

## 2. Authentication & OAuth2 Integration

- Authentication must support both **Stateless JWT** and **OAuth2 Mechanisms** (OAuth2 Social Login via Google/GitHub and Spring Security OAuth2 Resource Server integration).
- Never store plaintext passwords.
- Hash passwords using BCrypt with strength >= 12.
- Never expose password hashes through APIs.
- Never log passwords, JWT tokens, OAuth2 access/refresh tokens, or client secrets.
- Set appropriate token expiration (24h JWT, short-lived OAuth tokens).
- Protect all authentication endpoints with strict rate limiting.
- Securely map OAuth2 user profile attributes (email, name, picture) to internal user accounts.

## 3. Authorization

Every protected backend operation must verify
that the current user is allowed to perform it.

Verify:

- authenticated identity
- wedding membership
- role
- side
- resource visibility
- requested action

Never trust role, side, userId or weddingId
provided by the client.

## 4. Wedding Privacy

The system has three visibility scopes:

- BRIDE_PRIVATE
- GROOM_PRIVATE
- SHARED

Bride-side private information must not be
accessible to the groom or groom-side support.

Groom-side private information must not be
accessible to the bride or bride-side support.

Shared information must be accessible only
to authorized wedding members.

Support accounts must have permissions
appropriate to their side.

Enforce these restrictions on the backend.

## 5. IDOR Prevention

Never return or modify a resource merely
because the client knows its ID.

Check authorization for every resource,
including:

- tasks
- expenses
- guests
- vendors
- invitations
- timelines
- seating
- home preparation items

Test access using unrelated users and
members of the opposite side.

## 6. Input Validation

Validate all untrusted input.

Check:

- required values
- data types
- lengths
- dates
- amounts
- identifiers
- enum values
- pagination
- sorting
- file uploads

Reject invalid input safely.

## 7. Mass Assignment

Never blindly map request bodies into
protected entities.

Protect fields such as:

- userId
- weddingId
- role
- side
- visibility
- permissions
- createdAt

Allow updates only to fields explicitly
supported by the operation.

## 8. Rate Limiting

Implement server-side rate limiting for
abuse-prone endpoints.

At minimum evaluate:

- login
- registration
- password reset
- email verification
- public invitations
- RSVP
- invitation creation
- file uploads
- expensive search endpoints

Use stricter limits for authentication.

Return HTTP 429 when limits are exceeded.

Use appropriate distributed rate limiting
if the application runs across multiple instances.

Do not rely on in-memory limits in a
multi-instance production environment.

## 9. Public Invitations

Public invitation endpoints must:

- use unpredictable tokens
- support revocation
- support expiration
- validate RSVP input
- limit repeated requests
- expose only intended public information

Invitation tokens must never grant access
to the private wedding management system.

Avoid exposing sensitive guest information
to other guests.

## 10. Injection Protection

Use parameterized queries and safe ORM methods.

Never concatenate raw user input into SQL.

Validate dynamic sorting and filtering fields.

Consider injection risks in search and reporting.

## 11. Web Security

Configure:

- explicit CORS origins
- suitable security headers
- HTTPS in production
- CSRF protection where applicable
- secure cookie settings where applicable

Do not disable protections to make
development easier without documenting it.

## 12. Secrets

Never commit:

- database passwords
- JWT secrets
- API keys
- SMTP credentials
- private keys

Use environment variables or
a suitable secret-management mechanism.

Commit only safe example configuration.

## 13. Logging

Never log:

- passwords
- tokens
- session cookies
- private invitation tokens
- sensitive personal information

Use appropriate log levels.

Keep technical diagnostics separate
from public error responses.

## 14. File Uploads

If uploads are implemented:

- validate file type
- validate size
- generate safe filenames
- restrict extensions
- avoid executable storage locations
- do not trust original filenames

## 15. Security Testing

For every security-sensitive feature,
test both permitted and forbidden actions.

Check for:

- unauthorized access
- IDOR
- privilege escalation
- authentication bypass
- mass assignment
- sensitive data exposure
- abuse through repeated requests

Do not declare security complete merely
because the frontend hides restricted content.