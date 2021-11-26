package com.sollertia.habit.domain.notice.dto;

import com.sollertia.habit.global.utils.DefaultResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
public class NoticeResponseDto extends DefaultResponseDto {
    List<NoticeDto> notices;
}
