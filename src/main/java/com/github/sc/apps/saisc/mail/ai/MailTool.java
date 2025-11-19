package com.github.sc.apps.saisc.mail.ai;

import jakarta.mail.MessagingException;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class MailTool {

    private final JavaMailSender mailSender;

    @Autowired
    public MailTool(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @McpTool(name = "send_mail", description = "send a mail")
    @Tool(name = "send_mail", description = "send a mail")
    public void sendMail(
            @McpToolParam(description = "The recipient email address")
            @ToolParam(description = "The recipient email address")
            String recipient,
            @McpToolParam(description = "The subject of the mail")
            @ToolParam(description = "The subject of the mail")
            String subject,
            @McpToolParam(description = "The html formatted text of the mail")
            @ToolParam(description = "The html formatted text of the mail")
            String htmlBody
    ) throws MessagingException {
        var message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message, false, StandardCharsets.UTF_8.name());
        message.setContent(htmlBody, MediaType.TEXT_HTML_VALUE);
        message.setFrom("noreply@this-is-all.ai");
        messageHelper.setTo(recipient);
        messageHelper.setSubject(subject);
        mailSender.send(message);
    }
}
