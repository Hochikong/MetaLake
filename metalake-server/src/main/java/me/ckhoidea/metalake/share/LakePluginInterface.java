package me.ckhoidea.metalake.share;

import java.util.Map;

public interface LakePluginInterface {
    /**
     * 翻译为SQL查询语句
     */
    public String translateRequests(Map<String, Object> req);

    /**
     * 转换输出结果
     */
    public Object castResponse(Object response, Map<String, Object> param);
}
