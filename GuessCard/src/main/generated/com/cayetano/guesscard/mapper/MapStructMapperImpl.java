package com.cayetano.guesscard.mapper;

import com.cayetano.guesscard.model.dto.CardAddDto;
import com.cayetano.guesscard.model.dto.UserRegisterDto;
import com.cayetano.guesscard.model.entity.CardEntity;
import com.cayetano.guesscard.model.entity.UserEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-22T18:24:26+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.13 (Homebrew)"
)
@Component
public class MapStructMapperImpl implements MapStructMapper {

    @Override
    public UserEntity toUserEntity(UserRegisterDto userRegisterDto) {
        if ( userRegisterDto == null ) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        userEntity.setUsername( userRegisterDto.getUsername() );
        userEntity.setFullName( userRegisterDto.getFullName() );
        userEntity.setEmail( userRegisterDto.getEmail() );
        userEntity.setPassword( userRegisterDto.getPassword() );

        return userEntity;
    }

    @Override
    public CardEntity toCardEntity(CardAddDto cardAddDto) {
        if ( cardAddDto == null ) {
            return null;
        }

        CardEntity cardEntity = new CardEntity();

        cardEntity.setCardNumber( cardAddDto.getCardNumber() );
        cardEntity.setCvc( cardAddDto.getCvc() );
        cardEntity.setCardType( cardAddDto.getCardType() );
        cardEntity.setValidThru( cardAddDto.getValidThru() );

        return cardEntity;
    }
}
