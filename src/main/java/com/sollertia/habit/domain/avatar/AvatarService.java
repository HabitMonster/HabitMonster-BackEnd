package com.sollertia.habit.domain.avatar;

import com.sollertia.habit.domain.avatar.dto.AvatarListResponseDto;
import com.sollertia.habit.domain.avatar.dto.AvatarResponseDto;
import com.sollertia.habit.domain.avatar.dto.AvatarSelectRequestDto;
import com.sollertia.habit.domain.user.User;
import com.sollertia.habit.exception.AvatarNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvatarService {

    private AvatarRepository avatarRepository;
    private AvatarCollectionRepository avatarCollectionRepository;

    public AvatarListResponseDto getAllAvatars() {
        List<Avatar> avatars = avatarRepository.findAllByGrade(EvolutionGrade.EV1);
        return AvatarListResponseDto.builder()
                .avatars(avatars)
                .responseMessage("LV1 아바타를 불러오는데 성공했습니다.")
                .statusCode(200)
                .build();
    }

    public AvatarResponseDto selectAvatar(UserDetails userDetails,
                                          AvatarSelectRequestDto requestDto) {
        Avatar avatar = avatarRepository.findById(requestDto.getAvatarId()).orElseThrow(
                () -> new AvatarNotFoundException("올바르지 않은 아바타 아이디입니다.")
        );
        String avatarName = requestDto.getAvatarName();

        //todo userDetails에서 User 찾기
        User user = null;
        if ( user.getAvatar() != null ) {
            AvatarCollection avatarCollection = AvatarCollection.createAvatarCollection(user, avatar);
            avatarCollectionRepository.save(avatarCollection);
        }
        user.selectAvatar(avatar, avatarName);
//        return new AvatarResponseDto("아바타가 선택되었습니다.", avatar, avatarName);
        return AvatarResponseDto.builder()
                .responseMessage("아바타가 선택되었습니다.")
                .statusCode(200)
                .avatarId(avatar.getId())
                .avatarName(avatarName)
                .build();
    }
}
