# Tama Gargoyles — CURRENT_STATE

> Canonical snapshot of the Tama Gargoyles project  
> This document reflects what is **implemented or explicitly agreed**,  
> and clearly marks what is **unknown, incomplete, or missing**.

---

## 1. Confirmed Systems

### Authentication & Authorization
- OAuth2 login via **Auth0** using **Spring Security**
- All gameplay routes require authentication
- Users are redirected back to the app after login
- Logout clears the local session and logs out of Auth0 via `/v2/logout`
- Security filters are enabled in all environments, including tests

---

### User System
- Users are created automatically on first login
- User identity is based on the Auth0 email claim
- Username derivation order:
    1. Custom Auth0 claim `https://myapp.com/username`
    2. `preferred_username`
    3. Email prefix
- Username uniqueness enforced by appending numbers if needed
- Users have inventory fields:
    - `rocks`
    - `bugs`
    - `mystery_food`
- Inventory fields initialise to `0`

---

### Gargoyle (Pet) System
- Each user owns **one or more Gargoyles**
- If a user has **no gargoyles**, one is automatically created
- Gargoyles are persisted in **PostgreSQL**
- Gargoyles have the following stats:
    - hunger
    - happiness
    - health
    - experience
    - strength
    - speed
    - intelligence
- Gargoyles have both a **Type** and a **Status**

---

### Game Flow (`/game`)
- User must be authenticated
- Gargoyle selection logic:
    - Prefer a `CHILD` gargoyle if one exists
    - Otherwise select the first gargoyle by ID
- On each visit to `/game`:
    1. `resume()` is called
    2. `tick()` is called
    3. Gargoyle is saved
- Game data is rendered using **Thymeleaf**

---

### Virtual Time System
- Time progression is based on **real-world elapsed time**
- Time decay applies only when the gargoyle is **not paused**
- Hunger and happiness decay per minute
- Happiness decays faster when hunger is low
- Active time is tracked using `activeMinutes`
- Game “days” are derived from active minutes
- Pause/resume system prevents offline decay
- Implemented using `java.time.Clock`
- No scheduled jobs are used

---

### Gargoyle Interaction & Stats Management
- Hunger and happiness are bounded between `0–100`
- Stats are modified via POST endpoints:
    - `/hunger-increase`
    - `/hunger-decrease`
    - `/entertainment-increase`
    - `/entertainment-decrease`
- Values are clamped within valid ranges

---

### Pause System
- `/gargoyles/pause` pauses the currently selected gargoyle
- Uses the same selection logic as `/game`

---

### Gargoyle CRUD (Partial / Mixed Use)
- List all gargoyles: `/gargoyles`
- Create gargoyle via form:
    - `GET /gargoyles/new`
    - `POST /gargoyles`
- Rename gargoyle:
    - `GET /gargoyle/{id}/rename`
    - `POST /gargoyle/{id}/rename`
- Purpose (player-facing vs admin/debug) is unclear

---

### Testing
- Controller tests use `@WebMvcTest`
- Security is enabled in tests
- OAuth2 login simulated with `oauth2Login()`
- Services and repositories are mocked
- Order of operations (`resume → tick`) is explicitly tested
- Redirect behaviour and model attributes are tested

---

## 2. Life Stages & Types

### Referenced Life Stages
- **EGG**
    - Given to the user at the start
    - Hatches and is named
    - No explicit in-game behaviour defined in code
- **CHILD**
    - Default gargoyle type on creation
    - Can be fed and played with
    - Affected by virtual time decay
    - Cannot battle
    - Preferred active gargoyle in `/game`
- **ADULT**
    - Reached after growing (criteria not defined here)
    - Only life stage allowed to battle

---

### Behavioural Types
- **GOOD**
    - Mentioned as a type
    - Intended to follow player commands
- **BAD**
    - Mentioned as a type
    - May disobey commands
- ⚠️ Transition rules and full behavioural differences are **not fully defined**

---

## 3. Battle System (Partial / At Risk)

⚠️ **Important:**  
The chat where the battle system was actively implemented is missing.  
The following reflects only what is explicitly described elsewhere.

### Confirmed Facts
- Only **ADULT** gargoyles can battle
- Battles are **turn-based**
- Player chooses one action:
    - Strength
    - Speed
    - Intelligence
- Opponent choice is hidden

### Resolution Rules
- Rock–Paper–Scissors logic:
    - Strength beats Speed
    - Speed beats Intelligence
    - Intelligence beats Strength
- If both choose the same action:
    - Higher stat wins the round

### Behaviour Modifiers
- BAD gargoyles may disobey player commands
- This can help or harm the player

### Battle Outcomes
- Battle ends when HP reaches 0
- If player wins:
    - Receives 10 special food items (randomised)
- If player loses:
    - Gargoyle health decreases by 20%
    - User receives 2 special food items

⚠️ **Missing / Unverified**
- Persistence of battles
- Opponent generation
- Battle initiation limits
- Death handling
- Whether virtual time pauses during battles

---

## 4. Technical Decisions

### Stack
- Language: **Java 21**
- Build Tool: **Maven**
- Frameworks:
    - Spring Boot 3.5.x
    - Spring MVC
    - Spring Security
    - Spring Security OAuth2 Client
    - Spring Data JPA
    - Flyway
- Database: **PostgreSQL**
- ORM: Hibernate / JPA
- Connection Pool: HikariCP
- Web Server: Embedded Tomcat 10.1.x
- UI: **Thymeleaf (server-side rendering)**
- No frontend JS framework
- Architecture: MVC (Controller / Service / Repository)

---

### Testing
- JUnit 5
- Mockito
- spring-security-test
- Selenium present as a dependency (usage unclear)

---

### Visual Layer
- JavaFX discussed as a **possible future option**
- Not currently implemented
- Not yet aligned with Spring MVC architecture

---

## 5. Database & Environment

- Application uses Spring profiles (`dev` active)
- Flyway runs automatically on startup
- Database **must exist before startup**
- Flyway does **not** create databases
- Application fails to start if DB connection fails
- Database name must exactly match datasource config

⚠️ Known Issue:
- Database name mismatch:
    - Created manually: `gargoyle_spring_boot_development`
    - Configured in Spring: `gargoyle_springboot_development`

---

## 6. Constraints

- Group student project
- MVP-focused scope
- Accessibility must be considered
- Server-rendered UI only (no SPA)
- Tests must avoid merge conflicts
- Must be explainable to:
    - Developers
    - Student QA testers

---

## 7. Open Questions

- How do gargoyles evolve from CHILD → ADULT?
- How are GOOD vs BAD types assigned?
- Are battles persisted or session-based?
- Does virtual time pause during battles?
- How often can battles be initiated?
- How is opponent difficulty generated?
- What happens when a gargoyle dies?
- Are inventory limits enforced?
- Is `/gargoyles` player-facing or admin-only?
- What is the intended role of Selenium tests?

---

## 8. Risks & Known Gaps

- Missing battle-system chat → **high risk of incomplete implementation**
- JavaFX planning not yet reconciled with Spring MVC
- EGG stage conceptually present but weakly represented in code
- Mixed gameplay and CRUD routes blur responsibility boundaries
- Database naming inconsistency risks repeated environment failures

---

**This document is the single source of truth going forward.**
Any change must be reflected here.
