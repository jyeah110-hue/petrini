package com.petcare.petcare.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.petcare.petcare.common.security.LoginSuccessHandler;

import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;

/*
 * ═══════════════════════════════════════════════════════════════
 *  Spring Security 전체 구조 — 이 프로젝트에 적용된 흐름
 * ═══════════════════════════════════════════════════════════════
 *
 *  [서블릿 필터 체인 — Tomcat이 관리]
 *
 *    HTTP 요청
 *      │
 *      ├─ XssFilter          ← FilterConfig에서 등록한 우리 필터
 *      │
 *      ├─ DelegatingFilterProxy  ← Spring Boot가 자동으로 등록한 필터
 *      │    │
 *      │    │  이 필터는 Tomcat 세계와 Spring 세계의 다리 역할.
 *      │    │  Tomcat한테는 "나 일반 필터야"라고 하면서,
 *      │    │  실제로는 아래 SecurityFilterChain에게 일을 넘긴다.
 *      │    │
 *      │    └─ SecurityFilterChain  ← 이 파일에서 @Bean으로 등록한 것
 *      │         │
 *      │         ├─ CsrfFilter           → CSRF 토큰 검증
 *      │         ├─ UsernamePassword...  → POST /login 가로채서 인증 처리
 *      │         ├─ LogoutFilter          → /member/logout 가로채서 로그아웃
 *      │         └─ AuthorizationFilter   → URL별 권한(ADMIN, BIZ 등) 체크
 *      │
 *      └─ 기타 서블릿 필터들...
 *            │
 *            ▼
 *      DispatcherServlet → Controller
 *
 *
 *  [폼 로그인 흐름]
 *
 *    1. 사용자가 login.jsp에서 id/password 입력 → POST /login
 *    2. UsernamePasswordAuthenticationFilter가 가로챔
 *    3. MemberUserDetailsService.loadUserByUsername(id) 호출 → DB 조회
 *    4. PasswordEncoder(BCrypt)로 비밀번호 비교
 *    5. 성공 → LoginSuccessHandler (세션에 memberInfo 저장, 리다이렉트)
 *       실패 → /login?error=invalid 로 리다이렉트
 *
 *
 *  [이 설정으로 바뀌는 것들]
 *
 *    - 기존 MemberController의 @PostMapping("/login") → 제거 (Security가 처리)
 *    - 기존 MemberController의 @GetMapping("/member/logout") → 제거
 *    - 기존 컨트롤러의 session.getAttribute("memberInfo") == null 체크 → 제거 가능
 *    - MemberService의 new BCryptPasswordEncoder() → @Bean 주입으로 변경
 *
 * ═══════════════════════════════════════════════════════════════
 */

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final LoginSuccessHandler loginSuccessHandler;
    
    /**
     * BCryptPasswordEncoder를 Spring Bean으로 등록.
     * 
     * 이 Bean을 등록하면 Spring Security가 자동으로 비밀번호 비교에 사용하고,
     * MemberService에서도 같은 Bean을 주입받아 회원가입 시 암호화에 사용한다.
     * (기존: MemberService에서 new BCryptPasswordEncoder()를 직접 생성)
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // ── 1. URL별 접근 권한 ──────────────────────────────────
        // 규칙은 위에서 아래로 매칭 → 구체적 URL을 먼저, 포괄적 URL을 나중에
        // hasRole("ADMIN")은 내부적으로 "ROLE_ADMIN" 권한과 비교
        //   (MemberUserDetails.getAuthorities()에서 "ROLE_" + role 로 저장했음)
        http.authorizeHttpRequests(auth -> auth
            //???
            .dispatcherTypeMatchers(
                DispatcherType.FORWARD,
                DispatcherType.ERROR
            ).permitAll()

            //정적 리소스(모두 접근 가능)
            .requestMatchers("/resources/**", "/upload/**", "/favicon.ico").permitAll()
            //로그인(모두 접근 가능)
            .requestMatchers("/login", "/join", "/join/**", "/oauth/**").permitAll()
            //공개 페이지(모두 접근 가능)
            .requestMatchers("/",
                             "/hospital", "/hospital/**",
                             "/grooming", "/grooming/**",
                             "/studio", "/studio/**",
                             "/stay", "/stay/detail",
                             "/stay/checkAvailability",
                             "/store", "/store/**",
                             "/petmap", "/petmap/**",
                             "/community", "/community/**",
                             "/give", "/give/**",
                             "/event", "/event/**",
                             "/care", "/care/**",
                             "/error"
                            ).permitAll()
            //관리자 전용
            .requestMatchers("/admin/**").hasRole("ADMIN")
            // 사업자 전용
            .requestMatchers("/biz/**").hasRole("BIZ") 
            // 위에서 안 걸린 나머지 (마이페이지, 예약, 결제 등) → 로그인 필요
            .anyRequest().authenticated()           
        );

         // ── 2. 폼 로그인 ───────────────────────────────────────
        // loginPage        : 로그인 폼 페이지 (GET /login → login.jsp)
        // loginProcessingUrl: 이 URL로 POST가 오면 Security가 가로채서 인증 처리
        // usernameParameter : login.jsp의 <input name="id">
        // successHandler    : 인증 성공 시 → LoginSuccessHandler
        // failureUrl        : 인증 실패 시 → /login?error=invalid
        http.formLogin(form -> form
            .loginPage("/login")
            .loginProcessingUrl("/login")
            .usernameParameter("id")
            .passwordParameter("password")
            .successHandler(loginSuccessHandler)
            .failureUrl("/login?error=invalid")
            .permitAll()
        );

        // ── 3. 로그아웃 ───────────────────────────────────────
        // 기존 JSP에서 <a href="/member/logout">으로 GET 요청을 보내고 있어서
        // logoutRequestMatcher로 GET 허용 (Security 6 기본은 POST만(CSRF보호))
        RequestMatcher matcher = new AntPathRequestMatcher("/member/logout", "GET");
        http.logout(logout -> logout
            .logoutUrl("/member/logout")
            .logoutSuccessUrl("/")
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")
            .logoutRequestMatcher(matcher)
        );

        // ── 4. CSRF ──────────────────────────────────────────
        // CookieCsrfTokenRepository : 토큰을 쿠키(XSRF-TOKEN)에 저장
        //   withHttpOnlyFalse()     : JS(fetch)에서 쿠키를 읽을 수 있도록 허용
        //
        // CsrfTokenRequestAttributeHandler :
        //   Security 6 기본은 XorCsrfTokenRequestAttributeHandler (토큰을 XOR 인코딩)
        //   JSP에서 ${_csrf.token}으로 원본 토큰을 꺼내 쓰려면 일반 핸들러를 사용해야 함
        CsrfTokenRequestAttributeHandler handler = new CsrfTokenRequestAttributeHandler();
        handler.setCsrfRequestAttributeName("_csrf");

        http.csrf(csrf -> csrf
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .csrfTokenRequestHandler(handler)
        );

        //기본 httpBasic(브라우저가 자체적으로 띄워주는 인증창) 비활성화
        http.httpBasic(basic -> basic.disable());

        return http.build();
    }
}
