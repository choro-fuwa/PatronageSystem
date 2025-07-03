package cn.patronage.ipcount.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResultVO<T> {
    /**
     * 错误码.
     */
    private Integer code;

    /**
     * 提示信息.
     */
    private String msg;

    /**
     * 具体内容.
     */
    private T data;

    public ResultVO(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResultVO(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultVO() {
    }
}
