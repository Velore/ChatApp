package com.czh.service;

import com.czh.bo.LoginBo;
import com.czh.dao.AttentionMapper;
import com.czh.po.common.ReturnInfo;
import com.czh.po.common.StatusCode;
import com.czh.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;

import java.time.LocalDateTime;

/**
 * @author chenzhuohong
 */
public class AttentionService {

    /**
     * 添加关注
     * @param uid 要关注的帐号uid
     * @param loginBo 登录凭证,获取关注者的uid
     * @return 是否关注成功的返回信息
     */
    public static ReturnInfo addAttention(String uid, LoginBo loginBo){
        SqlSession session = MybatisUtils.getSqlSession();
        AttentionMapper mapper = session.getMapper(AttentionMapper.class);
        if(mapper.addAttention(loginBo.getLoginUid(), uid, LocalDateTime.now())!=1){
            return new ReturnInfo(StatusCode.ERROR_CODE, "添加关注失败");
        }
        session.commit();
        return new ReturnInfo(StatusCode.SUCCESS_CODE, "添加关注成功");
    }

    /**
     * 取消关注
     * @param uid 要取消关注的帐号
     * @param loginBo 登录凭证
     * @return 是否取消关注成功的返回信息
     */
    public static ReturnInfo deleteAttention(String uid, LoginBo loginBo){
        SqlSession session = MybatisUtils.getSqlSession();
        AttentionMapper mapper = session.getMapper(AttentionMapper.class);
        if(mapper.deleteAttention(loginBo.getLoginUid(), uid)!=1){
            return new ReturnInfo(StatusCode.ERROR_CODE, "添加关注失败");
        }
        session.commit();
        return new ReturnInfo(StatusCode.SUCCESS_CODE, "取消关注成功");
    }
}
