package br.gambialan.authservice.entrypoint;

import br.gambialan.authservice.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TokenController {

    @Autowired
    private JwtService service;

    @GetMapping("/token")
    public String get(@AuthenticationPrincipal Jwt jwt){
        var token = service.generateToken(jwt.getClaimAsString("email"));
        return token;
    }

    @GetMapping(value = {"/well-know","key"})
    public Map<String, Object> getJwt(){
        var publicKeyRsa = service.getPublicKey();
        Map<String, Object> key = new HashMap<>();
        key.put("keys", List.of(publicKeyRsa.toPublicJWK().toJSONObject()));
        return key;
    }
}
