package com.cuikeyao.agent.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;

/**
 * @author KEYAO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Process {
    /**
     * 进程状态
     */
    private String status;

    /**
     * 进程占用内存大小
     */
    private String memorySize;
}
