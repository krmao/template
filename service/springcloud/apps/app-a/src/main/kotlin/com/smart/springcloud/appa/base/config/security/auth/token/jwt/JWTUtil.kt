package com.smart.springcloud.appa.base.config.security.auth.token.jwt

import com.smart.springcloud.appa.base.config.config.CXConfigProperties
import com.smart.springcloud.appa.base.config.security.user.CXUserDetails
import com.smart.springcloud.appa.base.util.CXJsonUtil
import com.smart.springcloud.appa.database.model.UserModel
import io.jsonwebtoken.*
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*

@Suppress("MemberVisibilityCanPrivate", "unused")
@Component
class JWTUtil {

    private val ROLE_REFRESH_TOKEN = "ROLE_REFRESH_TOKEN"

    private val CLAIM_KEY_USER_ID = "user_id"
    private val CLAIM_KEY_AUTHORITIES = "scope"
    private val CLAIM_KEY_ACCOUNT_ENABLED = "enabled"
    private val CLAIM_KEY_ACCOUNT_NON_LOCKED = "non_locked"
    private val CLAIM_KEY_ACCOUNT_NON_EXPIRED = "non_expired"

    private val logger: Logger = LogManager.getLogger(JWTUtil::class.java.name)
    private var SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256

    @Autowired
    private lateinit var configProperties: CXConfigProperties

    @Throws(ExpiredJwtException::class, IllegalArgumentException::class, MalformedJwtException::class, SignatureException::class, UnsupportedJwtException::class)
    fun getUserFromToken(token: String?): CXUserDetails? {
        var user: CXUserDetails? = null
        try {
            val claims = getClaimsFromToken(token)
            val userId: Int = getUserIdFromToken(token) ?: -1
            val username = claims!!.subject
            val roles = claims[CLAIM_KEY_AUTHORITIES] as List<*>
            val authorities = parseArrayToAuthorities(roles).toMutableList()
            val accountEnabled = claims[CLAIM_KEY_ACCOUNT_ENABLED] as Boolean
            val accountNonLocked = claims[CLAIM_KEY_ACCOUNT_NON_LOCKED] as Boolean
            val accountNonExpired = claims[CLAIM_KEY_ACCOUNT_NON_EXPIRED] as Boolean

            logger.warn("getUserFromToken:claims=$claims")
            logger.warn("getUserFromToken:******************************************************************")
            logger.warn("getUserFromToken:******************************************************************")
            logger.warn("getUserFromToken:******************************************************************")
            logger.warn("getUserFromToken:userId=$userId")
            logger.warn("getUserFromToken:username=$username")
            logger.warn("getUserFromToken:roles=$roles")
            logger.warn("getUserFromToken:authorities=$authorities")
            logger.warn("getUserFromToken:accountEnabled=$accountEnabled")
            logger.warn("getUserFromToken:accountNonLocked=$accountNonLocked")
            logger.warn("getUserFromToken:accountNonExpired=$accountNonExpired")
            logger.warn("getUserFromToken:******************************************************************")
            logger.warn("getUserFromToken:******************************************************************")
            logger.warn("getUserFromToken:******************************************************************")

            user = CXUserDetails(UserModel(userId, username), accountEnabled, accountNonExpired, true, accountNonLocked, authorities)
        } catch (e: Exception) {
            logger.warn("getUserFromToken: error", e)
        }
        return user
    }

    @Throws(ExpiredJwtException::class, IllegalArgumentException::class, MalformedJwtException::class, SignatureException::class, UnsupportedJwtException::class)
    fun getUsernameFromToken(token: String?) = getClaimsFromToken(token)?.subject

    @Throws(ExpiredJwtException::class, IllegalArgumentException::class, MalformedJwtException::class, SignatureException::class, UnsupportedJwtException::class)
    fun getCreatedDateFromToken(token: String?) = getClaimsFromToken(token)?.issuedAt

    @Throws(ExpiredJwtException::class, IllegalArgumentException::class, MalformedJwtException::class, SignatureException::class, UnsupportedJwtException::class)
    private fun getClaimsFromToken(token: String?) = if (token?.isNotBlank() == true) Jwts.parser().setSigningKey(configProperties.jwt.secret).parseClaimsJws(token).body else null

    fun getUserIdFromToken(token: String?) = getClaimsFromToken(token)?.get(CLAIM_KEY_USER_ID) as? Int

    private fun generateExpirationDate(expiration: Long) = Date(System.currentTimeMillis() + expiration * 1000)

    fun getExpirationDateFromToken(token: String?) = getClaimsFromToken(token)?.expiration

    // 是否 TOKEN 已过期
    private fun isTokenExpired(token: String?) = getExpirationDateFromToken(token)?.before(Date()) ?: true

    private fun isTokenNotExpired(token: String?) = !isTokenExpired(token)

    // TOKEN创建时间在上一次修改密码时间之前
    private fun isCreatedBeforeLastPasswordReset(created: Date?, lastPasswordReset: Date?) = lastPasswordReset != null && created?.before(lastPasswordReset) ?: true

