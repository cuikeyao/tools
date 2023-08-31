package com.cuikeyao.agent.task;

import com.cuikeyao.agent.bean.Process;
import com.cuikeyao.agent.util.CommandUtils;
import com.cuikeyao.agent.util.HttpUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.cuikeyao.agent.constant.Constant.SERVICE_ABNORMAL;
import static com.cuikeyao.agent.constant.Constant.SERVICE_NORMAL;
import static com.cuikeyao.agent.constant.Constant.ZERO_MEMORY_SIZE;

/**
 * @author KEYAO
 */
@Log4j2
@Component
public class ScheduledTask {

    private final Map<String, Process> statusMap = new HashMap<>();

    @Value("${nginx.name}")
    String nginxName;

    @Value("${nginx.process}")
    String nginxProcess;

    @PostConstruct
    public void init() {
        statusMap.putIfAbsent(nginxName, new Process(SERVICE_ABNORMAL, ZERO_MEMORY_SIZE));
    }

    @Scheduled(fixedRateString = "${nginx.heartbeat}")
    public void processNginx() {
        processStatus(nginxName, nginxProcess);
    }

    public void processStatus(String processName, String processCmd) {
        getProcessInfoAsync(processName, processCmd);
        if (StringUtils.equals(statusMap.get(processName).getStatus(), SERVICE_NORMAL)) {
            HttpUtils.sendMessage(processName, statusMap.get(processName).getMemorySize());
        }
    }

    @Async
    void getProcessInfoAsync(String processName, String processCmd) {
        String pid = CommandUtils.getPid(processCmd);
        log.info("pid: " + pid);
        // 进程号不为数字则服务异常
        if (!StringUtils.isNumeric(pid)) {
            // 上一次也异常，则不打印日志。防止持续打印同样日志
            if (StringUtils.equals(statusMap.get(processName).getStatus(), SERVICE_NORMAL)) {
                log.error("服务异常：" + processName);
            }
            statusMap.put(processName, new Process(SERVICE_ABNORMAL, ZERO_MEMORY_SIZE));
            return;
        }

        String memorySize = CommandUtils.getMemorySize(pid);
        log.info("memory size: " + memorySize);
        statusMap.put(processName, new Process(SERVICE_NORMAL, memorySize));
    }
}
