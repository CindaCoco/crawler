package cindacoco.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Alias("jd")
public class JD {
    private Integer id;
    private String spu;
    private String sku;
    private String title;
    private Double price;
    private String pic;
    private String url;
}
