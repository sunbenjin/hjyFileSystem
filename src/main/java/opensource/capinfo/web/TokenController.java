package opensource.capinfo.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import opensource.capinfo.utils.JwtUtil;
import opensource.capinfo.utils.ResultData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/token")
@Api(description = "获取访问权限token")
public class TokenController {


    @ApiOperation(value = "获取token", notes = "获取token")
    @RequestMapping(value = "getToken", method = RequestMethod.POST)
    public ResultData getToken(String userId) {
        String token = JwtUtil.sign(userId);
        if (StringUtils.isNotBlank(token)) {
            ResultData result = ResultData.sucess("请求成功");
            result.setData(token);
            return result;
        }
        return ResultData.error("请求失败");
    }

}
