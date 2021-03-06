package campus.hackday.controller;

import campus.hackday.dto.Comment;
import campus.hackday.model.DefaultResponse;
import campus.hackday.model.ReactParam;
import campus.hackday.model.Status;
import campus.hackday.mysqlServiceImpl.CommentServiceImpl;
import campus.hackday.redisServiceImpl.CheckToReactServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("redis")
public class RedisReactController {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private CheckToReactServiceImpl checkToReactService;

  @Autowired
  private CommentServiceImpl commentService;

  // 공감/비공감 API 통합
  @GetMapping("{postId}/{commentId}/{userId}/{react}")
  public ResponseEntity<DefaultResponse> integrationReact
          (@PathVariable int postId, @PathVariable int commentId, @PathVariable int userId, @PathVariable ReactParam reactParam) {

    DefaultResponse res = new DefaultResponse();
    Comment comment = commentService.findById(commentId);
    checkToReactService.checkToReact(postId, comment.getId(), userId, reactParam);

    res.setData(comment);
    res.setMsg(commentId + "번 댓글에 대한 요청입니다.");
    res.setStatus(Status.SUCCESS);
    return new ResponseEntity<>(res, HttpStatus.OK);
  }



//  @GetMapping("{postId}/{commentId}/{userId}/pst")
//  public ResponseEntity<DefaultResponse> pstReact
//          (@PathVariable int postId, @PathVariable int commentId, @PathVariable int userId) {
//
//    DefaultResponse res = new DefaultResponse();
//    Comment comment = commentService.findById(commentId);
//
//    redisPstReactService.insert(comment.getId(), userId);
//
////    res.setData();
////    res.setMsg();
////    res.setStatusEnum(StatusEnum.SUCCESS);
//    return new ResponseEntity<>(res, HttpStatus.OK);
//  }
//
//  @GetMapping("{postId}/{commentId}/{userId}/ngt")
//  public ResponseEntity<DefaultResponse> ngtReact
//          (@PathVariable int postId, @PathVariable int commentId, @PathVariable int userId) {
//
//    DefaultResponse res = new DefaultResponse();
//    Comment comment = commentService.findById(commentId);
//
//    redisNgtReactService.insert(comment.getId(), userId);
//
////    res.setData();
////    res.setMsg();
////    res.setStatusEnum(StatusEnum.SUCCESS);
//    return new ResponseEntity<>(res, HttpStatus.OK);
//  }

}
