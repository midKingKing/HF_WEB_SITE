package com.hf.helper

import com.hf.authorize.SESSION_KEEP_ALIVE_TIME
import com.hf.dao.SessionMapper
import com.hf.dto.Session
import com.hf.dto.User
import com.hf.util.SessionUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class SessionHelper @Autowired constructor(private val sessionMapper: SessionMapper) {
    fun getBySessionId(sessionId: String): Session? = sessionMapper.selectByPrimaryKey(sessionId)

    fun createSession(user: User): Int =
        sessionMapper.insert(Session().apply {
            uuid = UUID.randomUUID().toString().replace("-", "")
            username = user.username
            expireTime = System.currentTimeMillis().also {
                this.createTime = it
            } + SESSION_KEEP_ALIVE_TIME
            SessionUtil.session.set(this)
        })


    fun deleteSession(session: Session): Int = sessionMapper.delete(session)

    fun touchSession(session: Session): Int {
        session.expireTime = System.currentTimeMillis() + 1800 * 1000
        return sessionMapper.updateByPrimaryKey(session)
    }
}