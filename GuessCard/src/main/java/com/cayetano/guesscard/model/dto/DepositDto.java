package com.cayetano.guesscard.model.dto;

import com.cayetano.guesscard.model.validation.card.CardCVCMatcher;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class DepositDto {

    @NotNull
    @Pattern(regexp = "\\d+|\\d+\\.\\d{1,2}", message = "Must be a valid format: \"12\" or \"12.3\" or \"12.34\"")
    private String amount;

    @NotBlank
    @Pattern(regexp = "\\d{3}", message = "must be exactly 3 digits")
    @CardCVCMatcher
    private String cvc;

}
