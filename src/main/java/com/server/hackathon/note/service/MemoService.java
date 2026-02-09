package com.server.hackathon.note.service;

import com.server.hackathon.common.exception.CustomException;
import com.server.hackathon.model.model.ModelInstance;
import com.server.hackathon.model.repository.ModelInstanceRepository;
import com.server.hackathon.note.controller.dto.MemoContentResponseDto;
import com.server.hackathon.note.controller.dto.MemoRequestDto;
import com.server.hackathon.note.controller.dto.MemoResponseDto;
import com.server.hackathon.note.model.Memo;
import com.server.hackathon.note.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemoService {

    private final MemoRepository memoRepository;
    private final ModelInstanceRepository modelInstanceRepository;

    @Transactional
    public MemoResponseDto upsertMemo(String memberShortUuid,String modelUuid, MemoRequestDto request) {

        ModelInstance modelInstance = modelInstanceRepository.findByMemberShortUuidAndModelShortUuid(memberShortUuid, modelUuid)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND,"해당 모델에 대한 사용자 인스턴스가 존재하지 않습니다."));

        Memo memo = memoRepository.findByModelInstanceId(modelInstance.getId())
                .orElse(null);

        if (memo == null) {
            memo = Memo.builder()
                    .modelInstance(modelInstance)
                    .content(request.content())
                    .build();
            memoRepository.save(memo);
        } else {
            memo.updateContent(request.content());
        }

        return new MemoResponseDto(
                memo.getShortUuid(),
                memo.getUpdatedAt() != null ? memo.getUpdatedAt().toString() : null
        );
    }

    public MemoContentResponseDto getMemo(String memberShortUuid, String modelUuid) {
        ModelInstance modelInstance = modelInstanceRepository.findByMemberShortUuidAndModelShortUuid(memberShortUuid, modelUuid)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND,"해당 모델에 대한 사용자 인스턴스가 존재하지 않습니다."));

        return memoRepository.findByModelInstanceId(modelInstance.getId())
                .map(memo -> new MemoContentResponseDto(
                        memo.getShortUuid(),
                        memo.getContent(),
                        memo.getUpdatedAt() != null ? memo.getUpdatedAt().toString() : null
                ))
                .orElse(null);
    }
}
