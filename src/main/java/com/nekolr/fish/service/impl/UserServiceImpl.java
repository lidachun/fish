package com.nekolr.fish.service.impl;

import com.nekolr.fish.dao.UserRepository;
import com.nekolr.fish.entity.User;
import com.nekolr.fish.exception.EntityExistException;
import com.nekolr.fish.service.UserService;
import com.nekolr.fish.service.dto.UserDTO;
import com.nekolr.fish.service.mapper.UserMapper;
import com.nekolr.fish.util.EncryptUtils;
import com.nekolr.fish.util.RandomUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author nekolr
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Resource
    private UserMapper userMapper;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDTO saveUser(User source) {

        if (ObjectUtils.isNotEmpty(userRepository.findByUsername(source.getUsername()))) {
            throw new EntityExistException(User.class, "username", source.getUsername());
        }

        if (StringUtils.isNotBlank(source.getEmail()) && ObjectUtils.isNotEmpty(userRepository.findByEmail(source.getEmail()))) {
            throw new EntityExistException(User.class, "email", source.getEmail());
        }

        if (StringUtils.isNotBlank(source.getPhone()) && ObjectUtils.isNotEmpty(userRepository.findByPhone(source.getPhone()))) {
            throw new EntityExistException(User.class, "phone", source.getPhone());
        }

        // 生成盐
        String salt = RandomUtils.randomString(6, false);
        String encryptedPwd = EncryptUtils.md5(source.getPassword() + salt);
        source.setSalt(salt);
        source.setPassword(encryptedPwd);
        User user = userRepository.save(source);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
