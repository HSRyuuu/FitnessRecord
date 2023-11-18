package com.example.fitnessrecord.community.board.service;

import com.example.fitnessrecord.community.board.dto.BoardMainDto;
import com.example.fitnessrecord.community.routinepost.persist.RoutinePost;
import com.example.fitnessrecord.community.routinepost.persist.RoutinePostRepository;
import com.example.fitnessrecord.global.util.PageConstant;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService {

  private final RoutinePostRepository routinePostRepository;

  @Override
  public BoardMainDto getBoardMainData(Integer page) {

    Page<RoutinePost> list =
        routinePostRepository.findAllByOrderByCreateDateTimeDesc(
            this.getPageRequest(page, PageConstant.MAIN_PAGE_SIZE));

    List<RoutinePost> top10 = routinePostRepository.findTop10ByLikesInLast24Hours(
        LocalDateTime.now().minusDays(1L), PageRequest.of(0, 10));

    return new BoardMainDto(page, top10, list);
  }

  private PageRequest getPageRequest(Integer page, int size) {
    return PageRequest.of(page - 1, size);
  }
}
