package campus.hackday.redisService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class RedisNgtReactService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  private static final String KEY = "Ngt";
  private RedisTemplate<String, Integer> redisTemplate;
  private SetOperations<String, Integer> setOperations;

  @Autowired
  private RedisPstReactService redisPstReactService;

  @Autowired
  public RedisNgtReactService(RedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @PostConstruct
  private void init() {
    setOperations = redisTemplate.opsForSet();
  }

  // 해당 키(댓글ID)에 대한 비공감 개수
  public int countMemberByKey(int commentId) {
    return setOperations.members(KEY + Integer.toString(commentId)).size();
  }

  // 비공감 삽입
  public void insert(int commentId, int userId) {
    if (redisPstReactService.isMember(commentId, userId)) {
      logger.error("이미 이 댓글에 공감하셨습니다.");
    }
    else if (this.isMember(commentId, userId)) {
      setOperations.remove(KEY + Integer.toString(commentId), userId);
    }
    else {
      setOperations.add(KEY + Integer.toString(commentId), userId);
    }
  }

  // 잘 들어갔는지 확인용
  public boolean isMember(int commentId, int userId) {
    return setOperations.isMember(KEY + Integer.toString(commentId), userId);
  }

}