package com.yupi.yuaicodemother.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.yupi.yuaicodemother.model.dto.app.AppQueryRequest;
import com.yupi.yuaicodemother.model.entity.App;
import com.yupi.yuaicodemother.model.entity.User;
import com.yupi.yuaicodemother.model.vo.AppVO;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * 应用 服务层。
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 */
public interface AppService extends IService<App> {
    public AppVO getAppVO(App app);

    public QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest);

    public List<AppVO> getAppVOList(List<App> appList);

    public Flux<String> chatToGenCode(Long appId, String message, User loginUser);

    public String deployApp(Long appId, User loginUser);

    void generateAppScreenshotAsync(Long appId, String appUrl);
}
