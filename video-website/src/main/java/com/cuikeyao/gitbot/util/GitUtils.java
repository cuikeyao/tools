package com.cuikeyao.gitbot.util;

import cn.hutool.core.util.RuntimeUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author KEYAO
 */
@Log4j2
@Component
public final class GitUtils {
    @Value("${github.remote-url}")
    private String gitRemoteUrl;

    private static String remoteUrl;

    private static final Pattern REPO_NAME_REGEX = Pattern.compile("/([^/]+)\\.git");

    private static String localPath = System.getProperty("user.dir");

    public static Path projectPath;

    @PostConstruct
    public void init() {
        remoteUrl = gitRemoteUrl;
        projectPath  = Path.of(localPath, getRepoName());
    }

    public static Boolean cloneRepository() throws GitAPIException, IOException, InterruptedException {
        boolean cloneResult = bashCmd("git clone " + remoteUrl, Path.of(localPath));
        if (cloneResult) {
            log.info("Clone successful");
            return true;
        } else {
            log.error("Clone failed");
            return false;
        }
    }

    public static void commitChanges() throws IOException, GitAPIException {
        try (
            // 打开本地仓库
            Repository repository = FileRepositoryBuilder.create(FileUtils.getFile(projectPath.toString(), ".git"));
            // 创建Git对象
            Git git = new Git(repository)) {

            // 检查是否有需要add的文件
            if (hasPendingChanges(git)) {
                // 获取当前时间作为提交信息
                String commitTime = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss").format(LocalDateTime.now());
                // 添加所有文件
                git.add().addFilepattern(".").call();
                git.commit().setMessage("GitBot 更新最新地址 " + commitTime).call();
            } else {
                log.info("没有需要提交的文件");
            }
        }
    }

    public static boolean pushChanges() throws IOException, InterruptedException {
        File gitDir = FileUtils.getFile(projectPath.toString(), ".git");

        if (!isRemoteMainBranchSameAsLocal(gitDir)) {
            boolean pushResult = pushCode();
            if (pushResult) {
                return true;
            }
            return false;
        } else {
            log.info("远程版本与本地版本一致，不需要push");
        }
        return true;
    }

    public static void deleteProject() throws IOException {
        FileUtils.deleteDirectory(projectPath.toFile());
    }

    private static boolean bashCmd(@NonNull String cmd, @NonNull Path path) {
        log.debug("bash cmd：" + cmd);
        String[] cmds = cmd.split(" ");
        ProcessBuilder processBuilder = new ProcessBuilder(cmds);
        processBuilder.directory(path.toFile());
        int exitCode;
        try {
            Process process = processBuilder.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.warn(line);
                }
            }
            exitCode = process.waitFor();
        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage());
            return false;
        }
        if (exitCode == 0) {
            return true;
        }
        return false;
    }

    private static String getBashResult(@NonNull String cmd, @NonNull Path path) {
        log.debug("bash cmd：" + cmd);
        String[] cmds = cmd.split(" ");
        ProcessBuilder processBuilder = new ProcessBuilder(cmds);
        processBuilder.directory(path.toFile());
        try {
            Process process = processBuilder.start();
            String result = RuntimeUtil.getResult(process, StandardCharsets.UTF_8);
            return result;
        } catch (IOException e) {
            log.error(e.getMessage());
            return StringUtils.EMPTY;
        }
    }

    private static boolean pushCode() {
        boolean pushResult = bashCmd("git push origin main", projectPath);
        if (pushResult) {
            log.info("Push successful");
            return true;
        } else {
            log.error("Push failed");
            return false;
        }
    }

    private static Boolean isRemoteMainBranchSameAsLocal(File gitDir) throws IOException {
        String localMainHead = getBashResult("git rev-parse main", gitDir.toPath());
        String remoteMainHead = getBashResult("git ls-remote origin main", gitDir.toPath());
        return remoteMainHead.startsWith(localMainHead.trim());
    }

    private static boolean hasPendingChanges(Git git) throws GitAPIException {
        return !git.status().call().getUncommittedChanges().isEmpty();
    }

    private static String getRepoName() {
        Matcher matcher = REPO_NAME_REGEX.matcher(remoteUrl);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "project";
    }
}
