package com.neutron.servicebackend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neutron.common.exception.BusinessException;
import com.neutron.common.model.dto.UserDTO;
import com.neutron.common.model.entity.User;
import com.neutron.common.model.mapper.UserMapper;
import com.neutron.common.model.request.UserLoginRequest;
import com.neutron.common.model.request.UserRegisterRequest;
import com.neutron.common.model.vo.KeysVO;
import com.neutron.common.response.ErrorCode;
import com.neutron.servicebackend.service.UserService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.neutron.common.constants.UserConstants.*;

/**
 * @author zzs
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2023-04-08 16:27:06
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    private static final String SALT = "user";

    @Override
    public UserDTO userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        if (BeanUtil.hasNullField(userLoginRequest)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        if (userAccount.length() > USER_ACCOUNT_MAX_LEN || userAccount.length() < USER_ACCOUNT_MIN_LEN) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数长度不符合");
        }
        validString(userAccount);

        String password = userLoginRequest.getPassword();
        if (password.length() > USER_PASSWORD_MAX_LEN || password.length() < USER_PASSWORD_MIN_LEN) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数长度不符合");
        }
        String encryptPassword = SecureUtil.md5(password + SALT);

        User user = query().eq("account", userAccount)
                .eq("password", encryptPassword)
                .one();
        if (BeanUtil.isEmpty(user)) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "用户尚未注册");
        }
        UserDTO userDTO = new UserDTO();
        BeanUtil.copyProperties(user, userDTO);
        request.getSession().setAttribute(USER_LOGIN_STATE, userDTO);

        return userDTO;
    }

    private static void validString(String info) {
        String regex = "[a-zA-Z0-9]+";
        Pattern compile = Pattern.compile(regex);
        Matcher matcher = compile.matcher(info);
        if (!matcher.matches()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数不符合规范，只能包含数字和字母");
        }
    }

    @Override
    public Boolean userRegister(UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null || BeanUtil.hasNullField(userRegisterRequest)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        if (userAccount.length() > USER_ACCOUNT_MAX_LEN || userAccount.length() < USER_ACCOUNT_MIN_LEN) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数长度不符合");
        }
        //校验账户内容
        validString(userAccount);

        String password = userRegisterRequest.getPassword();
        String checkedPassword = userRegisterRequest.getCheckedPassword();
        //校验密码长度
        if (password.length() > USER_PASSWORD_MAX_LEN
                || password.length() < USER_PASSWORD_MIN_LEN
                || checkedPassword.length() > USER_PASSWORD_MAX_LEN
                || checkedPassword.length() < USER_PASSWORD_MIN_LEN) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度不符合（8-20位）");
        }
        //校验密码是否一致
        if (!password.equals(checkedPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次密码输入不一致");
        }

        //判断该账号是否已经被注册
        Long count = query().eq("account", userAccount).count();
        if (count > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该用户已经注册过了");
        }

        //密码加密
        String encryptPassword = SecureUtil.md5(password + SALT);
        //将用户信息插入数据库
        User user = new User();
        user.setUsername("user_" + IdUtil.fastSimpleUUID());
        user.setAccount(userAccount);
        user.setPassword(encryptPassword);
        boolean save = save(user);
        if(!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册用户失败");
        }
        Long userId = user.getId();
        //用户注册的同时创建秘钥
        KeysVO keys = getKeys(userId);
        if (keys == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        return true;
    }

    @Override
    public KeysVO getKeys(Long userId) {
        String accessKey = IdUtil.fastSimpleUUID();
        String secretKey = IdUtil.fastSimpleUUID();
        KeysVO keysVO = new KeysVO();
        keysVO.setAccessKey(accessKey);
        keysVO.setSecretKey(secretKey);
        boolean update = update().eq("id", userId)
                .set("access_key", accessKey)
                .set("secret_key", secretKey)
                .update();
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新用户秘钥失败");
        }
        return keysVO;
    }


}




