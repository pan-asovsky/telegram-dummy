package ru.festtur.telegramdummy.reference.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.festtur.telegramdummy.reference.enums.ValidationType;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FullQuestionResponse {
    private int order;
    private String code;
    private String text;
    @JsonProperty("active")
    private boolean isActive;
    @JsonProperty("required")
    private boolean isRequired;
    private ValidationType validation;
    private String errorMessage;
}
