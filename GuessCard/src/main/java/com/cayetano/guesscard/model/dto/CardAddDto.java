package com.cayetano.guesscard.model.dto;

import com.cayetano.guesscard.model.enumerated.CardType;
import com.cayetano.guesscard.model.validation.card.UniqueCardNumber;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class CardAddDto {

    @NotBlank
    @Pattern(regexp = "\\d{16}", message = "must be exactly 16 digits")
    @UniqueCardNumber
    private String cardNumber;

    @NotBlank
    @Pattern(regexp = "\\d{3}", message = "must be exactly 3 digits")
    private String cvc;

    @NotNull
    private CardType cardType;

    @NotNull
    @Future
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate validThru;

}