    private fun isCreatedAfterLastPasswordReset(created: Date?, lastPasswordReset: Date?) = !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)

    fun generateAccessToken(userDetails: UserDetails?): String? {
        if (userDetails == null) {
            logger.warn("userDetails is null !!! generateAccessToken failure return null")
            return null
        }
        val user = userDetails as CXUserDetails
        val claims = generateClaims(user)
        claims.put(CLAIM_KEY_AUTHORITIES, CXJsonUtil.toJSON(authoritiesToArray(user.authorities)))
        return generateAccessToken(user.username, claims)
    }

    private fun generateClaims(user: CXUserDetails): MutableMap<String, Any> {
        val claims = HashMap<String, Any>()
        logger.warn("put to Claims userId:${user.userModel.userId}")
        claims.put(CLAIM_KEY_USER_ID, user.userModel.userId)
        claims.put(CLAIM_KEY_ACCOUNT_ENABLED, user.isEnabled)
        claims.put(CLAIM_KEY_ACCOUNT_NON_LOCKED, user.isAccountNonLocked)
        claims.put(CLAIM_KEY_ACCOUNT_NON_EXPIRED, user.isAccountNonExpired)
        return claims
    }

    private fun generateAccessToken(subject: String?, claims: Map<String, Any>?) = generateToken(subject, claims, configProperties.jwt.accessToken.expiration)

    private fun authoritiesToArray(authorities: Collection<GrantedAuthority>): List<*> = authorities.map { it.authority }

    private fun parseArrayToAuthorities(roles: List<*>): Collection<GrantedAuthority> {
        val authorities = ArrayList<GrantedAuthority>()
        var authority: SimpleGrantedAuthority
        for (role in roles) {
            authority = SimpleGrantedAuthority(role.toString())
            authorities.add(authority)
        }
        return authorities
    }

    fun generateRefreshToken(userDetails: UserDetails): String? {
        val user = userDetails as CXUserDetails
        val claims = generateClaims(user)
        val roles = arrayOf(ROLE_REFRESH_TOKEN)// 只授于更新 token 的权限
        claims.put(CLAIM_KEY_AUTHORITIES, CXJsonUtil.toJSON(roles))
        return generateRefreshToken(user.username, claims)
    }

    fun generateRefreshToken(subject: String, claims: Map<String, Any>) = generateToken(subject, claims, configProperties.jwt.refreshToken.expiration)

    fun canTokenBeRefreshed(token: String?, lastPasswordReset: Date?): Boolean = !isCreatedBeforeLastPasswordReset(getCreatedDateFromToken(token), lastPasswordReset) && !isTokenExpired(token)

    fun refreshToken(token: String?): String? {
        val claims = getClaimsFromToken(token)
        return generateAccessToken(claims?.subject, claims)
    }

    fun generateToken(subject: String?, claims: Map<String, Any>?, expiration: Long): String? {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setId(UUID.randomUUID().toString())
            .setIssuedAt(Date())
            .setExpiration(generateExpirationDate(expiration))
            .compressWith(CompressionCodecs.DEFLATE)
            .signWith(SIGNATURE_ALGORITHM, configProperties.jwt.secret)
            .compact()
    }

    fun validateToken(token: String?, userDetails: UserDetails?): Boolean {
        val user = userDetails as? CXUserDetails
        val userId = getUserIdFromToken(token)
        val username = getUsernameFromToken(token)
        val created = getCreatedDateFromToken(token)

        logger.warn("validateToken:--------> params")
        logger.warn("validateToken:token=$token")
        logger.warn("validateToken:userDetails=$userDetails")
        logger.warn("validateToken:--------> start check")
        logger.warn("validateToken:user != null ? ${user != null}   user=$user")
        logger.warn("validateToken:userId:$userId == user.userId:${user?.userModel?.userId} ? ${userId == user?.userModel?.userId}")
        logger.warn("validateToken:username:$username == user.username:${user?.username} ? ${username == user?.username}")
        logger.warn("validateToken:created=$created")

        val isUserNotNull = user != null
        val isUserIdValid = userId != -1 && userId == user?.userModel?.userId
        val isUserNameValid = username == user?.username
        val isTokenNotExpired = isTokenNotExpired(token)
        val isCreatedAfterLastPasswordReset = isCreatedAfterLastPasswordReset(created, user?.userModel?.lastPasswordResetTime)

        logger.warn("validateToken:isUserNotNull=$isUserNotNull")
        logger.warn("validateToken:isUserIdValid=$isUserIdValid")
        logger.warn("validateToken:isUserNameValid=$isUserNameValid")
        logger.warn("validateToken:isTokenNotExpired=$isTokenNotExpired")
        logger.warn("validateToken:isCreatedAfterLastPasswordReset=$isCreatedAfterLastPasswordReset")
        logger.warn("validateToken:<-------- end check")
        return isUserNotNull
            && isUserIdValid
            && isUserNameValid
            && isTokenNotExpired
            && isCreatedAfterLastPasswordReset
    }
}
