# 🧪 Controller Testing with Spring Security & Auth0
(Student Guide – read this if your tests suddenly start redirecting or crashing)
This project uses Spring Security + Auth0 + Thymeleaf.
That means controller tests need a little setup, otherwise you’ll hit confusing errors.
This guide shows exactly what to do and why.

## 🚨 Common problems you’ll see
### ❌ Problem 1: Tests redirect instead of returning 200
You expect:
```java 
.andExpect(status().isOk())
```

But you get:
```text 
302 → /oauth2/authorization/auth0
```
👉 This means Spring Security thinks the user is not logged in.

### ❌ Problem 2: Thymeleaf crashes with _csrf.parameterName
Error looks like:
```text 
Exception evaluating SpringEL expression: "_csrf.parameterName"
```
👉 This happens when security is half-enabled (very common in tests).
## ✅ The Golden Rules (memorise these)
### ✅ Rule 1: ALWAYS use `@WithMockUser` in controller tests
This tells Spring Security:
“Pretend a user is logged in.”
```java 
@WithMockUser
@Test
void myTest() throws Exception {
...
}
```
Without this → redirect to Auth0.
---

✅ Rule 2: NEVER disable security filters in MVC tests
❌ Do NOT do this:
```java
@AutoConfigureMockMvc(addFilters = false)
```
Why?
- Spring Security **adds the CSRF token**
- Thymeleaf templates expect `_csrf`
- Turning filters off = missing CSRF = template crash
✅ Leave filters **ON** and mock the user instead.

### ✅ Rule 3: POST requests MUST include CSRF
Every `@PostMapping` test needs this:
```java
.with(csrf())
```
Example:
```java

mockMvc.perform(post("/game/action")
.with(csrf())
.param("delta", "10"))
.andExpect(status().is3xxRedirection());
```
Without CSRF → **403 Forbidden**

### ✅ Rule 4: Mock CurrentUserService correctly
If your controller does this:
```java
User user = currentUserService.getCurrentUser(authentication);
```
Then your test **must** include:
```java
when(currentUserService.getCurrentUser(any()))
.thenReturn(user);
```
If you forget → `NullPointerException`.

## 🧩 Required dependency (check your `pom.xml`)
Make sure this exists:
```xml
<dependency>
<groupId>org.springframework.security</groupId>
<artifactId>spring-security-test</artifactId>
<scope>test</scope>
</dependency>
```
Without it → `@WithMockUser` won’t work.

## 🧱 Standard Controller Test Template (copy this)
```java
@WebMvcTest(SomeController.class)
class SomeControllerTest {

    @Autowired MockMvc mockMvc;

    @MockitoBean CurrentUserService currentUserService;
    @MockitoBean SomeRepository someRepository;

    @Test
    @WithMockUser
    void GET_page_rendersSuccessfully() throws Exception {
        User u = new User();
        u.setId(1L);

        when(currentUserService.getCurrentUser(any()))
                .thenReturn(u);

        mockMvc.perform(get("/some-page"))
                .andExpect(status().isOk())
                .andExpect(view().name("some-page"));
    }
}
```
🧠 Mental model (important for learning)
Think of controller tests like this:

| Layer      | Real?    | Why                        |
| ---------- | -------- | -------------------------- |
| Controller | ✅ real   | This is what we’re testing |
| Security   | ✅ real   | Needed for Auth + CSRF     |
| User       | ❌ fake   | `@WithMockUser`            |
| Services   | ❌ mocked | Fast + predictable         |
| Database   | ❌ mocked | No real data               |

If any part is half-real / half-fake → weird errors.

---

## 🛟 Quick “something broke” checklist
If a controller test fails, ask:
1. ❓ Do I have `@WithMockUser`?
2. ❓ Is this a POST without `.with(csrf())`?
3. ❓ Did I accidentally disable filters?
4. ❓ Did I mock `CurrentUserService.getCurrentUser(any())`?
5. ❓ Am I testing the right controller in `@WebMvcTest(...)`?
99% of issues are one of these.

---
## 🎯 Final reassurance
If you hit these issues:
- **You didn’t do anything wrong**
- This is normal when adding security
- Every professional Spring project has the same setup
Once you’ve copied this pattern, `controller tests become boring again` — which is exactly what we want 😄