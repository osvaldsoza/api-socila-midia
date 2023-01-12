package br.com.osvaldsoza.dto;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest extends PanacheEntity {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Age is required")
    private Integer age;
}
