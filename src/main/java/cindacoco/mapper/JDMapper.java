package cindacoco.mapper;

import cindacoco.pojo.JD;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface JDMapper {
    Integer save(JD jd);
    List<JD> findAll(JD jd);
}
