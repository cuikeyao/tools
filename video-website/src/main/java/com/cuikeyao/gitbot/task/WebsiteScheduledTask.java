package com.cuikeyao.gitbot.task;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import com.cuikeyao.gitbot.util.GitUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * @author KEYAO
 */
@Log4j2
@Component
public class WebsiteScheduledTask {

    private static String address = "";

    private static final String https = "https";

    @Value("${cpolar.username}")
    private String username;

    @Value("${cpolar.password}")
    private String password;

    @Value("${cpolar.token-url}")
    private String tokenUrl;

    @Value("${cpolar.addr-url}")
    private String addrUrl;

    @Scheduled(fixedRateString = "${cpolar.period}")
    public void refresh() {
        String body = String.format("""
                {"email":"%s","password":"%s"}""", username, password);
        HttpResponse tokenResponse = HttpRequest.post(tokenUrl)
                .body(body)
                .execute();
        log.debug(tokenResponse.body());
        JSONObject tokenJsonObject = new JSONObject(tokenResponse.body());
        String token = tokenJsonObject.getJSONObject("data").getStr("token");
        log.debug(token);

        HttpResponse addrResponse = HttpRequest.get(addrUrl)
                .header("Authorization", "Bearer " + token)
                .execute();
        log.debug(addrResponse.body());
        String addr = getAddrFromJsonStr(addrResponse.body());

        if (!StringUtils.equals(address, addr)) {
            // 更新网址
            log.info("网站已更新，开始刷新网址，新地址：" + addr);
            boolean result = updateAddr(addr);
            if (result) {
                address = addr;
            } else {
                log.error("刷新网址失败！！！");
            }
        }
    }

    private String getAddrFromJsonStr(String jsonStr) {
        JSONObject addrJsonObject = new JSONObject(jsonStr);
        String addr = addrJsonObject.getJSONObject("data")
                .getJSONArray("items").getJSONObject(0).getStr("public_url");
        if (addr.startsWith(https)) {
            return addr;
        }
        return addrJsonObject.getJSONObject("data")
                .getJSONArray("items").getJSONObject(1).getStr("public_url");
    }

    private boolean updateAddr(String addr) {
        try {
            GitUtils.deleteProject();

            // 下载代码
            GitUtils.cloneRepository();

            // 修改文件
            String script = String.format("""
                    <script type="text/javascript">window.location.href = "%s";</script>""", addr);
            File file = FileUtils.getFile(GitUtils.projectPath.toFile(), "index.html");
            if (file.exists()) {
                FileUtils.write(file, script,"UTF-8", false);
            } else {
                log.error("文件目录不存在：" + file.getAbsolutePath());
                return false;
            }

            // 提交更新
            GitUtils.commitChanges();

            // push代码
            boolean pushResult = GitUtils.pushChanges();
            if (!pushResult) {
                return false;
            }

            // 删除工程，防止下一次clone失败
            GitUtils.deleteProject();

            return true;

        } catch (GitAPIException | IOException | InterruptedException e) {
            log.error(e.getMessage());
            return false;
        }
    }
}
