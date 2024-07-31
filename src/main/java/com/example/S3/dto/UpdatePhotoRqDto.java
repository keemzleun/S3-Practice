package com.example.want.api.photo.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePhotoRqDto {
    private Long blockId;
    private List<PhotoListRsDto.PhotoInfoDto> photoList;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PhotoInfoDto{
        private Long id;
        private String url;
    }
}
