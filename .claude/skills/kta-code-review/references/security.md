# Security

Findings here default to Major. Promote to Critical for any of: hardcoded secret, broken
authentication, broken cryptography, unbounded deserialisation, SQL/command injection, PII in
plaintext logs.

## Secrets

### Hardcoded credentials, keys, tokens, passwords

```kotlin
// CRITICAL — never commit
const val API_KEY = "sk_live_…"
private const val DB_PASSWORD = "p@ssw0rd"
val token = "eyJhbGciOi…"
```

Detection patterns: identifiers containing `key|secret|password|passwd|token|credential|auth`,
base64-ish strings ≥ 20 chars, JWT-shaped strings, AWS-style `AKIA…`, GitHub `ghp_…`/`gho_…`/
`ghs_…`, Stripe `sk_live_…`/`sk_test_…`, Google `AIza…`.

The reviewer redacts the literal value in the report and flags Critical. Recommended fix: load from
environment, build config, or a secret-store; add the file/path to `.gitignore` if config-style;
rotate the leaked secret.

### Secret in log / exception / error response

```kotlin
log.info("auth failed for $user with token=$token")    // CRITICAL
throw IllegalStateException("config: $appConfig")      // may include secrets
```

Logs and error responses commonly leak. Sanitize at the logger boundary; never log full request
bodies, full headers, or full configs.

### Secret in version control history

If the diff *removes* a secret, the secret is still in history — the fix requires rotation, not just
deletion. Note this in the finding.

## Input validation

Treat anything from outside the process as hostile.

### Injection

```kotlin
// CRITICAL: SQL injection
db.exec("SELECT * FROM users WHERE name = '$name'")

// CRITICAL: command injection
Runtime.getRuntime().exec("sh -c 'do $userInput'")

// CRITICAL: path traversal
File(uploadDir, fileName).readBytes()         // fileName = "../../etc/passwd"
```

Fix:

- SQL → parameterised queries; never string-interpolate user input into SQL.
- Shell → avoid `sh -c`; use `ProcessBuilder` with separate args, no shell.
- Paths → validate against a whitelist; reject `..`, absolute paths, and resolve to canonical form
  before checking the prefix.

### Deserialisation

- Java native serialisation on untrusted input → Critical.
- JSON / YAML / XML libraries with polymorphic-by-default deserialisation → Critical (gadget chain
  risk).
- Always set a maximum input size; bounded reads only.

### XML / XXE

```xml
<!-- CRITICAL when parser allows external entities -->
<!DOCTYPE foo [<!ENTITY x SYSTEM "file:///etc/passwd">]>
```

Disable external entities and DTDs at the parser level.

## Cryptography

### Weak algorithms

```kotlin
MessageDigest.getInstance("MD5")        // broken
MessageDigest.getInstance("SHA1")       // broken
Cipher.getInstance("DES/…")             // broken
Cipher.getInstance("AES/ECB/…")         // pattern leakage
Cipher.getInstance("AES/CBC/PKCS5Padding")  // padding-oracle territory unless authenticated
```

Use:

- Hashing (general): SHA-256 / SHA-512.
- Password hashing: Argon2id, scrypt, or bcrypt — **never** raw SHA / MD5.
- Symmetric: AES-256-GCM (authenticated). Random 96-bit IV, never reuse with the same key.
- Asymmetric: Ed25519 / X25519 / RSA-2048+.

### Key management

- Keys hardcoded in source → Critical.
- Keys derived from low-entropy passwords without a KDF (PBKDF2 / Argon2) → Major.
- Same IV/nonce reused across messages with the same key → Critical (catastrophic for GCM).
- Keys logged or serialised to disk in plaintext → Critical.

### Random

```kotlin
// BAD: not cryptographically secure
val token = Random.nextBytes(32)

// GOOD
val token = ByteArray(32).also { SecureRandom().nextBytes(it) }
```

`Random` / `Math.random()` for security-relevant values (tokens, IDs that must be unguessable, keys,
IVs) → Critical.

### Custom crypto

A class named `CustomEncryptor` / `MyHash` / etc. is almost always a bug. Use library primitives and
don't roll your own.

## Transport

### HTTP instead of HTTPS

```kotlin
val url = "http://api.example.com/v1/users"      // Major+
retrofit.baseUrl("http://internal.svc/")         // Major
```

Allow HTTP only for explicit dev/loopback flows guarded by build configuration.
`cleartextTrafficPermitted="true"` in network-security config → Major.

### Disabled / weakened TLS

