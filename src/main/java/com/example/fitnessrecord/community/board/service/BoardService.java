package com.example.fitnessrecord.community.board.service;


import com.example.fitnessrecord.community.board.dto.BoardMainDto;

public interface BoardService {

  /**
   * BoardMain에는 최근 24시간 내의 가장 많은 likes를 받은 데이터와 페이지에 따른 게시물을 보여준다.
   */
  BoardMainDto getBoardMainData(Integer page);
}
