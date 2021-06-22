package com.smart.springcloud.security.authclient.web

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction
import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.reactive.function.client.WebClient
import javax.servlet.http.HttpServletRequest

@Controller
class AuthorizationController(private val webClient: WebClient, @param:Value("\${messages.base-uri}") private val messagesBaseUri: String) {

    @GetMapping(value = ["/authorize"], params = ["grant_type=authorization_code"])
    fun authorizationCodeGrant(model: Model, @RegisteredOAuth2AuthorizedClient("messaging-client-authorization-code") authorizedClient: OAuth2AuthorizedClient?): String {
        val messages = webClient
            .get()
            .uri(messagesBaseUri)
            .attributes(ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient(authorizedClient))
            .retrieve()
            .bodyToMono(Array<String>::class.java)
            .block()
        model.addAttribute("messages", messages)
        return "index"
    }

    // '/authorized' is the registered 'redirect_uri' for authorization_code
    @GetMapping(value = ["/authorized"], params = [OAuth2ParameterNames.ERROR])
    fun authorizationFailed(model: Model, request: HttpServletRequest): String {
        val errorCode = request.getParameter(OAuth2ParameterNames.ERROR)
        if (StringUtils.hasText(errorCode)) {
            model.addAttribute(
                "error",
                OAuth2Error(
                    errorCode,
                    request.getParameter(OAuth2ParameterNames.ERROR_DESCRIPTION),
                    request.getParameter(OAuth2ParameterNames.ERROR_URI)
                )
            )
        }
        return "index"
    }

    @GetMapping(value = ["/authorize"], params = ["grant_type=client_credentials"])
    fun clientCredentialsGrant(model: Model): String {
        val messages = webClient
            .get()
            .uri(messagesBaseUri)
            .attributes(ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId("messaging-client-client-credentials"))
            .retrieve()
            .bodyToMono(Array<String>::class.java)
            .block()
        model.addAttribute("messages", messages)
        return "index"
    }

}
