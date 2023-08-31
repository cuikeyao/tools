package com.cuikeyao.agent.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RuntimeUtil;
import lombok.extern.log4j.Log4j2;

import static com.cuikeyao.agent.constant.Constant.GET_MEMORY_SIZE;
import static com.cuikeyao.agent.constant.Constant.GET_PID;

/**
 * @author KEYAO
 */
@Log4j2
public final class CommandUtils {

    public static String execCommand(String cmd) {
        String[] strings;
        if (FileUtil.isWindows()) {
            strings = new String[]{"cmd.exe", "/c", cmd};
        } else {
            strings = new String[]{"/bin/bash", "-c", cmd};
        }
        return RuntimeUtil.execForStr(CharsetUtil.CHARSET_UTF_8, strings).trim();
    }

    public static String getPid(String processCmd) {
        String cmd = String.format(GET_PID, processCmd);
        log.info("get pid cmd: " + cmd);
        return execCommand(cmd);
    }

    public static String getMemorySize(String pid) {
        String cmd = String.format(GET_MEMORY_SIZE, pid);
        log.info("get memory size cmd: " + cmd);
        return execCommand(cmd);
    }
}
