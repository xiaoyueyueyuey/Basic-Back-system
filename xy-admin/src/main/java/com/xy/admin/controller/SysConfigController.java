package com.xy.admin.controller;


import com.xy.admin.common.cache.CacheCenter;
import com.xy.admin.domain.common.CommandInvoker;
import com.xy.admin.dto.config.ConfigDTO;
import com.xy.admin.query.ConfigQuery;
import com.xy.admin.query.service.SysConfigService;
import com.xy.common.base.BaseResponseData;
import com.xy.common.enums.common.BusinessTypeEnum;
import com.xy.domain.system.config.UpdateConfigCommand;
import com.xy.domain.system.config.UpdateConfigCommandHandler;
import com.xy.infrastructure.base.BaseController;
import com.xy.admin.customize.aop.accessLog.AccessLog;
import com.xy.infrastructure.page.PageDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 参数配置 信息操作处理
 * @author valarchie
 */
@RestController
@RequestMapping("/system")
@Validated
@RequiredArgsConstructor
@Tag(name = "配置API", description = "配置相关的增删查改")
public class SysConfigController extends BaseController {
    @Resource
    private SysConfigService sysConfigService;
    @Resource
    private CommandInvoker commandInvoker;
    @Resource
    private UpdateConfigCommandHandler updateConfigCommandHandler;
    /**
     * 获取参数配置列表
     */
    @Operation(summary = "参数列表", description = "分页获取配置参数列表")
    @PreAuthorize("@permission.has('system:config:list')")
    @GetMapping("/configs")
    public BaseResponseData<PageDTO<ConfigDTO>> list(ConfigQuery query) {
        PageDTO<ConfigDTO> page = sysConfigService.getConfigList(query);
        return BaseResponseData.ok(page);
    }

    /**
     * 根据参数编号获取详细信息
     */
    @PreAuthorize("@permission.has('system:config:query')")
    @GetMapping(value = "/config/{configId}")
    @Operation(summary = "配置信息", description = "配置的详细信息")
    public BaseResponseData<ConfigDTO> getInfo(@NotNull @Positive @PathVariable Integer configId) {
        ConfigDTO config = sysConfigService.getConfigInfo(configId);
        return BaseResponseData.ok(config);
    }
    /**
     * 修改参数配置
     */
    @PreAuthorize("@permission.has('system:config:edit')")
    @AccessLog(title = "参数管理", businessType = BusinessTypeEnum.MODIFY)
    @Operation(summary = "配置修改", description = "配置修改")
    @PutMapping(value = "/config/{configId}")
    public BaseResponseData<Void> edit(@NotNull @Positive @PathVariable Integer configId, @RequestBody UpdateConfigCommand command) {
        command.setConfigId(configId);
        Boolean execute = commandInvoker.execute(updateConfigCommandHandler, command);
        if(!execute){
            return BaseResponseData.fail();
        }
        return BaseResponseData.ok();
    }
    /**
     * 刷新参数缓存
     */
    @Operation(summary = "刷新配置缓存")
    @PreAuthorize("@permission.has('system:config:remove')")
    @AccessLog(title = "参数管理", businessType = BusinessTypeEnum.CLEAN)
    @DeleteMapping("/configs/cache")
    public BaseResponseData<Void> refreshCache() {
        CacheCenter.configCache.invalidateAll();
        return BaseResponseData.ok();
    }
}
