package me.ckhoidea.metalake.share;

import java.util.Map;

public interface LakePluginInterface {
    /**
     * 翻译为SQL查询语句
     */
    public String translateRequests(Map<String, Object> req);

    public Map<String, Object> castResponse(Object response);
}
