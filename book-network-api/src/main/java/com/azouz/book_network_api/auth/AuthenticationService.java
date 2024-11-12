package com.azouz.book_network_api.auth;

import com.azouz.book_network_api.email.EmailService;
import com.azouz.book_network_api.email.EmailTemplateName;
import com.azouz.book_network_api.role.RoleRepository;
import com.azouz.book_network_api.security.JwtService;
import com.azouz.book_network_api.user.Token;
import com.azouz.book_network_api.user.TokenRepository;
import com.azouz.book_network_api.user.User;
import com.azouz.book_network_api.user.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private  final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;
   public void register (RegistrationRequest request) throws MessagingException {
       var userRole = roleRepository.findByName("USER")
               .orElseThrow(() -> new IllegalStateException("ROLE USER was not initiated"));
       var user = User.builder()
               .firstname(request.getFirstname())
               .lastname(request.getLastname())
               .email(request.getEmail())
               .password(passwordEncoder.encode(request.getPassword()))
               .accountLocked(false)
               .enabled(false)
               .roles(List.of(userRole))
               .build();
       userRepository.save(user);
       sendValidationEmail(user);
   }

    private void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);
        emailService.sendEmail(
                user.getEmail(),
                user.fullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account Activation"
        );
        System.out.println(EmailTemplateName.ACTIVATE_ACCOUNT);
    }

    private String generateAndSaveActivationToken(User user) {
        //generate token
        String generatedToken = generateActivationCode(6);
        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generatedToken;
    }

    private String generateActivationCode(int length) {
        String characters = "01234567879";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i< length; i++){
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
       var auth = authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(
                       request.getEmail(),
                       request.getPassword()
               )
       ); //if we arrive at this point then the authentication went perfectly
       var claims =  new HashMap<String, Object>();
       var user = ((User)auth.getPrincipal());
       claims.put("fullName", user.fullName());
       var jwtToken = jwtService.generateToken(user, claims);
       return AuthenticationResponse.builder().token(jwtToken).build();
    }

//    @Transactional
    public void activateAccount(String token) throws MessagingException {
       Token savedToken = tokenRepository.findByToken(token)
               //todo exception has to be defined
               .orElseThrow(()-> new RuntimeException("Invalid token"));
        if (savedToken.getUser() == null) {
            throw new RuntimeException("User associated with the token is null");
        }
       if(LocalDateTime.now().isAfter(savedToken.getExpiredAt())){
           sendValidationEmail(savedToken.getUser());
           System.out.println("Email sent to user: " + savedToken.getUser().getEmail());
           throw new RuntimeException("Activation token has expired, A new token has been send to the same email");
       }
       var user = userRepository.findById(savedToken.getUser().getId())
               .orElseThrow(() -> new UsernameNotFoundException("User not found"));
       user.setEnabled(true);
       userRepository.save(user);
       savedToken.setValidatedAt(LocalDateTime.now());
       tokenRepository.save(savedToken);
    }
}
