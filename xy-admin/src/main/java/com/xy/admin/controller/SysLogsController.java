package com.xy.admin.controller;


import cn.hutool.core.collection.CollUtil;
import com.xy.admin.customize.aop.accessLog.AccessLog;
import com.xy.admin.domain.common.CommandInvoker;
import com.xy.admin.dto.log.LoginLogDTO;
import com.xy.admin.dto.log.OperationLogDTO;
import com.xy.admin.query.LoginLogQuery;
import com.xy.admin.query.OperationLogQuery;
import com.xy.admin.query.service.SysLoginInfoService;
import com.xy.admin.query.service.SysOperationLogService;
import com.xy.common.base.BaseResponseData;
import com.xy.common.enums.common.BusinessTypeEnum;
import com.xy.domain.system.log.login.command.DeleteLoginLogCommand;
import com.xy.domain.system.log.login.handler.DeleteLoginInfoHandler;
import com.xy.domain.system.log.operation.command.DeleteOperationLogCommand;
import com.xy.domain.system.log.operation.handler.DeleteOperationLogHandler;
import com.xy.infrastructure.base.BaseController;
import com.xy.infrastructure.page.PageDTO;
import com.xy.infrastructure.utils.poi.CustomExcelUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统访问记录
 *
 * @author valarchie
 */
@Tag(name = "日志API", description = "日志相关API")
@RestController
@RequestMapping("/logs")
@Validated
@RequiredArgsConstructor
public class SysLogsController extends BaseController {
    @Resource
    SysLoginInfoService sysLoginInfoService;
    @Resource
    SysOperationLogService sysOperationLogService;
    @Resource
    CommandInvoker commandInvoker;
    @Resource
    DeleteLoginInfoHandler deleteLoginInfoHandler;
    @Resource
    DeleteOperationLogHandler deleteOperationLogHandler;

    @Operation(summary = "登录日志列表")
    @PreAuthorize("@permission.has('monitor:logininfor:list')")
    @GetMapping("/loginLogs")
    public BaseResponseData<PageDTO<LoginLogDTO>> loginInfoList(LoginLogQuery query) {
        PageDTO<LoginLogDTO> pageDTO = sysLoginInfoService.getLoginInfoList(query);
        return BaseResponseData.ok(pageDTO);
    }

    @Operation(summary = "登录日志导出", description = "将登录日志导出到excel")
    @AccessLog(title = "登录日志", businessType = BusinessTypeEnum.EXPORT)
    @PreAuthorize("@permission.has('monitor:logininfor:export')")
    @GetMapping("/loginLogs/excel")
    public void loginInfosExcel(HttpServletResponse response, LoginLogQuery query) {
        PageDTO<LoginLogDTO> pageDTO = sysLoginInfoService.getLoginInfoList(query);
        CustomExcelUtil.writeToResponse(pageDTO.getRows(), LoginLogDTO.class, response);
    }

    @Operation(summary = "删除登录日志")
    @PreAuthorize("@permission.has('monitor:logininfor:remove')")
    @AccessLog(title = "登录日志", businessType = BusinessTypeEnum.DELETE)
    @DeleteMapping("/loginLogs")
    public BaseResponseData<Void> removeLoginInfos(@RequestParam @NotNull @NotEmpty List<Long> ids) {
        ArrayList<Long> distinct = CollUtil.distinct(ids);
        DeleteLoginLogCommand deleteLoginLogCommand = new DeleteLoginLogCommand();
        deleteLoginLogCommand.setInfoIds(distinct);
        Boolean execute = commandInvoker.execute(deleteLoginInfoHandler, deleteLoginLogCommand);
        if(!execute){
            return BaseResponseData.fail();
        }
        return BaseResponseData.ok();
    }
    @Operation(summary = "操作日志列表")
    @PreAuthorize("@permission.has('monitor:operlog:list')")
    @GetMapping("/operationLogs")
    public BaseResponseData<PageDTO<OperationLogDTO>> operationLogs(OperationLogQuery query) {
        PageDTO<OperationLogDTO> pageDTO = sysOperationLogService.getOperationLogList(query);
        return BaseResponseData.ok(pageDTO);
    }
//    @GetMapping("/download")
//    public ResponseEntity<InputStreamResource> downloadFile() throws IOException {
//        // 从文件系统或其他位置获取文件输入流
//        File file = new File("path/to/file");
//        InputStream inputStream = new FileInputStream(file);
//        CustomExcelUtil.wri
//
//        // 创建一个 InputStreamResource 对象，将文件输入流包装在其中
//        InputStreamResource resource = new InputStreamResource(inputStream);
//
//        // 返回 ResponseEntity 对象，其中包含 InputStreamResource 对象和文件名
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .contentLength(file.length())
//                .body(resource);
//    }

    /**
     * 可否改成以上的形式 TODO
     *
     * @param response
     * @param query
     */
    @Operation(summary = "操作日志导出")
    @AccessLog(title = "操作日志", businessType = BusinessTypeEnum.EXPORT)
    @PreAuthorize("@permission.has('monitor:operlog:export')")
    @GetMapping("/operationLogs/excel")
    public void operationLogsExcel(HttpServletResponse response, OperationLogQuery query) {
        PageDTO<OperationLogDTO> pageDTO = sysOperationLogService.getOperationLogList(query);
        CustomExcelUtil.writeToResponse(pageDTO.getRows(), OperationLogDTO.class, response);
    }

    @Operation(summary = "删除操作日志")
    @AccessLog(title = "操作日志", businessType = BusinessTypeEnum.DELETE)
    @PreAuthorize("@permission.has('monitor:operlog:remove')")
    @DeleteMapping("/operationLogs")
    public BaseResponseData<Void> removeOperationLogs(@RequestParam List<Long> operationIds) {
        List ids = CollUtil.distinct(operationIds);//去重
        DeleteOperationLogCommand deleteOperationLogCommand = new DeleteOperationLogCommand();
        deleteOperationLogCommand.setOperationIds(ids);
        commandInvoker.execute(deleteOperationLogHandler, deleteOperationLogCommand);
        return BaseResponseData.ok();
    }
}
