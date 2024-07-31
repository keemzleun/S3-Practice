package com.example.want.api.photo.controller;

import com.example.want.api.photo.dto.PhotoListRsDto;
import com.example.want.api.photo.service.PhotoService;
import com.example.want.common.CommonResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/photo")
public class PhotoController {

    private final PhotoService photoService;

    @PostMapping(value="/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> photoUpload(@RequestPart Long blockId, @RequestPart(value = "files", required = false) List<MultipartFile> files){
        // 입력된 파일의 개수가 10개 이하인지 판별
        if (files.size() > 10) {
            return new ResponseEntity<>(new CommonResDto(HttpStatus.BAD_REQUEST, "There are too many files to upload", null), HttpStatus.BAD_REQUEST);
        }
        try {
            List<PhotoListRsDto.PhotoInfoDto> infoDtos = new ArrayList<>();
            for (MultipartFile file : files){
                PhotoListRsDto.PhotoInfoDto photoInfoDto = photoService.uploadFile(blockId, file);  // 이미지 파일 id, url
                infoDtos.add(photoInfoDto);
            }
            PhotoListRsDto photoListRsDto = PhotoListRsDto.builder()
                    .blockId(blockId)
                    .photoList(infoDtos)
                    .build();
            return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "Successfully uploaded " + files.size() + " files.", photoListRsDto), HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(new CommonResDto(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload files.", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{blockId}/photos")
    public ResponseEntity<?> photoList(@PathVariable Long blockId){
        PhotoListRsDto photos = photoService.photoList(blockId);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "Success", photos), HttpStatus.OK);
    }

    // update
    // dto에 blckId, photoUrl 넣어서

}