```kotlin
// CRITICAL
HostnameVerifier { _, _ -> true }
trustManager = object : X509TrustManager {
    override fun checkServerTrusted(...) { /* nothing */ }
}
SSLContext.getInstance("TLS").init(null, arrayOf(trustAll), null)
```

"Trust all" code paths often started life as a debug shim and shipped to prod. Always Critical.

### Certificate pinning

Recommended for high-value mobile/desktop clients. Missing pinning is Major in security-sensitive
contexts (banking, healthcare); not always required for general apps. Don't blindly mandate.

## Authentication and authorisation

- Auth checks must be on the server. Client-side checks are advisory.
- Tokens stored in plain `SharedPreferences` / `localStorage` / unencrypted file → Major (Critical
  if long-lived refresh token).
- Session tokens that don't rotate on privilege change → Major.
- Authorization decisions based on client-supplied role claims without server verification →
  Critical.
- Timing-attack-vulnerable comparison (`==` on tokens) → Minor in most contexts, Major for HMAC
  verification. Use constant-time compare (`MessageDigest.isEqual` on byte arrays).

## Data protection

### PII in logs / analytics

- Email, phone, full name, address, government ID, IP, precise location → don't log raw.
- Use a stable hash or pseudonym for correlation; redact the rest.

### Encryption at rest

- Sensitive data persisted to disk (SQLite, files, key-value stores) without OS-managed protection →
  Major.
- "Encryption" with a key sitting next to the data → useless.

### Memory

- Long-lived `String` containing a password is hard to clear; prefer `CharArray` and zero it after
  use in security-critical paths.

## Web / mobile specifics (apply when relevant)

- WebView with `setJavaScriptEnabled(true)` and `addJavascriptInterface` exposing host APIs →
  Critical for untrusted content.
- Intent / deeplink handling that performs privileged actions based on URL parameters without auth →
  Major.
- Exported components (Activities, Services, Receivers) without `permission` or signature checks →
  Major.
- File provider with overly broad URI permissions → Major.

## Server-side specifics (apply when relevant)

- CSRF: state-changing endpoints without CSRF protection on browser sessions → Major.
- Open redirect: redirecting to a user-supplied URL without origin check → Major.
- SSRF: server-side fetch of a user-supplied URL without allowlist → Critical.
- Mass assignment: deserialising request JSON directly into a domain entity that exposes admin
  fields → Critical.
- Rate limiting absent on auth / password-reset / OTP endpoints → Major.

## Logging and error handling for security

- Auth failures should log at INFO/WARN with a stable identifier (no password, no token).
- Stack traces in HTTP responses leak internals → Major in production.
- Verbose error messages distinguishing "user does not exist" vs "wrong password" enable
  enumeration → Minor/Major.

## Build and dependency hygiene

- `android:debuggable="true"` in a release build → Major.
- `android:allowBackup="true"` for an app holding sensitive data → Major.
- Dependencies pinned to versions with known CVEs → Major (run a dep-audit; out of scope of pure
  code review but worth flagging).
- `// TODO: remove before prod` left in security-sensitive code → Major.

## What the reviewer must do with secrets in input

If the reviewed code or diff contains a literal secret, the reviewer:

1. Treats it as Critical.
2. Redacts the value in the report (`API_KEY = "sk_…[REDACTED]"`).
3. Notes that the secret must be rotated even if the file is amended, because git history retains
   it.
4. Does **not** include the full literal in any output, summary, or quoted snippet.

## Common findings

| Pattern                                   | Severity | Fix                                   |
|-------------------------------------------|----------|---------------------------------------|
| Hardcoded API key / token / password      | Critical | rotate + load from env/secret-store   |
| `Random.nextBytes` for token              | Critical | `SecureRandom`                        |
| MD5/SHA1 for password or signature        | Critical | Argon2id / SHA-256-HMAC               |
| AES/ECB                                   | Critical | AES-GCM with unique IV                |
| String-interpolated SQL                   | Critical | parameterised query                   |
| `sh -c "$userInput"`                      | Critical | `ProcessBuilder` with arg list        |
| `HostnameVerifier { _, _ -> true }`       | Critical | use platform default                  |
| HTTP base URL in production               | Major    | HTTPS                                 |
| Token in SharedPreferences (mobile)       | Major    | OS-managed keystore / encrypted prefs |
| Logging request body / headers            | Major    | sanitize at logger                    |
| PII in analytics events                   | Major    | hash or omit                          |
| Exception messages in HTTP error response | Major    | generic message + server-side log id  |
