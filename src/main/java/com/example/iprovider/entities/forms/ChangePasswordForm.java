package com.example.iprovider.entities.forms;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordForm {
    @NotNull
    private String oldPass;
    @NotNull
    @Size(min = 8, message = "Password must have at least 8 characters")
    private String newPass;
    @NotNull
    private String oneMoreNewPass;

    public void encodeAll(PasswordEncoder passwordEncoder) {
        this.oldPass = passwordEncoder.encode(oldPass);
        this.newPass = passwordEncoder.encode(newPass);
        this.oneMoreNewPass = passwordEncoder.encode(oneMoreNewPass);
    }
}
